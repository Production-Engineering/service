package ro.unibuc.URLShortener.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomUserDetailsServiceTest {
    @Mock
    AccountRepository accountRepository;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @InjectMocks
    CustomUserDetailsService userDetailsService = new CustomUserDetailsService(accountRepository);

    @Test
    public void test_loadUserByUsername()
    {
        //Arrange
        String email = "radu.ndlcu@gmail.com";
        String password = encoder.encode("1234");
        Account radu = new Account(email,
                "Radu", "Nedelcu", password);
        Role admin = new Role(1,"ADMIN");
        radu.addRole(admin);

        when(accountRepository.findByEmail(any())).thenReturn(Optional.of(radu));

        //Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        AssertAccount(userDetails,radu);
    }
    @Test
    public void test_loadUserByUsernameNotFound()
    {
        //Arrange
        String email = "radu.ndlcu@gmail.com";

        when(accountRepository.findByEmail(any())).thenReturn(Optional.empty());

        //Act
        try{
            userDetailsService.loadUserByUsername(email);}
        catch(Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), UsernameNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "User not found with email: "+ email);
        }
    }
    @Test
    public void test_loadAllUsers()
    {
        //Arrange
        Account radu = new Account("radu.ndlcu@gmail.com",
                "Radu", "Nedelcu", encoder.encode("1234"));
        Account admin = new Account("admin@gmail.com", "admin", "admin", encoder.encode("admin"));
        Role userRole = new Role(1,"USER");
        Role adminRole = new Role(2, "ADMIN");
        radu.addRole(userRole);
        admin.addRole(adminRole);
        when(accountRepository.findAll()).thenReturn(List.of(radu,admin));

        //Act
        var allUsers = userDetailsService.loadAllUsers();

        // Assert
        AssertAccount(allUsers.get(0),radu);
        AssertAccount(allUsers.get(1), admin);
    }
    private void AssertAccount(UserDetails userDetails, Account acc)
    {
        Assertions.assertEquals(userDetails.getUsername(), acc.getEmail());
        Assertions.assertEquals(userDetails.getPassword(), acc.getPassword());
        Assertions.assertTrue(userDetails.getAuthorities().containsAll(userDetailsService.mapRolesToAuthorities(acc.getRoles())));
    }


}
