package org.ktilis.yandexmusiclib;

import org.json.JSONObject;
import org.ktilis.yandexmusiclib.exeptions.NoTokenFoundException;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Account {
    private static final String BaseUrl = "https://api.music.yandex.net:443";

    @Async
    public static CompletableFuture<JSONObject> expiriments() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/account/experiments";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> promocode(String promocode, String language) throws IOException, ExecutionException, InterruptedException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/account/consume-promo-code";
        JSONObject result = NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, "code="+promocode+"&language="+language, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> showSettings() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/account/settings";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> settingsChange(JSONObject data) throws IOException, ExecutionException, InterruptedException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/account/settings";
        JSONObject result = NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, data.toString(), true).get();
        return CompletableFuture.completedFuture(result);
    }

    public static CompletableFuture<JSONObject> showInformAccount() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/account/status";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    public static CompletableFuture<JSONObject> getLikesTrack() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/users/" + Token.getUserId() + "/likes/tracks";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    public static CompletableFuture<JSONObject> getDislikesTracks() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/users/" + Token.getUserId() + "/dislikes/tracks";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);

    }
}
