package com.ci1330.firstwork;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Injector {
    private Parser parser;

    //id -> clase
    private Map<String, Class> beanClassMap;

    // propiedades de cada uno
    private Map<Class, Object> beanObjects;

    public Injector(String filepath) {
        this.parser = new Parser(filepath);
        this.beanClassMap = new HashMap<String, Class>();
        this.beanObjects = new HashMap<Class, Object>();
        this.fillClassMap();
        this.initializeBeans();
        printResults();
    }

    private void fillClassMap(){
        String[] beanIds = this.parser.getBeanNames();
        String[] beanClasses = this.parser.getBeanClasses();
        for (int i = 0; i < beanIds.length; i++) {
            try {
                this.beanClassMap.put(beanIds[i], Class.forName(beanClasses[i]));
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private void initializeBeans(){
        Set<Map.Entry<String, Class>> entrySet = this.beanClassMap.entrySet();
        Iterator<Map.Entry<String, Class>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Class> currEntry = iterator.next();
            try {
                Constructor constructor = currEntry.getValue().getConstructor();
                Object instance = constructor.newInstance();
                beanObjects.put(currEntry.getValue(), instance);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Initialized beans");
    }

    private void addDependencies(){

    }

    public void printResults(){
        Set<Map.Entry<String, Class>> entries = beanClassMap.entrySet();
        Iterator<Map.Entry<String, Class>> iterator = entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Class> currEntry = iterator.next();
            System.out.println("Bean name: " + currEntry.getKey());
            System.out.println("Bean class: " + currEntry.getValue().toString());
            System.out.println();
        }
    }
}
