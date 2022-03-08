package ro.unibuc.hello.data;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.

@Document(collection = "encodings")
public class Url {
    @NonNull private String shortUrl;
    @Id @NonNull private String longUrl;

    public Url(String longUrl){
        this.longUrl = longUrl;
    }

    public void shortenUrl(){
        shortUrl = "aaa";
    }

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
