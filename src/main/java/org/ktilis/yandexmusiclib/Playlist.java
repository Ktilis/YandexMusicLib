package org.ktilis.yandexmusiclib;

import org.json.JSONObject;
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
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public static CompletableFuture<JSONObject> getPlaylistUser(String userId) throws IOException, InterruptedException, ExecutionException {
        if (Token.token != "")
        {
            String urlToRequest = "/users/" + userId + "/playlists/list";
            JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get();
            return CompletableFuture.completedFuture(result);
        }
        else
        {
            return error_not_token();
        }
    }

    @Async
    public static CompletableFuture<JSONObject> informPlaylist(String playlistUid, String playlistKind) throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/users/" + playlistUid + "/playlists/" + playlistKind;
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    private static CompletableFuture<JSONObject> error_not_token() {return CompletableFuture.completedFuture(new JSONObject("{\"error\": \"Not token\"}"));}
}
