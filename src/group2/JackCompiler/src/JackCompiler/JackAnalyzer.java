package JackCompiler;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Jack Analyzer
 * reference: pp. 217-218 textbook
 */
public class JackAnalyzer {

    private JackTokenizer tokenizer;
    private Document doc;

    private HashSet<Byte> operators;
    private HashSet<Byte> unaryOperators;
    private HashMap<JackTokenizer.KeyWord, String> keywordConstants;

    public JackAnalyzer(JackTokenizer tokenizer) {
        try {
            this.tokenizer = tokenizer;

            operators = new HashSet<Byte>();
            operators.add((byte)'+');
            operators.add((byte)'-');
            operators.add((byte)'*');
            operators.add((byte)'/');
            operators.add((byte)'&');
            operators.add((byte)'|');
            operators.add((byte)'<');
            operators.add((byte)'>');
            operators.add((byte)'=');

            unaryOperators = new HashSet<Byte>();
            unaryOperators.add((byte)'-');
            unaryOperators.add((byte)'~');

            keywordConstants = new HashMap<JackTokenizer.KeyWord, String>();
            keywordConstants.put(JackTokenizer.KeyWord.TRUE, "true");
            keywordConstants.put(JackTokenizer.KeyWord.FALSE, "false");
            keywordConstants.put(JackTokenizer.KeyWord.NULL, "null");
            keywordConstants.put(JackTokenizer.KeyWord.THIS, "this");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document analyze() {

        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            procClass();

            // deep copy
            return this.doc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void writeXML(Document document, String filename) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            StreamResult result =  new StreamResult(new StringWriter());

            //t.setParameter(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            //writing to file
            FileOutputStream fop = null;
            File file;
            try {

                file = new File(filename);
                fop = new FileOutputStream(file);

                // if file doesnt exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }

                // get the content in bytes
                String xmlString = result.getWriter().toString();
                // System.out.println(xmlString);
                byte[] contentInBytes = xmlString.getBytes();

                fop.write(contentInBytes);
                fop.flush();
                fop.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fop != null) {
                        fop.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void _assert(boolean condition) throws JackCompilerException{
        if (!condition) {
            throw new JackCompilerException();
        }
    }

    private Element _createTextElement(String tagName, String text) {
        Element t1 = this.doc.createElement(tagName);
        Text t2 = this.doc.createTextNode(text);
        t1.appendChild(t2);
        return t1;
    }

    // Program Structure

    private void procClass() throws JackCompilerException {
        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.CLASS);
        Element currNode = doc.createElement("class");
        currNode.appendChild(_createTextElement("keyword", " class "));

        procClassName(currNode);

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL
                && tokenizer.getSymbol() == '{');

        currNode.appendChild(_createTextElement("symbol", " { "));

        tokenizer.advance();
        procClassVarDecOrSubroutineDec(currNode);

        currNode.appendChild(_createTextElement("symbol", " } "));
        doc.appendChild(currNode);
    }

    private void procClassVarDecOrSubroutineDec(Element currNode) throws JackCompilerException {

        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}') {
            return;
        }
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD);

        Element newNode = null;

        switch (tokenizer.getKeyWord()) {
            case STATIC:
            case FIELD:
                newNode = doc.createElement("classVarDec");

                switch (tokenizer.getKeyWord()) {
                    case STATIC:
                        newNode.appendChild(_createTextElement("keyword", " static "));
                        break;
                    case FIELD:
                        newNode.appendChild(_createTextElement("keyword", " field "));
                        break;
                }

                procType(newNode);

                procVarName(newNode);

                while (true) {
                    tokenizer.advance();
                    _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);
                    if (tokenizer.getSymbol() == ';') {
                        newNode.appendChild(_createTextElement("symbol", " ; "));
                        break;
                    } else {
                        _assert(tokenizer.getSymbol() == ',');
                        newNode.appendChild(_createTextElement("symbol", " , "));
                        procVarName(newNode);
                    }
                }

                break;
            case CONSTRUCTOR:
            case FUNCTION:
            case METHOD:
                newNode = doc.createElement("subroutineDec");

