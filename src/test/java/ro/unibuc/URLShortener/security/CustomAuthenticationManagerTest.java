package ro.unibuc.URLShortener.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Role;
import ro.unibuc.URLShortener.services.AccountService;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CustomAuthenticationManagerTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    PasswordEncoder encoder;
    @InjectMocks
    CustomAuthenticationManager authenticationManager = new CustomAuthenticationManager();


    @Test
    public void test_authenticateUserBadCredentials()
    {
        //Arrange
        String email = "radu.ndlcu@gmail.com";
        String password = encoder.encode("1234");
        Account radu = new Account(email,
                "Radu", "Nedelcu", password);
        radu.addRole(new Role(1,"ADMIN"));
        System.out.println(password);
        Authentication testAuth = new UsernamePasswordAuthenticationToken(email, password );
        when(accountRepository.findByEmail(any())).thenReturn(Optional.of(radu));

        //Act
        try{
        Authentication returned = authenticationManager.authenticate(testAuth);}
        catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), BadCredentialsException.class);
            Assertions.assertEquals(ex.getMessage(), "1000");
        }
    }
    @Test
    public void test_authenticateUserNotFound()
    {
        //Arrange
        String email = "radu.ndlcu@gmail.com";
        String password = encoder.encode("1234");
        Authentication testAuth = new UsernamePasswordAuthenticationToken(email, password );
        when(accountRepository.findByEmail(any())).thenReturn(Optional.empty());

        //Act
        try{
            Authentication returned = authenticationManager.authenticate(testAuth);}
        catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), BadCredentialsException.class);
            Assertions.assertEquals(ex.getMessage(), "1000");
        }
    }
}
