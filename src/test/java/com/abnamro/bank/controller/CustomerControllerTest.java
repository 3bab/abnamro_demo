package com.abnamro.bank.controller;


import com.abnamro.bank.domain.dto.CustomerDto;
import com.abnamro.bank.service.AccountServiceImpl;
import com.abnamro.bank.service.CustomerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.abnamro.bank.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @MockBean
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Test get /customers GET")
    public void testGetCustomers() throws Exception {
        when(customerService.getAll()).thenReturn(createdCustomers());

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("Test create /customers POST")
    public void testCreateCustomer() throws Exception {
        CustomerDto customer = createdOneCustomerDto();
        when(customerService.create(any())).thenReturn(customer);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createdOneCustomer()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(
                        "Location",
                        org.hamcrest.Matchers.matchesPattern(".*/customers/" + customer.uuid())));
    }


    @Test
    @DisplayName("Test create /customers/{uuid}/accounts POST")
    public void testCreateCustomerAccount() throws Exception {
        CustomerDto customer = createdOneCustomerDto();
        when(accountService.createAccount(any(), any())).thenReturn(customer.accounts().iterator().next());

        mockMvc.perform(post(String.format("/customers/%s/accounts", customer.uuid()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createdOneCustomer()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(
                        "Location",
                        org.hamcrest.Matchers.matchesPattern(".*/customers/" + customer.uuid() + "/accounts/" +
                                customer.accounts().iterator().next().uuid())));
    }

    @Test
    @DisplayName("Test update /customers/{uuid} PUT")
    public void testUpdateCustomer() throws Exception {
        Mockito.doNothing().when(customerService).updateCustomer(isA(UUID.class), isA(CustomerDto.class));

        mockMvc.perform(put(String.format("/customers/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createdOneCustomer()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test delete /customers/{uuid} DELETE")
    public void testDeleteCustomer() throws Exception {
        Mockito.doNothing().when(customerService).updateCustomer(any(), any());

        mockMvc.perform(delete(String.format("/customers/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createdOneCustomer()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @MethodSource({"testSearchWithFirstAndLastName", "testSearchWithFirstName", "testSearchWithLastName"})
    @DisplayName("Test /search with first and last name parameters GET")
    public void testCustomerSearch(String query) throws Exception {
        CustomerDto customer = createdOneCustomerDto();
        when(customerService.findCustomer(any(), any())).thenReturn(List.of(customer));

        mockMvc.perform(get("/customers/search?" + query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private static Stream<Arguments> testSearchWithFirstAndLastName() {
        return Stream.of(Arguments.of("firstName=test_first_name&lastName=test_last_name"));
    }

    private static Stream<Arguments> testSearchWithFirstName() {
        return Stream.of(Arguments.of("firstName=test_first_name"));
    }

    private static Stream<Arguments> testSearchWithLastName() {
        return Stream.of(Arguments.of("lastName=test_last_name"));
    }
}
