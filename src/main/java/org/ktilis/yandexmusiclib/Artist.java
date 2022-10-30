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

    private @Getter Integer countAlsoAlbums;
    private @Getter Integer countDirectAlbums;
    private @Getter Integer countAlsoTracks;
    private @Getter Integer countTracks;

    private @Getter Boolean available;
    private @Getter Boolean noPicturesFromSearch;
    private @Getter Boolean ticketsAvailable;
    private @Getter Cover.OgImage ogImage;
    private @Getter Cover cover;
    private @Getter Integer likesCount;
    private @Getter Boolean various;
    private @Getter ArrayList<String> genres;
    private @Getter Integer ratingsWeek;
    private @Getter Integer ratingsMonth;
    private @Getter Integer ratingsDay;
    private @Getter String name;
    private @Getter ArrayList<Link.ArtistLink> links;
    private @Getter ArrayList<Cover> allCovers;
    private @Getter ArrayList<Album> lastReleaseIds;
    private @Getter ArrayList<Artist> similarArtists;
    private @Getter Boolean hasPromotions;
    private @Getter ArrayList<String> concerts;
    private @Getter ArrayList<String> vinyls;
    private @Getter ArrayList<Album> alsoAlbums;
    private @Getter ArrayList<Album> lastReleases;
    private @Getter ArrayList<Track.ChartTrack> tracksInChart;
    private @Getter String backgroundVideoUrl;
    private @Getter ArrayList<Track> popularTracks;


    public Artist(Integer id) {
        this.id = id;
    }

    @Async
    public CompletableFuture<JSONObject> getTracks(@Nullable String page, @Nullable String pageSize) throws IOException, InterruptedException, ExecutionException {
        if(Objects.isNull(page)) page = "0";
        if(Objects.isNull(pageSize)) pageSize = "20";
        String urlToRequest = "/artists/" + id + "/tracks?page=" + page + "&page-size=" + pageSize;
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getTrackIdsByRating() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/artists/" + id + "/track-ids-by-rating";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getBriefInfo() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/artists/" + id + "/brief-info";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
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

        return CompletableFuture.completedFuture(NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get());
    }

    @Async
    public CompletableFuture<JSONObject> getInformation() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/artists/" + id;

        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get().getJSONObject("result");

        this.dbAliases = new ArrayList<>();
        for(Object s : result.getJSONObject("artist").getJSONArray("dbAliases")) {
            String ss = (String) s;
            this.dbAliases.add(ss);
        }
        this.composer = result.getJSONObject("artist").getBoolean("composer");

        this.countAlsoAlbums = result.getJSONObject("artist").getJSONObject("counts").getInt("alsoAlbums");
        this.countDirectAlbums = result.getJSONObject("artist").getJSONObject("counts").getInt("directAlbums");
        this.countAlsoTracks = result.getJSONObject("artist").getJSONObject("counts").getInt("alsoTracks");
        this.countTracks = result.getJSONObject("artist").getJSONObject("counts").getInt("tracks");

        this.available = result.getJSONObject("artist").getBoolean("available");
        try {
            this.noPicturesFromSearch = result.getJSONObject("artist").getBoolean("noPicturesFromSearch");
        } catch (Exception ignored) {}
        this.ticketsAvailable = result.getJSONObject("artist").getBoolean("ticketsAvailable");
        this.ogImage = new Cover.OgImage(result.getJSONObject("artist").getString("ogImage"));
        this.cover = new Cover(result.getJSONObject("artist").getJSONObject("cover").getString("prefix"),
                result.getJSONObject("artist").getJSONObject("cover").getString("type"),
                result.getJSONObject("artist").getJSONObject("cover").getString("uri")
        );
        this.likesCount = result.getJSONObject("artist").getInt("likesCount");
        this.various = result.getJSONObject("artist").getBoolean("various");
        this.genres = new ArrayList<>();
        for(Object o : result.getJSONObject("artist").getJSONArray("genres")) {
            String s = (String) o;
            this.genres.add(s);
        }
        this.ratingsWeek = result.getJSONObject("artist").getJSONObject("ratings").getInt("week");
        this.ratingsMonth = result.getJSONObject("artist").getJSONObject("ratings").getInt("month");;
        this.ratingsDay = result.getJSONObject("artist").getJSONObject("ratings").getInt("day");;
        this.name = result.getJSONObject("artist").getString("name");
        this.links = new ArrayList<>();
        for(Object o : result.getJSONObject("artist").getJSONArray("links")) {
            JSONObject obj = (JSONObject) o;
            String socialNetwork = null;
            try {
                socialNetwork = obj.getString("socialNetwork");
            } catch (Exception ignored) {};
            this.links.add(new Link.ArtistLink(obj.getString("href"),
                    obj.getString("title"),
                    obj.getString("type"),
                    socialNetwork
            ));
        }
        this.allCovers = new ArrayList<>();
        for(Object o : result.getJSONArray("allCovers")) {
            JSONObject obj = (JSONObject) o;
            this.allCovers.add(new Cover(obj.getString("prefix"),
                    obj.getString("type"),
                    obj.getString("uri")
            ));
        }
        this.lastReleaseIds = new ArrayList<>();
        for(Object o : result.getJSONArray("lastReleaseIds")) {
            Integer i = (Integer) o;
            this.lastReleaseIds.add(new Album(i));
        }
        this.similarArtists = new ArrayList<>();
        for(Object o : result.getJSONArray("similarArtists")) {
            JSONObject obj = (JSONObject) o;
            this.similarArtists.add(new Artist(obj.getInt("id")));
        }
        this.hasPromotions = result.getBoolean("hasPromotions");
        this.concerts = null; //TODO: concerts
        this.vinyls = null; //TODO: vinyls
        this.alsoAlbums = new ArrayList<>();
        for(Object o : result.getJSONArray("alsoAlbums")) {
            JSONObject obj = (JSONObject) o;
            this.alsoAlbums.add(new Album(obj.getInt("id")));
        }
        this.lastReleases = new ArrayList<>();
        for(Object o : result.getJSONArray("lastReleases")) {
            JSONObject obj = (JSONObject) o;
            this.lastReleases.add(new Album(obj.getInt("id")));
        }
        this.tracksInChart = new ArrayList<>();
        try {
            for (Object o : result.getJSONArray("tracksInChart")) {
                JSONObject obj = (JSONObject) o;
                this.tracksInChart.add(new Track.ChartTrack(obj.getInt("listeners"),
                        obj.getJSONObject("trackId").getInt("id"),
                        obj.getInt("shift"),
                        obj.getString("progress"),
                        obj.getInt("position")
                ));
            }
        } catch (Exception ignored) {}
        try {
            this.backgroundVideoUrl = result.getString("backgroundVideoUrl");
        } catch (Exception ignored) {}
        this.popularTracks = new ArrayList<>();
        for(Object o : result.getJSONArray("popularTracks")) {
            JSONObject obj = (JSONObject) o;
            this.popularTracks.add(new Track(obj.getInt("id")));
        }

        return CompletableFuture.completedFuture(result);
    }
}
