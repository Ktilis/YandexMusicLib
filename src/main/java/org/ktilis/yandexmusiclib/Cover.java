package org.ktilis.yandexmusiclib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


@AllArgsConstructor
public class Cover {
    private @Getter String prefix;
    private @Getter String type;
    private String url;

    /**
     * @param size format "200x200"(200px200px), available "100x100";"200x200";"300x300";"400x400", default "200x200"
     * @return url
     */
    public URL getURL(@Nullable String size) throws MalformedURLException {
        if(Objects.isNull(size)) size = "200x200";
        return new URL("https://"+url.replaceAll("%%", size));
    }

    /**
     * @return url of Image, 200x200 pixels
     */
    public URL getURL() throws MalformedURLException {
        return this.getURL(null);
    }


    @AllArgsConstructor
    public static class OgImage {
        private String url;

        /**
         * @param size format "200x200"(200px200px), available "100x100";"200x200";"300x300";"400x400", default "200x200"
         * @return url
         */
        public URL getURL(@Nullable String size) throws MalformedURLException {
            if(Objects.isNull(size)) size = "200x200";
            return new URL("https://"+url.replaceAll("%%", size));
        }

        /**
         * @return url of Image, 200x200 pixels
         */
        public URL getURL() throws MalformedURLException {
            return this.getURL(null);
        }
    }

    @AllArgsConstructor
    public static class TrackCover {
        private String url;

        /**
         * @param size format "200x200"(200px200px), available "100x100";"200x200";"300x300";"400x400", default "200x200"
         * @return url
         */
        public URL getURL(@Nullable String size) throws MalformedURLException {
            if(Objects.isNull(size)) size = "200x200";
            return new URL("https://"+url.replaceAll("%%", size));
        }

        /**
         * @return url of Image, 200x200 pixels
         */
        public URL getURL() throws MalformedURLException {
            return this.getURL(null);
        }
    }
}
