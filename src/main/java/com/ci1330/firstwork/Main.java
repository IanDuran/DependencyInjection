package com.ci1330.firstwork;

import com.ci1330.firstwork.injector.AnnotationInjector;
import com.ci1330.firstwork.injector.Injector;
import com.ci1330.firstwork.injector.XMLInjector;

public class Main {
    public static void main(String... args) {
        Parser parser = new Parser("src/main/resources/test.xml");
        Injector injector;
        if(parser.getConfigType()){
            injector = new AnnotationInjector(parser);
        } else {
            injector = new XMLInjector(parser);
            Teacher t = (Teacher) injector.getBeanByName("teacher");
            System.out.println(t.getName());
        }
    }
}
