package com.xfrog.platform.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.repository.ApplicationRepository;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseApplicationRepository <DTO, PO extends AuditPO, M extends BaseMapperEx<PO>>
        implements ApplicationRepository<DTO> {

    protected POToDTOConverter<PO, DTO> converter;
    protected M mapper;

    public BaseApplicationRepository(M mapper, POToDTOConverter<PO, DTO> converter) {
        this.converter = converter;
        this.mapper = mapper;
    }

    @Override
    public DTO queryById(Long id) {
        QueryWrapper<PO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("deleted", false);
        return converter.toDTO(mapper.selectOne(queryWrapper));
    }

    @Override
    public List<DTO> queryByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new LinkedList<>();
        }
        QueryWrapper<PO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        queryWrapper.eq("deleted", false);

        return converter.toDTOList(mapper.selectList(queryWrapper));
    }
}
