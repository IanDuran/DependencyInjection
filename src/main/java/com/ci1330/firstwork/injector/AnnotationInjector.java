package com.ci1330.firstwork.injector;

public class AnnotationInjector extends AbstractInjector{

    private boolean autowireByType = true;

    public AnnotationInjector(String filepath){
        super(filepath);
        this.fillClassMap();
        this.initializeBeans();
        this.addDependencies();
    }

    @Override
    public void fillClassMap() {

    }

    @Override
    public void initializeBeans() {

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
