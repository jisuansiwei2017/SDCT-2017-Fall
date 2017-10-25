package JackCompiler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

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



            // deep copy
            Document ret = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            ret.importNode(this.xmlDocument.getDocumentElement(), true);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



}
