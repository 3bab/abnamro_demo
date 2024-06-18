package com.abnamro.bank.service;

import com.abnamro.bank.domain.Account;
import com.abnamro.bank.domain.Transaction;
import com.abnamro.bank.domain.dto.TransactionDto;
import com.abnamro.bank.exception.AccountBalanceTooSmallException;
import com.abnamro.bank.exception.AccountNotFoundException;
import com.abnamro.bank.repository.AccountRepository;
import com.abnamro.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void transfer(TransactionDto transactionDto) {

        Optional<Account> fromAccountOptional = accountRepository.findById(transactionDto.fromAccountUuid());
        Optional<Account> toAccountOptional = accountRepository.findById(transactionDto.toAccountUuid());

        if (fromAccountOptional.isEmpty() || toAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("One of transaction accounts not found");
        }

        Account fromAccount = fromAccountOptional.get();
        Account toAccount = toAccountOptional.get();

        BigDecimal amount = transactionDto.amount();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new AccountBalanceTooSmallException("Account balance is too small");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transactionRepository.save(new Transaction(fromAccount, toAccount, amount, LocalDateTime.now()));
    }
}
