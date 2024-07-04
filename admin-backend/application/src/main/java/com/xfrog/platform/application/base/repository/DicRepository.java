package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;

public interface DicRepository {
    PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO);
}
