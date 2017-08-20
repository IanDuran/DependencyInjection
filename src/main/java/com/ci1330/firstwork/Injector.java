package com.ci1330.firstwork;

import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class Injector {
    private Parser parser;

    //id -> clase
    private Map<String, Class> beanClassMap;

    // Objetos por clase
    private Map<Class, Object> beanObjectsByType;

    //Objetos por id
    private Map<String, Object> beanObjectsById;

    public Injector(String filepath) {
        this.parser = new Parser(filepath);
        this.beanClassMap = new HashMap<String, Class>();
        this.beanObjectsByType = new HashMap<Class, Object>();
        this.beanObjectsById = new HashMap<String, Object>();
        this.fillClassMap();
        this.initializeBeans();
        this.addDependencies();
        System.out.println();
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
                this.beanObjectsById.put(currEntry.getKey(), instance);
                this.beanObjectsByType.put(currEntry.getValue(), instance);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Initialized beans");
    }

    private void addDependencies(){
        Set<Map.Entry<String, Class>> entrySet = this.beanClassMap.entrySet();
        Iterator<Map.Entry<String, Class>> entryIterator = entrySet.iterator();
        while(entryIterator.hasNext()){
            Map.Entry<String, Class> currEntry = entryIterator.next();
            List<Pair<String, String>> list = this.parser.getBeanProperties(currEntry.getKey());
            if(list != null){
                for(int i = 0; i < list.size(); i++){
                    String propertyName = list.get(i).getKey();
                    String propertyReference = list.get(i).getValue();
                    try {
                        Field currField = currEntry.getValue().getDeclaredField(propertyName);
                        currField.setAccessible(true);
                        currField.set(beanObjectsById.get(currEntry.getKey()), beanObjectsById.get(propertyReference));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
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
