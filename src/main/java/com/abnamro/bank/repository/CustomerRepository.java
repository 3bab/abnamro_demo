package com.abnamro.bank.repository;

import com.abnamro.bank.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {
    public Iterable<Customer> findByFirstName(String firstName);
    public Iterable<Customer> findByLastName(String lastName);
    public Iterable<Customer> findByFirstNameAndLastName(@Nullable String firstName, @Nullable String lastName);
}
