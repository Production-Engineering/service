package ro.unibuc.URLShortener.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * No need to implement this interface.
 * Spring Data MongoDB automatically creates a class it implementing the interface when you run the application.
 */
@Repository
public interface UrlRepository extends MongoRepository<Url, String> {

    Boolean existsByShortUrl(String shortUrl);
    Boolean existsByLongUrl(String longUrl);

    Url findByShortUrl(String shortUrl);
    Url findByLongUrl(String longUrl);

}