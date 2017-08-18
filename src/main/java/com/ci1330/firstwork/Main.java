package com.ci1330.firstwork;

import nu.xom.Builder;
import nu.xom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String... args) {
        try {
            File file = new File("test.xml");
            InputStream is = new FileInputStream(file);
            Builder parser = new Builder();
            Document doc = parser.build(is);
            System.out.println(doc.toXML());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
