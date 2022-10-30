package org.ktilis.yandexmusiclib;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

public class NetworkManager {
    private static HttpClient client = HttpClient.newHttpClient();
    private static final String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36";

    @Async
    public static CompletableFuture<JSONObject> postReq(String url, String postParams) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");

        httpURLConnection.setRequestProperty("User-Agent", userAgent);
        httpURLConnection.setRequestProperty("accept", "application/json");

        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(postParams.getBytes());
        os.flush();
        os.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        return CompletableFuture.completedFuture(new JSONObject(response.toString()));
    }

    @Async
    public static CompletableFuture<JSONObject> getWithHeaders(String url, boolean authorization) throws IOException, InterruptedException {
        URL obj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");

        httpURLConnection.setRequestProperty("User-Agent", userAgent);
        httpURLConnection.setRequestProperty("accept", "application/json");
        if(authorization) {
            httpURLConnection.setRequestProperty("Authorization", "OAuth "+ Token.getToken());
        }

        httpURLConnection.setDoOutput(true);;
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        return CompletableFuture.completedFuture(new JSONObject(response.toString()));
    }

    @Async
    public static CompletableFuture<JSONObject> postDataAndHeaders(String url, String postParams, boolean authorization) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");

        httpURLConnection.setRequestProperty("User-Agent", userAgent);
        httpURLConnection.setRequestProperty("accept", "application/json");
        if(authorization) {
            httpURLConnection.setRequestProperty("Authorization", "OAuth "+ Token.getToken());
        }

        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(postParams.getBytes());
        os.flush();
        os.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        return CompletableFuture.completedFuture(new JSONObject(response.toString()));
    }

    @Async
    public static CompletableFuture<JSONObject> get(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");

        httpURLConnection.setRequestProperty("User-Agent", userAgent);
        httpURLConnection.setRequestProperty("accept", "application/json");

        httpURLConnection.setDoOutput(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        return CompletableFuture.completedFuture(new JSONObject(response.toString()));
    }
    public static CompletableFuture<String> getXml(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");

        httpURLConnection.setRequestProperty("User-Agent", userAgent);
        httpURLConnection.setRequestProperty("accept", "application/json");

        httpURLConnection.setDoOutput(true);;
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        return CompletableFuture.completedFuture(response.toString());
    }
}
