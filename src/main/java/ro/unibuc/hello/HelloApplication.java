package ro.unibuc.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ro.unibuc.hello.data.Account;
import ro.unibuc.hello.data.AccountRepository;
import ro.unibuc.hello.data.Role;
import ro.unibuc.hello.data.RoleRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = AccountRepository.class)
public class HelloApplication {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

	@PostConstruct
	public void runAfterObjectCreated() {
		accountRepository.deleteAll();
		accountRepository.save(new Account("radu.ndlcu@gmail.com",
				"Radu", "Nedelcu","1234"));
		roleRepository.save(new Role(1,"admin"));
		roleRepository.save(new Role(2,"user"));
	}

}