                switch (tokenizer.getKeyWord()) {
                    case CONSTRUCTOR:
                        newNode.appendChild(_createTextElement("keyword", " constructor "));
                        break;
                    case FUNCTION:
                        newNode.appendChild(_createTextElement("keyword", " function "));
                        break;
                    case METHOD:
                        newNode.appendChild(_createTextElement("keyword", " method "));
                        break;
                }

                procTypeOrVoid(newNode);

                procSubroutineName(newNode);

                tokenizer.advance();
                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '(');
                newNode.appendChild(_createTextElement("symbol", " ( "));

                procParameterList(newNode);

                newNode.appendChild(_createTextElement("symbol", " ) "));

                procSubroutineBody(newNode);

                break;
            default:
                _assert(false);
        }

        currNode.appendChild(newNode);

        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}') {
            return;
        }
        procClassVarDecOrSubroutineDec(currNode);
    }

    private void procType(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        switch (tokenizer.getTokenType()) {
            case KEYWORD:
                switch (tokenizer.getKeyWord()) {
                    case INT:
                        currNode.appendChild(_createTextElement("keyword", " int "));
                        break;
                    case CHAR:
                        currNode.appendChild(_createTextElement("keyword", " char "));
                        break;
                    case BOOLEAN:
                        currNode.appendChild(_createTextElement("keyword", " boolean "));
                        break;
                    default:
                        _assert(false);
                }
                break;
            case IDENTIFIER:
                currNode.appendChild(_createTextElement("identifier", " " + tokenizer.getIdentifier() + " "));
                break;
            default:
                _assert(false);
        }
    }

    private void procTypeOrVoid(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        switch (tokenizer.getTokenType()) {
            case KEYWORD:
                switch (tokenizer.getKeyWord()) {
                    case VOID:
                        currNode.appendChild(_createTextElement("keyword", " void "));
                        break;
                    case INT:
                        currNode.appendChild(_createTextElement("keyword", " int "));
                        break;
                    case CHAR:
                        currNode.appendChild(_createTextElement("keyword", " char "));
                        break;
                    case BOOLEAN:
                        currNode.appendChild(_createTextElement("keyword", " boolean "));
                        break;
                    default:
                        _assert(false);
                }
                break;
            case IDENTIFIER:
                currNode.appendChild(_createTextElement("identifier", " " + tokenizer.getIdentifier() + " "));
                break;
            default:
                _assert(false);
        }

    }

    private void procParameterList(Element currNode) throws JackCompilerException {
        Element newNode = doc.createElement("parameterList");

        tokenizer.advance();
        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')') {

        } else {
            switch (tokenizer.getTokenType()) {
                case KEYWORD:
                    switch (tokenizer.getKeyWord()) {
                        case INT:
                            newNode.appendChild(_createTextElement("keyword", " int "));
                            break;
                        case CHAR:
                            newNode.appendChild(_createTextElement("keyword", " char "));
                            break;
                        case BOOLEAN:
                            newNode.appendChild(_createTextElement("keyword", " boolean "));
                            break;
                        default:
                            _assert(false);
                    }
                    break;
                case IDENTIFIER:
                    newNode.appendChild(_createTextElement("identifier", " " + tokenizer.getIdentifier() + " "));
                    break;
                default:
                    _assert(false);
            }

            procVarName(newNode);

            while (true) {
                tokenizer.advance();
                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);
                if (tokenizer.getSymbol() == ',') {
                    newNode.appendChild(_createTextElement("symbol", " , "));
                    procType(newNode);
                    procVarName(newNode);
                } else if (tokenizer.getSymbol() == ')') {
                    break;
                } else {
                    _assert(false);
                }
            }
        }

        currNode.appendChild(newNode);
    }

    private void procVarDecWithoutFirstToken(Element currNode) throws JackCompilerException {
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD && tokenizer.getKeyWord() == JackTokenizer.KeyWord.VAR);

        Element newNode = doc.createElement("varDec");
        newNode.appendChild(_createTextElement("keyword", " var "));

        procType(newNode);
        procVarName(newNode);

        while (true) {
            tokenizer.advance();
            _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);
            if (tokenizer.getSymbol() == ',') {
                currNode.appendChild(_createTextElement("symbol", " , "));
                procVarName(newNode);
            } else {
                _assert(tokenizer.getSymbol() == ';');
                newNode.appendChild(_createTextElement("symbol", " ; "));
                break;
            }
        }

        currNode.appendChild(newNode);
    }

    private void procIdentifier(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.IDENTIFIER);
        currNode.appendChild(_createTextElement("identifier", " " + tokenizer.getIdentifier() + " "));
    }

    private void procVarName(Element currNode) throws JackCompilerException {
        procIdentifier(currNode);
    }

    private void procClassName(Element currNode) throws JackCompilerException {
        procIdentifier(currNode);
    }

    private void procSubroutineName(Element currNode) throws JackCompilerException {
        procIdentifier(currNode);
    }

    // Statements

    private boolean shouldAdvance; // set false in case of an "if" statement without "else"

    private void procStatements(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        procStatementsWithoutFirstToken(currNode);
    }

    private void procStatementsWithoutFirstToken(Element currNode) throws JackCompilerException {
        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}') {
            Element newNode = doc.createElement("statements");
            currNode.appendChild(newNode);
            return;
        }

        this.shouldAdvance = true;

        Element newNode = doc.createElement("statements");
        while (true) {
            if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL) {
                System.out.println((char)tokenizer.getSymbol());
            }
            _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD);
            switch (tokenizer.getKeyWord()) {
                case LET:
                    procLetStatementWithoutFirstToken(newNode);
                    break;
                case IF:
                    procIfStatementWithoutFirstToken(newNode);
                    break;
                case WHILE:
                    procWhileStatementWithoutFirstToken(newNode);
                    break;
                case DO:
                    procDoStatementWithoutFirstToken(newNode);
                    tokenizer.advance();
                    break;
                case RETURN:
                    procReturnStatementWithoutFirstToken(newNode);
                    break;
                default:
                    _assert(false);
                    break;
            }
            if (shouldAdvance) {
                tokenizer.advance();
            } else {
                shouldAdvance = true;
            }
            if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}') {
                break;
            }
        }

        currNode.appendChild(newNode);
    }

    private void procSubroutineBody(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '{');

        Element newNode = doc.createElement("subroutineBody");
        newNode.appendChild(_createTextElement("symbol", " { "));

        boolean flag;

        while (true) {

            tokenizer.advance();
            if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}') {
                newNode.appendChild(_createTextElement("symbol", " } "));
                flag = false;
                break;
            } else {
                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD);

                if (tokenizer.getKeyWord() == JackTokenizer.KeyWord.VAR) {
                    procVarDecWithoutFirstToken(newNode);
                } else {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            procStatementsWithoutFirstToken(newNode);
        }

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}');
        newNode.appendChild(_createTextElement("symbol", " } "));

        currNode.appendChild(newNode);

    }

    private void procLetStatementWithoutFirstToken(Element currNode) throws JackCompilerException {
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.LET);

        Element newNode = doc.createElement("letStatement");
        newNode.appendChild(_createTextElement("keyword", " let "));

        procVarName(newNode);

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);

        switch (tokenizer.getSymbol()) {
            case '[':
                newNode.appendChild(_createTextElement("symbol", " [ "));

                procExpression(newNode);

                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ']');
                newNode.appendChild(_createTextElement("symbol", " ] "));

                tokenizer.advance();
                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '=');

            case '=':
                newNode.appendChild(_createTextElement("symbol", " = "));
                break;
            default:
                _assert(false);
        }

        procExpression(newNode);

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ';');
        newNode.appendChild(_createTextElement("symbol", " ; "));

        currNode.appendChild(newNode);
    }

    private void procIfStatementWithoutFirstToken(Element currNode) throws JackCompilerException {
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.IF);

        Element newNode = doc.createElement("ifStatement");
        newNode.appendChild(_createTextElement("keyword", " if "));

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '(');
        newNode.appendChild(_createTextElement("symbol", " ( "));

        procExpression(newNode);

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')');
        newNode.appendChild(_createTextElement("symbol", " ) "));

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '{');
        newNode.appendChild((_createTextElement("symbol", " { ")));

        procStatements(newNode);

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}');
        newNode.appendChild(_createTextElement("symbol", " } "));

        tokenizer.advance();

        if (tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.ELSE) {
            newNode.appendChild(_createTextElement("keyword", " else "));

            tokenizer.advance();
            _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '{');
            newNode.appendChild(_createTextElement("symbol", " { "));

            procStatements(newNode);

            _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}');
            newNode.appendChild(_createTextElement("symbol", " } "));

        } else {
            shouldAdvance = false;
        }

        currNode.appendChild(newNode);
    }

    private void procWhileStatementWithoutFirstToken(Element currNode) throws JackCompilerException {

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.WHILE);

        Element newNode = doc.createElement("whileStatement");
        newNode.appendChild(_createTextElement("keyword", " while "));

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '(');
        newNode.appendChild(_createTextElement("symbol", " ( "));

        procExpression(newNode);

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')');
        newNode.appendChild(_createTextElement("symbol", " ) "));

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '{');
        newNode.appendChild((_createTextElement("symbol", " { ")));

        procStatements(newNode);

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}');
        newNode.appendChild(_createTextElement("symbol", " } "));


        currNode.appendChild(newNode);
    }

    private void procDoStatementWithoutFirstToken(Element currNode) throws JackCompilerException {

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.DO);
        Element newNode = doc.createElement("doStatement");
        newNode.appendChild(_createTextElement("keyword", " do "));

        procSubroutineCall(newNode);

        newNode.appendChild(_createTextElement("symbol", " ; "));
        currNode.appendChild(newNode);
    }

    private void procReturnStatementWithoutFirstToken(Element currNode) throws JackCompilerException {

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.RETURN);

        Element newNode = doc.createElement("returnStatement");
        newNode.appendChild(_createTextElement("keyword", " return "));

        tokenizer.advance();
        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ';') {
            newNode.appendChild(_createTextElement("symbol", " ; "));
        } else {
            procExpressionWithoutFirstToken(newNode);
            newNode.appendChild(_createTextElement("symbol", " ; "));
        }

        currNode.appendChild(newNode);
    }

    // Expressions



    private void procExpressionWithoutFirstToken(Element currNode) throws JackCompilerException {
        // ends after a right bracket or a semicolon or a right square bracket or a comma

        Element newNode = doc.createElement("expression");

        while (true) {
            procTermWithoutFirstToken(newNode);
            _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);

            if (operators.contains(tokenizer.getSymbol())) {
                newNode.appendChild(_createTextElement("symbol", " " + String.valueOf((char)tokenizer.getSymbol()) + " "));
                tokenizer.advance();
            } else if (tokenizer.getSymbol() == ')' || tokenizer.getSymbol() == ';' || tokenizer.getSymbol() == ']' || tokenizer.getSymbol() == ',') {
                currNode.appendChild(newNode);
                return;
            } else {
                _assert(false);
            }
        }

    }

    private void procTerm(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        procTermWithoutFirstToken(currNode);
    }

    private void procTermWithoutFirstToken(Element currNode) throws JackCompilerException {
        // ends after a right bracket or a semicolon or a right square bracket or a comma or an operator

        Element newNode = doc.createElement("term");
        Element subNode;

        switch (tokenizer.getTokenType()) {
            case INT_CONST:
                newNode.appendChild(_createTextElement("integerConstant", " " + Integer.toString(tokenizer.getIntVal()) + " "));
                tokenizer.advance();
                break;

            case STRING_CONST:
                newNode.appendChild(_createTextElement("stringConstant", " " + tokenizer.getStringVal() + " "));
                tokenizer.advance();
                break;

            case SYMBOL:

                if (tokenizer.getSymbol() == '(') {
                    procExpression(newNode);
                    //tokenizer.advance();
                } else {
                    _assert(unaryOperators.contains(tokenizer.getSymbol()));
                    newNode.appendChild(_createTextElement("symbol", " " + String.valueOf((char)tokenizer.getSymbol()) + " "));
                    procTerm(newNode);
                }
                break;

            case IDENTIFIER:
                String identifierName = tokenizer.getIdentifier();

                tokenizer.advance();
                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);

                switch (tokenizer.getSymbol()) {
                    case '.':
                        subNode = doc.createElement("subroutineCall");

                        subNode.appendChild(_createTextElement("identifier", " " + identifierName + " "));
                        subNode.appendChild(_createTextElement("symbol", " . "));

                        procSubroutineName(subNode);

                        subNode.appendChild(_createTextElement("symbol", " ( "));

                        procExpressionList(subNode);

                        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')');
                        subNode.appendChild(_createTextElement("symbol", " ) "));

                        newNode.appendChild(subNode);
                        tokenizer.advance();
                        break;

                    case '(':
                        subNode = doc.createElement("subroutineCall");

                        subNode.appendChild(_createTextElement("identifier", " " + identifierName + " "));
                        subNode.appendChild(_createTextElement("symbol", " ( "));

                        procExpressionList(subNode);

                        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')');
                        subNode.appendChild(_createTextElement("symbol", " ) "));

                        newNode.appendChild(subNode);
                        tokenizer.advance();
                        break;

                    case '[':
                        newNode.appendChild(_createTextElement("identifier", " " + identifierName + " "));
                        newNode.appendChild(_createTextElement("symbol",  " [ "));
                        procExpression(newNode);
                        newNode.appendChild(_createTextElement("symbol", " ] "));
                        tokenizer.advance();
                        break;

                    default:
                        newNode.appendChild(_createTextElement("identifier", " " + identifierName + " "));
                        break;
                }

                break;

            case KEYWORD:
                _assert(keywordConstants.containsKey(tokenizer.getKeyWord()));

                newNode.appendChild(_createTextElement("keyword", " " + keywordConstants.get(tokenizer.getKeyWord()) + " "));
                tokenizer.advance();

                break;
        }

        currNode.appendChild(newNode);

    }

    private void procExpression(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        procExpressionWithoutFirstToken(currNode);
    }

    private void procSubroutineCall(Element currNode) throws JackCompilerException {
        tokenizer.advance();
        procSubroutineCallWithoutFirstToken(currNode);
    }

    private void procSubroutineCallWithoutFirstToken(Element currNode) throws JackCompilerException {
        // ends after a semicolon

        Element newNode = doc.createElement("subroutineCall");

        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.IDENTIFIER);
        String identifierName = tokenizer.getIdentifier();

        newNode.appendChild(_createTextElement("identifier", " " + identifierName + " "));

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);

        switch (tokenizer.getSymbol()) {
            case '.':
                newNode.appendChild(_createTextElement("symbol", " . "));
                procSubroutineName(newNode);
                tokenizer.advance();
            case '(':
                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '(');
                newNode.appendChild(_createTextElement("symbol", " ( "));

                procExpressionList(newNode);

                _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')');
                newNode.appendChild(_createTextElement("symbol", " ) "));
                break;
            default:
                _assert(false);

        }

        currNode.appendChild(newNode);
    }

    private void procExpressionList(Element currNode) throws JackCompilerException {
        // ends after a right bracket

        tokenizer.advance();
        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == ')') {
            currNode.appendChild(doc.createElement("expressionList"));
            return;
        }

        //tokenizer.advance();

        Element newNode = doc.createElement("expressionList");

        while (true) {
            procExpressionWithoutFirstToken(newNode);
            _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL);

            switch (tokenizer.getSymbol()) {
                case ',':
                    newNode.appendChild(_createTextElement("symbol", " , "));
                    tokenizer.advance();
                    break;
                case ')':
                    currNode.appendChild(newNode);
                    return;
                default:
                    _assert(false);
            }

        }
    }

    /**
     * Unit test
     * @param args paths of the input file (*.jack) and output file (*.xml)
     */
    public static void main(String[] args) {
        try {
            JackAnalyzer test = new JackAnalyzer(new JackTokenizer(args[0]));
            Document doc = test.analyze();

            writeXML(doc, args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
