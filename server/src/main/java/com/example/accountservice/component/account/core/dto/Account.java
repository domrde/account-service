package com.example.accountservice.component.account.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Account {

    private final Integer id;

    private final Long value;

    @JsonCreator
    public Account(@JsonProperty("id") Integer id,
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
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
               Objects.equals(value, account.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

    @Override
    public String toString() {
        return "Account{" +
               "id=" + id +
               ", value=" + value +
               '}';
    }
}
