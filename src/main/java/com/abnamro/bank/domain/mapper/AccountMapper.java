package com.abnamro.bank.domain.mapper;

import com.abnamro.bank.domain.Account;
import com.abnamro.bank.domain.dto.AccountDto;

import java.util.HashSet;
import java.util.Set;

public class AccountMapper {

    public static Iterable<AccountDto> mapEntityToDto(Iterable<Account> accountEntities) {
        Set<AccountDto> accounts = new HashSet<>();
        for (Account account : accountEntities) {
            accounts.add(mapEntityToDto(account));
        }

        return accounts;
    }

    public static AccountDto mapEntityToDto(Account account) {
        return new AccountDto(
                account.getUuid(),
                account.getBalance(),
                account.getAccountNumber()
        );
    }

    public static Account mapDtoToEntityToCreate(AccountDto accountDto) {
        return new Account(accountDto.balance(), accountDto.accountNumber());
    }
}
