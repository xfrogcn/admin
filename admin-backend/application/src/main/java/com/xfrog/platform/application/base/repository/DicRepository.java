package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;

import java.util.List;

public interface DicRepository extends PageableApplicationRepository<DicDTO, QueryDicRequestDTO> {
//    PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO);

//    DicDTO queryById(Long id);

    List<DicDTO> queryByTypes(List<String> types);

    List<DicItemDTO> queryItemsByDicId(List<Long> dicIds);
}
