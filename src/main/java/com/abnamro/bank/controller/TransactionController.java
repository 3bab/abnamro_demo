package com.abnamro.bank.controller;

import com.abnamro.bank.domain.dto.TransactionDto;
import com.abnamro.bank.service.TransactionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final TransactionServiceImpl transactionService;


    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;

    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionDto transactionDto) {
        transactionService.transfer(transactionDto);

        return ResponseEntity.ok("Transfer successful");
    }
}
