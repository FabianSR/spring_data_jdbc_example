package com.example.repository;

import com.example.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.BDDAssertions.then;

@Testcontainers
@SpringBootTest
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository repository;

    @Container
    static GenericContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("dbScript.sql");

    @DynamicPropertySource
    static void containerProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> ((PostgreSQLContainer) postgreSQLContainer).getJdbcUrl());
        registry.add("spring.datasource.password", () -> ((PostgreSQLContainer) postgreSQLContainer).getPassword());
        registry.add("spring.datasource.name", () -> ((PostgreSQLContainer) postgreSQLContainer).getUsername());
    }

    @Test
    public void givenNewCustomer_shouldSaveCustomerInDataBase() {
        //given
        final Customer newCustomer = new Customer();
        newCustomer.setIdentifier("0002");
        newCustomer.setName("juan");
        //when
        Customer customerSaved = repository.save(newCustomer);
        //then
        then(customerSaved).isNotNull();
    }

    @Test
    public void givenOldCustomer_shouldUpdateCustomerInDataBase() {
        //Given
        final Customer oldCustomer = repository.findById("0001").orElseThrow(AssertionError::new);
        oldCustomer.setName(oldCustomer.getName() + " hall");
        //when
        repository.save(oldCustomer);
        //then
        then(repository.findById("0001").map(Customer::getName).map("kevin hall"::equals).orElse(false)).isTrue();
    }
}
