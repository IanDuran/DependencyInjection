package com.ci1330.firstwork;

import com.ci1330.firstwork.annotations.Autowired;
import com.ci1330.firstwork.annotations.Scope;

@Scope("prototype")
public class Teacher {

    @Autowired
    private Student pupil;

    private String name;


    public Teacher(){
        this.name = "Carlos Vargas";
    }


    public String getName() {
        return this.name;
    }

    public String getPupilName() {
        return this.pupil.getName();
    }
}
