package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.infrastructure.base.mapper.DicMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DicRepositoryImpl implements DicRepository {
    private final DicMapper dicMapper;

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
}