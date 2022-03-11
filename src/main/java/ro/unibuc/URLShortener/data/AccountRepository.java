package ro.unibuc.URLShortener.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * No need to implement this interface.
 * Spring Data MongoDB automatically creates a class it implementing the interface when you run the application.
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    List<Account> findByDateCreated(Date dateCreated);

}