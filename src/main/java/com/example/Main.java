package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String line[] = in.readLine().split(" ");
            String method = line[0];
            String resource = line[1];
            String version = line[2];
            String header;
            do {
                header = in.readLine();
                //System.out.println(header);
            } while (header.isEmpty());
            if (resource.equals("/")) {
                resource = "/index.html";
            }
            String type;
            File file = new File("htdocs" + resource);
            if (file.exists()) {
                switch (resource.split("\\.")[1]) {
                    case "html":
                        type = "text/html";
                        break;
                    case "txt":
                        type = "text/plain";
                        break;
                    default:
                        type = "";
                        break;
                }
                out.writeBytes(version + " 200 OK\n");
                out.writeBytes("Content-Type: " + type +"\n");
                out.writeBytes("Content-Length: " + file.length() +"\n");
                out.writeBytes("\n");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[8192];
                int n;
                while ((n = input.read(buf)) != 1) {
                    out.write(buf, 0, n);
                }
                input.close();
            } else {
                out.writeBytes(version + " 404 NOT FOUND\n");
                out.writeBytes("Content-Length: 0\n");
                out.writeBytes("\n");
                out.writeBytes("");
            }
        }
    }
}