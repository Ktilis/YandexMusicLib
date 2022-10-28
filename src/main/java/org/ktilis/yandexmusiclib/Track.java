package org.ktilis.yandexmusiclib;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;

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
import java.util.concurrent.ExecutionException;

@ToString
public class Track {
    private static final String BaseUrl = "https://api.music.yandex.net:443";
    public final Integer id;
    public final String title;
    public final ArrayList<Artist> artists;
    public final ArrayList<Album> albums;
    public final String ogImage;
    public final int durationMs;
    public final String coverUri;
    public final String backgroundVideoUri;


    public Track(Integer id,
                 String title,
                 ArrayList<Artist> artists,
                 ArrayList<Album> albums,
                 String ogImage,
                 Integer durationMs,
                 String coverUri,
                 String backgroundVideoUri) {
        this.id = id;
        this.title = title;
        this.artists = artists;
        this.albums = albums;
        this.ogImage = ogImage;
        this.durationMs = durationMs;
        this.coverUri = coverUri;
        this.backgroundVideoUri = backgroundVideoUri;
    }
    public Track(Integer id) {
        try {
            this.id = id;

            JSONObject infoObj = this.getInformation().getJSONArray("result").getJSONObject(0);

            this.title = infoObj.getString("title");
            this.ogImage = infoObj.getString("ogImage");
            this.durationMs = infoObj.getInt("durationMs");
            this.coverUri = infoObj.getString("coverUri");

            String backgroundVideoUri;
            try {
                backgroundVideoUri = infoObj.getString("backgroundVideoUri");
            } catch (Exception e) {
                backgroundVideoUri = "null";
            }
            this.backgroundVideoUri = backgroundVideoUri;

            this.artists = new ArrayList<>();
            for(Object artis : infoObj.getJSONArray("artists")) {
                JSONObject artist = (JSONObject) artis;
                this.artists.add(new Artist(artist.getInt("id"),artist.getString("name"), artist.getBoolean("composer"), artist.getBoolean("various")));
            }

            this.albums = new ArrayList<>();
            for(Object albu : infoObj.getJSONArray("albums")) {
                JSONObject album = (JSONObject) albu;
                this.albums.add(new Album(album.getInt("id")));
            }



        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getDownloadInfo(String trackId) throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/tracks/" + trackId + "/download-info";

        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get();
        return result;
    }
    public String getMp3Link() throws IOException, InterruptedException, ExecutionException {
        if (Token.token != "")
        {
            String urlToRequest = "/tracks/" + id + "/download-info";
            JSONObject downloadInfoObj = PostGet.getWithHeaders(BaseUrl + urlToRequest, true).get();

            String resultGetXml = PostGet.getXml(downloadInfoObj.getJSONArray("result").getJSONObject(0).getString("downloadInfoUrl")).get();

            JSONObject xmlResult = getXmlOfJson(resultGetXml);

            String host = xmlResult.getString("host");
            String path = xmlResult.getString("path");
            String ts = xmlResult.getString("ts");
            String s = xmlResult.getString("s");

            String secret = String.format("XGRlBW9FXlekgbPrRHuSiA%s%s", path.substring(1, path.length() - 1), s);
            String sign = getMd5(secret);

            return String.format("https://%s/get-%s/%s/%s/%s", host, "mp3", sign, ts, path);
        }
        else
        {
            return "Error: Not token";
        }
    }

    public static JSONObject likesTracks(ArrayList<String> likeTracks, String userId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.token, ""))
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
            return PostGet.postDataAndHeaders(BaseUrl + urlToRequest, "track-ids="+likeTracksIdString, true).get();
        }
        else
        {
            return error_not_token();
        }
    }

    public static JSONObject removeLikesTracks(ArrayList<String> likeTracks, String userId) throws IOException, InterruptedException, ExecutionException {
        if (!Objects.equals(Token.token, ""))
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
            return PostGet.postDataAndHeaders(BaseUrl + urlToRequest, "track-ids="+removeTracksIdString, true).get();
        }

        else
        {
            return error_not_token();
        }
    }

    public JSONObject getInformation() throws IOException, ExecutionException, InterruptedException {
        String urlToRequest = "/tracks";
        String tracksIdString = id.toString();

        JSONObject result = PostGet.postDataAndHeaders(BaseUrl + urlToRequest, "track-ids="+tracksIdString+"&with-positions=false", false).get();
        return result;
    }

    public JSONObject getTrackSimilar() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/tracks/" + id + "/similar";
        JSONObject result = PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
        return result;
    }

    public JSONObject getSupplement() throws IOException, InterruptedException, ExecutionException {
        String urlToRequest = "/tracks/" + id + "/supplement";
        return PostGet.getWithHeaders(BaseUrl + urlToRequest, false).get();
    }

    private static JSONObject error_not_token() {return new JSONObject("{\"error\": \"Not token\"}");}

    private static JSONObject getXmlOfJson(String data)
    {
        String value = null;
        try
        {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(data.getBytes());
            ObjectMapper objectMapper = new ObjectMapper();
            value = objectMapper.writeValueAsString(jsonNode);


        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return new JSONObject(value);
    }

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
