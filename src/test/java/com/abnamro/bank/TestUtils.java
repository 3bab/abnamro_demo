package com.abnamro.bank;

import com.abnamro.bank.domain.Account;
import com.abnamro.bank.domain.Customer;
import com.abnamro.bank.domain.dto.AccountDto;
import com.abnamro.bank.domain.dto.CustomerDto;
import com.abnamro.bank.domain.dto.TransactionDto;
import com.abnamro.bank.domain.mapper.AccountMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class TestUtils {
    public static List<CustomerDto> createdCustomers() {
        List<CustomerDto> customers = new ArrayList<>();
        customers.add(createdOneCustomerDto());
        customers.add(createdOneCustomerDto());

        return customers;
    }

    public static TransactionDto createdTransaction() {
        return new TransactionDto(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal(100));
    }

    public static CustomerDto createdOneCustomerDto() {
        Account account = new Account();
        account.setUuid(UUID.randomUUID());
        Set<Account> accounts = new HashSet<>();
        accounts.add(account);
        return new CustomerDto(
                UUID.randomUUID(),
                "test_first_name",
                "test_last_name",
                Date.from(Instant.now()),
                AccountMapper.mapEntityToDto(accounts)) ;
    }

    public static Customer createdOneCustomer() {
        Account account = new Account();
        account.setUuid(UUID.randomUUID());
        Set<Account> accounts = new HashSet<>();
        accounts.add(account);
        return new Customer(
                UUID.randomUUID(),
                "test_first_name",
                "test_last_name",
                Date.from(Instant.now()),
                accounts) ;
    }

    public static CustomerDto createdCustomerDetails() {
        return new CustomerDto(
                UUID.randomUUID(),
                "test_first_name",
                "test_last_name",
                Date.from(Instant.now()),
                Set.of());
    }

    public static Account createdOneAccount() {
        Account account = new Account();
        account.setUuid(UUID.randomUUID());
        account.setBalance(new BigDecimal(100));
        return account;
    }

    public static AccountDto createdOneAccountDto() {
        return new AccountDto(UUID.randomUUID(), new BigDecimal(100), "number");
    }

    public static TransactionDto createdTransactionDto() {
        return createdTransactionDto(new BigDecimal(100));
    }

    public static TransactionDto createdTransactionDto(BigDecimal amount) {
        return new TransactionDto(UUID.randomUUID(), UUID.randomUUID(), amount);
    }

    public static String asJsonString(final Object obj) {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        try {
            return om.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
