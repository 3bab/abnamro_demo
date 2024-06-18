package com.abnamro.bank.service;

import com.abnamro.bank.domain.dto.TransactionDto;

public interface TransactionService {
    void transfer(TransactionDto transactionDto);
}
