package com.example.model.core;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.stream.Stream;

@Data
public abstract class AbstractEntity<I> implements Persistable<I> {

    @Transient
    public boolean isNew = true;

    @Override
    public I getId() {
        return Stream.of(this.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                }).findFirst().map(field ->
                {
                    try {
                        return (I) field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).orElse(null);
    }

}
