package com.abnamro.bank.controller;

import com.abnamro.bank.domain.dto.CustomerDto;
import com.abnamro.bank.service.CustomerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<Iterable<CustomerDto>> getCustomers() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customer) {
        CustomerDto createdCustomer = customerService.create(customer);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCustomer.uuid())
                .toUri();

        return ResponseEntity.created(uri)
                    .body(createdCustomer);
    }

    @PutMapping(value = "/{customerUUID}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(@PathVariable UUID customerUUID, @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(customerUUID, customerDto);
    }

    @DeleteMapping(value = "/{customerUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID customerUUID) {
        customerService.deleteCustomer(customerUUID);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Iterable<CustomerDto>> findCustomer(@RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String lastName) {
        if (firstName != null && lastName != null) {
            return ResponseEntity.ok().body(customerService.findCustomer(firstName, lastName));
        } else if (firstName != null) {
            return ResponseEntity.ok().body(customerService.findCustomerByFirstName(firstName));
        } else if (lastName != null) {
            return ResponseEntity.ok().body(customerService.findCustomerByLastName(lastName));
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
