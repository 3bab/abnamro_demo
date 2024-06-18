package com.abnamro.bank.service;

import com.abnamro.bank.domain.Account;
import com.abnamro.bank.domain.dto.TransactionDto;
import com.abnamro.bank.exception.AccountBalanceTooSmallException;
import com.abnamro.bank.exception.AccountNotFoundException;
import com.abnamro.bank.repository.AccountRepository;
import com.abnamro.bank.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.abnamro.bank.TestUtils.createdOneAccount;
import static com.abnamro.bank.TestUtils.createdTransactionDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Test create transaction")
    public void testCreateTransaction() throws AccountNotFoundException, AccountBalanceTooSmallException {
        when(accountRepository.findById(any())).thenReturn(Optional.of(createdOneAccount()));

        transactionService.transfer(new TransactionDto(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal(100)));

        verify(accountRepository, times(2)).save(any());
        verify(transactionRepository).save(any());
    }

    @Test
    @DisplayName("Test transfer amount throw account not found exception")
    public void testTransferAmount() throws AccountNotFoundException {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        AccountNotFoundException ex = Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.transfer(createdTransactionDto()));

        Assert.isTrue(ex.getMessage().equals("One of transaction accounts not found"), "Exception not correct");
    }

    @Test
    @DisplayName("Test transfer amount throw account balance too small exception")
    public void testTransferAmountTooSmall() throws AccountNotFoundException {
        Account accountFrom = createdOneAccount();
        Account accountTo = createdOneAccount();
        accountFrom.setBalance(new BigDecimal(100));

        when(accountRepository.findById(accountFrom.getUuid())).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findById(accountTo.getUuid())).thenReturn(Optional.of(accountTo));

        AccountBalanceTooSmallException ex = Assertions.assertThrows(
                AccountBalanceTooSmallException.class,
                () -> transactionService.transfer(
                        new TransactionDto(accountFrom.getUuid(), accountTo.getUuid(), new BigDecimal(101))));

        Assert.isTrue(ex.getMessage().equals("Account balance is too small"), "Exception not correct");
    }
}
