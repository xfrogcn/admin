package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.dto.UpdatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.application.permission.repository.PermissionItemRepository;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.repository.PermissionItemDomainRepository;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionItemServiceImplTest {

    @Mock
    private PermissionItemDomainRepository permissionItemDomainRepository;
    @Mock
    private PermissionItemRepository permissionItemRepository;
    @Mock
    private RolePermissionItemDomainRepository rolePermissionItemDomainRepository;

    @InjectMocks
    private PermissionItemServiceImpl permissionItemService;

    @Test
    void createPermissionItem_Success() {
        PermissionItem parent = PermissionFixtures.createDefaultPermissionItem().build();
        CreatePermissionItemRequestDTO requestDTO = PermissionDTOFixtures.defaultCreatePermissionItemRequestDTO()
                .parentId(parent.getId())
                .build();

        when(permissionItemDomainRepository.findById(requestDTO.getParentId())).thenReturn(parent);
        when(permissionItemDomainRepository.existsByCode(requestDTO.getCode(), null)).thenReturn(false);
        when(permissionItemDomainRepository.save(any(PermissionItem.class))).thenReturn(PermissionFixtures.createDefaultPermissionItem().build());

        // Act
        Long result = permissionItemService.createPermissionItem(requestDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(permissionItemDomainRepository, times(1))
                .save(argThat(domain -> domain.getCode().equals(requestDTO.getCode())
                        && domain.getName().equals(requestDTO.getName())
                        && domain.getType().equals(requestDTO.getType())
                        && domain.getPlatform().equals(requestDTO.getPlatform())
                        && domain.getParentId().equals(requestDTO.getParentId())
                ));
    }

    @Test
    void createPermissionItem_ThrowsNotFoundException_WhenParentIdIsInvalid() {
        CreatePermissionItemRequestDTO requestDTO = PermissionDTOFixtures.defaultCreatePermissionItemRequestDTO()
                .parentId(1L)
                .build();

        when(permissionItemDomainRepository.findById(requestDTO.getParentId())).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> permissionItemService.createPermissionItem(requestDTO));
    }

    @Test
    void createPermissionItem_ThrowsBusinessException_WhenCodeAlreadyExists() {
        PermissionItem parent = PermissionFixtures.createDefaultPermissionItem().build();
        CreatePermissionItemRequestDTO requestDTO = PermissionDTOFixtures.defaultCreatePermissionItemRequestDTO()
                .parentId(parent.getId())
                .build();

        when(permissionItemDomainRepository.findById(requestDTO.getParentId())).thenReturn(parent);
        when(permissionItemDomainRepository.existsByCode(requestDTO.getCode(), null)).thenReturn(true);

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> permissionItemService.createPermissionItem(requestDTO));
    }


    @Test
    void updatePermissionItem_Success() {
        PermissionItem permissionItem = PermissionFixtures.createDefaultPermissionItem().build();
        UpdatePermissionItemRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdatePermissionItemRequestDTO()
                .name("updated_name")
                .type("updated_type")
                .build();

        when(permissionItemDomainRepository.findById(permissionItem.getId())).thenReturn(permissionItem);

        // Act
        permissionItemService.updatePermissionItem(permissionItem.getId(), requestDTO);

        // Assert
        verify(permissionItemDomainRepository, times(1))
                .save(argThat(domain -> domain.getName().equals(requestDTO.getName())
                        && domain.getType().equals(requestDTO.getType())
                ));
    }

    @Test
    void updatePermissionItem_NotFoundException() {
        // Arrange
        when(permissionItemDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> permissionItemService.updatePermissionItem(1L, new UpdatePermissionItemRequestDTO()));
    }

    @Test
    void listPermissionItems_Success() {
        permissionItemService.listPermissionItems(true);
        verify(permissionItemRepository, times(1)).queryAll(true);
    }

    @Test
    void deletePermissionItem_NotFoundException() {
        // Arrange
        when(permissionItemDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> permissionItemService.deletePermissionItem(1L));
    }

    @Test
    void deletePermissionItem_ShouldThrowExceptionWhenItemHasChildren() {
        PermissionItem permissionItem = PermissionFixtures.createDefaultPermissionItem().build();

        when(permissionItemDomainRepository.findById(permissionItem.getId())).thenReturn(permissionItem);
        when(permissionItemDomainRepository.existsChildren(permissionItem.getId())).thenReturn(true);

        assertThrows(FailedPreconditionException.class, () -> permissionItemService.deletePermissionItem(permissionItem.getId()));
    }

    @Test
    void deletePermissionItem_ShouldDeleteRolePermissionItemsAndPermissionItem() {
        PermissionItem permissionItem = PermissionFixtures.createDefaultPermissionItem().build();
        RolePermissionItem rolePermissionItem = PermissionFixtures.createDefaultRolePermissionItem()
                        .permissionItemId(permissionItem.getId()).roleId(1L).build();

        when(permissionItemDomainRepository.findById(permissionItem.getId())).thenReturn(permissionItem);
        when(permissionItemDomainRepository.existsChildren(permissionItem.getId())).thenReturn(false);
        when(rolePermissionItemDomainRepository.getByPermissionItemId(permissionItem.getId())).thenReturn(List.of(rolePermissionItem));

        permissionItemService.deletePermissionItem(permissionItem.getId());

        verify(permissionItemDomainRepository, times(1)).logicDelete(permissionItem.getId());
        verify(rolePermissionItemDomainRepository, times(1))
                .logicDeleteAll(List.of(rolePermissionItem));
    }
}