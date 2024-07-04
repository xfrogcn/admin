package com.xfrog.framework.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Min(1)
    private Integer pageNum;
    @NotNull
    @Min(1)
    @Max(300)
    private Integer pageSize;
    @Valid
    private List<SortItem> sortItems;
}
