package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.oplog.OpLogMDC;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.UpdateUserParameterRequestDTO;
import com.xfrog.platform.application.base.dto.UserSettingsDTO;
import com.xfrog.platform.application.base.repository.LangRepository;
import com.xfrog.platform.application.base.repository.UserParameterRepository;
import com.xfrog.platform.application.base.service.UserParameterService;
import com.xfrog.platform.domain.base.aggregate.UserParameter;
import com.xfrog.platform.domain.base.command.CreateUserParameterCommand;
import com.xfrog.platform.domain.base.repository.UserParameterDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserParameterServiceImpl implements UserParameterService {

    private final UserParameterRepository userParameterRepository;
    private final UserParameterDomainRepository userParameterDomainRepository;
    private final LangRepository langRepository;

    @Override
    public UserSettingsDTO getUserSettings(String application) {
        PrincipalInfo principalInfo = CurrentPrincipalContext.currentPrincipal();
        if (principalInfo == null) {
            return UserSettingsDTO.builder()
                    .langs(new ArrayList<>())
                    .parameters(new HashMap<>())
                    .build();
        }

        List<LangDTO> langs = langRepository.queryAllByApplication(application, true);
        Map<String, String> userParameters = userParameterRepository.queryUserParameters(principalInfo.getUserId(), application);

        return UserSettingsDTO.builder()
                .userId(principalInfo.getUserId())
                .langs(langs)
                .parameters(userParameters)
                .build();
    }

    @Override
    @Transactional
    public void updateUserParameters(String application, UpdateUserParameterRequestDTO requestDTO) {
        PrincipalInfo principalInfo = CurrentPrincipalContext.currentPrincipal();
        if (principalInfo == null) {
            return;
        }

        if (requestDTO == null || requestDTO.getParameters() == null || requestDTO.getParameters().isEmpty()) {
            return;
        }

        OpLogMDC.putBizCode(String.join(",", requestDTO.getParameters().keySet()));

        Map<String, UserParameter> originalParameters = userParameterDomainRepository.findByUserIdAndApplicationAndNames(
                principalInfo.getUserId(), application, requestDTO.getParameters().keySet().stream().toList()).stream()
                .collect(Collectors.toMap(UserParameter::getParameterName, Function.identity()));
        List<UserParameter> toBeSaveParameters = new ArrayList<>();
        requestDTO.getParameters().entrySet().forEach((parameter) -> {
            UserParameter userParameter = originalParameters.get(parameter.getKey());
            if (userParameter == null) {
                CreateUserParameterCommand command = CreateUserParameterCommand.builder()
                        .application(application)
                        .userId(principalInfo.getUserId())
                        .parameterName(parameter.getKey())
                        .parameterValue(parameter.getValue())
                        .build();
                userParameter = UserParameter.create(command);
            } else {
                userParameter.update(parameter.getValue());
            }
            toBeSaveParameters.add(userParameter);
        });

        userParameterDomainRepository.saveAll(toBeSaveParameters);

    }
}
