package JackCompiler;

import org.w3c.dom.*;

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

    private static class Label {
        static int count = -1;
        static String next() {
            count++;
            return "LABEL_" + Integer.toString(count);
        }
    }

    private Document doc;
    private ArrayList<String> codes;
    private HashMap<String, Symbol> fieldMap;
    private HashMap<String, Symbol> staticMap;
    private HashMap<String, Symbol> localMap;
    private HashMap<String, Symbol> argumentMap;
    private HashMap<String, String> subroutineMap;

    private String className;

    public JackCodeGenerator(Document doc) {
        this.doc = doc;
    }

    public ArrayList<String> generate() throws JackCompilerException {
        this.codes = new ArrayList<String>();

        this.className = getChildren(doc.getDocumentElement()).get(1).getFirstChild().getNodeValue();

        procFieldsAndStatics(doc.getDocumentElement());
        procSubroutineMap(doc.getDocumentElement());

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

    private void procSubroutineMap(Element node) throws JackCompilerException {
        this.subroutineMap = new HashMap<String, String>();

        ArrayList<Element> children = getChildren(node);

        for (Element dec : children) {
            if (dec.getTagName().equals("subroutineDec")) {
                ArrayList<Element> list = getChildren(dec);
                String name = list.get(2).getFirstChild().getNodeValue();
                String type = list.get(0).getFirstChild().getNodeValue();
                this.subroutineMap.put(name, type);
            }
        }
    }

    private void procSubroutine(Element node) throws JackCompilerException {
        this.localMap = new HashMap<String, Symbol>();
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

        procSubroutineStatements(children.get(6));
    }

    private void procMethod(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String returnType = children.get(1).getFirstChild().getNodeValue();
        String subroutineName = children.get(2).getFirstChild().getNodeValue();

        codes.add("function " + className + "." + subroutineName + " " + Integer.toString(localMap.size()));
        codes.add("push argument 0");
        codes.add("pop pointer 0");
        procSubroutineStatements(children.get(6));
    }

    private void procConstructor(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String returnType = children.get(1).getFirstChild().getNodeValue();
        String subroutineName = children.get(2).getFirstChild().getNodeValue();

        codes.add("function " + className + "." + subroutineName + " " + Integer.toString(localMap.size()));

        codes.add("push constant " + Integer.toString(this.fieldMap.size()));
        codes.add("call Memory.alloc 1");

        procSubroutineStatements(children.get(6));
    }

    private void procStatements(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        for (Element statement : children) {
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


    private void procSubroutineStatements(Element node) throws JackCompilerException {
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

    private enum SymbolType {
        Static,
        Local,
        Argument,
        Field
    }

    private SymbolType findSymbolType(String name) throws JackCompilerException {
        if (this.fieldMap.get(name) != null) {
            return SymbolType.Field;
        } else if (this.staticMap.get(name) != null) {
            return SymbolType.Static;
        } else if (this.localMap.get(name) != null) {
            return SymbolType.Local;
        } else if (this.argumentMap.get(name) != null) {
            return SymbolType.Argument;
        } else {
            return null;
        }
    }

    private void procLetStatement(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String leftVarName = children.get(1).getFirstChild().getNodeValue();

        int pos;
        switch (children.get(2).getFirstChild().getNodeValue()) {
            case "=":
                procExpression(children.get(3));
                switch (findSymbolType(leftVarName)) {
                    case Field:
                        pos = fieldMap.get(leftVarName).address;
                        codes.add("pop this " + Integer.toString(pos));
                        break;
                    case Static:
                        pos = staticMap.get(leftVarName).address;
                        codes.add("pop static " + Integer.toString(pos));
                        break;
                    case Local:
                        pos = localMap.get(leftVarName).address;
                        codes.add("pop local " + Integer.toString(pos));
                        break;
                    case Argument:
                        pos = argumentMap.get(leftVarName).address;
                        codes.add("pop argument " + Integer.toString(pos));
                        break;
                }
                break;
            case "[":
                procExpression(children.get(3));
                switch (findSymbolType(leftVarName)) {
                    case Field:
                        pos = fieldMap.get(leftVarName).address;
                        codes.add("push this " + Integer.toString(pos));
                        break;
                    case Static:
                        pos = staticMap.get(leftVarName).address;
                        codes.add("push static " + Integer.toString(pos));
                        break;
                    case Local:
                        pos = localMap.get(leftVarName).address;
                        codes.add("push local " + Integer.toString(pos));
                        break;
                    case Argument:
                        pos = argumentMap.get(leftVarName).address;
                        codes.add("push argument " + Integer.toString(pos));
                        break;
                }
                codes.add("add");
                procExpression(children.get(6));
                codes.add("pop temp 0");
                codes.add("pop pointer 1");
                codes.add("push temp 0");
                codes.add("pop that 0");
                break;
        }
    }

    private void procIfStatement(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        switch (children.size()) {
            case 7: // if
                procExpression(children.get(2));
                codes.add("not");
                String label = Label.next();
                codes.add("if-goto " + label);
                procStatements(children.get(5));
                codes.add("label " + label);
                break;
            case 11: // if-else
                procExpression(children.get(2));
                codes.add("not");
                String label1 = Label.next();
                String label2 = Label.next();
                codes.add("if-goto " + label1);
                procStatements(children.get(5));
                codes.add("goto " + label2);
                codes.add("label " + label1);
                procStatements(children.get(9));
                codes.add("label " + label2);
                break;
            default:
                throw new JackCompilerException();
        }
        procStatements(children.get(2));

    }

    private void procWhileStatement(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        String label1 = Label.next();
        String label2 = Label.next();

        codes.add("label " + label1);
        procExpression(children.get(2));
        codes.add("not");
        codes.add("if-goto " + label2);
        procStatements(children.get(5));
        codes.add("goto " + label1);
        codes.add("label " + label2);
    }

    private void procDoStatement(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        procSubroutineCall(children.get(1));
        codes.add("pop temp 0");
    }

    private void procReturnStatement(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);
        switch (children.size()) {
            case 2:
                codes.add("push constant 0");
                codes.add("return");
                break;
            case 3:
                procExpression(children.get(1));
                codes.add("return");
        }
    }

    //

    private void procExpression(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);

        if (children.size() == 0) {
            return;
        }

        procTerm(children.get(0));

        for (int i = 1; i < children.size(); i += 2) {
            procTerm(children.get(i + 1));
            switch (children.get(i).getFirstChild().getNodeValue()) {
                case "+":
                    codes.add("add");
                    break;
                case "-":
                    codes.add("sub");
                    break;
                case "*":
                    codes.add("call Math.multiply 2");
                    break;
                case "/":
                    codes.add("call Math.divide 2");
                    break;
                case "&":
                    codes.add("and");
                    break;
                case "|":
                    codes.add("or");
                    break;
                case "<":
                    codes.add("lt");
                    break;
                case ">":
                    codes.add("gt");
                    break;
                case "=":
                    codes.add("eq");
                    break;
            }
        }

    }

    private void procSubroutineCall(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);

        String className = null;
        String subroutineName = null;

        int parameterCount = 0;

        switch (children.get(1).getFirstChild().getNodeValue()) {
            case ".":
                String classNameOrVarName = null;
                classNameOrVarName = children.get(0).getFirstChild().getNodeValue();
                subroutineName = children.get(2).getFirstChild().getNodeValue();
                SymbolType symbolType = findSymbolType(classNameOrVarName);
                if (symbolType == null) {
                    // it is a className
                    className = classNameOrVarName;
                } else switch (symbolType) {
                    // it is a varName
                    case Static:
                        className = this.staticMap.get(classNameOrVarName).type;
                        codes.add("push static " + Integer.toString(this.staticMap.get(classNameOrVarName).address));
                        parameterCount++;
                        break;
                    case Field:
                        className = this.fieldMap.get(classNameOrVarName).type;
                        codes.add("push this " + Integer.toString(this.fieldMap.get(classNameOrVarName).address));
                        parameterCount++;
                        break;
                    case Argument:
                        className = this.argumentMap.get(classNameOrVarName).type;
                        codes.add("push argument " + Integer.toString(this.argumentMap.get(classNameOrVarName).address));
                        parameterCount++;
                        break;
                    case Local:
                        className = this.localMap.get(classNameOrVarName).type;
                        codes.add("push local " + Integer.toString(this.localMap.get(classNameOrVarName).address));
                        parameterCount++;
                        break;
                }
                break;
            case "(":
                className = this.className;
                subroutineName = children.get(0).getFirstChild().getNodeValue();
                switch (this.subroutineMap.get(subroutineName)) {
                    case "constructor":
                        break;
                    case "method":
                        codes.add("push argument 0");
                        parameterCount++;
                        break;
                    case "function":

                        break;
                }
                break;
        }

        ArrayList<Element> expressions = getChildren(children.get(4));
        for (Element expr : expressions) {
            if (expr.getTagName().equals("expression")) {
                procExpression(expr);
                parameterCount++;
            }

        }

        codes.add("call " + className + "." + subroutineName + " " + Integer.toString(parameterCount));

    }

    private void procTerm(Element node) throws JackCompilerException {
        ArrayList<Element> children = getChildren(node);

        String tagName = children.get(0).getTagName();
        if (tagName.equals("integerConstant")) {
            codes.add("push constant " + children.get(0).getFirstChild().getNodeValue());
        } else if (tagName.equals("stringConstant")) {
            String str = children.get(0).getFirstChild().getNodeValue();
            codes.add("push constant " + Integer.toString(str.length()));
            codes.add("call String.new 1");
            for (int i = 0; i < str.length(); i++) {
                codes.add("push constant " + Integer.toString((int)str.charAt(i)));
                codes.add("call String.appendChar 2");
            }
        } else if (tagName.equals("keyword")) {
            switch (children.get(0).getFirstChild().getNodeValue()) {
                case "true":
                    codes.add("push constant 0");
                    codes.add("not");
                    break;
                case "false":
                    codes.add("push constant 0");
                    break;
                case "null":
                    codes.add("push constant 0");
                    break;
                case "this":
                    codes.add("push argument 0");
                    break;
                default:
                    throw new JackCompilerException();
            }
        } else if (tagName.equals("subroutineCall")) {
            procSubroutineCall(children.get(0));
        } else if (tagName.equals("expression")) {
            procExpression(children.get(0));
        } else if (tagName.equals("symbol")) {
            procTerm(children.get(1));
            switch (children.get(0).getFirstChild().getNodeValue()) {
                case "-":
                    codes.add("sub");
                    break;
                case "~":
                    codes.add("not");
                    break;
                default:
                    throw new JackCompilerException();
            }
        } else if (tagName.equals("identifier")) {
            String name = children.get(0).getFirstChild().getNodeValue();
            switch (children.size()) {
                case 1:
                    switch (findSymbolType(children.get(0).getFirstChild().getNodeValue())) {
                        case Field:
                            codes.add("push this " + Integer.toString(this.fieldMap.get(name).address));
                            break;
                        case Static:
                            codes.add("push static " + Integer.toString(this.staticMap.get(name).address));
                            break;
                        case Local:
                            codes.add("push local " + Integer.toString(this.localMap.get(name).address));
                            break;
                        case Argument:
                            codes.add("push argument " + Integer.toString(this.argumentMap.get(name).address));
                            break;
                    }
                    break;
                case 4:
                    procExpression(children.get(2));
                    switch (findSymbolType(children.get(0).getFirstChild().getNodeValue())) {
                        case Field:
                            codes.add("push this " + Integer.toString(this.fieldMap.get(name).address));
                            break;
                        case Static:
                            codes.add("push static " + Integer.toString(this.staticMap.get(name).address));
                            break;
                        case Local:
                            codes.add("push local " + Integer.toString(this.localMap.get(name).address));
                            break;
                        case Argument:
                            codes.add("push argument " + Integer.toString(this.argumentMap.get(name).address));
                            break;
                    }
                    codes.add("add");
                    codes.add("pop pointer 1");
                    codes.add("push that 0");
                    break;
                default:
                    throw new JackCompilerException();
            }
        }
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

            Document doc = new JackAnalyzer(new JackTokenizer(args[0]))
                    .analyze();

            JackAnalyzer.writeXML(doc, args[1]);

            ArrayList<String> codes = new JackCodeGenerator(doc)
                    .generate();

            FileWriter fw = new FileWriter(args[2]);
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
