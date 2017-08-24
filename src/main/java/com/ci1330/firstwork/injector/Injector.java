package com.ci1330.firstwork.injector;

public interface Injector {
    void fillClassMap();
    void initializeBeans();
    void addDependencies();
    Object getBeanByName(String beanName);
    Object getBeanByType(String beanType);
}
