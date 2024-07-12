package com.xfrog.platform.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageQueryDTO;
import com.xfrog.framework.po.AuditPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageableMapper<PO extends AuditPO, DTO, QueryDTO extends PageQueryDTO> extends BaseMapperEx<PO> {
    List<DTO> queryBy(@Param("queryDTO") QueryDTO queryDTO, @Param("page") Page<DTO> page);
}
