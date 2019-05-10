package com.example.accountservice.component.account.amount.api;

import com.example.accountservice.component.account.amount.service.AmountService;
import com.example.accountservice.component.metric.service.MetricService;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.*;

import static com.example.accountservice.common.URLs.ACCOUNT_AMOUNT_URL;

@RestController
@RequestMapping(ACCOUNT_AMOUNT_URL)
public class AmountController implements AccountServiceApi {

    private final AmountService amountService;

    public AmountController(AmountService amountService) {
        this.amountService = amountService;
    }

    @Override
    @Timed(value = MetricService.RETRIEVE_METRIC_NAME)
    @GetMapping("/{id}")
    public Long getAmount(@PathVariable Integer id) {
        return amountService.getAmount(id);
    }

    @Override
    @Timed(value = MetricService.UPDATE_METRIC_NAME)
    @PutMapping("/{id}")
    public void addAmount(@PathVariable Integer id, @RequestParam Long value) {
        amountService.addAmount(id, value);
    }
}
