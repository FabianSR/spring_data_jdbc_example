package com.example.model;

import com.example.model.core.AbstractEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Customer extends AbstractEntity<String> {
    @Id
    private String identifier;
    private String name;
}
