package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.DicApi;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.application.base.service.DicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dics")
public class DicController implements DicApi {

    private final DicService dicService;

    @Override
    public Long createDic(CreateDicRequestDTO dic) {
        return dicService.createDic(dic);
    }

    @Override
    public PageDTO<DicDTO> listDics(QueryDicRequestDTO queryDTO) {
        return dicService.listDics(queryDTO);
    }

    @Override
    public DicDTO getDic(Long dicId) {
        return dicService.getDic(dicId);
    }

    @Override
    public void updateDic(Long dicId, UpdateDicRequestDTO dic) {
        dicService.updateDic(dicId, dic);
    }

    @Override
    public void deleteDic(Long dicId) {
        dicService.deleteDic(dicId);
    }
}
