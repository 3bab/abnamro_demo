package com.abnamro.bank.service;

import com.abnamro.bank.domain.Customer;
import com.abnamro.bank.domain.dto.CustomerDto;
import com.abnamro.bank.exception.CustomerNotFoundException;
import com.abnamro.bank.repository.AccountRepository;
import com.abnamro.bank.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.abnamro.bank.domain.mapper.CustomerMapper.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public CustomerDto create(CustomerDto customerDto) {
        Customer customer = mapDtoToEntityToCreate(customerDto);
        return mapEntityToCustomerDtoToCreate(customerRepository.save(customer));
    }

    @Override
    public Iterable<CustomerDto> getAll() {
        return mapEntityToCustomerDto(customerRepository.findAll());
    }

    @Override
    public void updateCustomer(UUID customerUUID, CustomerDto customerDto) {
        Customer customer = findCustomerOrThrow(customerUUID);
        updateAndSaveCustomerDetails(customer, customerDto);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerUUID) throws CustomerNotFoundException {
        Customer customer = findCustomerOrThrow(customerUUID);

        accountRepository.deleteAll(customer.getAccounts());
        customerRepository.delete(customer);
    }

    @Override
    public Iterable<CustomerDto> findCustomer(String firstName, String lastName) {
        return mapEntityToCustomerDto(customerRepository.findByFirstNameAndLastName(firstName, lastName));
    }

    @Override
    public Iterable<CustomerDto> findCustomerByFirstName(String firstName) {
        return mapEntityToCustomerDto(customerRepository.findByFirstName(firstName));
    }

    @Override
    public Iterable<CustomerDto> findCustomerByLastName(String lastName) {
        return mapEntityToCustomerDto(customerRepository.findByLastName(lastName));
    }

    private Customer findCustomerOrThrow(UUID customerUUID) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerUUID);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(String.format("Customer with id %s not found", customerUUID));
        }

        return customer.get();
    }

    private void updateAndSaveCustomerDetails(Customer customer, CustomerDto customerDto) {
        customer.setFirstName(customerDto.firstName());
        customer.setLastName(customerDto.lastName());
        customer.setDateOfBirth(customerDto.dateOfBirth());

        customerRepository.save(customer);
    }
}
