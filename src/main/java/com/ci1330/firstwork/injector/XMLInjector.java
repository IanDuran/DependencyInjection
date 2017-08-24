package com.ci1330.firstwork.injector;

import com.ci1330.firstwork.Parser;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class XMLInjector extends AbstractInjector{


    /**
     * Create parser instance, get the 3 structures from it, fill class map, initialize beans and add dependencies to them.
     * @param filepath the path of the XML file to be parsed.
     */
    public XMLInjector(String filepath) {
        super(filepath);
        this.fillClassMap();
        this.initializeBeans();
        this.addDependencies();
        System.out.println();
    }

    /**
     * Fill class map with data from the arrays of the parser.
     */
    @Override
    public void fillClassMap(){
        String[] beanIds = super.parser.getBeanNames();
        String[] beanClasses = super.parser.getBeanClasses();
        for (int i = 0; i < beanIds.length; i++) {
            try {
                super.beanClassMap.put(beanIds[i], Class.forName(beanClasses[i]));
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Initialize beans from bean class map.
     */
    @Override
    public void initializeBeans(){
        Set<Map.Entry<String, Class>> entrySet = super.beanClassMap.entrySet();
        Iterator<Map.Entry<String, Class>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Class> currEntry = iterator.next();
            try {
                Constructor constructor = currEntry.getValue().getConstructor();
                Object instance = constructor.newInstance();
                super.beanObjectsById.put(currEntry.getKey(), instance);
                super.beanObjectsByType.put(currEntry.getValue(), instance);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Initialized beans");
    }

    /**
     * Add dependencies to the beans from bean class map.
     */
    @Override
    public void addDependencies(){
        Set<Map.Entry<String, Class>> entrySet = super.beanClassMap.entrySet();
        Iterator<Map.Entry<String, Class>> entryIterator = entrySet.iterator();
        while(entryIterator.hasNext()){
            Map.Entry<String, Class> currEntry = entryIterator.next();
            List<Pair<String, String>> list = super.parser.getBeanProperties(currEntry.getKey());
            if(list != null){
                for(int i = 0; i < list.size(); i++){
                    String propertyName = list.get(i).getKey();
                    String propertyReference = list.get(i).getValue();
                    try {
                        Field currField = currEntry.getValue().getDeclaredField(propertyName);
                        currField.setAccessible(true);
                        currField.set(super.beanObjectsById.get(currEntry.getKey()), super.beanObjectsById.get(propertyReference));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Return a bean matching the given name.
     * @param beanName the bean name.
     * @return the resulting bean.
     */
    @Override
    public Object getBeanByName(String beanName){
        return super.beanObjectsById.get(beanName);
    }

    /**
     * Return a bean matching the given type.
     * @param beanType the bean name.
     * @return the resulting bean.
     */
    @Override
    public Object getBeanByType(String beanType){
        try {
            Class beanClass = Class.forName(beanType);
            return super.beanObjectsByType.get(beanClass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void printResults(){ //quitar
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
