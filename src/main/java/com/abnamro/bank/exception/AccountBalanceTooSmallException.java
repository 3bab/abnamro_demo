package com.abnamro.bank.exception;

public class AccountBalanceTooSmallException extends RuntimeException {
    public AccountBalanceTooSmallException(String message) {
        super(message);
    }
}
