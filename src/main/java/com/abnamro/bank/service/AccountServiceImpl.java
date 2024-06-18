package com.abnamro.bank.service;

import com.abnamro.bank.domain.Account;
import com.abnamro.bank.domain.Customer;
import com.abnamro.bank.domain.Transaction;
import com.abnamro.bank.domain.dto.AccountDto;
import com.abnamro.bank.exception.AccountNotFoundException;
import com.abnamro.bank.exception.CustomerNotFoundException;
import com.abnamro.bank.repository.AccountRepository;
import com.abnamro.bank.repository.CustomerRepository;
import com.abnamro.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.abnamro.bank.domain.mapper.AccountMapper.mapDtoToEntityToCreate;
import static com.abnamro.bank.domain.mapper.AccountMapper.mapEntityToDto;

@Service
public class AccountServiceImpl implements AccountService{

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public AccountDto createAccount(UUID customerUUID, AccountDto accountDto) {
        Customer customer = findCustomerOrThrow(customerUUID);
        Account account = mapDtoToEntityToCreate(accountDto);

        account.setCustomer(customer);
        //account.setCustomertUuid(customerUUID);
        customer.getAccounts().add(account);

        account = accountRepository.save(account);
        customerRepository.save(customer);

        return mapEntityToDto(account);
    }

    @Override
    @Transactional
    public void deleteCustomerAccount(UUID customerUUID, UUID accountUUID) {
        Customer customer = findCustomerOrThrow(customerUUID);

        Account account = findAccountOrThrow(customer, accountUUID);

        Set<Account> accounts = customer.getAccounts();
        accounts.remove(account);
        customer.setAccounts(accounts);

        customerRepository.save(customer);
        accountRepository.delete(account);
    }

    private Account findAccountOrThrow(Customer customer, UUID accountUUID) throws AccountNotFoundException {
        return customer.getAccounts().stream()
                .filter(account -> account.getUuid().equals(accountUUID))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", accountUUID)));
    }

    private Customer findCustomerOrThrow(UUID customerUUID) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerUUID);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(String.format("Customer with id %s not found", customerUUID));
        }

        return customer.get();
    }
}
