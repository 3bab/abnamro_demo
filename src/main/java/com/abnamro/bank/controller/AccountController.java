package com.abnamro.bank.controller;


import com.abnamro.bank.domain.dto.AccountDto;
import com.abnamro.bank.service.AccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers")
public class AccountController {

    private final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/{customerUUID}/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@PathVariable UUID customerUUID, @RequestBody AccountDto account) {
        AccountDto createdAccount = accountService.createAccount(customerUUID, account);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAccount.uuid())
                .toUri();

        return ResponseEntity.created(uri)
                .body(createdAccount);
    }

    @DeleteMapping(value = "/{customerUUID}/accounts/{accountUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerAccount(@PathVariable UUID customerUUID, @PathVariable UUID accountUUID) {
        accountService.deleteCustomerAccount(customerUUID, accountUUID);
    }
}
