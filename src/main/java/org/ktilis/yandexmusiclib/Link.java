package org.ktilis.yandexmusiclib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

public class Link {

    @ToString
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

    @AllArgsConstructor
    @ToString
    public static class DownloadInfo {
        private @Getter Boolean preview;
        private @Getter String container;
        private @Getter String codec;
        private @Getter Integer bitrateInKbps;
        private @Getter String m3u8Url;
        private @Getter Boolean direct;
        private @Getter Boolean gain;
    }
}
