package com.example.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Customer {
    @Id
    private String identifier;
    private String name;
}
