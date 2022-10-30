package org.ktilis.yandexmusiclib;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Account {
    private static final String BaseUrl = "https://api.music.yandex.net:443";

    @Async
    public static CompletableFuture<JSONObject> expiriments() throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/account/experiments";
            JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }

    @Async
    public static CompletableFuture<JSONObject> promocode(String promocode, String language) throws IOException, ExecutionException, InterruptedException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/account/consume-promo-code";
            JSONObject result = NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, "code="+promocode+"&language="+language, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }

    @Async
    public static CompletableFuture<JSONObject> showSettings() throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/account/settings";
            JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }

    @Async
    public static CompletableFuture<JSONObject> settingsChange(String data) throws IOException, ExecutionException, InterruptedException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/account/settings";

            JSONObject result = NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, data, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }

    public static CompletableFuture<JSONObject> showInformAccount() throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/account/status";

            JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }

    public static CompletableFuture<JSONObject> getLikesTrack(String userId) throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/users/" + userId + "/likes/tracks";

        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    public static CompletableFuture<JSONObject> getDislikesTracks(String userId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/users/" + userId + "/dislikes/tracks";
            JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }


    private static CompletableFuture<JSONObject> error_not_token() {return CompletableFuture.completedFuture(new JSONObject("{\"error\": \"Not token\"}"));}
}
