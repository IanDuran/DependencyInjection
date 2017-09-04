package com.ci1330.firstwork;

import com.ci1330.firstwork.annotations.Scope;

@Scope //default
public class Student {
    private String name;

    public Student(){
        this.name = "Cangri";
    }

    public String getName(){
        return name;
    }
}
