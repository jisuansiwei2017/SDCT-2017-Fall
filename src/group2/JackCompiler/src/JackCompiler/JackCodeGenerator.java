package JackCompiler;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.HashMap;

public class JackCodeGenerator {

    private Document doc;
    private ArrayList<String> codes;
    private HashMap<String, Integer> fieldMap;
    private HashMap<String, Integer> staticMap;
    private String className;

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
        for (String str : codes) {
            System.out.println(str);
        }

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

        this.className = getChildren(doc.getDocumentElement())[1].getFirstChild().getNodeValue().trim();

        Element[] nodesClassVarDec =  getChildrenByTagName(doc.getDocumentElement(),"classVarDec");
        for (Element currNode : nodesClassVarDec) {
            Element[] children = getChildren(currNode);
            switch (children[0].getTextContent()) {
                case " field ":
                    for (int i = 2; i < children.length; i++) {
                        if (children[i].getTagName().equals("identifier")) {
                            fieldMap.put(children[i].getFirstChild().getNodeValue().trim(), fieldMap.size());
                        }
                    }
                    break;
                case " static ":
                    for (int i = 2; i < children.length; i++) {
                        if (children[i].getTagName().equals("identifier")) {
                            staticMap.put(children[i].getFirstChild().getNodeValue().trim(), fieldMap.size());
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
        switch (children[0].getFirstChild().getNodeValue().trim()) {
            case "constructor":
                procConstructor(dec);
                break;
            case "function":
                procFunction(dec);
                break;
            case "method":
                procMethod(dec);
                break;
        }
    }

    private void procConstructor(Element dec) throws JackCompilerException {
        Element[] children = getChildren(dec);
        String constructorName = children[2].getFirstChild().getNodeValue().trim();
        ArrayList<Pair> parameters = getParameterList(children[4]);
        ArrayList<Pair> localVariables = getLocalVariableList(children[6]);
        codes.add("function " + className + "." + constructorName + " " + localVariables.size());
    }

    private void procFunction(Element dec) throws JackCompilerException {
        Element[] children = getChildren(dec);
        String constructorName = children[2].getFirstChild().getNodeValue().trim();
        ArrayList<Pair> parameters = getParameterList(children[4]);
        ArrayList<Pair> localVariables = getLocalVariableList(children[6]);
        codes.add("function " + className + "." + constructorName + " " + localVariables.size());
    }

    private void procMethod(Element dec) throws JackCompilerException {
        Element[] children = getChildren(dec);
        String constructorName = children[2].getFirstChild().getNodeValue().trim();
        ArrayList<Pair> parameters = getParameterList(children[4]);
        ArrayList<Pair> localVariables = getLocalVariableList(children[6]);
        codes.add("function " + className + "." + constructorName + " " + localVariables.size());
    }


    private class Pair {
        String name;
        String type;
    }

    private ArrayList<Pair> getParameterList(Element param) throws JackCompilerException {
        Element[] children = getChildren(param);
        ArrayList<Pair> ret = new ArrayList<Pair>();
        Pair temp = null;
        for (int i = 0; i < children.length; i++) {
            if (i % 3 == 0) {
                temp = new Pair();
                temp.type = children[i].getFirstChild().getNodeValue().trim();
            } else if (i % 3 == 1) {
                temp.name = children[i].getFirstChild().getNodeValue().trim();
                ret.add(temp);
            }
        }
        return ret;
    }

    private ArrayList<Pair> getLocalVariableList(Element subroutineBody) throws JackCompilerException {
        Element[] children = getChildren(subroutineBody);
        ArrayList<Pair> ret = new ArrayList<Pair>();

        for (Element child : children) {
            if (child.getTagName().equals("varDec")) {
                Element[] list = getChildren(child);
                String typeName = list[1].getFirstChild().getNodeValue().trim();
                for (int j = 2; j < list.length; j += 2) {
                    String name = list[j].getFirstChild().getNodeValue().trim();
                    Pair temp = new Pair();
                    temp.name = name;
                    temp.type = typeName;
                    ret.add(temp);
                }
            }
        }

        return ret;
    }

}
