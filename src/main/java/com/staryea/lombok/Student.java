package com.staryea.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Student {
    private String name;
    private int age;
    @NonNull
    private List<String> teachers;
}
