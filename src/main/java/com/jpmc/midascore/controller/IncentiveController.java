package com.jpmc.midascore.controller;

import com.jpmc.midascore.model.Incentive;
import com.jpmc.midascore.model.Transaction;
import org.springframework.web.bind.annotation.*;

@RestController
public class IncentiveController {

    @PostMapping("/incentive")
    public Incentive calculateIncentive(@RequestBody Transaction transaction) {
        // Dummy incentive logic: 1% of the transaction amount
        float incentiveAmount = transaction.getAmount() * 0.01f;
        return new Incentive(incentiveAmount);
    }
}

