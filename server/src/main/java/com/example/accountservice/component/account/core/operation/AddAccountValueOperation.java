package com.example.accountservice.component.account.core.operation;

import com.example.accountservice.component.account.core.dto.Account;

import java.util.Objects;

public class AddAccountValueOperation implements AccountOperation {

    private final Integer id;

    private final Long value;

    public AddAccountValueOperation(Integer id, Long value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public Account apply(Account existing) {
        return new Account(existing.getId(), existing.getValue() + getValue());
    }

    @Override
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
