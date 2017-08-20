package com.ci1330.firstwork;

import nu.xom.Builder;
import nu.xom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class Parser {
    private Document configurationFile;
    public Parser(String filename){
        File file = new File(filename);
        try{
            InputStream inputStream = new FileInputStream(file);
            Builder builder = new Builder();
            configurationFile = builder.build(inputStream);
            inputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String[] getBeanNames(){
        return null;
    }

    public String[] getBeanClasses(){
        return null;
    }

    public Map<String, String> getBeanProperties(String beanClass){
        //Ocupo un map que vaya desde el nombre del Property a lo que referencia el property
        //Digamos que se name a ref en los tags
        return null;
    }
}
