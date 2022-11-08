package org.ktilis.yandexmusiclib;

import lombok.Getter;
import org.json.JSONObject;
import org.ktilis.yandexmusiclib.exeptions.NoTokenFoundException;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Rotor {
    private static final String BaseUrl = "https://api.music.yandex.net:443";

    @Async
    public static CompletableFuture<JSONObject> stationList(@Nullable String language, @Nullable String page, @Nullable String pageSize) throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        if(Objects.isNull(language)) language = "ru";
        if(Objects.isNull(page)) page = "0";
        if(Objects.isNull(pageSize)) pageSize = "10";
        String urlToRequest = "/rotor/stations/list?language=" + language + "&page=" + page + "&page-size=" + pageSize;
        return CompletableFuture.completedFuture(NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get());
    }

    @Async
    public static CompletableFuture<JSONObject> stationsDashboard() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/rotor/stations/dashboard";
        return CompletableFuture.completedFuture(NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get());
    }

    public static class Station {
        private final @Getter String id;

        public Station(String id) {
            this.id = id;
        }

        @Async
        public CompletableFuture<ArrayList<Track>> getTracks() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
            if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
            String urlToRequest = "/rotor/station/" + id + "/tracks";
            JSONObject obj = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get().getJSONObject("result");
            ArrayList<Track> list = new ArrayList<>();
            for(Object obj2_1 : obj.getJSONArray("sequence")) {
                JSONObject obj2_2 = (JSONObject) obj2_1;
                JSONObject trackObj = obj2_2.getJSONObject("track");
                list.add(new Track(
                        trackObj.getInt("id")
                ));
            }
            return CompletableFuture.completedFuture(list);
        }

        @Async
        public CompletableFuture<JSONObject> getInfo() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
            if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
            String urlToRequest = "/rotor/station/" + id + "/info";
            return CompletableFuture.completedFuture(NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get());
        }
    }
}
