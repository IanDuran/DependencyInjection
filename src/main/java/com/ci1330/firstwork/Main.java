package com.ci1330.firstwork;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String... args) {
        try {
            File file = new File("src\\main\\resources\\testa.xml");
            InputStream is = new FileInputStream(file);
            Builder parser = new Builder();
            Document doc = parser.build(is);
            printChildren(doc.getRootElement());
            System.out.println(doc.toXML());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void printChildren(Element e){
        System.out.println(e.getValue());
        Elements elements = e.getChildElements();
        for(int i=0;i<elements.size();i++){
            Element child = elements.get(i);
            printChildren(child);
        }
    }
}
