package ro.unibuc.URLShortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.URLShortener.data.*;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {AccountRepository.class, UrlRepository.class})
public class URLShortenerApp {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(URLShortenerApp.class, args);
    }

    @PostConstruct
    public void runAfterObjectCreated() {
        accountRepository.deleteAll();
        roleRepository.deleteAll();
        urlRepository.deleteAll();

        Account radu = new Account("radu.ndlcu@gmail.com",
                "Radu", "Nedelcu", encoder.encode("1234"));
        Account admin = new Account("admin@gmail.com", "admin", "admin", encoder.encode("admin"));

        Role adminRole = new Role(1, "ADMIN");
        Role userRole = new Role(2, "USER");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        Url url = new Url("https://github.com/Production-Engineering/service/tree/main/src/main", "1234");

        admin.addRole(adminRole);
        radu.addRole(userRole);
        radu.addURL(url);
        urlRepository.save(url);
        accountRepository.save(admin);
        accountRepository.save(radu);
    }

}
