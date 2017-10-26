package JackCompiler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Removes all comments and white space from the input stream and breaks it
 * into Jack-language tokens, as specified by the Jack grammar.
 * <p>Reference: See textbook on page 214</p>
 */
public class JackTokenizer {

    public enum TokenType {
        KEYWORD,
        SYMBOL,
        IDENTIFIER,
        INT_CONST,
        STRING_CONST
    }

    public enum KeyWord {
        CLASS,
        METHOD,
        FUNCTION,
        CONSTRUCTOR,
        INT,
        BOOLEAN,
        CHAR,
        VOID,
        VAR,
        STATIC,
        FIELD,
        LET,
        DO,
        IF,
        ELSE,
        WHILE,
        RETURN,
        TRUE,
        FALSE,
        NULL,
        THIS
    }

    private byte[] bytes;
    private int pos;
    private TokenType tokenType;
    private KeyWord keyWord;
    private byte symbol;
    private String identifier;
    private int intVal;
    private String stringVal;

    private HashSet<Byte> symbolSet;

    private HashMap<String, KeyWord> keywordSet;

    public JackTokenizer(String path) throws IOException {
        /*
        File file = new File(path);
        bytes = new byte[(int)file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(bytes);
        */

        bytes = readFileWithCommentsRemoved(path);

        pos = 0;

        symbolSet = new HashSet<Byte>();
        symbolSet.add((byte)'{');
        symbolSet.add((byte)'}');
        symbolSet.add((byte)'(');
        symbolSet.add((byte)')');
        symbolSet.add((byte)'[');
        symbolSet.add((byte)']');
        symbolSet.add((byte)'.');
        symbolSet.add((byte)',');
        symbolSet.add((byte)';');
        symbolSet.add((byte)'+');
        symbolSet.add((byte)'-');
        symbolSet.add((byte)'*');
        symbolSet.add((byte)'/');
        symbolSet.add((byte)'&');
        symbolSet.add((byte)'|');
        symbolSet.add((byte)'<');
        symbolSet.add((byte)'>');
        symbolSet.add((byte)'=');
        symbolSet.add((byte)'~');

        keywordSet = new HashMap<String, KeyWord>();
        keywordSet.put("class", KeyWord.CLASS);
        keywordSet.put("method", KeyWord.METHOD);
        keywordSet.put("function", KeyWord.FUNCTION);
        keywordSet.put("constructor", KeyWord.CONSTRUCTOR);
        keywordSet.put("int", KeyWord.INT);
        keywordSet.put("boolean", KeyWord.BOOLEAN);
        keywordSet.put("char", KeyWord.CHAR);
        keywordSet.put("void", KeyWord.VOID);
        keywordSet.put("var", KeyWord.VAR);
        keywordSet.put("static", KeyWord.STATIC);
        keywordSet.put("field", KeyWord.FIELD);
        keywordSet.put("let", KeyWord.LET);
        keywordSet.put("do", KeyWord.DO);
        keywordSet.put("if", KeyWord.IF);
        keywordSet.put("else", KeyWord.ELSE);
        keywordSet.put("while", KeyWord.WHILE);
        keywordSet.put("return", KeyWord.RETURN);
        keywordSet.put("true", KeyWord.TRUE);
        keywordSet.put("false", KeyWord.FALSE);
        keywordSet.put("null", KeyWord.NULL);
        keywordSet.put("this", KeyWord.THIS);

        this.tokenType = null;
    }

    public boolean hasMoreTokens() {
        return pos < bytes.length;
    }

    public void advance() throws JackCompilerException{
        if (!hasMoreTokens()) {
            throw new JackCompilerException();
        }

        byte curr = bytes[pos];
        if (curr == '"') {
            tokenType = TokenType.STRING_CONST;
            stringVal = "";
            pos++;
            while (bytes[pos] != '"') {
                stringVal += (char)bytes[pos];
                pos++;
            }
            pos++;

        } else if (curr >= '0' && curr <= '9') {
            tokenType = TokenType.INT_CONST;
            int val = 0;
            while (pos < bytes.length
                    && bytes[pos] >= '0'
                    && bytes[pos] <= '9') {
                val = 10 * val + (bytes[pos] - '0');
                pos++;
            }
            intVal = val;
        } else if (symbolSet.contains(curr)) {
            tokenType = TokenType.SYMBOL;
            symbol = curr;
            pos++;
        } else {
            String word = readTillSeparatorOrSymbol();
            if (keywordSet.containsKey(word)) {
                tokenType = TokenType.KEYWORD;
                keyWord = keywordSet.get(word);
            } else {
                tokenType = TokenType.IDENTIFIER;
                identifier = word;
            }
        }

        skipSeparator();
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public KeyWord getKeyWord() {
        return this.keyWord;
    }

    public byte getSymbol() {
        return this.symbol;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public int getIntVal() {
        return this.intVal;
    }

    public String getStringVal() {
        return this.stringVal;
    }

    private void skipSeparator() {
        while (pos < bytes.length && (
                (bytes[pos] == ' ')
                || (bytes[pos] == '\t')
                || (bytes[pos] == '\r')
                || (bytes[pos] == '\n')
        )) {
            pos++;
        }
    }

    private String readTillSeparatorOrSymbol() {
        String str = "";
        while (pos < bytes.length
                && (bytes[pos] != ' ')
                && (bytes[pos] != '\t')
                && (bytes[pos] != '\r')
                && (bytes[pos] != '\n')
                && (!symbolSet.contains(bytes[pos]))) {
            str += (char)bytes[pos];
            pos++;
        }
        return str;
    }

    public String toString() {
        if (this.tokenType == null) {
            return "Token Type: null";
        }
        switch (this.tokenType) {
            case STRING_CONST:
                return "STRING_CONST: " + stringVal;
            case SYMBOL:
                return "SYMBOL: " + new String(new char[] {(char)symbol});
            case KEYWORD:
                return "KEYWORD: " + keyWord.toString();
            case INT_CONST:
                return "INT_CONST: " + Integer.toString(intVal);
            case IDENTIFIER:
                return "IDENTIFIER: " + identifier;
        }
        return "error";
    }

    public static void main(String[] args) {
        try {
            JackTokenizer test = new JackTokenizer(args[0]);
            while (test.hasMoreTokens()) {
                test.advance();
                System.out.println(test.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static byte[] readFileWithCommentsRemoved(String path) throws IOException {
        byte[] buf = new byte[1];
        ArrayList<Byte> ret = new ArrayList<Byte>();
        File file = new File(path);
        FileInputStream stream = new FileInputStream(file);
        while (stream.read(buf) != -1) {
            if (buf[0] != '/') {
                ret.add(buf[0]);
            } else {
                if (stream.read(buf) != -1) {
                    if (buf[0] == '*') {
                        while (stream.read(buf) != -1) {
                            if (buf[0] == '*') {
                                if (stream.read(buf) != -1 && buf[0] == '/') {
                                    break;
                                }
                            }
                        }
                    } else if (buf[0] == '/') {
                        while (stream.read(buf) != -1) {
                            if (buf[0] == '\n') {
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }

        byte[] temp = new byte[ret.size()];
        for (int i = 0; i < ret.size(); i++) {
            temp[i] = ret.get(i);
        }
        return temp;
    }

}
