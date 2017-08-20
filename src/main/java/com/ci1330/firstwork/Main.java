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
        /*try {
            File file = new File("src\\main\\resources\\test.xml");
            InputStream is = new FileInputStream(file);
            Builder parser = new Builder();
            Document doc = parser.build(is);
            //voy a hacerlo pasito por pasito
            Element root = doc.getRootElement(); //este saca el raiz, que es el que engloba todoo
            //getQualifiedName me da lo que hay dentro del tag
            Element child = root.getFirstChildElement("beans"); // este saca un hijo que tenga de tag el nombre que le paso
            Elements children = child.getChildElements(); //este saca todos los hijos de ese elemento, que serian todos los messages
            Element e;
            for(int i=0;i<children.size();i++){ //aca itero por cada message
                e = children.get(i); //saco el n-esimo hijo
                System.out.println(e.getAttribute(0).getQualifiedName()+"="+e.getAttribute(0).getValue()); //imprimo el valor
                System.out.println(e.getAttribute(1).getQualifiedName()+"="+e.getAttribute(1).getValue()); //imprimo el valor
             } //si lo ve enredado, recuerde que un XML tiene forma de arbol.
            System.out.println(doc.toXML());
        } catch (Exception e) {
            System.out.println(e);
        }*/
        Parser parser = new Parser("src\\main\\resources\\test.xml");
    }
}
