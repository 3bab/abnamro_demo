package com.abnamro.bank.service;

import com.abnamro.bank.domain.dto.CustomerDto;

import java.util.UUID;

public interface CustomerService {
    CustomerDto create(CustomerDto customer);

    Iterable<CustomerDto> getAll();

    void updateCustomer(UUID customerUUID, CustomerDto customerDetails);

    void deleteCustomer(UUID customerUUID);

    Iterable<CustomerDto> findCustomer(String firstName, String lastName);

    Iterable<CustomerDto> findCustomerByFirstName(String firstName);

    Iterable<CustomerDto> findCustomerByLastName(String lastName);

}
