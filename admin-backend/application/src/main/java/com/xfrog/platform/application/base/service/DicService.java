package com.xfrog.platform.application.base.service;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;

import java.util.List;

public interface DicService {
    Long createDic(CreateDicRequestDTO dic);

    void updateDic(Long dicId, UpdateDicRequestDTO dic);

    void deleteDic(Long dicId);

    PageDTO<DicDTO> listDics(QueryDicRequestDTO queryDTO);

    DicDTO getDic(Long dicId);

    List<DicDTO> getDicByTypes(List<String> dicTypes);

    Long createDicItem(Long dicId, CreateDicItemRequestDTO requestDTO);

    void updateDicItem(Long dicId, Long itemId, UpdateDicItemRequestDTO requestDTO);

}
