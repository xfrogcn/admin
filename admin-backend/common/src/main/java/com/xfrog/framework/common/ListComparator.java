package com.xfrog.framework.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class ListComparator {
    @AllArgsConstructor
    @Getter
    public static class CompareResult<T, U> {
        private final List<U> added;
        private final List<T> updated;
        private final List<T> removed;
    }

    public static <T, U> CompareResult<T, U> compare(List<T> oldList, List<U> newList, BiFunction<T, U, Boolean> checker) {
        List<U> added = new ArrayList<>();
        List<T> updated = new ArrayList<>();
        List<T> removed = new ArrayList<>();

        // oldList中不存在的
        List<T> actualOldList = oldList == null ? new ArrayList<>() :  oldList;
        List<U> actualNewList = newList == null ? new ArrayList<>() :  newList;

        removed = actualOldList.stream()
                .filter(oldItem -> actualNewList.stream().noneMatch(newItem -> checker.apply(oldItem, newItem)))
                .collect(Collectors.toList());
        added = actualNewList.stream()
                .filter(newItem -> actualOldList.stream().noneMatch(oldItem -> checker.apply(oldItem, newItem)))
                .collect(Collectors.toList());
        updated = actualOldList.stream()
                .filter(oldItem -> actualNewList.stream().anyMatch(newItem -> checker.apply(oldItem, newItem)))
                .collect(Collectors.toList());

        return new CompareResult<>(added, updated, removed);
    }
}
