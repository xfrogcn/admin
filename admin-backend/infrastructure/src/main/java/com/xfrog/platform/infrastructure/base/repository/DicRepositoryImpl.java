package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.infrastructure.base.converter.DicItemPOConverter;
import com.xfrog.platform.infrastructure.base.converter.DicPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.DicItemPO;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import com.xfrog.platform.infrastructure.base.mapper.DicItemMapper;
import com.xfrog.platform.infrastructure.base.mapper.DicMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DicRepositoryImpl implements DicRepository {
    private final DicMapper dicMapper;
    private final DicItemMapper dicItemMapper;

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "dic.created_time",
                    "type", "dic.type",
                    "name", "dic.name"));

    @Override
    public PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO) {
        Page<DicDTO> page = PageUtils.page(queryDTO, ORDER_FIELD_MAP);
        List<DicDTO> dicDTOS = dicMapper.queryAll(queryDTO, page);
        return PageUtils.result(page, dicDTOS);
    }

    @Override
    public DicDTO findById(Long id) {
        DicPO dicPO = dicMapper.selectById(id);
        return DicPOConverter.INSTANCE.toDTO(dicPO);
    }

    @Override
    public List<DicDTO> findByTypes(List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return new LinkedList<>();
        }

        LambdaQueryWrapper<DicPO> queryWrapper = new LambdaQueryWrapper<DicPO>()
                .in(DicPO::getType, types)
                .eq(DicPO::getDeleted, false);

        return DicPOConverter.INSTANCE.toDTOList(dicMapper.selectList(queryWrapper));
    }

    @Override
    public List<DicItemDTO> findItemsByDicId(List<Long> dicIds) {
        if (CollectionUtils.isEmpty(dicIds)) {
            return new LinkedList<>();
        }

        LambdaQueryWrapper<DicItemPO> queryWrapper = new LambdaQueryWrapper<DicItemPO>()
                .in(DicItemPO::getDicId, dicIds)
                .eq(DicItemPO::getDeleted, false);

        return DicItemPOConverter.INSTANCE.toDTOList(dicItemMapper.selectList(queryWrapper));
    }
}