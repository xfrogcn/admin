package com.xfrog.framework.exception.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ValidationResult extends ArrayList<ValidationResultItem> {

    private final HashMap<String, ValidationResultItem> memberResultMap =
            new HashMap<>();

    public void addResult(String member, String message) {
        ValidationResultItem resultItem = memberResultMap.computeIfAbsent(member, name -> {
           ValidationResultItem newItem = new ValidationResultItem(member);
           this.add(newItem);
           return newItem;
        });
        resultItem.getMessages().add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ValidationResult that = (ValidationResult) o;
        return Objects.equals(memberResultMap, that.memberResultMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), memberResultMap);
    }
}
