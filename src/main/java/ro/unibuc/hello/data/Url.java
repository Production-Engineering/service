package ro.unibuc.hello.data;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.Charset;
import java.util.Random;

@Document(collection = "encodings")
public class Url {

    private String userEmail; //If a logged in user created the url, store the email
    private String shortUrl;
    @Id @NonNull private String longUrl; //By having this as the ID we restrict users from shortening the same URL, should change

    public Url(@NonNull String longUrl){
        this.longUrl = longUrl;
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
