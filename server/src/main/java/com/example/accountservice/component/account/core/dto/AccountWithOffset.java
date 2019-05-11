package com.example.accountservice.component.account.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AccountWithOffset extends Account {

    private final Long offset;

    @JsonCreator
    public AccountWithOffset(@JsonProperty("id") Integer id,
                             @JsonProperty("value") Long value,
                             @JsonProperty("offset") Long offset) {
        super(id, value);
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
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
        AccountWithOffset that = (AccountWithOffset) o;
        return Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), offset);
    }

    @Override
    public String toString() {
        return "AccountWithOffset{" +
               "id=" + getId() +
               ", value=" + getValue() +
               ", offset=" + offset +
               '}';
    }
}
