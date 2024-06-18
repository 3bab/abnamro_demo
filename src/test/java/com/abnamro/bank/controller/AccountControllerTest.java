package com.abnamro.bank.controller;

import com.abnamro.bank.domain.dto.CustomerDto;
import com.abnamro.bank.service.AccountServiceImpl;
import com.abnamro.bank.service.CustomerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.abnamro.bank.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @MockBean
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Test create /customers/{uuid}/accounts POST")
    public void testCreateCustomerAccount() throws Exception {
        CustomerDto customer = createdOneCustomerDto();
        Mockito.when(accountService.createAccount(any(), any())).thenReturn(customer.accounts().iterator().next());

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
    @DisplayName("Test delete /customers/{uuid}/accounts/{uuid} DELETE")
    public void testDeleteAccount() throws Exception {
        Mockito.doNothing().when(accountService).deleteCustomerAccount(isA(UUID.class), isA(UUID.class));

        mockMvc.perform(delete(String.format("/customers/%s/accounts/%s", UUID.randomUUID(), UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createdOneCustomer()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
