package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.infrastructure.base.mapper.DicMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DicRepositoryImpl implements DicRepository {
    private final DicMapper dicMapper;

    @Override
    public PageDTO<DicDTO> queryAll(QueryDicRequestDTO queryDTO) {
        Page<DicDTO> page = PageUtils.page(queryDTO);
        List<DicDTO> dicDTOS = dicMapper.queryAll(queryDTO);
        return PageUtils.result(page, dicDTOS);
    }
}