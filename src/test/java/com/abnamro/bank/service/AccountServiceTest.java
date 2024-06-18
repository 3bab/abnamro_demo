package com.abnamro.bank.service;

import com.abnamro.bank.domain.Account;
import com.abnamro.bank.domain.Customer;
import com.abnamro.bank.exception.AccountNotFoundException;
import com.abnamro.bank.exception.CustomerNotFoundException;
import com.abnamro.bank.repository.AccountRepository;
import com.abnamro.bank.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.abnamro.bank.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Test create account")
    public void testCreateAccount() throws CustomerNotFoundException {
        when(customerRepository.findById(any())).thenReturn(Optional.of(createdOneCustomer()));
        when(accountRepository.save(any())).thenReturn(createdOneAccount());

        accountService.createAccount(UUID.randomUUID(), createdOneAccountDto());

        verify(accountRepository).save(any());
        verify(customerRepository).save(any());
    }

    @Test
    @DisplayName("Test delete account")
    public void testDeleteAccount() throws CustomerNotFoundException, AccountNotFoundException {
        Customer customer = createdOneCustomer();
        UUID accountUUID = UUID.randomUUID();
        Set<Account> accounts = customer.getAccounts();
        accounts.add(new Account(accountUUID, null, null, null, null));
        customer.setAccounts(accounts);

        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        accountService.deleteCustomerAccount(UUID.randomUUID(), accountUUID);

        verify(accountRepository).delete(any());
        verify(customerRepository).save(any());
    }

    @Test
    @DisplayName("Test throw account not found")
    public void testTransferAmount() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(createdOneCustomer()));

        UUID accountUUID = UUID.randomUUID();

        AccountNotFoundException ex = Assertions.assertThrows(
                AccountNotFoundException.class, () -> accountService.deleteCustomerAccount(any(), accountUUID));

        Assert.isTrue(ex.getMessage().equals(String.format("Account with id %s not found", accountUUID)),
                "Exception not correct");
    }
}