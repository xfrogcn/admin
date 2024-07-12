package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.repository.DataScopeRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.infrastructure.permission.converter.DataScopePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.DataScopePO;
import com.xfrog.platform.infrastructure.permission.mapper.DataScopeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DataScopeRepositoryImpl implements DataScopeRepository {

    private final DataScopeMapper mapper;

    @Override
    public List<DataScopeDTO> findByTargetTypeAndTargetId(DataScopeTargetType targetType, List<Long> targetIds) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return new LinkedList<>();
        }

        List<DataScopePO> dataScopePOS = mapper.selectList(new LambdaQueryWrapper<DataScopePO>()
                .eq(DataScopePO::getDeleted, false)
                .eq(DataScopePO::getTargetType, targetType)
                .in(DataScopePO::getTargetId, targetIds));

        return DataScopePOConverter.INSTANCE.toDTOList(dataScopePOS);
    }
}
