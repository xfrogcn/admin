package com.xfrog.platform.infrastructure.base.mapper;

import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.LangPO;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LangMapper extends PageableMapper<LangPO, LangDTO, QueryLangRequestDTO> {

}
