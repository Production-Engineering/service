package ro.unibuc.hello.data;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.Charset;
import java.util.Random;

@Document(collection = "encodings")
public class Url {
    private String shortUrl;
    @Id @NonNull private String longUrl;

    public Url(@NonNull String longUrl){
        this.longUrl = longUrl;
    }

    public void shortenUrl(){
        byte[] array = new byte[8];
        new Random().nextBytes(array);
        shortUrl = new String(array, Charset.forName("UTF-8"));
    }

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
