package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.base.converter.DicDTOToCommandConverter;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.application.base.service.DicService;
import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.command.CreateDicCommand;
import com.xfrog.platform.domain.base.command.UpdateDicCommand;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DicServiceImpl implements DicService {

    private final DicRepository dicRepository;
    private final DicDomainRepository dicDomainRepository;
    private final DicDTOToCommandConverter converter = DicDTOToCommandConverter.INSTANCE;


    @Override
    @Transactional
    public Long createDic(CreateDicRequestDTO createDicRequestDTO) {
        if (dicDomainRepository.existsByTypeOrName(createDicRequestDTO.getType(), createDicRequestDTO.getName(), null)) {
            throw new FailedPreconditionException("dic name or type exists");
        }

        CreateDicCommand createDicCommand = converter.requestToCreateCommand(createDicRequestDTO);
        Dic dic = Dic.create(createDicCommand);

        return dicDomainRepository.save(dic).getId();
    }

    @Override
    @Transactional
    public void updateDic(Long dicId, UpdateDicRequestDTO updateDicRequestDTO) {
        Dic oldDic = dicDomainRepository.findById(dicId);
        if (oldDic == null) {
            throw new NotFoundException("dic not found");
        }
        if (dicDomainRepository.existsByTypeOrName(updateDicRequestDTO.getType(), updateDicRequestDTO.getName(), List.of(dicId))) {
            throw new FailedPreconditionException("dic name or type exists");
        }
        UpdateDicCommand updateDicCommand = converter.requestToUpdateCommand(updateDicRequestDTO);
        oldDic.update(updateDicCommand);

        dicDomainRepository.save(oldDic);
    }

    @Override
    @Transactional
    public void deleteDic(Long dicId) {
        dicDomainRepository.logicDelete(dicId);
    }

    @Override
    public PageDTO<DicDTO> listDics(QueryDicRequestDTO queryDTO) {
        return dicRepository.queryAll(queryDTO);
    }

    @Override
    public DicDTO getDic(Long dicId) {
        DicDTO dicDTO = dicRepository.findById(dicId);
        if (dicDTO != null) {
            fillDicItems(List.of(dicDTO));
        }
        return dicDTO;
    }

    @Override
    public List<DicDTO> getDicByTypes(List<String> dicTypes) {
        List<DicDTO> dics = dicRepository.findByTypes(dicTypes);
        fillDicItems(dics);
        return dics;
    }

    private void fillDicItems(List<DicDTO> dics) {
        if (CollectionUtils.isEmpty(dics)) {
            return;
        }

        List<DicItemDTO> dicItems = dicRepository.findItemsByDicId(dics.stream()
                .map(DicDTO::getId)
                .toList());

        dics.forEach(dic -> {
            dic.setDicItems(dicItems.stream()
                .filter(dicItem -> dicItem.getDicId().equals(dic.getId()))
                .sorted((a, b) -> a.getDisplayOrder() - b.getDisplayOrder())
                .toList());
        });
    }

    @Override
    public Long createDicItem(Long dicId, CreateDicItemRequestDTO requestDTO) {
        return null;
    }

    @Override
    public void updateDicItem(Long dicId, Long itemId, UpdateDicItemRequestDTO requestDTO) {

    }
}
