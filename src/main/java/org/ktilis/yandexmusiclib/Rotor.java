package org.ktilis.yandexmusiclib;

import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Rotor {
    private static final String BaseUrl = "https://api.music.yandex.net:443";

    public static JSONObject stationList(@Nullable String language, @Nullable String page, @Nullable String pageSize) throws IOException, InterruptedException, ExecutionException {
        if (Token.token != "")
        {
            if(Objects.isNull(language)) language = "ru";
            if(Objects.isNull(page)) page = "0";
            if(Objects.isNull(pageSize)) pageSize = "10";

            String urlToRequest = "/rotor/stations/list?language=" + language + "&page=" + page + "&page-size=" + pageSize;
            return PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get();
        }
        else
        {
            return error_not_token();
        }
    }

    public static JSONObject stationsDashboard() throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.token, ""))
        {
            String urlToRequest = "/rotor/stations/dashboard";
            return PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get();
        }
        else
        {
            return error_not_token();
        }
    }

    public static ArrayList<Track> getTracks(String rotorId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.token, ""))
        {
            String urlToRequest = "/rotor/station/" + rotorId + "/tracks";

            JSONObject obj = PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get().getJSONObject("result");

            ArrayList<Track> list = new ArrayList<>();
            for(Object obj2_1 : obj.getJSONArray("sequence")) {
                JSONObject obj2_2 = (JSONObject) obj2_1;
                JSONObject trackObj = obj2_2.getJSONObject("track");

                ArrayList<Artist> artists = new ArrayList<>();
                for(Object obj1 : trackObj.getJSONArray("artists")) {
                    JSONObject artist = (JSONObject) obj1;
                    artists.add(new Artist(artist.getInt("id"), artist.getString("name"), artist.getBoolean("composer"), artist.getBoolean("various")));
                }

                ArrayList<Album> albums = new ArrayList<>();
                for(Object obj1 : trackObj.getJSONArray("albums")) {
                    JSONObject album = (JSONObject) obj1;
                    albums.add(new Album(album.getInt("id")));
                }

                String backgroundVideoUri;
                try {
                    backgroundVideoUri = trackObj.getString("backgroundVideoUri");
                } catch (Exception e) {
                    backgroundVideoUri = "null";
                }

                list.add(new Track(
                        trackObj.getInt("id"),
                        trackObj.getString("title"),
                        artists,
                        albums,
                        Objects.isNull(trackObj.getString("ogImage")) ? "null" : trackObj.getString("ogImage"),
                        trackObj.getInt("durationMs"),
                        Objects.isNull(trackObj.getString("coverUri")) ? "null" : trackObj.getString("coverUri"),
                        backgroundVideoUri
                ));
            }

            return list;
        }
        else
        {
            return null;
        }
    }

    public static JSONObject getInfo(String rotorId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.token, ""))
        {
            String urlToRequest = "/rotor/station/" + rotorId + "/info";
            return PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get();
        }
        else
        {
            return error_not_token();
        }
    }

    private static JSONObject error_not_token() {return new JSONObject("{\"error\": \"Not token\"}");}
}
