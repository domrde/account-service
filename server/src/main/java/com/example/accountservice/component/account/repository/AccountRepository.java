package com.example.accountservice.component.account.repository;

import com.example.accountservice.component.account.dto.Account;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.accountservice.system.config.AppConfig.CACHE_NAME;

@Repository
public class AccountRepository {

    private static final String TABLE_NAME = "account";

    private final ResultSetExtractor<Optional<Account>> accountResultSetExtractor;

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        accountResultSetExtractor = rs -> {
            if (rs.next()) {
                return Optional.of(new Account(rs.getInt("id"), rs.getLong("value")));
            } else {
                return Optional.empty();
            }
        };
    }

    @CachePut(value = CACHE_NAME, key = "#id")
    @Transactional
    public Optional<Account> addAmount(Integer id, Long value) {
        jdbcTemplate.update("INSERT INTO " + TABLE_NAME + " AS a(id, value) VALUES (?, ?) " +
                            "ON CONFLICT (id) DO UPDATE SET value = a.value + excluded.value", id, value);
        return getAmount(id);
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    @Transactional(readOnly = true)
    public Optional<Account> getAmount(Integer id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", accountResultSetExtractor, id);
    }
}
