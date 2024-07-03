package com.xfrog.platform.infrastructure.authserver.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xfrog.framework.common.DateTimeUtils;
import com.xfrog.platform.application.authserver.dto.SessionInformationDTO;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.application.authserver.repository.OAuth2SessionsRepository;
import com.xfrog.platform.infrastructure.authserver.dataobject.OAuth2PrincipalPO;
import com.xfrog.platform.infrastructure.authserver.dataobject.OAuth2SessionPO;
import com.xfrog.platform.infrastructure.authserver.mapper.OAuth2PrincipalsMapper;
import com.xfrog.platform.infrastructure.authserver.mapper.OAuth2SessionsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OAuth2SessionsRepositoryImpl implements OAuth2SessionsRepository {
    private final OAuth2SessionsMapper oAuth2SessionsMapper;
    private final OAuth2PrincipalsMapper oAuth2PrincipalsMapper;

    @Override
    public Long savePrincipal(UserDetailsDTO principal) {
        OAuth2PrincipalPO principalPO = findPrincipalByUserId(principal.getUserId());
        if (principalPO == null) {
            principalPO = OAuth2PrincipalPO.builder()
                    .userId(principal.getUserId())
                    .principalInfo(principal)
                    .createdTime(DateTimeUtils.utcNow())
                    .updatedTime(DateTimeUtils.utcNow())
                    .build();
        }
        else {
            principalPO.setPrincipalInfo(principal);
            principalPO.setUpdatedTime(DateTimeUtils.utcNow());
        }
        oAuth2PrincipalsMapper.save(principalPO);
        return principalPO.getId();
    }

    @Override
    public Long saveSession(Long principalId, Long userId, String sessionIdHash, SessionInformation sessionInformation) {
        OAuth2SessionPO sessionPO = findSessionBySessionId(sessionInformation.getSessionId());
        if (sessionPO == null) {
            sessionPO = OAuth2SessionPO.builder()
                    .sessionId(sessionInformation.getSessionId())
                    .sessionIdHash(sessionIdHash)
                    .userId(userId)
                    .principalId(principalId)
                    .lastRequest(sessionInformation.getLastRequest())
                    .expired(sessionInformation.isExpired())
                    .createdTime(DateTimeUtils.utcNow())
                    .updatedTime(DateTimeUtils.utcNow())
                    .build();
        } else {
            sessionPO.setUserId(userId);
            sessionPO.setPrincipalId(principalId);
            sessionPO.setLastRequest(sessionInformation.getLastRequest());
            sessionPO.setExpired(sessionInformation.isExpired());
            sessionPO.setUpdatedTime(DateTimeUtils.utcNow());
        }

        oAuth2SessionsMapper.save(sessionPO);

        return sessionPO.getId();
    }

    @Override
    public void updateSessionLastRequest(String sessionId, Date lastRequest) {
        oAuth2SessionsMapper.update(new LambdaUpdateWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getSessionId, sessionId)
                .set(OAuth2SessionPO::getLastRequest, lastRequest)
                .set(OAuth2SessionPO::getUpdatedTime, DateTimeUtils.utcNow()));
    }

    @Override
    public void expiredSession(String sessionId) {
        oAuth2SessionsMapper.update(new LambdaUpdateWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getSessionId, sessionId)
                .set(OAuth2SessionPO::getExpired, true)
                .set(OAuth2SessionPO::getUpdatedTime, DateTimeUtils.utcNow()));
    }

    @Override
    public SessionInformationDTO getSession(String sessionId) {
        OAuth2SessionPO sessionPO = findSessionBySessionId(sessionId);
        if (sessionPO == null) {
            return null;
        }
        OAuth2PrincipalPO principalPO = findPrincipalByUserId(sessionPO.getUserId());
        if (principalPO == null) {
            return null;
        }
        SessionInformationDTO sessionInformationDTO = new SessionInformationDTO(principalPO.getPrincipalInfo(), sessionPO.getSessionId(), sessionPO.getLastRequest(), sessionPO.getExpired());
        sessionInformationDTO.setPrincipalId(principalPO.getId());
        sessionInformationDTO.setUserId(sessionPO.getUserId());
        sessionInformationDTO.setAuthorizationId(sessionPO.getAuthorizationId());

        return sessionInformationDTO;
    }

    @Override
    public SessionInformationDTO getSessionBySessionIdHash(String sessionIdHash) {
        OAuth2SessionPO sessionPO = findSessionBySessionIdHash(sessionIdHash);
        if (sessionPO == null) {
            return null;
        }
        OAuth2PrincipalPO principalPO = findPrincipalByUserId(sessionPO.getUserId());
        if (principalPO == null) {
            return null;
        }
        SessionInformationDTO sessionInformationDTO = new SessionInformationDTO(principalPO.getPrincipalInfo(), sessionPO.getSessionId(), sessionPO.getLastRequest(), sessionPO.getExpired());
        sessionInformationDTO.setPrincipalId(principalPO.getId());
        sessionInformationDTO.setUserId(sessionPO.getUserId());
        sessionInformationDTO.setAuthorizationId(sessionPO.getAuthorizationId());

        return sessionInformationDTO;
    }

    @Override
    public Boolean hasSession(Long userId) {
        return oAuth2SessionsMapper.exists(new LambdaQueryWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getUserId, userId));
    }

    @Override
    public void removePrincipal(Long userId) {
        oAuth2PrincipalsMapper.delete(new LambdaQueryWrapper<OAuth2PrincipalPO>()
                .eq(OAuth2PrincipalPO::getUserId, userId));
    }

    @Override
    public List<UserDetailsDTO> getAllPrincipals() {
        List<OAuth2PrincipalPO> principalPOS = oAuth2PrincipalsMapper.selectList(new LambdaQueryWrapper<OAuth2PrincipalPO>());
        return principalPOS.stream().map(OAuth2PrincipalPO::getPrincipalInfo).collect(Collectors.toList());
    }

    @Override
    public List<SessionInformation> getAllSessions(Long userId, boolean includeExpiredSessions) {
        OAuth2PrincipalPO principalPO = findPrincipalByUserId(userId);
        if (principalPO == null) {
            return new LinkedList<>();
        }
        List<OAuth2SessionPO> sessionPOS = oAuth2SessionsMapper.selectList(new LambdaQueryWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getUserId, principalPO.getUserId()));

        return sessionPOS.stream().map(sessionPO -> {
            SessionInformationDTO sessionInformationDTO = new SessionInformationDTO(principalPO.getPrincipalInfo(), sessionPO.getSessionId(), sessionPO.getLastRequest(), sessionPO.getExpired());
            sessionInformationDTO.setPrincipalId(principalPO.getId());
            sessionInformationDTO.setUserId(sessionPO.getUserId());
            sessionInformationDTO.setAuthorizationId(sessionPO.getAuthorizationId());
            return (SessionInformation) sessionInformationDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeSession(String sessionId) {
        oAuth2SessionsMapper.delete(new LambdaQueryWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getSessionId, sessionId));
    }

    private OAuth2SessionPO findSessionBySessionId(String sessionId) {
        LambdaQueryWrapper<OAuth2SessionPO> queryWrapper = new LambdaQueryWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getSessionId, sessionId);
        return oAuth2SessionsMapper.selectOne(queryWrapper);
    }

    private OAuth2SessionPO findSessionBySessionIdHash(String sessionIdHash) {
        LambdaQueryWrapper<OAuth2SessionPO> queryWrapper = new LambdaQueryWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getSessionIdHash, sessionIdHash);
        return oAuth2SessionsMapper.selectOne(queryWrapper);
    }

    private OAuth2PrincipalPO findPrincipalByUserId(Long userId) {
        LambdaQueryWrapper<OAuth2PrincipalPO> queryWrapper = new LambdaQueryWrapper<OAuth2PrincipalPO>()
                .eq(OAuth2PrincipalPO::getUserId, userId);
        return oAuth2PrincipalsMapper.selectOne(queryWrapper);
    }

    @Override
    public void relatedAuthorization(String sessionIdHash, String authorizationId) {
        oAuth2SessionsMapper.update(new LambdaUpdateWrapper<OAuth2SessionPO>()
                .eq(OAuth2SessionPO::getSessionIdHash, sessionIdHash)
                .set(OAuth2SessionPO::getAuthorizationId, authorizationId));
    }
}
