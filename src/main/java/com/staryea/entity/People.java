package com.staryea.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "people")
public class People {
    @Id
    private String id;
    @Field
    private Name name;
    @Field
    private Integer age;
    @Field
    private String addr;
    @Field
    private String school;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "People{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", age='" + age + '\'' +
                ", addr='" + addr + '\'' +
                ", school='" + school + '\'' +
                '}';
    }
}
