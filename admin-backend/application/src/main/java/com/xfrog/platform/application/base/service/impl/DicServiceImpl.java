package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.converter.DicDTOToCommandConverter;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.application.base.service.DicService;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DicServiceImpl implements DicService {

    private final DicRepository dicRepository;
    private final DicDomainRepository dicDomainRepository;
    private final DicDTOToCommandConverter converter = DicDTOToCommandConverter.INSTANCE;


    @Override
    public Long createDic(CreateDicRequestDTO dic) {
//        Dic entity = converter.requestToCreateCommand(dic).toEntity();
//        return dicRepository.save(entity).getId();
        return null;
    }

    @Override
    public void updateDic(Long dicId, UpdateDicRequestDTO dic) {

    }

    @Override
    public void deleteDic(Long dicId) {

    }

    @Override
    public PageDTO<DicDTO> listDics(QueryDicRequestDTO queryDTO) {
        return dicRepository.queryAll(queryDTO);
    }

    @Override
    public DicDTO getDic(Long dicId) {
        return null;
    }

    @Override
    public Long createDicItem(Long dicId, CreateDicItemRequestDTO requestDTO) {
        return null;
    }

    @Override
    public void updateDicItem(Long dicId, Long itemId, UpdateDicItemRequestDTO requestDTO) {

    }
}
