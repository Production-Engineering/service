package ro.unibuc.URLShortener.data;

import com.mongodb.lang.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "encodings")
public class Url {


    @Id
    @NonNull
    private String shortUrl;
    @NonNull
    private String longUrl;

    @NonNull
    public Date getDateCreated() {
        return dateCreated;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @NonNull
    private Date dateCreated;
    private String userEmail; //If a logged in user created the url, store the email

    public Url(@NonNull String longUrl, @NonNull String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.dateCreated = new Date();
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public @NotNull String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(@NonNull String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @NonNull
    public String getLongUrl() {
        return longUrl;
    }

}
