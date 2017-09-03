package com.ci1330.firstwork.injector;

import com.ci1330.firstwork.Parser;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XMLInjector extends AbstractInjector{


    /**
     * Get the 3 structures from parser, fill class map, initialize beans and add dependencies to them.
     * @param parser the parser.
     */
    public XMLInjector(Parser parser) {
        super();
        super.parser = parser;
        this.fillClassMap();
        this.initializeBeans();
        this.addDependencies();
    }

    /**
     * Fill class map with data from the arrays of the parser.
     */
    @Override
    public void fillClassMap(){
        String[] beanIds = super.parser.getBeanNames();
        String[] beanClasses = super.parser.getBeanClasses();
        String[] beanScopes = super.parser.getScopes();
        for (int i = 0; i < beanIds.length; i++) {
            try {
                super.beanClassMap.put(beanIds[i], Class.forName(beanClasses[i]));
                super.beanScopes.put(Class.forName(beanClasses[i]), beanScopes[i]);
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
            if(super.beanScopes.get(currEntry.getValue()).equals("singleton")) {
                try {
                    Constructor constructor = currEntry.getValue().getConstructor();
                    Object instance = constructor.newInstance();
                    super.beanObjectsById.put(currEntry.getKey(), instance);
                    super.beanObjectsByType.put(currEntry.getValue(), instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                        if(super.beanScopes.get(currField.getType()).equals("singleton"))
                            currField.set(super.beanObjectsById.get(currEntry.getKey()), super.beanObjectsById.get(propertyReference));
                        else
                            this.insertPrototypeDependencies(super.beanObjectsById.get(currEntry.getKey()), currField);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void insertPrototypeDependencies(Object instance, Field field){
        try{
            Constructor constructor = field.getType().getConstructor();
            Object initializedField = constructor.newInstance();
            field.set(instance, initializedField);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private Object getPrototypeBeanByName(String beanName){
        Object prototypeBean = null;
        try {
            Constructor constructor = super.beanClassMap.get(beanName).getConstructor();
            prototypeBean = constructor.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        return prototypeBean;
    }

    private Object getPrototypeBeanByClass(Class beanClass){
        Object prototypeBean = null;
        try{
            Constructor constructor = beanClass.getConstructor();
            prototypeBean = constructor.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        return prototypeBean;
    }

    /**
     * Return a bean matching the given name.
     * @param beanName the bean name.
     * @return the resulting bean.
     */
    @Override
    public Object getBeanByName(String beanName){
        Object bean;
        if(super.beanScopes.get(super.beanClassMap.get(beanName)).equals("singleton"))
            bean = super.beanObjectsById.get(beanName);

        else
            bean = this.getPrototypeBeanByName(beanName);

        return bean;
    }

    /**
     * Return a bean matching the given type.
     * @param beanType the bean name.
     * @return the resulting bean.
     */
    @Override
    public Object getBeanByType(String beanType){
        Object bean = null;
        try {
            Class beanClass = Class.forName(beanType);
            if(super.beanScopes.get(beanClass).equals("singleton"))
                bean = super.beanObjectsByType.get(beanClass);
            else
                bean = this.getPrototypeBeanByClass(beanClass);

        }catch (Exception e){
            e.printStackTrace();
        }
        return bean;
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
