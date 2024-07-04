package com.xfrog.platform.infrastructure.base.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DicMapper extends BaseMapperEx<DicPO> {
    List<DicDTO> queryAll(@Param("queryDTO") QueryDicRequestDTO queryDTO, @Param("page") Page<DicDTO> page);
}
