package ro.unibuc.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.userdetails.User;
import ro.unibuc.hello.data.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.hello.data.Account;
import ro.unibuc.hello.data.AccountRepository;
import ro.unibuc.hello.data.Role;
import ro.unibuc.hello.data.RoleRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {AccountRepository.class, UrlRepository.class})
public class HelloApplication {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UrlRepository urlRepository;

	private PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

	@PostConstruct
	public void runAfterObjectCreated() {
		accountRepository.deleteAll();
		roleRepository.deleteAll();
		urlRepository.deleteAll();

		Account radu = new Account("radu.ndlcu@gmail.com",
				"Radu", "Nedelcu",encoder.encode("1234"));
		Account admin = new Account("admin@gmail.com","admin","admin",encoder.encode("admin"));

		Role adminRole = new Role(1,"ADMIN");
		Role userRole = new Role(2,"USER");

		roleRepository.save(adminRole);
		roleRepository.save(userRole);

		admin.addRole(adminRole);
		radu.addRole(userRole);

		accountRepository.save(admin);
		accountRepository.save(radu);
	}

}
