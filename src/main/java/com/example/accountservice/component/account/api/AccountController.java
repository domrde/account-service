package com.example.accountservice.component.account.api;

import com.example.accountservice.component.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

import static com.example.accountservice.common.URLs.ACCOUNT_URL;

@RestController
@RequestMapping(ACCOUNT_URL)
public class AccountController implements AccountServiceApi {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @GetMapping("/{id}")
    public Long getAmount(@PathVariable Integer id) {
        return accountService.getAmount(id);
    }

    @Override
    @PostMapping("/{id}")
    public void addAmount(@PathVariable Integer id, @RequestParam Long value) {
        accountService.addAmount(id, value);
    }
}
