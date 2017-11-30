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

    public JackCodeGenerator(Document doc) {
        this.doc = doc;
        this.codes = new ArrayList<String>();
    }

    public String[] generate() throws JackCompilerException {

        procClass();

        return (String[])this.codes.toArray();
    }

    private void procClass() throws JackCompilerException {

    }

}
