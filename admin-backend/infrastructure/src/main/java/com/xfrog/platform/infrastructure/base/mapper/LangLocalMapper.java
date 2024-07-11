package com.xfrog.platform.infrastructure.base.mapper;

import com.xfrog.platform.application.base.dto.LangLocalDTO;
import com.xfrog.platform.infrastructure.base.dataobject.LangLocalPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LangLocalMapper extends BaseMapperEx<LangLocalPO> {
    List<LangLocalDTO> queryByLangCorpusId(@Param("langCorpusId") Long langCorpusId);
}
