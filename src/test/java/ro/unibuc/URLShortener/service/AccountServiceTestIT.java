package ro.unibuc.URLShortener.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.URLShortener.data.*;
import ro.unibuc.URLShortener.dto.AccountDTO;
import ro.unibuc.URLShortener.dto.LoginDTO;
import ro.unibuc.URLShortener.services.AccountService;

@SpringBootTest
public class AccountServiceTestIT {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UrlRepository urlRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder encoder;
    @BeforeEach
    public void setup()
    {
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
    @Test
    public void test_authenticate()
    {
        accountService.authenticateUser(new LoginDTO("radu.ndlcu@gmail.com", "1234"));
        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assertions.assertEquals("radu.ndlcu@gmail.com", authenticatedEmail);
    }
    @Test
    public void test_authenticatebadcredentials()
    {
       try{ accountService.authenticateUser(new LoginDTO("radu.ndlcu@gmail.com", "12345"));}
       catch (Exception ex) {  Assertions.assertEquals(ex.getClass(), BadCredentialsException.class);
           Assertions.assertEquals(ex.getMessage(), "1000");}
    }
    @Test
    public void test_createAccount()
    {
        String email = "radu2.ndlcu@gmail.com";
        AccountDTO dto = new AccountDTO("Radu", "Nedelcu", "12345", "12345", email);

        var response = accountService.createAccount(dto);
        Assertions.assertEquals("User registered successfully", response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(accountRepository.findByEmail(email).isPresent());
    }
    @Test
    public void test_createAccountAlreadyExists()
    {
        String email = "radu.ndlcu@gmail.com";
        AccountDTO dto = new AccountDTO("Radu", "Nedelcu", "12345", "12345", email);
        Assertions.assertTrue(accountRepository.findByEmail(email).isPresent());
        var response = accountService.createAccount(dto);
        Assertions.assertEquals("Email already used!", response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void test_changePassword()
    {
        String email = "radu.ndlcu@gmail.com";

        accountService.authenticateUser(new LoginDTO(email, "1234"));
       var response = accountService.changePassword("1234","asdf");
        Assertions.assertEquals("Password changed sucessfully!", response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        String changedPass = accountRepository.findByEmail(email).get().getPassword();
        Assertions.assertTrue(encoder.matches("asdf", changedPass));
    }
    @Test
    public void test_changePasswordBadCredentials()
    {
        String email = "radu.ndlcu@gmail.com";

        accountService.authenticateUser(new LoginDTO(email, "1234"));
        var response = accountService.changePassword("12345","asdf");
        Assertions.assertEquals("Old password is incorrect", response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String changedPass = accountRepository.findByEmail(email).get().getPassword();
        Assertions.assertTrue(encoder.matches("1234", changedPass));
    }
}
