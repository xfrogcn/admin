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
public class PageDTO<T> {
    private Long pageNum;
    private Long pageSize;
    private Long total;
    private Long pages;
    private List<T> data;
}
