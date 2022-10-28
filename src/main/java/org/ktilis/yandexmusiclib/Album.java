package org.ktilis.yandexmusiclib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@ToString
@AllArgsConstructor
public class Album {
    private static final String BaseUrl = "https://api.music.yandex.net:443";
    private @Getter final Integer id;
    private @Getter Boolean availableForPremiumUsers;
    private @Getter Boolean availablePartially;
    private @Getter Integer year;
    private @Getter final Date releaseDate;
    private @Getter Boolean available;
    private @Getter String title;
    private String ogImage;
    private @Getter ArrayList<String> labels;
    private @Getter final String metaTagId;
    private @Getter Boolean availableForMobile;
    private @Getter Integer trackCount;
    private @Getter ArrayList<Artist> artists;
    private @Getter String genre;
    private @Getter String metaType;
    private @Getter ArrayList<String> bests;
    private @Getter String coverUri;
    private @Getter Boolean recent;

    public Album(Integer id) {
        this.id = id;
        availableForPremiumUsers = null;
        availablePartially = null;
        year = null;
        releaseDate = null;
        available = null;
        title = null;
        ogImage = null;
        labels = null;
        metaTagId = null;
        availableForMobile = null;
        trackCount = null;
        artists = null;
        genre = null;
        metaType = null;
        bests = null;
        coverUri = null;
        recent = null;
    }

    @Async
    public CompletableFuture<JSONObject> getInformation() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/albums/" + id;
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();

        if(title != null || year != null || trackCount != null) {
            JSONObject obj = result.getJSONObject("result");

            try {
                ArrayList<Artist> artists = new ArrayList<>();
                for(Object objj : obj.getJSONArray("artists")) {
                    JSONObject objjj = (JSONObject) objj;
                    artists.add(new Artist(objjj.getInt("id"), objjj.getString("name"), objjj.getBoolean("composer"), objjj.getBoolean("various")));
                }
                ArrayList<String> labels = new ArrayList<>();
                for(Object objj : obj.getJSONArray("labels")) {
                    labels.add(((JSONObject) objj).getString("name"));
                }

                this.availableForPremiumUsers = obj.getBoolean("availableForPremiumUsers");
                this.title = obj.getString("title");
                this.trackCount = obj.getInt("trackCount");
                this.available = obj.getBoolean("available");
                this.availableForMobile = obj.getBoolean("availableForMobile");
                this.availablePartially = obj.getBoolean("availablePartially");
                this.ogImage = obj.getString("ogImage");
                this.genre = obj.getString("genre");
                this.year = obj.getInt("year");
                this.artists = artists;
                this.labels = labels;

            } catch (Exception e) {}
        }

        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getTracks() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/albums/" + id + "/with-tracks";
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    /**
     *
     * Param "size", format "200x200"(200px200px), available "100x100";"200x200";"300x300";"400x400", default "200x200"
     *
     * @param size
     * @return url
     */
    public String getOgImageUrl(@Nullable String size) {
        if(Objects.isNull(ogImage)) return null;
        if(Objects.isNull(size)) size = "200x200";

        return ogImage.replaceAll("%%", size);
    }
}
