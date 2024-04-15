package com.example.BatchProcessing.repo;

import com.example.BatchProcessing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
