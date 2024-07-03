package com.xfrog.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryDTO {
    private Integer pageNum;
    private Integer pageSize;
    private List<SortItem> sortItems;
}
