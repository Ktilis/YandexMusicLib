package org.ktilis.yandexmusiclib;

import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ToString
public class Artist {
    private static final String BaseUrl = "https://api.music.yandex.net:443";
    private @Getter Integer id;
    private @Getter ArrayList<String> dbAliases;
    private @Getter Boolean composer;

    private @Getter Integer countsAlsoAlbums;
    private @Getter Integer countsDirectAlbums;
    private @Getter Integer countsAlsoTracks;
    private @Getter Integer countsTracks;

    private @Getter Boolean available;
    private @Getter Boolean noPicturesFromSearch;
    private @Getter Boolean ticketsAvailable;
    private String ogImage;
    private @Getter String cover;
    private @Getter Integer likesCount;
    private @Getter Boolean various;
    private @Getter ArrayList<String> genres;
    private @Getter Integer ratingsWeek;
    private @Getter Integer ratingsMonth;
    private @Getter Integer ratingsDay;
    private @Getter String name;
    private @Getter ArrayList<Link.ArtistLink> links;
    private @Getter ArrayList<Cover> allCovers;
    private @Getter ArrayList<Integer> lastReleaseIds;
    private @Getter ArrayList<Artist> similarArtists;
    private @Getter Boolean hasPromotions;
    private @Getter ArrayList<String> concerts;
    private @Getter ArrayList<String> vinyls;
    private @Getter ArrayList<Album> alsoAlbums;
    private @Getter ArrayList<Track> lastReleases;
    private @Getter ArrayList<Track.ChartTrack> tracksInChart;
    private @Getter String backgroundVideoUrl;
    private @Getter ArrayList<Track> popularTracks;


    public Artist(Integer id, String name, boolean composer, boolean various) {
        this.id = id;
    }
    public Artist(Integer id) {
        this.id = id;

    }

    @Async
    public CompletableFuture<JSONObject> getTracks(@Nullable String page, @Nullable String pageSize) throws IOException, InterruptedException, ExecutionException {
        if(Objects.isNull(page)) page = "0";
        if(Objects.isNull(pageSize)) pageSize = "20";
        String urlToRequest = "/artists/" + id + "/tracks?page=" + page + "&page-size=" + pageSize;
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getTrackIdsByRating() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/artists/" + id + "/track-ids-by-rating";
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getBriefInfo() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/artists/" + id + "/brief-info";
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getDirectAlbums(@Nullable String page, @Nullable String pageSize, @Nullable String sortBy) throws IOException, InterruptedException, ExecutionException {
        if(Objects.isNull(page)) page = "0";
        if(Objects.isNull(pageSize)) pageSize = "20";
        if(Objects.isNull(sortBy)) sortBy = "--";

        String urlToRequest;
        if (sortBy == "--") {
            urlToRequest = "/artists/" + id + "/direct-albums?page=" + page + "&page-size=" + pageSize;
        } else {
            urlToRequest = "/artists/" + id + "/direct-albums?page=" + page + "&page-size=" + pageSize + "&sort-by=" + sortBy;
        }

        return CompletableFuture.completedFuture(PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get());
    }

    @Async
    public CompletableFuture<JSONObject> getInformation() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/artists/" + id;
        return CompletableFuture.completedFuture(PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get());
    }

    /**
     *
     * Param "size", format "200x200"(200px200px), available "100x100";"200x200";"300x300";"400x400", default "200x200"
     *
     * @param size
     * @return url
     */
    public String getOgImage(String size) {
        if(Objects.isNull(size)) size = "200x200";
        return ogImage.replaceAll("%%", size);
    }
}
