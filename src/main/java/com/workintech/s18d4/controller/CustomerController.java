package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.CustomerService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/customer", "/customers"})
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public CustomerResponse find(@PathVariable Long id) {
        return toResponse(customerService.find(id));
    }

    @PostMapping
    public CustomerResponse saveCustomer(@RequestBody Customer customer) {
        syncAddress(customer);
        syncAccounts(customer);
        return toResponse(customerService.save(customer));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        syncAddress(customer);
        syncAccounts(customer);
        return toResponse(customerService.save(customer));
    }

    @DeleteMapping("/{id}")
    public CustomerResponse remove(@PathVariable Long id) {
        return toResponse(customerService.delete(id));
    }

    private CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary());
    }

    private void syncAddress(Customer customer) {
        if (customer.getAddress() != null) {
            customer.getAddress().setCustomer(customer);
        }
    }

    private void syncAccounts(Customer customer) {
        if (customer.getAccounts() != null) {
            customer.getAccounts().forEach(account -> account.setCustomer(customer));
        }
    }
}
