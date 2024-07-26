package com.xfrog.platform.application.authserver.service.impl;

import com.xfrog.framework.common.DateTimeUtils;
import com.xfrog.framework.oplog.OpLogger;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.platform.application.authserver.constant.AuthServerOperationLogConstants;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLastLoginTimeUpdater {
    private final UserDomainRepository userRepository;
    private final ObjectProvider<OpLogger> opLoggerProvider;
    public void updateLastLoginTime(Long userId) {

        User user = userRepository.findById(userId);
        if (user == null) {
            return;
        }

        userRepository.updateLastLoginTime(userId, DateTimeUtils.utcNow());

        OpLogger opLogger = opLoggerProvider.getIfAvailable();
        if (opLogger != null) {
            opLogger.success(
                    user.getId(),
                    AuthServerOperationLogConstants.OP_TYPE_AUTH,
                    AuthServerOperationLogConstants.BIZ_TYPE_AUTH,
                    OperationActionConstants.LOGIN,
                    userId.toString(),
                    user.getUserName());
        }
    }
}
