package com.example.accountservice.component.account.core.repository;

import com.example.accountservice.component.account.core.dto.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public void batchReplace(List<Account> updatedAccounts) {
        String sql = "INSERT INTO " + TABLE_NAME + " AS a(id, value) VALUES (?, ?) " +
                     "ON CONFLICT (id) DO UPDATE SET value = excluded.value";

        List<Object[]> args = updatedAccounts.stream()
                .map(op -> new Object[]{op.getId(), op.getValue()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, args);
    }

    @Transactional(readOnly = true)
    public Optional<Account> getAccount(Integer id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", accountResultSetExtractor, id);
    }
}
