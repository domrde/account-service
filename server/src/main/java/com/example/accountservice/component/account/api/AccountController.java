package com.example.accountservice.component.account.api;

import com.example.accountservice.component.account.service.BufferedAccountService;
import com.example.accountservice.component.metric.service.MetricService;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.*;

import static com.example.accountservice.common.URLs.ACCOUNT_URL;

@RestController
@RequestMapping(ACCOUNT_URL)
public class AccountController implements AccountServiceApi {

    private final BufferedAccountService accountService;

    public AccountController(BufferedAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @Timed(value = MetricService.RETRIEVE_METRIC_NAME)
    @GetMapping("/{id}")
    public Long getAmount(@PathVariable Integer id) {
        return accountService.getAmount(id);
    }

    @Override
    @Timed(value = MetricService.UPDATE_METRIC_NAME)
    @PostMapping("/{id}")
    public void addAmount(@PathVariable Integer id, @RequestParam Long value) {
        accountService.addAmount(id, value);
    }
}
