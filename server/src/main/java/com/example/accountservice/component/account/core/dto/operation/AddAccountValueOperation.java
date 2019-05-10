package com.example.accountservice.component.account.core.dto.operation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AddAccountValueOperation implements AccountOperation {

    private final Integer id;

    private final Long value;

    @JsonCreator
    public AddAccountValueOperation(@JsonProperty("id") Integer id,
                                    @JsonProperty("value") Long value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddAccountValueOperation that = (AddAccountValueOperation) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
