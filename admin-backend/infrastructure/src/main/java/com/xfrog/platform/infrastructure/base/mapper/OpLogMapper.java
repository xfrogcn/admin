package com.xfrog.platform.infrastructure.base.mapper;

import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.OpLogPO;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpLogMapper  extends PageableMapper<OpLogPO, OpLogDTO, QueryOpLogRequestDTO> {
}
