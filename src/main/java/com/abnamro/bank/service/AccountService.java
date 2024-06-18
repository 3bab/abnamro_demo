package com.abnamro.bank.service;

import com.abnamro.bank.domain.dto.AccountDto;

import java.util.UUID;

public interface AccountService {
    AccountDto createAccount(UUID customerUUID, AccountDto account);

    void deleteCustomerAccount(UUID customerUUID, UUID accountUUID);
}
