package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.base.converter.DicDTOToCommandConverter;
import com.xfrog.platform.application.base.converter.DicItemDTOToCommandConverter;
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
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.domain.base.command.CreateDicCommand;
import com.xfrog.platform.domain.base.command.CreateDicItemCommand;
import com.xfrog.platform.domain.base.command.UpdateDicCommand;
import com.xfrog.platform.domain.base.command.UpdateDicItemCommand;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import com.xfrog.platform.domain.base.repository.DicItemDomainRepository;
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
    private final DicItemDomainRepository dicItemDomainRepository;
    private final DicDTOToCommandConverter converter = DicDTOToCommandConverter.INSTANCE;


    @Override
    @Transactional
    public Long createDic(CreateDicRequestDTO createDicRequestDTO) {
        if (dicDomainRepository.existsByTypeOrName(createDicRequestDTO.getType(), createDicRequestDTO.getName(), null)) {
            throw new FailedPreconditionException("dic name or type exists");
        }

        CreateDicCommand createDicCommand = converter.toCreateCommand(createDicRequestDTO);
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
        UpdateDicCommand updateDicCommand = converter.toUpdateCommand(updateDicRequestDTO);
        oldDic.update(updateDicCommand);

        dicDomainRepository.save(oldDic);
        dicRepository.removeCache(dicId);
        dicRepository.removeDicCacheByType(oldDic.getType());
    }

    @Override
    @Transactional
    public void deleteDic(Long dicId) {
        Dic oldDic = dicDomainRepository.findById(dicId);
        if (oldDic == null) {
            throw new NotFoundException("dic not found");
        }

        dicDomainRepository.logicDelete(dicId);
        dicRepository.removeCache(dicId);
        dicRepository.removeDicCacheByType(oldDic.getType());
    }

    @Override
    public PageDTO<DicDTO> listDics(QueryDicRequestDTO queryDTO) {
        return dicRepository.queryBy(queryDTO);
    }

    @Override
    public DicDTO getDic(Long dicId) {
        DicDTO dicDTO = dicRepository.queryById(dicId);
        if (dicDTO != null) {
            fillDicItems(List.of(dicDTO));
        }
        return dicDTO;
    }

    @Override
    public List<DicDTO> getDicByTypes(List<String> dicTypes) {
        List<DicDTO> dics = dicRepository.queryByTypes(dicTypes);
        fillDicItems(dics);
        return dics;
    }

    private void fillDicItems(List<DicDTO> dics) {
        if (CollectionUtils.isEmpty(dics)) {
            return;
        }

        List<DicItemDTO> dicItems = dicRepository.queryItemsByDicId(dics.stream()
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
    @Transactional
    public Long createDicItem(Long dicId, CreateDicItemRequestDTO requestDTO) {
        Dic dic = dicDomainRepository.findById(dicId);
        if (dic == null) {
            throw new NotFoundException("dic not found");
        }

        if (dicItemDomainRepository.existsByDisplayText(requestDTO.getDisplayText(), null)) {
            throw new FailedPreconditionException("dic item display text exists");
        }

        CreateDicItemCommand createDicItemCommand = DicItemDTOToCommandConverter.INSTANCE.toCreateCommand(requestDTO);
        createDicItemCommand.setDicId(dicId);

        DicItem dicItem = DicItem.create(createDicItemCommand);

        Long dicItemId = dicItemDomainRepository.save(dicItem).getId();

        dicRepository.removeDicItemsCacheByDicId(dic.getId());

        return dicItemId;
    }

    @Override
    @Transactional
    public void updateDicItem(Long dicId, Long itemId, UpdateDicItemRequestDTO requestDTO) {
        Dic dic = dicDomainRepository.findById(dicId);
        if (dic == null) {
            throw new NotFoundException("dic not found");
        }

        if (dicItemDomainRepository.existsByDisplayText(requestDTO.getDisplayText(), List.of(itemId))) {
            throw new FailedPreconditionException("dic item display text exists");
        }

        DicItem dicItem = dicItemDomainRepository.findById(itemId);
        if (dicItem == null) {
            throw new NotFoundException("dic item not found");
        }

        UpdateDicItemCommand updateDicItemCommand = DicItemDTOToCommandConverter.INSTANCE.toUpdateCommand(requestDTO);
        dicItem.update(updateDicItemCommand);

        dicItemDomainRepository.save(dicItem);

        dicRepository.removeDicItemsCacheByDicId(dic.getId());
    }
}
