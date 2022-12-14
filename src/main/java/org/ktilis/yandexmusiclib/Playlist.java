package org.ktilis.yandexmusiclib;

import org.json.JSONObject;
import org.ktilis.yandexmusiclib.exeptions.NoTokenFoundException;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Playlist {
    private static final String BaseUrl = "https://api.music.yandex.net:443";

    @Async
    public static CompletableFuture<JSONObject> getTrack(String uid, String kind, @Nullable String page, @Nullable String pageSize) throws IOException, InterruptedException, ExecutionException {
        if(Objects.isNull(page)) page = "0";
        if(Objects.isNull(pageSize)) pageSize = "20";

        String urlToRequest = "/users/" + uid + "/playlists/" + kind + "?page=" + page + "&page-size=" + pageSize;
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getPlaylistUser() throws IOException, InterruptedException, ExecutionException, NoTokenFoundException {
        if (Objects.equals(Token.getToken(), "")) throw new NoTokenFoundException();
        String urlToRequest = "/users/" + Token.getUserId() + "/playlists/list";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> informPlaylist(String playlistUid, String playlistKind) throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/users/" + playlistUid + "/playlists/" + playlistKind;
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }
}
