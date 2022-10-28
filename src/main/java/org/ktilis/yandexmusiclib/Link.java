package org.ktilis.yandexmusiclib;

import org.springframework.lang.Nullable;

public class Link {
    public static class ArtistLink {
        public String href;
        public String title;
        public String type;

        public String socialNetwork;


        public ArtistLink(String href, String title, String type, @Nullable String socialNetwork) {
            this.href = href;
            this.title = title;
            this.type = type;
            this.socialNetwork = socialNetwork;
        }
    }
}
