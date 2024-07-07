package com.xfrog.platform.infrastructure.base.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.LangPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LangMapper extends BaseMapperEx<LangPO> {
    List<LangDTO> queryAll(@Param("queryDTO") QueryLangRequestDTO queryDTO, @Param("page") Page<LangDTO> page);
}
