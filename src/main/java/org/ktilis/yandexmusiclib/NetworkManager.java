package org.ktilis.yandexmusiclib;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public class NetworkManager {
    private static HttpClient client = HttpClient.newHttpClient();
    private static final String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36";
    private static final String X_Yandex_Music_Client = "YandexMusicAndroid/24022571";
    private static final String SECRET = "p93jhgh689SBReK6ghtw62";

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

    /**
     * @author Gleb Liutsko
     * @param trackId
     */
    @Async
    public static CompletableFuture<JSONObject> getDownloadInfoRequest(Integer trackId) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String TIMESTAMP = String.valueOf(System.currentTimeMillis() / 1000);

        // Generate sign
        String data = trackId+TIMESTAMP;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String sign = Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));

        String URL = "https://api.music.yandex.net/tracks/"+trackId+"/download-info?can_use_streaming=true&ts="+TIMESTAMP+"&sign="+sign;
        URL obj = new URL(URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");


        httpURLConnection.setRequestProperty("X-Yandex-Music-Client", X_Yandex_Music_Client);
        httpURLConnection.setRequestProperty("accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "OAuth "+ Token.getToken());

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
    @Async
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
