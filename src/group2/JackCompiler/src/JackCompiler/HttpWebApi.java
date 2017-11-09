package JackCompiler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.function.Consumer;


public class HttpWebApi {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(80), 10);
            server.setExecutor(null);
            server.createContext(
                    "/endpoint",
                    (HttpExchange httpExchange) -> {

                        String tempDir = System.getProperty("java.io.tmpdir");;

                        InputStream input = httpExchange.getRequestBody();
                        StringBuilder stringBuilder = new StringBuilder();

                        new BufferedReader(new InputStreamReader(input))
                                .lines()
                                .forEach((String s) -> stringBuilder.append(s + "\n"));

                        String s = stringBuilder.toString();

                        File txt = new File(tempDir + "temp.jack");
                        if (!txt.exists()) {
                            txt.createNewFile();
                        }

                        byte bytes[] = new byte[512];
                        bytes = s.getBytes();
                        int b = s.length();
                        FileOutputStream fos = new FileOutputStream(txt);
                        fos.write(bytes, 0, b);
                        fos.close();

                        JackAnalyzer.main(new String[]{tempDir + "temp.jack", tempDir + "temp.xml"});

                        String ret = readTxtFile(tempDir + "temp.xml");
                        httpExchange.sendResponseHeaders(200, ret.length());

                        OutputStream output = httpExchange.getResponseBody();
                        output.write(ret.getBytes());

                        httpExchange.close();
                    }
            );

            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readTxtFile(String filePath) {
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                String str = "";
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    str += (lineTxt + "\r\n");
                }
                read.close();
                return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
