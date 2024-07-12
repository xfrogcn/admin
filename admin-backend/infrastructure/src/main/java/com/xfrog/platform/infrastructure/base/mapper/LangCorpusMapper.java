package com.xfrog.platform.infrastructure.base.mapper;

import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LangCorpusMapper extends PageableMapper<LangCorpusPO, LangCorpusDTO, QueryLangCorpusRequestDTO> {

}
