package com.xfrog.platform.infrastructure.base.mapper;

import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DicMapper extends PageableMapper<DicPO, DicDTO, QueryDicRequestDTO> {

}
