package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.DicApi;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.application.base.service.DicService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dics")
public class DicController implements DicApi {

    private final DicService dicService;

    @Override
    @Authorization("admin:platform:dic:create")
    public Long createDic(CreateDicRequestDTO dic) {
        return dicService.createDic(dic);
    }

    @Override
    @Authorization(value = "admin:platform:dic", demoDisabled = false)
    public PageDTO<DicDTO> listDics(QueryDicRequestDTO queryDTO) {
        return dicService.listDics(queryDTO);
    }

    @Override
    @Authorization(value = "admin:platform:dic:items", demoDisabled = false)
    public DicDTO getDic(Long dicId) {
        return dicService.getDic(dicId);
    }

    @Override
    @Authorization("admin:platform:dic:edit")
    public void updateDic(Long dicId, UpdateDicRequestDTO dic) {
        dicService.updateDic(dicId, dic);
    }

    @Override
    @Authorization("admin:platform:dic:delete")
    public void deleteDic(Long dicId) {
        dicService.deleteDic(dicId);
    }

    @Override
    @Authorization("admin:platform:dic:createitem")
    public Long createDicItem(Long dicId, CreateDicItemRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Authorization("admin:platform:dic:edititem")
    public void updateDicItem(Long dicId, Long itemId, UpdateDicItemRequestDTO requestDTO) {

    }
}
