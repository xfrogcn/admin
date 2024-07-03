package com.xfrog.platform.infrastructure.authserver.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.authserver.dto.RegisteredClientDTO;
import com.xfrog.platform.application.authserver.repository.ClientsRepository;
import com.xfrog.platform.infrastructure.authserver.converter.RegisteredClientPOConverter;
import com.xfrog.platform.infrastructure.authserver.mapper.ClientsMapper;
import com.xfrog.platform.infrastructure.authserver.dataobject.RegisteredClientPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClientsRepositoryImpl implements ClientsRepository {

    private final ClientsMapper clientsMapper;

    @Override
    public void save(RegisteredClientDTO registeredClient) {
        RegisteredClientPO registeredClientPO = RegisteredClientPOConverter.INSTANCE.toPO(registeredClient);
        clientsMapper.save(registeredClientPO, true);
    }

    @Override
    public RegisteredClientDTO findById(Long id) {
        RegisteredClientPO registeredClientPO = clientsMapper.selectById(id);
        return RegisteredClientPOConverter.INSTANCE.toDTO(registeredClientPO);
    }

    @Override
    public RegisteredClientDTO findByClientId(String clientId) {
        LambdaQueryWrapper<RegisteredClientPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegisteredClientPO::getClientId, clientId);
        RegisteredClientPO registeredClientPO =  clientsMapper.selectOne(queryWrapper);
        return RegisteredClientPOConverter.INSTANCE.toDTO(registeredClientPO);
    }
}
