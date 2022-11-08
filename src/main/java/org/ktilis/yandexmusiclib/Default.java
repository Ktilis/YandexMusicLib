package org.ktilis.yandexmusiclib;

import org.json.JSONObject;
import org.ktilis.yandexmusiclib.exeptions.NoTokenFoundException;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Default {
    private static final String BaseUrl = "https://api.music.yandex.net:443";

    @Async
    public static CompletableFuture<JSONObject> search(String searchRequest, @Nullable Integer page, @Nullable String typeSearch, @Nullable Boolean nocorrect) throws IOException, ExecutionException, InterruptedException {
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(typeSearch)) typeSearch = "all";
        if (Objects.isNull(nocorrect)) nocorrect = false;
        String urlToRequest = "/search?text=" + searchRequest
                + "&page=" + page
                + "&type=" + typeSearch
                + "&nocorrect=" + nocorrect;
        JSONObject result = NetworkManager.get(BaseUrl + urlToRequest).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> searchBest(String searchRequest) throws IOException, ExecutionException, InterruptedException {
        String urlToRequest = "/search/suggest?part=" + searchRequest;
        JSONObject result = NetworkManager.get(BaseUrl + urlToRequest).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getAllGenres() throws IOException, ExecutionException, InterruptedException {
        String urlToRequest = "/genres";
        JSONObject result = NetworkManager.get(BaseUrl + urlToRequest).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getAllFeed(@Nullable Integer page) throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        if (Objects.isNull(page)) page = 0;
        String urlToRequest = "/feed?page=" + page;
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getChart() throws IOException, ExecutionException, InterruptedException {
        String urlToRequest = "/landing3/chart";;
        JSONObject result = NetworkManager.get(BaseUrl + urlToRequest).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getNewPlaylists() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/landing3/new-playlists";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getPodcasts() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/landing3/podcasts";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);

    }
}

