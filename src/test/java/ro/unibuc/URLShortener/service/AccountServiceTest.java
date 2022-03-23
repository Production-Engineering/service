package ro.unibuc.URLShortener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import ro.unibuc.URLShortener.data.*;
import ro.unibuc.URLShortener.dto.AccountDTO;
import ro.unibuc.URLShortener.services.AccountService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @InjectMocks
    AccountService accountService = new AccountService();

    @Test
    public void test_listAll() {
    //Arange
        Account radu = new Account("radu.ndlcu@gmail.com",
                "Radu", "Nedelcu", encoder.encode("1234"));
        Account admin = new Account("admin@gmail.com", "admin", "admin", encoder.encode("admin"));

        List<Account> resp = List.of(radu,admin);
        when(accountRepository.findAll()).thenReturn(resp);

        //Act
        List<Account> serviceResponse = accountService.listAll().getBody();
        //Assert
        Assertions.assertEquals(resp,serviceResponse);
    }
    @Test
    public void test_createAccountPasswordNotMatching() {
        //Arange
        AccountDTO dto = new AccountDTO("Radu", "Nedelcu", "12345", "1234", "radu.ndlcu@gmail.com");

        //Act
        var response = accountService.createAccount(dto);

        //Assert
        Assertions.assertEquals("Passwords don't match", response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void test_createAccountEmailAlreadyUsed() {
        //Arange
        AccountDTO dto = new AccountDTO("Radu", "Nedelcu", "12345", "12345", "radu.ndlcu@gmail.com");

        when(accountRepository.existsByEmail(any())).thenReturn(true);

        //Act
        var response = accountService.createAccount(dto);
        //Assert
        Assertions.assertEquals("Email already used!", response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void test_createAccount() {
        //Arange
        AccountDTO dto = new AccountDTO("Radu", "Nedelcu", "12345", "12345", "radu.ndlcu@gmail.com");

        when(accountRepository.existsByEmail(any())).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role(1, "USER")));
        //Act
        var response = accountService.createAccount(dto);
        //Assert
        Assertions.assertEquals("User registered successfully", response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
