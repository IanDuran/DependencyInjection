package com.ci1330.firstwork;

import javafx.util.Pair;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class Parser {
    private Document configurationFile;
    private String[] beans;
    private String[] classes;
    private Map<String, List<Pair<String, String>>> properties;
    /*El principal es un mapa para que los sacados sean O(1)
    El key es el id del bean
    La lista es de pares, y cada par es un property del bean, esta lista no la pude hacer como mapa porque no pueden haber
    entradas con la misma llave.
     */

    /**
     * Initialize properties map, opens XML configuration file and calls a parser for it.
     * @param filename the XML configuration file.
     */
    public Parser(String filename) {
        File file = new File(filename);
        try {
            InputStream inputStream = new FileInputStream(file);
            Builder builder = new Builder();
            this.configurationFile = builder.build(inputStream); //faltaba el this, Ian parece de progra 1 en el 104....
            this.properties = new HashMap<String, List<Pair<String, String>>>();
            parseXML();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a XML configuration file.
     */
    public void parseXML() {
        try {
            Element root = this.configurationFile.getRootElement(); //este saca el raiz, que es el que engloba todoo
            Element child = root.getFirstChildElement("beans"); // este saca un hijo que tenga de tag el nombre que le paso
            Elements children = child.getChildElements(); //este saca todos los hijos de ese elemento, que serian todos los messages
            Element e;
            Elements attributes;
            Element attribute;
            List props;
            Pair prop;
            this.beans = new String[children.size()];
            this.classes = new String[children.size()];
            for (int i = 0; i < children.size(); i++) { //aca itero por cada message
                e = children.get(i); //saco el n-esimo hijo
                this.beans[i] = e.getAttribute(0).getValue(); //meto el id a la lista de ids de beans
                this.classes[i] = e.getAttribute(1).getValue(); //meto la clase(nombre) a la lista de clases de beans
                //System.out.print(e.getAttribute(0).getQualifiedName()+"="+e.getAttribute(0).getValue()+" "); //imprimo el valor
                //System.out.println(e.getAttribute(1).getQualifiedName()+"="+e.getAttribute(1).getValue()); //imprimo el valor
                attributes = e.getChildElements(); //saco todos los hijos, que seran los properties
                for (int j = 0; j < attributes.size(); j++) {
                    attribute = attributes.get(j);
                    props = this.properties.get(e.getAttribute(0).getValue());
                    if (props == null) { //si el mapa no tenia esa entrada la crea
                        props = new LinkedList<Pair<String, String>>();
                        prop = new Pair<String, String>(attribute.getAttribute(0).getValue(), attribute.getAttribute(1).getValue());
                        props.add(prop);
                        this.properties.put(e.getAttribute(0).getValue(), props);
                    } else { //sino debo meter el dato en la lista que ya existe
                        prop = new Pair<String, String>(attribute.getAttribute(0).getValue(), attribute.getAttribute(1).getValue());
                        props.add(prop);
                    }
                    //System.out.print(attribute.getAttribute(0).getQualifiedName() + "=" + attribute.getAttribute(0).getValue()+" ");
                    //System.out.println(attribute.getAttribute(1).getQualifiedName() + "=" + attribute.getAttribute(1).getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printData(); //quitar
    }


    public void printData(){ //quitar, solo por prueba.
        System.out.println("Ids de beans");
        for (int i = 0; i < this.beans.length; i++) {
            System.out.println(this.beans[i]);
        }
        System.out.println("\nClases de beans");
        for (int i = 0; i < this.classes.length; i++) {
            System.out.println(this.classes[i]);
        }
        List prop2;
        Pair pair2;
        Iterator it = this.properties.entrySet().iterator(); //itera por cada entrada del mapa
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next(); //saca una entrada
            System.out.println("\nPropiedades de " + pair.getKey() + ":");
            prop2 = (LinkedList) pair.getValue(); //casteo el value a lista
            Iterator it2 = prop2.iterator(); //itera sobre la lista
            while (it2.hasNext()) {
                pair2 = (Pair) it2.next();
                System.out.println(pair2.getKey().toString() + "=" + pair2.getValue().toString());
            }
            it.remove(); //segun StackOverflow evita problemas de concurrencia
        }
    }

    /**
     * Returns an array of bean IDs.
     * @return the bean IDs array.
     */
    public String[] getBeanNames() {
        return this.beans;
    }

    /**
     * Returns an array of bean classes.
     * @return the bean classes array.
     */
    public String[] getBeanClasses() {
        return this.classes;
    }

    /**
     * Return the list of properties from a given bean id.
     * @param beanClass the bean id.
     * @return the list of properties.
     */
    public List<Pair<String, String>> getBeanProperties(String beanClass) {
        return this.properties.get(beanClass);
    }
}

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
            Elements attributes;
            Element attribute;
            for(int i=0;i<children.size();i++){ //aca itero por cada message
                e = children.get(i); //saco el n-esimo hijo
                System.out.println(e.getAttribute(0).getQualifiedName()+"="+e.getAttribute(0).getValue()); //imprimo el valor
                System.out.println(e.getAttribute(1).getQualifiedName()+"="+e.getAttribute(1).getValue()); //imprimo el valor
                attributes = e.getChildElements();
                for(int j=0;j<attributes.size();j++){
                    attribute = attributes.get(j);
                    System.out.println(attribute.getAttribute(0).getQualifiedName() + "=" + attribute.getAttribute(0).getValue());
                    System.out.println(attribute.getAttribute(1).getQualifiedName() + "=" + attribute.getAttribute(1).getValue());
                }
             } //si lo ve enredado, recuerde que un XML tiene forma de arbol.
            //System.out.println(doc.toXML());
        } catch (Exception e) {
            System.out.println(e);
        }*/

