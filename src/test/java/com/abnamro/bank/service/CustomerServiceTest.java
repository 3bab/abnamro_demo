package com.abnamro.bank.service;

import com.abnamro.bank.exception.CustomerNotFoundException;
import com.abnamro.bank.repository.AccountRepository;
import com.abnamro.bank.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

import static com.abnamro.bank.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    @DisplayName("Test create customer")
    public void testCreateCustomer() {
        when(customerRepository.save(any())).thenReturn(createdOneCustomer());

        customerService.create(createdOneCustomerDto());

        verify(customerRepository).save(any());
    }

    @Test
    @DisplayName("Test get all customers")
    public void testGetCustomers() {
        customerService.getAll();

        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Test update customer")
    public void testUpdateCustomer() throws CustomerNotFoundException {
        when(customerRepository.findById(any())).thenReturn(Optional.of(createdOneCustomer()));

        customerService.updateCustomer(UUID.randomUUID(), createdCustomerDetails());

        verify(customerRepository).save(any());
    }

    @Test
    @DisplayName("Test delete customer")
    public void testDeleteCustomer() throws CustomerNotFoundException {
        when(customerRepository.findById(any())).thenReturn(Optional.of(createdOneCustomer()));

        customerService.deleteCustomer(UUID.randomUUID());

        verify(customerRepository).delete(any());
        verify(accountRepository).deleteAll(any());
    }

    @Test
    @DisplayName("Test find customer by first and last names")
    public void testFinCustomerByFirstAndLastNames() {
        customerService.findCustomer("test", "test");

        verify(customerRepository).findByFirstNameAndLastName(any(), any());
    }

    @Test
    @DisplayName("Test find customer by first name")
    public void testFinCustomerByFirstName() {
        customerService.findCustomerByFirstName("test");

        verify(customerRepository).findByFirstName(any());
    }

    @Test
    @DisplayName("Test find customer by last name")
    public void testFinCustomerByLastName() {
        customerService.findCustomerByLastName("test");

        verify(customerRepository).findByLastName(any());
    }

    @Test
    @DisplayName("Test throw customer not found")
    public void testTransferAmount() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        UUID customerUUID = UUID.randomUUID();

        CustomerNotFoundException ex = Assertions.assertThrows(
                CustomerNotFoundException.class, () -> customerService.updateCustomer(customerUUID, any()));

        Assert.isTrue(ex.getMessage().equals(String.format("Customer with id %s not found", customerUUID)),
                "Exception not correct");
    }
}
