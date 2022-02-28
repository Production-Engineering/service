package ro.unibuc.hello.data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

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