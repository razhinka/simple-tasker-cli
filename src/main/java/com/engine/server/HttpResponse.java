package com.engine.server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final int statusCode;
    private final String statusText;
    private final Map<String, String> headers = new HashMap<>();
    private final String body;

    public HttpResponse(int statusCode, String statusText, String body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body != null ? body : "";
        this.headers.put("Content-Type", "application/json; charset=UTF-8");
        this.headers.put("Access-Control-Allow-Origin", "*");
        this.headers.put("Content-Length", String.valueOf(this.body.getBytes(StandardCharsets.UTF_8).length));
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, "OK", body);
    }

    public static HttpResponse created(String body) {
        return new HttpResponse(201, "Created", body);
    }

    public static HttpResponse notFound(String message) {
        return new HttpResponse(404, "Not Found", "{\"error\": \"" + message + "\"}");
    }

    public static HttpResponse badRequest(String message) {
        return new HttpResponse(400, "Bad Request", "{\"error\": \"" + message + "\"}");
    }

    public static HttpResponse internalServerError(String message) {
        return new HttpResponse(500, "Internal Server Error", "{\"error\": \"" + message + "\"}");
    }

    public String toRawHttp() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        builder.append("\r\n");
        builder.append(body);
        return builder.toString();
    }
}