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
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("Server avviato!");
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
            } while (header.isEmpty());
            if (resource.equals("/")) {
                resource = "/index.html";
            }
            File file = new File("htdocs" + resource);
            if (file.exists()) {
                out.writeBytes(version + " 200 OK\n");
                out.writeBytes("Content-Type: " + getConentType(file) +"\n");
                out.writeBytes("Content-Length: " + file.length() +"\n");
                out.writeBytes("\n");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[8192];
                int n;
                while ((n = input.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }
                input.close();
            } else {
                out.writeBytes(version + " 404 NOT FOUND\n");
                out.writeBytes("Content-Length: 0\n");
                out.writeBytes("\n");
                out.writeBytes("");
            }
            s.close();
        }
    }

    private static String getConentType(File file) {
        String ext = file.getName().split("\\.")[1];
        System.out.println(file.getName());
        switch (ext) {
            case "html":
            case "htm":
                return "text/html";
            case "css":
                return "text/css";
            case "jpg":
            case "jpeg":
            case "jpg_large":
            case "jfif":
                return "image/jpeg";
            case "webp":
                return "image/webp";
            case "png":
                return "image/png";
            case "mp4":
                return "video/mp4";
            default:
                return "";
        }
    }
}