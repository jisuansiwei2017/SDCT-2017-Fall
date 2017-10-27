package JackCompiler;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Jack Analyzer
 * reference: pp. 217-218 textbook
 */
public class JackAnalyzer {

    private JackTokenizer tokenizer;
    private Document xmlDocument;

    public JackAnalyzer(JackTokenizer tokenizer) {
        try {
            this.tokenizer = tokenizer;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document analyze() {

        try {
            this.xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            procClass();

            // deep copy
            return this.xmlDocument;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void _assert(boolean condition) throws JackCompilerException{
        if (!condition) {
            throw new JackCompilerException();
        }
    }

    private Element _createTextElement(String tagName, String text) {
        Element t1 = this.xmlDocument.createElement(tagName);
        Text t2 = this.xmlDocument.createTextNode(text);
        t1.appendChild(t2);
        return t1;
    }

    private void procClass() throws JackCompilerException {
        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.CLASS);
        Element currNode = xmlDocument.createElement("class");
        currNode.appendChild(_createTextElement("keyword", " class "));

        procClassName(currNode);

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL
                && tokenizer.getSymbol() == '{');

        currNode.appendChild(_createTextElement("symbol", " { "));

        procClassVarDecOrSubroutineDec(currNode);

        currNode.appendChild(_createTextElement("symbol", " } "));
        xmlDocument.appendChild(currNode);
    }

    private void procClassVarDecOrSubroutineDec(Element currNode) throws JackCompilerException {
        System.out.println(1);

        tokenizer.advance();
        if (tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.getSymbol() == '}') {
            return;
        }
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD);

        Element newNode = null;

        switch (tokenizer.getKeyWord()) {
            case STATIC:
            case FIELD:
                newNode = xmlDocument.createElement("classVarDec");

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
                newNode = xmlDocument.createElement("subroutineDec");

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
        Element newNode = xmlDocument.createElement("parameterList");

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

    private void procSubroutineBody(Element currNode) throws JackCompilerException {

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
                System.out.println(xmlString);
                byte[] contentInBytes = xmlString.getBytes();

                fop.write(contentInBytes);
                fop.flush();
                fop.close();

                System.out.println("Done");

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

    public static void main(String[] args) {
        try {
            JackAnalyzer test = new JackAnalyzer(new JackTokenizer("C:/Users/kingi/Desktop/JackTest/test.jack"));
            Document doc = test.analyze();

            writeXML(doc, "C:/Users/kingi/Desktop/JackTest/test.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
