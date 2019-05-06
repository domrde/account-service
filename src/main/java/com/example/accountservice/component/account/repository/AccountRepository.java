package com.example.accountservice.component.account.repository;

import com.example.accountservice.component.account.dto.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class AccountRepository {

    private static final String TABLE_NAME = "account";

    private final ResultSetExtractor<Optional<Account>> accountResultSetExtractor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AccountRepository() {
        accountResultSetExtractor = rs -> {
            if (rs.next()) {
                return Optional.of(new Account(rs.getInt("id"), rs.getLong("value")));
            } else {
                return Optional.empty();
            }
        };
    }

    @Transactional
    public void addAmount(Integer id, Long value) {
        jdbcTemplate.update("INSERT INTO " + TABLE_NAME + " AS a(id, value) VALUES (?, ?) " +
                            "ON CONFLICT (id) DO UPDATE SET value = a.value + excluded.value", id, value);
    }

    @Transactional(readOnly = true)
    public Optional<Account> retrieve(Integer id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", accountResultSetExtractor, id);
    }
}
