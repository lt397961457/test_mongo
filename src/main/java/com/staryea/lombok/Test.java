package com.staryea.lombok;

import lombok.experimental.var;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> teachers = new ArrayList<String>();
        Student student1 = new Student("aa",1,teachers);
        System.out.println(student1.getName());
        System.out.println(student1.getAge());
        System.out.println(student1.getTeachers());
        System.out.println(student1.toString());

//        var list = new ArrayList<String>();
    }
}
