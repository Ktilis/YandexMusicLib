package org.ktilis.yandexmusiclib;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ToString
public class Track {
    private static final String BaseUrl = "https://api.music.yandex.net:443";
    private @Getter Integer id;
    private @Getter Integer realId;
    private @Getter String title;
    private @Getter ArrayList<Artist> artists;
    private @Getter ArrayList<Album> albums;
    private @Getter Cover.OgImage ogImage;
    private @Getter Integer durationMs;
    private @Getter Cover.TrackCover cover;
    private @Getter String backgroundVideoUri;
    private @Getter String trackSource;
    private @Getter Boolean availableForPremiumUsers;
    private @Getter Boolean availableFullWithoutPermission;
    private @Getter Boolean available;
    private @Getter Boolean lyricsAvailable;
    private @Getter String type;
    private @Getter Boolean hasAvailableSyncLyrics;
    private @Getter Boolean hasAvailableTextLyrics;
    private @Getter Integer previewDurationMs;
    private @Getter String majorName;
    private @Getter Integer majorId;
    private @Getter Integer fileSize;
    private @Getter String storageDir;
    private @Getter String trackSharingFlag;

    public Track(Integer id) {
        this.id = id;
    }

    @Async
    public CompletableFuture<JSONObject> getDownloadInfo() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/tracks/" + id + "/download-info";

        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<String> getMp3Link() throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/tracks/" + id + "/download-info";

            // Getting xml download info
            JSONObject downloadInfoObj = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, true).get();
            String resultGetXml = NetworkManager.getXml(downloadInfoObj.getJSONArray("result").getJSONObject(0).getString("downloadInfoUrl")).get();



            // Converting xml to json
            String xmlResultString = null;
            try
            {
                XmlMapper xmlMapper = new XmlMapper();
                JsonNode jsonNode = xmlMapper.readTree(resultGetXml.getBytes());
                ObjectMapper objectMapper = new ObjectMapper();
                xmlResultString = objectMapper.writeValueAsString(jsonNode);


            } catch (IOException e)
            {
                e.printStackTrace();
            }
            assert xmlResultString != null;
            JSONObject xmlResult = new JSONObject(xmlResultString);

            // Generating mp3 link
            String host = xmlResult.getString("host");
            String path = xmlResult.getString("path");
            String ts = xmlResult.getString("ts");
            String s = xmlResult.getString("s");

            String secret = String.format("XGRlBW9FXlekgbPrRHuSiA%s%s", path.substring(1, path.length() - 1), s);
            String sign = getMd5(secret);

            return CompletableFuture.completedFuture(String.format("https://%s/get-%s/%s/%s/%s", host, "mp3", sign, ts, path));
        }
        else
        {
            return CompletableFuture.completedFuture("Error: Not token");
        }
    }

    @Async
    public static CompletableFuture<JSONObject> likesTracks(ArrayList<String> likeTracks, String userId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/users/" + userId + "/likes/tracks/add-multiple";

            StringBuilder likeTracksIdString = new StringBuilder();
            int countTracksId = likeTracks.size();

            for (int i = 0; i < likeTracks.size(); i++) {
                if (countTracksId - 1 == i) {
                    likeTracksIdString.append(likeTracks.get(i));
                } else {
                    likeTracksIdString.append(likeTracks.get(i)).append(",");
                }
            }
            return CompletableFuture.completedFuture(NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, "track-ids="+likeTracksIdString, true).get());
        }
        else
        {
            return error_not_token();
        }
    }

    @Async
    public static CompletableFuture<JSONObject> removeLikesTracks(ArrayList<String> likeTracks, String userId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.getToken(), ""))
        {
            String urlToRequest = "/users/" + userId + "/likes/tracks/remove";

            StringBuilder removeTracksIdString = new StringBuilder();
            int countTracksId = likeTracks.size();

            for (int i = 0; i < likeTracks.size(); i++) {
                if (countTracksId - 1 == i) {
                    removeTracksIdString.append(likeTracks.get(i));
                } else {
                    removeTracksIdString.append(likeTracks.get(i)).append(",");
                }
            }
            return CompletableFuture.completedFuture(NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, "track-ids="+removeTracksIdString, true).get());
        }

        else
        {
            return error_not_token();
        }
    }

    @Async
    public CompletableFuture<JSONObject> getInformation() throws IOException, ExecutionException, InterruptedException {
        String urlToRequest = "/tracks";
        String tracksIdString = id.toString();

        JSONObject result = NetworkManager.postDataAndHeaders(BaseUrl + urlToRequest, "track-ids="+tracksIdString+"&with-positions=false", false).get().getJSONArray("result").getJSONObject(0);

        this.title = result.getString("title");
        this.type = result.getString("type");
        this.ogImage = new Cover.OgImage(result.getString("ogImage"));
        this.durationMs = result.getInt("durationMs");
        this.cover = new Cover.TrackCover(result.getString("coverUri"));
        String backgroundVideoUri;
        try {
            backgroundVideoUri = result.getString("backgroundVideoUri");
        } catch (Exception e) {
            backgroundVideoUri = "null";
        }
        this.backgroundVideoUri = backgroundVideoUri;
        this.artists = new ArrayList<>();
        for(Object o : result.getJSONArray("artists")) {
            JSONObject artist = (JSONObject) o;
            this.artists.add(new Artist(artist.getInt("id")));
        }
        this.albums = new ArrayList<>();
        for(Object o : result.getJSONArray("albums")) {
            JSONObject album = (JSONObject) o;
            this.albums.add(new Album(album.getInt("id")));
        }
        this.availableForPremiumUsers = result.getBoolean("availableForPremiumUsers");
        this.availableFullWithoutPermission = result.getBoolean("availableFullWithoutPermission");
        this.available = result.getBoolean("available");
        this.lyricsAvailable = result.getBoolean("lyricsAvailable");
        this.hasAvailableSyncLyrics = result.getJSONObject("lyricsInfo").getBoolean("hasAvailableSyncLyrics");
        this.hasAvailableTextLyrics = result.getJSONObject("lyricsInfo").getBoolean("hasAvailableTextLyrics");
        this.previewDurationMs = result.getInt("previewDurationMs");
        this.majorId = result.getJSONObject("major").getInt("id");
        this.majorName = result.getJSONObject("major").getString("name");
        this.fileSize = result.getInt("fileSize");
        this.realId = Integer.valueOf(result.getString("realId"));
        this.trackSharingFlag = result.getString("trackSharingFlag");

        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getTrackSimilar() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/tracks/" + id + "/similar";
        JSONObject result = NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JSONObject> getSupplement() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/tracks/" + id + "/supplement";
        return CompletableFuture.completedFuture(NetworkManager.getWithHeaders(BaseUrl + urlToRequest, false).get());
    }

    @Async
    private static CompletableFuture<JSONObject> error_not_token() {return CompletableFuture.completedFuture(new JSONObject("{\"error\": \"Not token\"}"));}

    private static String getMd5(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @AllArgsConstructor
    @ToString
    public static class ChartTrack {
        private @Getter Integer listeners;
        private @Getter Integer trackId;
        private @Getter Integer shift;
        private @Getter String progress;
        private @Getter Integer position;
    }
}
