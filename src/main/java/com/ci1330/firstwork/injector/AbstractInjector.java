package com.ci1330.firstwork.injector;

import com.ci1330.firstwork.Parser;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInjector implements Injector{

    protected Parser parser;

    //id -> clase
    protected Map<String, Class> beanClassMap;

    // Objetos por clase
    protected Map<Class, Object> beanObjectsByType;

    //Objetos por id
    protected Map<String, Object> beanObjectsById;

    protected AbstractInjector(String filepath){
        this.parser = new Parser(filepath);
        this.beanClassMap = new HashMap<String, Class>();
        this.beanObjectsByType = new HashMap<Class, Object>();
        this.beanObjectsById = new HashMap<String, Object>();
    }

    public void fillClassMap() {

    }

    public void initializeBeans() {

    }

    public void addDependencies() {

    }

    public Object getBeanByName(String beanName) {
        return null;
    }

    public Object getBeanByType(String beanType) {
        return null;
    }
}
