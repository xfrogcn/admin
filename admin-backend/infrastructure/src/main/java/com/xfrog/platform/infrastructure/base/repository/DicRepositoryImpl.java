package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.xfrog.platform.infrastructure.persistent.repository.BasePageableApplicationRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class DicRepositoryImpl extends BasePageableApplicationRepository<DicDTO, DicPO, DicMapper, QueryDicRequestDTO>
        implements DicRepository {

    private final DicItemMapper dicItemMapper;

    public DicRepositoryImpl(DicMapper dicMapper, DicItemMapper dicItemMapper) {
        super(dicMapper, DicPOConverter.INSTANCE);
        this.dicItemMapper = dicItemMapper;
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "dic.created_time",
                    "type", "dic.type",
                    "name", "dic.name"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }

    //    @Override
//    public PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO) {
//        Page<DicDTO> page = PageUtils.page(queryDTO, ORDER_FIELD_MAP);
//        List<DicDTO> dicDTOS = dicMapper.queryAll(queryDTO, page);
//        return PageUtils.result(page, dicDTOS);
//    }

//    @Override
//    public DicDTO queryById(Long id) {
//        DicPO dicPO = dicMapper.selectById(id);
//        return DicPOConverter.INSTANCE.toDTO(dicPO);
//    }

    @Override
    public List<DicDTO> queryByTypes(List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return new LinkedList<>();
        }

        LambdaQueryWrapper<DicPO> queryWrapper = new LambdaQueryWrapper<DicPO>()
                .in(DicPO::getType, types)
                .eq(DicPO::getDeleted, false);

        return DicPOConverter.INSTANCE.toDTOList(mapper.selectList(queryWrapper));
    }

    @Override
    public List<DicItemDTO> queryItemsByDicId(List<Long> dicIds) {
        if (CollectionUtils.isEmpty(dicIds)) {
            return new LinkedList<>();
        }

        LambdaQueryWrapper<DicItemPO> queryWrapper = new LambdaQueryWrapper<DicItemPO>()
                .in(DicItemPO::getDicId, dicIds)
                .eq(DicItemPO::getDeleted, false);

        return DicItemPOConverter.INSTANCE.toDTOList(dicItemMapper.selectList(queryWrapper));
    }
}