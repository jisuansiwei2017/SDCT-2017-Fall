package JackCompiler;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.PrintWriter;

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

    private void _assert(boolean condition) throws JackCompilerException{
        if (!condition) {
            throw new JackCompilerException();
        }
    }

    private Element _createTextElement(String tagName, String text, Document doc) {
        Element t1 = doc.createElement(tagName);
        Text t2 = doc.createTextNode(text);
        t1.appendChild(t2);
        return t1;
    }

    private void procClass() throws JackCompilerException {
        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.KEYWORD
                && tokenizer.getKeyWord() == JackTokenizer.KeyWord.CLASS);
        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.IDENTIFIER);
        String className = tokenizer.getIdentifier();

        tokenizer.advance();
        _assert(tokenizer.getTokenType() == JackTokenizer.TokenType.SYMBOL
                && tokenizer.getSymbol() == '{');

        Element currNode = xmlDocument.createElement("class");
        currNode.appendChild(_createTextElement("keyword", " class ", xmlDocument));
        currNode.appendChild(_createTextElement("identifier", " " + className + " ", xmlDocument));
        currNode.appendChild(_createTextElement("symbol", " { ", xmlDocument));

        procClassVarDecOrSubroutineDec(currNode);

        currNode.appendChild(_createTextElement("symbol", " } ", xmlDocument));
        xmlDocument.appendChild(currNode);
    }

    private void procClassVarDecOrSubroutineDec(Element currNode) throws JackCompilerException {

    }

    private static void writeXML(Document document,String filename) {
        try {
            document.normalize();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);

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
