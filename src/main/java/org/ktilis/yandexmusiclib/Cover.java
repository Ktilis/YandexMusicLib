package org.ktilis.yandexmusiclib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@ToString
@AllArgsConstructor
public class Cover {
    private @Getter String prefix;
    private @Getter String type;
    private String url;

    /**
     *
     * Param "size", format "200x200"(200px200px), available "100x100";"200x200";"300x300";"400x400", default "200x200"
     *
     * @param size
     * @return url
     */
    public URL getURL(@Nullable String size) throws MalformedURLException {
        if(Objects.isNull(size)) size = "200x200";
        return new URL(url.replaceAll("%%", size));
    }
}
