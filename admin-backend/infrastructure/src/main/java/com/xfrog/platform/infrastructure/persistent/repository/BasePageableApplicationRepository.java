package com.xfrog.platform.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.dto.PageQueryDTO;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;

import java.util.List;
import java.util.Map;

public abstract class BasePageableApplicationRepository<DTO, PO extends AuditPO, M extends PageableMapper<PO, DTO, QueryDTO>, QueryDTO extends PageQueryDTO>
        extends BaseApplicationRepository<DTO, PO, M> implements PageableApplicationRepository<DTO, QueryDTO> {
    public BasePageableApplicationRepository(M mapper, POToDTOConverter<PO, DTO> converter) {
        super(mapper, converter);
    }

    @Override
    public PageDTO<DTO> queryBy(QueryDTO queryDTO) {
        Page<DTO> page = PageUtils.page(queryDTO, orderedFieldMap());
        List<DTO> dtoList = mapper.queryBy(queryDTO, page);
        return PageUtils.result(page, dtoList);
    }

    protected abstract Map<String, String> orderedFieldMap();
}
