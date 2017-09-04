package com.ci1330.firstwork.injector;

import com.ci1330.firstwork.Parser;
import com.ci1330.firstwork.annotations.Autowired;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class AnnotationInjector extends AbstractInjector{

    private boolean autowireByType = true;

    public AnnotationInjector(Parser parser) {
        super();
        super.parser = parser;
        this.fillClassMap();
        this.initializeBeans();
        this.addDependencies();
    }

    @Override
    public void fillClassMap(){
        String[] beanClasses = super.parser.getBeanClasses();
        String[] beanAutowires = super.parser.getAutowiring();
        for (int i = 0; i < beanClasses.length; i++) {
            try {
                super.beanAutowirings.put(Class.forName(beanClasses[i]), beanAutowires[i]);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        /*Iterator it = super.beanAutowirings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }*/
    }

    /*Constructor constructor = currEntry.getValue().getConstructor();
                    Object instance = constructor.newInstance();
                    super.beanObjectsById.put(currEntry.getKey(), instance);
                    super.beanObjectsByType.put(currEntry.getValue(), instance);*/
    /*
    Ocupo instanciar los atributos(Fields) dependiendo de cual es el scope
     */
    @Override
    public void initializeBeans() {
        Iterator it = super.beanAutowirings.entrySet().iterator();
        String scope;
        Class classs;
        Field[] fields;
        Field field;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            scope = (String) pair.getValue();
            if(scope.equals("singleton")) {
                classs = (Class) pair.getKey();
                fields = classs.getDeclaredFields();
                for(int i=0;i<fields.length;i++){
                    field = fields[i];
                    if(field.getAnnotation(Autowired.class) != null){
                        //aca tengo que instanciar el field e inyectarlo a classs
                    }
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @Override
    public void addDependencies() {

    }

    @Override
    public Object getBeanByName(String beanName) {
        return null;
    }

    @Override
    public Object getBeanByType(String beanType) {
        return null;
    }
}
