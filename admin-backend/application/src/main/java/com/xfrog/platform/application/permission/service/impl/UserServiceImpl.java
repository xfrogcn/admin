package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.common.ListComparator;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.framework.exception.business.PermissionDeniedException;
import com.xfrog.framework.oplog.OpLogMDC;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.converter.UserDTOConverter;
import com.xfrog.platform.application.permission.repository.RoleRepository;
import com.xfrog.platform.application.permission.repository.TenantRepository;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.application.permission.service.UserService;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserRoleDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDomainRepository userDomainRepository;
    private final UserRepository userRepository;
    private final UserRoleDomainRepository userRoleDomainRepository;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantsRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public PageDTO<UserDTO> listUsers(QueryUserRequestDTO queryUserRequestDTO) {
        PageDTO<UserDTO> result = userRepository.queryBy(queryUserRequestDTO);
        if (!CollectionUtils.isEmpty(result.getData())) {
            Map<Long, List<Long>> userRoleIdsMap = userRepository.queryUserRoleIds(result.getData().stream()
                            .map(UserDTO::getId).toList());
            Map<Long, RoleDTO> roleMap = roleRepository.queryByIds(userRoleIdsMap.values().stream()
                            .flatMap(List::stream).distinct().toList())
                    .stream()
                    .collect(Collectors.toMap(RoleDTO::getId, Function.identity()));
            result.getData().forEach(user -> {
                user.setRoles(userRoleIdsMap.getOrDefault(user.getId(), new ArrayList<>()).stream()
                        .map(roleMap::get)
                        .filter(role -> role != null && Boolean.TRUE.equals(role.getEnabled()))
                        .toList());
            });
        }

        return  result;
    }

    @Override
    @Transactional
    public Long createUser(CreateUserRequestDTO userDTO) {
        User oldUser = userDomainRepository.findByUserName(userDTO.getUserName());
        if (oldUser != null) {
            throw new AlreadyExistsException("user name already exists");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = User.builder()
                .userName(userDTO.getUserName())
                .password("{bcrypt}"+encodedPassword)
                .name(userDTO.getName())
                .sex(userDTO.getSex())
                .mobilePhone(userDTO.getMobilePhone())
                .email(userDTO.getEmail())
                .organizationId(userDTO.getOrganizationId())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(userDTO.getEnabled())
                .build();

        user = userDomainRepository.save(user);

        return user.getId();
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UpdateUserRequestDTO userDTO) {
        User user = userDomainRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("user not found");
        }
        OpLogMDC.putBizCode(user.getUserName());
        user.update(userDTO.getOrganizationId(), userDTO.getName(), userDTO.getSex(), userDTO.getMobilePhone(), userDTO.getEmail());
        userDomainRepository.save(user);
        userRepository.removeCache(userId);
    }

    @Override
    public UserDTO getUserDetail(Long userId) {
       return userRepository.queryById(userId);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId) {
        resetPassword(userId, "123456");
    }

    private void resetPassword(Long userId, String password) {
        User user = userDomainRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("user not found");
        }
        OpLogMDC.putBizCode(user.getUserName());
        String encodedPassword = passwordEncoder.encode(password);
        user.changePassword("{bcrypt}"+encodedPassword);
        userDomainRepository.save(user);
    }

    @Override
    @Transactional
    public void disableUser(Long userId) {
        User user = userDomainRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("user not found");
        }
        OpLogMDC.putBizCode(user.getUserName());
        user.changeEnabled(false);
        userDomainRepository.save(user);
        userRepository.removeCache(userId);
    }

    @Override
    @Transactional
    public void enableUser(Long userId) {
        User user = userDomainRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("user not found");
        }
        OpLogMDC.putBizCode(user.getUserName());
        user.changeEnabled(true);
        userDomainRepository.save(user);
        userRepository.removeCache(userId);
    }

    @Override
    @Transactional
    public void changeCurrentUserPassword(String newPassword) {
        PrincipalInfo principalInfo = CurrentPrincipalContext.currentPrincipal();
        if (principalInfo == null || principalInfo.isSystem() || principalInfo.getUserId() == null) {
            return;
        }
        resetPassword(principalInfo.getUserId(), newPassword);
    }

    @Override
    @Transactional
    public void grantRoles(Long userId, List<Long> roleIds) {
        User user = userDomainRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("user not found");
        }
        OpLogMDC.putBizCode(user.getUserName());
        List<UserRole> userRoles = userRoleDomainRepository.getByUserId(userId);
        ListComparator.CompareResult<UserRole, Long> compareResult = ListComparator.compare(
                userRoles,
                roleIds,
                (userRole, roleId) -> Objects.equals(userRole.getRoleId(), roleId));
        // 此处不用管更新
        List<UserRole> added = compareResult.getAdded().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userId).build()).collect(Collectors.toList());
        userRoleDomainRepository.saveAll(added);
        userRoleDomainRepository.logicDeleteAll(compareResult.getRemoved());

        userRepository.removeUserRoleIdsCache(userId);
        userRepository.removeUserPermissionCodesCache(userId);
    }

    @Override
    public CurrentUserInfoDTO getCurrentUserDetail() {
        Long userId = CurrentPrincipalContext.currentPrincipal().getUserId();
        CurrentUserInfoDTO userDTO = UserDTOConverter.INSTANCE.toCurrentUser(userRepository.queryById(userId));
        if (userDTO != null) {
           TenantDTO tenant = tenantsRepository.queryByCode(userDTO.getTenantId());
           if (tenant == null ||Boolean.FALSE.equals(tenant.getEnabled())) {
               throw new PermissionDeniedException("tenant is disabled");
           }
           userDTO.setRootOrganizationId(tenant.getOrganizationId());

        }
        return userDTO;
    }

    @Override
    public List<String> getCurrentUserPermissionCodes() {
        Long userId = CurrentPrincipalContext.currentPrincipal().getUserId();
        return getUserPermissionCodes(userId);
    }

    @Override
    public List<String> getUserPermissionCodes(Long userId) {
        return userRepository.queryUserPermissionCodes(userId);
    }
}
