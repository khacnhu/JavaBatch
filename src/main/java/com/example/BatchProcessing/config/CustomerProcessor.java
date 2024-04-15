package com.example.BatchProcessing.config;

import com.example.BatchProcessing.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.orm.hibernate5.HibernateTemplate;



public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
//        if (customer.getGender().equalsIgnoreCase("Female")) {
//            return customer;
//        }


        return customer;
    }
}
