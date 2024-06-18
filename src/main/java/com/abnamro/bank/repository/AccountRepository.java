package com.abnamro.bank.repository;


import com.abnamro.bank.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountRepository extends CrudRepository <Account, UUID> {
}
