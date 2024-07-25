package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.base.DicApi;
import com.xfrog.platform.application.base.constant.BaseOperationLogConstants;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dics")
public class DicController implements DicApi {

    private final DicService dicService;

    @Override
    @Authorization("admin:platform:dic:create")
    @OperationLog(bizId = "#return", bizCode = "#p0.type", bizType = BaseOperationLogConstants.BIZ_TYPE_DIC, bizAction = OperationActionConstants.CREATE)
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
    public List<DicDTO> getDicByTypes(List<String> dicTypes) {
        return dicService.getDicByTypes(dicTypes);
    }

    @Override
    @Authorization("admin:platform:dic:edit")
    @OperationLog(bizId = "#p0", bizCode = "#p1.type", bizType = BaseOperationLogConstants.BIZ_TYPE_DIC, bizAction = OperationActionConstants.UPDATE)
    public void updateDic(Long dicId, UpdateDicRequestDTO dic) {
        dicService.updateDic(dicId, dic);
    }

    @Override
    @Authorization("admin:platform:dic:delete")
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_DIC, bizAction = OperationActionConstants.DELETE)
    public void deleteDic(Long dicId) {
        dicService.deleteDic(dicId);
    }

    @Override
    @Authorization("admin:platform:dic:createitem")
    @OperationLog(bizId = "#return", bizCode = "#p1.displayText", bizType = BaseOperationLogConstants.BIZ_TYPE_DIC_ITEM, bizAction = OperationActionConstants.CREATE)
    public Long createDicItem(Long dicId, CreateDicItemRequestDTO requestDTO) {
        return dicService.createDicItem(dicId, requestDTO);
    }

    @Override
    @Authorization("admin:platform:dic:edititem")
    @OperationLog(bizId = "#p1", bizCode = "#p2.displayText", bizType = BaseOperationLogConstants.BIZ_TYPE_DIC_ITEM, bizAction = OperationActionConstants.UPDATE)
    public void updateDicItem(Long dicId, Long itemId, UpdateDicItemRequestDTO requestDTO) {
        dicService.updateDicItem(dicId, itemId, requestDTO);
    }
}
