package JackCompiler;

import org.w3c.dom.*;
import sun.plugin.com.JavaClass;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class JackCodeGenerator {

    private class Symbol {
        String name;
        String type;
        int address;
        Symbol(String name, String type, int address) {
            this.name = name;
            this.type = type;
            this.address = address;
        }
    }

    private Document doc;
    private ArrayList<String> codes;
    private HashMap<String, Symbol> fieldMap;
    private HashMap<String, Symbol> staticMap;
    private HashMap<String, Symbol> localMap;
    private HashMap<String, Symbol> argumentMap;
    private String className;

    public JackCodeGenerator(Document doc) {
        this.doc = doc;
    }

    public ArrayList<String> generate() throws JackCompilerException {
        this.codes = new ArrayList<String>();

        this.className = getChildren(doc.getDocumentElement()).get(1).getFirstChild().getNodeValue();

        procFieldsAndStatics(doc.getDocumentElement());

        for (Element subroutineDec : getChildren(doc.getDocumentElement())) {
            if (subroutineDec.getTagName().equals("subroutineDec")) {
                procSubroutine(subroutineDec);
            }
        }

        return this.codes;
    }

    private void procFieldsAndStatics(Element node) throws JackCompilerException {
        this.fieldMap = new HashMap<String, Symbol>();
        this.staticMap = new HashMap<String, Symbol>();
        ArrayList<Element> children = getChildren(node);
        for (int i = 3; i < children.size() - 1; i++) {
            Element dec = children.get(i);
            if (dec.getTagName().equals("classVarDec")) {
                ArrayList<Element> list = getChildren(dec);
                String typeName = list.get(1).getFirstChild().getNodeValue();
                switch (list.get(0).getFirstChild().getNodeValue()) {
                    case "static":
                        for (int j = 2; j < list.size() - 1; j += 2) {
                            String varName = list.get(j).getFirstChild().getNodeValue();
                            this.staticMap.put(varName, new Symbol(varName, typeName, fieldMap.size()));
                        }
                        break;
                    case "field":
                        for (int j = 2; j < list.size() - 1; j += 2) {
                            String varName = list.get(j).getFirstChild().getNodeValue();
                            this.fieldMap.put(varName, new Symbol(varName, typeName, fieldMap.size()));
                        }
                        break;
                }
            }
        }
    }

    private void procLocals(Element node) throws JackCompilerException {
        this.localMap = new HashMap<String, Symbol>();
        ArrayList<Element> children = getChildren(node);
        for (int i = 1; i < children.size() - 1; i++) {
            Element dec = children.get(i);
            if (dec.getTagName().equals("varDec")) {
                ArrayList<Element> list = getChildren(dec);
                String typeName = list.get(1).getFirstChild().getNodeValue();
                switch (list.get(0).getFirstChild().getNodeValue()) {
                    case "var":
                        for (int j = 2; j < list.size() - 1; j += 2) {
                            String varName = list.get(j).getFirstChild().getNodeValue();
                            this.localMap.put(varName, new Symbol(varName, typeName, localMap.size()));
                        }
                        break;
                }
            }
        }
    }

    private void procArguments(Element node, boolean isStatic) throws JackCompilerException {
        this.argumentMap = new HashMap<String, Symbol>();
        ArrayList<Element> children = getChildren(node);

        if (!isStatic) {
            this.argumentMap.put("this", new Symbol("this", className, 0));
        }

        for (int i = 0; i < children.size() - 1; i += 3) {
            String type = children.get(i).getFirstChild().getNodeValue();
            String name = children.get(i + 1).getFirstChild().getNodeValue();
            this.argumentMap.put(name, new Symbol(name, type, argumentMap.size()));
        }

    }

    private void procSubroutine(Element node) throws JackCompilerException {
        HashMap<String, Symbol> localMap = new HashMap<String, Symbol>();
        ArrayList<Element> children = getChildren(node);
        String subroutineType = children.get(0).getFirstChild().getNodeValue();

        procLocals(children.get(6));

        switch (subroutineType) {
            case "function":
                procArguments(children.get(4), true);
                procFunction(node);
                break;
            case "method":
                procArguments(children.get(4), false);
                procMethod(node);
                break;
            case "constructor":
                procArguments(children.get(4), true);
                procConstructor(node);
                break;
        }
    }

    private void procFunction(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String returnType = children.get(1).getFirstChild().getNodeValue();
        String subroutineName = children.get(2).getFirstChild().getNodeValue();

        codes.add("function " + className + "." + subroutineName + " " + Integer.toString(localMap.size()));

        procStatements(children.get(6));
    }

    private void procMethod(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String returnType = children.get(1).getFirstChild().getNodeValue();
        String subroutineName = children.get(2).getFirstChild().getNodeValue();

        codes.add("function " + className + "." + subroutineName + " " + Integer.toString(localMap.size()));
        procStatements(children.get(6));
    }

    private void procConstructor(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String returnType = children.get(1).getFirstChild().getNodeValue();
        String subroutineName = children.get(2).getFirstChild().getNodeValue();

        codes.add("function " + className + "." + subroutineName + " " + Integer.toString(localMap.size()));

        codes.add("push constant " + Integer.toString(this.fieldMap.size()));
        codes.add("call Memory.alloc 1");

        procStatements(children.get(6));
    }

    private void procStatements(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        for (Element st : children) {
            if (st.getTagName().equals("statements")) {
                ArrayList<Element> list = getChildren(st);
                for (Element statement : list) {
                    switch (statement.getTagName()) {
                        case "letStatement":
                            procLetStatement(statement);
                            break;
                        case "ifStatement":
                            procIfStatement(statement);
                            break;
                        case "whileStatement":
                            procWhileStatement(statement);
                            break;
                        case "doStatement":
                            procDoStatement(statement);
                            break;
                        case "returnStatement":
                            procReturnStatement(statement);
                            break;
                    }
                }
            }
        }
    }

    private void procLetStatement(Element node) throws JackCompilerException {

    }

    private void procIfStatement(Element node) throws JackCompilerException {

    }

    private void procWhileStatement(Element node) throws JackCompilerException {

    }

    private void procDoStatement(Element node) throws JackCompilerException {

    }

    private void procReturnStatement(Element node) throws JackCompilerException {

    }

    private static ArrayList<Element> getChildren(Element node) {
        ArrayList<Element> arr = new ArrayList<Element>();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp instanceof Element) {
                arr.add((Element)temp);
            }
        }
        return arr;
    }

    /**
     * Unit test
     * @param args paths of the input files (*.jack) and output file (*.xml)
     */
    public static void main(String[] args) {
        try {

            ArrayList<String> codes = new JackCodeGenerator(new JackAnalyzer(new JackTokenizer(args[0]))
                    .analyze())
                    .generate();

            FileWriter fw = new FileWriter(args[1]);
            for (String code : codes) {
                fw.write(code);
                fw.write("\r\n");
            }
            fw.flush();
            fw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
