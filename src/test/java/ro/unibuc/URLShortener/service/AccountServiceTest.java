package ro.unibuc.URLShortener.service;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Role;
import ro.unibuc.URLShortener.data.Url;
import ro.unibuc.URLShortener.services.AccountService;

import java.util.Optional;

@SpringBootTest
public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountService accountService = new AccountService();

    //@Test
    public void test_changePassword() {

        //Arrange
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String email = "admin@gmail.com";
        Account acc = new Account("admin@gmail.com", "admin", "admin", encoder.encode("admin"));
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(acc));
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        //Mockito.when(mockedStatic::getContext()).thenReturn(secCont);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.getPrincipal()).thenReturn(email);

        //Act
        accountService.changePassword("admin","admin2");


        //Assert
        acc = accountRepository.findByEmail(email).get();
        Assertions.assertTrue(encoder.matches("admin2", acc.getPassword()));
    }
}
