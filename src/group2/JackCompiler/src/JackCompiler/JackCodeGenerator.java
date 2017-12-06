package JackCompiler;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class JackCodeGenerator {

    private Document doc;
    private ArrayList<String> codes;
    private HashMap<String, Integer> fieldMap;
    private HashMap<String, Integer> staticMap;

    public JackCodeGenerator(Document doc) {
        this.doc = doc;
    }

    public String[] generate() throws JackCompilerException {

        this.codes = new ArrayList<String>();

        procClass();

        /*
        try {
            procClass();
        } catch (Exception e) {
            throw new JackCompilerException();
        }
        */

        String[] ret = new String[codes.size()];
        return codes.toArray(ret);
    }

    private static Element[] getChildrenByTagName(Element node, String tagName) {
        ArrayList<Element> arr = new ArrayList<Element>();
        NodeList list = node.getElementsByTagName(tagName);
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element)list.item(i);
            arr.add(e);
        }
        Element[] ret = new Element[arr.size()];
        return arr.toArray(ret);
    }

    private static Element[] getChildren(Element node) {
        ArrayList<Element> arr = new ArrayList<Element>();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp instanceof Element) {
                arr.add((Element)temp);
            }
        }
        Element[] ret = new Element[arr.size()];
        return arr.toArray(ret);
    }

    private void procClass() throws JackCompilerException {
        fieldMap = new HashMap<String, Integer>();
        staticMap = new HashMap<String, Integer>();

        Element[] nodesClassVarDec =  getChildrenByTagName(doc.getDocumentElement(),"classVarDec");
        for (Element currNode : nodesClassVarDec) {
            Element[] children = getChildren(currNode);
            switch (children[0].getTextContent()) {
                case " field ":
                    for (int i = 2; i < children.length; i++) {
                        if (children[i].getTagName() == "identifier") {
                            fieldMap.put(children[i].getTextContent().trim(), fieldMap.size());
                        }
                    }
                    break;
                case " static ":
                    for (int i = 2; i < children.length; i++) {
                        if (children[i].getTagName() == "identifier") {
                            staticMap.put(children[i].getTextContent().trim(), fieldMap.size());
                        }
                    }
                    break;
            }
        }

        Element[] nodesSubroutineDec = getChildrenByTagName(doc.getDocumentElement(), "subroutineDec");
        for (Element currNode : nodesSubroutineDec) {
            procSubroutineDec(currNode);
        }
    }

    private void procSubroutineDec(Element dec) throws JackCompilerException {
        Element[] children = getChildren(dec);
        switch (children[0].getTextContent()) {
            case " constructor ":
                procConstructor(dec);
                break;
            case " function ":
                procFunction(dec);
                break;
            case " method ":
                procMethod(dec);
        }
    }

    private void procConstructor(Element dec) throws JackCompilerException {

    }

    private void procFunction(Element dec) throws JackCompilerException {

    }

    private void procMethod(Element dec) throws JackCompilerException {

    }

}
