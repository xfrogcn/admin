package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;

import java.util.List;

public interface DicRepository {
    PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO);

    DicDTO findById(Long id);

    List<DicDTO> findByTypes(List<String> types);

    List<DicItemDTO> findItemsByDicId(List<Long> dicIds);
}
