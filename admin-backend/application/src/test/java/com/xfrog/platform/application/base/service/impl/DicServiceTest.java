package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.aggregate.DicFixtures;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import com.xfrog.platform.domain.base.repository.DicItemDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DicServiceTest {

    @Mock
    private DicRepository dicRepository;
    @Mock
    private DicDomainRepository dicDomainRepository;
    @Mock
    private DicItemDomainRepository dicItemDomainRepository;
    @InjectMocks
    private DicServiceImpl dicService;

    @Test
    void createDic_shouldThrowFailedPreconditionExceptionWhenDicTypeOrNameExists() {
        when(dicDomainRepository.existsByTypeOrName(eq("type"), eq("name"), any()))
                .thenReturn(true);

        CreateDicRequestDTO createDicRequestDTO = DicDTOFixtures.defaultCreateDicRequestDTO()
                .type("type").name("name").build();
        assertThrows(FailedPreconditionException.class, () -> dicService.createDic(createDicRequestDTO));
    }

    @Test
    void createDic_shouldCreateDicSuccessfullyWhenTypeAndNameDoNotExist() {
        when(dicDomainRepository.existsByTypeOrName(eq("type"), eq("name"), any()))
                .thenReturn(false);

        when(dicDomainRepository.save(any(Dic.class))).thenAnswer(invocation -> DicFixtures.createDefaultDic().build());

        CreateDicRequestDTO createDicRequestDTO = DicDTOFixtures.defaultCreateDicRequestDTO()
                .type("type").name("name").build();

        dicService.createDic(createDicRequestDTO);

        verify(dicDomainRepository, times(1))
                .save(argThat(dic -> dic.getType().equals("type")
                        && dic.getName().equals("name")));
    }

    @Test
    void updateDic_shouldThrowNotFoundExceptionWhenDicDoesNotExist() {

        when(dicDomainRepository.findById(1L)).thenReturn(null);

        UpdateDicRequestDTO updateDicRequestDTO = DicDTOFixtures.defaultUpdateDicRequestDTO().build();
        assertThrows(NotFoundException.class, () -> dicService.updateDic(1L, updateDicRequestDTO));
    }

    @Test
    void updateDic_shouldThrowFailedPreconditionExceptionWhenDicTypeOrNameExists() {
        Dic dic = DicFixtures.createDefaultDic().build();
        UpdateDicRequestDTO updateDicRequestDTO = DicDTOFixtures.defaultUpdateDicRequestDTO()
                .type("updated_type")
                .name("updated_name")
                .build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicDomainRepository.existsByTypeOrName(updateDicRequestDTO.getType(), updateDicRequestDTO.getName(), List.of(dic.getId())))
                .thenReturn(true);

        assertThrows(FailedPreconditionException.class, () -> dicService.updateDic(dic.getId(), updateDicRequestDTO));
    }

    @Test
    void updateDic_shouldSuccessfullyUpdateDicWhenValid() {
        Dic dic = DicFixtures.createDefaultDic().build();
        UpdateDicRequestDTO updateDicRequestDTO = DicDTOFixtures.defaultUpdateDicRequestDTO()
                .type("updated_type")
                .name("updated_name")
                .build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicDomainRepository.existsByTypeOrName(updateDicRequestDTO.getType(), updateDicRequestDTO.getName(), List.of(dic.getId())))
                .thenReturn(false);

        dicService.updateDic(dic.getId(), updateDicRequestDTO);

        verify(dicDomainRepository, times(1))
                .save(argThat(domain -> domain.getType().equals("updated_type")
                        && domain.getName().equals("updated_name")));
    }

    @Test
    void deleteDic_ShouldSuccessfully() {
        dicService.deleteDic(1L);
        verify(dicDomainRepository, times(1))
                .logicDelete(1L);
    }

    @Test
    void listDics_ShouldSuccessfully() {
        QueryDicRequestDTO queryDicRequestDTO = QueryDicRequestDTO.builder()
                .pageNum(1).pageSize(10)
                .build();
        dicService.listDics(queryDicRequestDTO);
        verify(dicRepository, times(1))
                .queryAll(queryDicRequestDTO);
    }

    @Test
    void getDic_ShouldSuccessfully() {
        dicService.getDic(1L);
        verify(dicRepository, times(1))
                .queryById(1L);
    }

    @Test
    void getDicByTypes_ShouldSuccessfully() {
        dicService.getDicByTypes(anyList());
        verify(dicRepository, times(1))
                .queryByTypes(anyList());
    }

    @Test
    void createDicItem_createDicItemShouldThrowNotFoundExceptionWhenDicDoesNotExist() {
        when(dicDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> dicService.createDicItem(1L, new CreateDicItemRequestDTO()));
    }

    @Test
    void createDicItem_createDicItemShouldThrowFailedPreconditionExceptionWhenDisplayTextExists() {
        Dic dic = DicFixtures.createDefaultDic().build();
        CreateDicItemRequestDTO createDicItemRequestDTO = DicDTOFixtures.defaultCreateDicItemRequestDTO().build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicItemDomainRepository.existsByDisplayText(eq(createDicItemRequestDTO.getDisplayText()), any())).thenReturn(true);
        assertThrows(FailedPreconditionException.class, () -> dicService.createDicItem(dic.getId(), createDicItemRequestDTO));
    }

    @Test
    void createDicItem_createDicItemShouldSucceedWhenConditionsAreMet() {
        Dic dic = DicFixtures.createDefaultDic().build();
        CreateDicItemRequestDTO createDicItemRequestDTO = DicDTOFixtures.defaultCreateDicItemRequestDTO().build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicItemDomainRepository.existsByDisplayText(eq(createDicItemRequestDTO.getDisplayText()), any())).thenReturn(false);
        when(dicItemDomainRepository.save(any(DicItem.class))).thenReturn(DicFixtures.createDefaultDicItem(dic.getId()).build());

        Long resultId = dicService.createDicItem(dic.getId(), createDicItemRequestDTO);
        assertThat(resultId).isNotNull();

        verify(dicItemDomainRepository, times(1))
                .save(argThat(domain -> domain.getDisplayText().equals(createDicItemRequestDTO.getDisplayText())
                        && domain.getValue().equals(createDicItemRequestDTO.getValue())
                        && domain.getDicId().equals(dic.getId())
                ));
    }

    @Test
    void updateDicItem_updateDicItemShouldThrowNotFoundExceptionWhenDicNotFound() {

        when(dicDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                dicService.updateDicItem(1L, 1L, new UpdateDicItemRequestDTO()));
    }

    @Test
    void updateDicItem_updateDicItemShouldThrowNotFoundExceptionWhenDicItemNotFound() {

        Dic dic = DicFixtures.createDefaultDic().build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicItemDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                dicService.updateDicItem(dic.getId(), 1L, new UpdateDicItemRequestDTO()));
    }

    @Test
    void updateDicItem_updateDicItemShouldThrowFailedPreconditionExceptionWhenDisplayTextExists() {
        Dic dic = DicFixtures.createDefaultDic().build();
        DicItem dicItem = DicFixtures.createDefaultDicItem(dic.getId()).build();
        UpdateDicItemRequestDTO requestDTO = DicDTOFixtures.defaultUpdateDicItemRequestDTO().build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicItemDomainRepository.existsByDisplayText(eq(requestDTO.getDisplayText()), any())).thenReturn(true);

        assertThrows(FailedPreconditionException.class, () ->
                dicService.updateDicItem(dic.getId(), dicItem.getId(), requestDTO));
    }

    @Test
    void updateDicItem_updateDicItemShouldSuccessWhenAllConditionsMet() {
        Dic dic = DicFixtures.createDefaultDic().build();
        DicItem dicItem = DicFixtures.createDefaultDicItem(dic.getId()).build();
        UpdateDicItemRequestDTO requestDTO = DicDTOFixtures.defaultUpdateDicItemRequestDTO()
                .value("updated_value")
                .langCode("updated_langCode")
                .extValue1("updated_extValue1")
                .extValue2("updated_extValue2")
                .displayOrder(1)
                .memo("updated_memo")
                .build();

        when(dicDomainRepository.findById(dic.getId())).thenReturn(dic);
        when(dicItemDomainRepository.existsByDisplayText(eq(requestDTO.getDisplayText()), any())).thenReturn(false);
        when(dicItemDomainRepository.findById(dicItem.getId())).thenReturn(dicItem);

        dicService.updateDicItem(dic.getId(), dicItem.getId(), requestDTO);

        verify(dicItemDomainRepository, times(1))
                .save(argThat(domain -> domain.getDisplayText().equals(requestDTO.getDisplayText())
                        && domain.getValue().equals(requestDTO.getValue())
                        && domain.getLangCode().equals(requestDTO.getLangCode())
                        && domain.getExtValue1().equals(requestDTO.getExtValue1())
                        && domain.getExtValue2().equals(requestDTO.getExtValue2())
                        && domain.getDisplayOrder().equals(requestDTO.getDisplayOrder())
                        && domain.getMemo().equals(requestDTO.getMemo())
                        && domain.getDicId().equals(dic.getId())
                        &&domain.getId().equals(dicItem.getId())
                ));
    }

}