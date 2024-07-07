package com.xfrog.platform.infrastructure.base.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LangCorpusMapper extends BaseMapperEx<LangCorpusPO> {
    List<LangCorpusDTO> queryAll(@Param("queryDTO") QueryLangCorpusRequestDTO queryDTO, @Param("page") Page<LangCorpusDTO> page);
}
