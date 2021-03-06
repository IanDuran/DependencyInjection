package com.ci1330.firstwork;

import com.ci1330.firstwork.annotations.Service;
import javafx.util.Pair;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Parser {

    private Document configurationFile;
    private String[] beans;
    private String[] classes;
    private String[] scopes;
    private String[] autowiring;
    private boolean configType;

    //name -> ref
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
            this.parseXML();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a XML configuration file.
     */
    @Service(value = "Hola!")
    private void parseXML() {
        try {
            Element root = this.configurationFile.getRootElement(); //este saca el raiz, que es el que engloba todoo
            if(root.getFirstChildElement("annotation-config")!=null){ //si es Annotation config
                this.configType = true;
                this.getAnnotationConfigData(root);
            } else { //si es XML config
                this.configType = false;
                this.getXMLconfigData(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getXMLconfigData(Element root){
        Element child = root.getFirstChildElement("beans"); // este saca un hijo que tenga de tag el nombre que le paso
        Elements children = child.getChildElements(); //este saca todos los hijos de ese elemento, que serian todos los messages
        Element e;
        Elements attributes;
        Element attribute;
        List<Pair<String, String>> props;
        Pair<String, String> prop;
        int childrenSize = children.size();
        this.classes = new String[childrenSize];
        this.beans = new String[childrenSize];
        this.scopes = new String[childrenSize];
        for (int i = 0; i < childrenSize; i++) {
            e = children.get(i); //saco el n-esimo hijo
            this.beans[i] = e.getAttributeValue("id"); //meto el id a la lista de ids de beans
            this.classes[i] = e.getAttributeValue("class"); //meto la clase(nombre) a la lista de clases de beans
            this.scopes[i] = e.getAttributeValue("scope");
            //this.autowiring[i] = e.getAttributeValue("autowiring");
            if (this.scopes[i] == null)
                this.scopes[i] = "singleton";//Singleton por default
            //System.out.println(this.scopes[i]);
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
    }


    public void getAnnotationConfigData(Element root){
        Element child = root.getFirstChildElement("beans"); // este saca un hijo que tenga de tag el nombre que le paso
        Elements children = child.getChildElements(); //este saca todos los hijos de ese elemento, que serian todos los messages
        Element component_scan = root.getFirstChildElement("components-scan");
        if(component_scan!=null){
            Elements component_scan_children = component_scan.getChildElements(); //saco tags de paquetes;
            int childrenSize = component_scan_children.size();
            this.classes = new String[childrenSize];
            this.autowiring = new String[childrenSize];
            for(int i = 0; i < childrenSize; i++){
                child = component_scan_children.get(i);
                this.classes[i] = child.getAttributeValue("class");
                this.autowiring[i] = child.getAttributeValue("autowiring");
            }
        } else {
            System.out.println("A components-scan tag must be specified for annotation configuration.");
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

    /**
     * Returns an array filled with the scopes of the beans.
     * @return an array of bean scopes.
     */
    public String[] getScopes(){
        return this.scopes;
    }

    /**
     * Returns an array filled with the autowirings of the beans.
     * @return an array of bean autowirings.
     */
    public String[] getAutowiring(){
        return this.autowiring;
    }

    /**
     * Returns the type of configuration.
     * @return the configuration type.
     */
    public boolean getConfigType(){
        return this.configType;
    }
}