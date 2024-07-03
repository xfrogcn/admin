package com.xfrog.framework.exception.validation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ValidationResultItem {
    private String member;
    private final Set<String> messages;

    public ValidationResultItem() {
        messages = new HashSet<>();
    }

    public ValidationResultItem(String member) {
        this.member = member;
        this.messages = new HashSet<>();
    }

    public String getMember() {
        return member;
    }

    public Set<String> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationResultItem that = (ValidationResultItem) o;
        return Objects.equals(member, that.member) && Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, messages);
    }
}
