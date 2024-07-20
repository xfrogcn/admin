package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.repository.CacheablePageableApplicationRepository;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;

import java.util.List;

public interface DicRepository extends CacheablePageableApplicationRepository<DicDTO, QueryDicRequestDTO> {

    List<DicDTO> queryByTypes(List<String> types);

    List<DicItemDTO> queryItemsByDicId(List<Long> dicIds);

    void removeDicCacheByType(String type);

    void removeDicItemsCacheByDicId(Long dicId);
}
