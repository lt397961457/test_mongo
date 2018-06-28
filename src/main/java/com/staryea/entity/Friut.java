package com.staryea.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document(collection = "friut")
public class Friut {
    @Field
    private String username;
    @Field
    private String[] fruit;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getFruit() {
        return fruit;
    }

    public void setFruit(String[] fruit) {
        this.fruit = fruit;
    }

    @Override
    public String toString() {
        return "Friut{" +
                "username='" + username + '\'' +
                ", fruit=" + Arrays.toString(fruit) +
                '}';
    }
}
