package com.engine.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpParser {
    public static HttpRequest parse(InputStream inputStream) throws IOException {
        String method;
        String path;
        Map<String, String> headers = new HashMap<>();
        String body = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String headerLine;

        String line = reader.readLine();
        String[] header = line.split(" ");
        if (header.length != 3) {
            return null;
        }
        method = header[0];
        path = header[1];

        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            int colonIndex = headerLine.indexOf(":");
            if (colonIndex != -1) {
                String key = headerLine.substring(0, colonIndex).trim().toLowerCase();
                String value = headerLine.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }

        if (headers.containsKey("content-length")) {
            int length = Integer.parseInt(headers.get("content-length"));
            char[] buf = new char[length];
            int readed = reader.read(buf, 0, length);
            if (readed != length) {
                return null;
            }
            body = new String(buf);
        }

        return new HttpRequest(method, path, headers, body);
    }
}
