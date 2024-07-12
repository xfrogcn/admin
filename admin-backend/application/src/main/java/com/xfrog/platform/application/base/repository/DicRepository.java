package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;

import java.util.List;

public interface DicRepository {
    PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO);

    DicDTO queryById(Long id);

    List<DicDTO> queryByTypes(List<String> types);

    List<DicItemDTO> queryItemsByDicId(List<Long> dicIds);
}
