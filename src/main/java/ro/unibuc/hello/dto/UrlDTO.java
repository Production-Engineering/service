package ro.unibuc.hello.dto;

import com.mongodb.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UrlDTO {
    @NonNull
    private String shortUrl;

    @NonNull
    private String longUrl;

    public UrlDTO() {}

    @NonNull
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(@NonNull String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @NonNull
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(@NonNull String longUrl) {
        this.longUrl = longUrl;
    }
}
