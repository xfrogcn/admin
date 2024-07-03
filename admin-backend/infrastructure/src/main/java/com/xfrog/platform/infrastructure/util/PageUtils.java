package com.xfrog.platform.infrastructure.util;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.xfrog.framework.common.SortOrder;
import com.xfrog.framework.dto.PageQueryDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class PageUtils {

    public static <T> PageDTO<T> page(PageQueryDTO pageQueryDTO) {
        return page(pageQueryDTO, null);
    }

    public static <T> PageDTO<T> page(PageQueryDTO pageQueryDTO, Map<String, String> sortFieldMap) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setCurrent(pageQueryDTO.getPageNum());
        pageDTO.setSize(pageQueryDTO.getPageSize());
        if (pageQueryDTO.getSortItems() != null) {
            pageDTO.setOrders(pageQueryDTO.getSortItems().stream()
                    .filter(Objects::nonNull)
                    .map(item -> SortOrder.ASC.equals(item.getOrder())
                            ? OrderItem.asc(getOrderColumnName(item.getField(), sortFieldMap))
                            : OrderItem.desc(getOrderColumnName(item.getField(), sortFieldMap))
                    ).toList());

        }
        return pageDTO;
    }

    private static String getOrderColumnName(String fieldName, Map<String, String> sortFieldMap) {
        if (sortFieldMap == null) {
            return fieldName;
        }
        return sortFieldMap.getOrDefault(fieldName, fieldName);
    }

   public static <T, P> com.xfrog.framework.dto.PageDTO<T> result(Page<P> pageDTO, List<T> list) {
       com.xfrog.framework.dto.PageDTO<T> result = new com.xfrog.framework.dto.PageDTO<>();
       result.setPageSize(pageDTO.getSize());
       result.setPageNum(pageDTO.getCurrent());
       result.setTotal(pageDTO.getTotal());
       result.setPages(pageDTO.getPages());
       result.setData(list);
       return result;
   }
}
