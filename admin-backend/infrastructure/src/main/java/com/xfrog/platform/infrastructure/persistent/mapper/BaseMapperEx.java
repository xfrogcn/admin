package com.xfrog.platform.infrastructure.persistent.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xfrog.framework.po.IdPO;
import org.springframework.util.CollectionUtils;

import java.util.List;

public interface BaseMapperEx<T extends IdPO> extends BaseMapper<T> {
    default int save(T entity) {
        return save(entity, false);
    }

    default int save(T entity, boolean check) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            if (check) {
                if (selectById(entity.getId()) == null) {
                    return insert(entity);
                }
            }
            return updateById(entity);
        }
    }

    default void saveAll(List<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }

        entities.forEach(this::save);
    }
}
