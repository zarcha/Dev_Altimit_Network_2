package com.altimit_server.ControlPanel;

import java.net.*;
import java.io.*;

import com.altimit_server.util.PropertiesManager;
import com.sun.net.httpserver.*;

public class AltimitHttpServer {

    private static HttpServer server;

    public static void StartHttpServer(PropertiesManager propertiesManager){

        try{
            server = HttpServer.create(new InetSocketAddress(propertiesManager.getHttpPort()), 0);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        server.createContext("/", new Handler());
        server.start();
        System.out.println("Http server has started...");
    }

    private static class Handler implements HttpHandler{
        public void handle(HttpExchange t) throws IOException {
            String root = "C:\\Users\\zarch\\Documents\\Projects\\Dev_Altimit_Network_2\\Java_AltmitServer\\out\\production\\Java_AltmitServer\\dist";
            String uri = t.getRequestURI().toString();

            if("/".equals(uri)){
                uri = "/index.html";
            }

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(uri);

            File file = new File(root + uri).getCanonicalFile();

            if (!file.getPath().startsWith(root)) {
                // Suspected path traversal attack: reject with 403 error.
                String response = "403 (Forbidden)\n";
                t.sendResponseHeaders(403, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (!file.isFile()) {
                // Object does not exist or is not a file: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Object exists and is a file: accept with response code 200.
                final byte[] buffer = new byte[0x10000];
                int count = 0;

                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                FileInputStream fs = new FileInputStream(file);

                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer,0,count);
                }

                fs.close();
                os.close();
            }
        }
    }

}
