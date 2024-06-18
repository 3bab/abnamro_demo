package com.abnamro.bank.domain.mapper;

import com.abnamro.bank.domain.Customer;
import com.abnamro.bank.domain.dto.CustomerDto;

import java.util.HashSet;
import java.util.Set;

public class CustomerMapper {

    public static Iterable<CustomerDto> mapEntityToCustomerDto(Iterable<Customer> customerEntities) {
        Set<CustomerDto> customers = new HashSet<>();
        for (Customer customer : customerEntities) {
            customers.add(mapEntityToCustomerDto(customer));
        }

        return customers;
    }

    public static CustomerDto mapEntityToCustomerDto(Customer customer) {
        return new CustomerDto(
            customer.getUuid(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getDateOfBirth(),
            AccountMapper.mapEntityToDto(customer.getAccounts()));
    }

    public static CustomerDto mapEntityToCustomerDtoToCreate(Customer customer) {
        return new CustomerDto(
                customer.getUuid(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDateOfBirth(),
                Set.of());
    }

    public static Customer mapDtoToEntityToCreate(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.firstName());
        customer.setLastName(customerDto.lastName());
        customer.setDateOfBirth(customerDto.dateOfBirth());

        return customer;
    }
}
