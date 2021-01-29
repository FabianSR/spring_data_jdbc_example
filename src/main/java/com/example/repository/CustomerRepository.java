package com.example.repository;

import com.example.model.Customer;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

    @Modifying
    default Customer merge(final Customer customer) {
        return this.findById(customer.getId()).map(
                c -> {
                    customer.setNew(false);
                    return this.save(customer);
                }
        ).orElse(this.save(customer));
    }
}
