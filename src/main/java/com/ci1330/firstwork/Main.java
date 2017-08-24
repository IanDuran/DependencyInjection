package com.ci1330.firstwork;

import com.ci1330.firstwork.annotations.Service;
import com.ci1330.firstwork.injector.Injector;
import com.ci1330.firstwork.injector.XMLInjector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Main {
    public static void main(String... args) {
        Parser parser = new Parser("src/main/resources/test.xml");
        Injector injector = new XMLInjector("src/main/resources/test.xml"); //src\main\resources\test.xml
        Group group = (Group)injector.getBeanByName("group");
        Teacher t = group.getTeacher();
        parser.getPackages();
        System.out.println(t.getName());
        /*Method[] methods = Parser.class.getDeclaredMethods();
        for(Method method:methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Service) {
                    Service a = (Service) annotation;
                    System.out.println(a.value());
                }
            }
        }*/
    }
}
