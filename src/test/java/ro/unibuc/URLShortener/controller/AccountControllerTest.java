package ro.unibuc.URLShortener.controller;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.util.JSONPObject;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.Role;
import ro.unibuc.URLShortener.dto.AccountDTO;
import ro.unibuc.URLShortener.services.AccountService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void test_createAccount() throws Exception {
        //Arange
        AccountDTO accountDTO = new AccountDTO("Radu", "Nedelcu", "1234", "1234","radu.ndlcu@gmail.com");
        ResponseEntity<String> response = new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        when(accountController.createAccount(any())).thenReturn(response);

        //Act
        MvcResult result = mockMvc.perform(post("/createAccount")
                .content(objectMapper.writeValueAsString(accountDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        //Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), response.getBody());
    }

    @Test
    public void test_createAccountPasswordsNotMatching() throws Exception {
        //Arange
        AccountDTO accountDTO = new AccountDTO("Radu", "Nedelcu", "1234", "12534","radu.ndlcu@gmail.com");
        ResponseEntity<String> response = new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);
        when(accountController.createAccount(any())).thenReturn(response);

        //Act
        MvcResult result = mockMvc.perform(post("/createAccount")
                .content(objectMapper.writeValueAsString(accountDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        //Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), response.getBody());
    }

    @Test
    @WithMockUser(username = "viewUser", authorities = { "ADMIN" })
    public void test_listAll() throws Exception {
        //Arange
        Account radu = new Account("radu.ndlcu@gmail.com",
                "Radu", "Nedelcu", encoder.encode("1234"));
        Account admin = new Account("admin@gmail.com", "admin", "admin", encoder.encode("admin"));

        ResponseEntity<List<Account>> response = new ResponseEntity<>(List.of(radu,admin), HttpStatus.OK);
        when(accountController.listAll()).thenReturn(response);

        //Act
        MvcResult result = mockMvc.perform(get("/admin/info")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Assert
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(List.of(radu,admin));
        Assertions.assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(List.of(radu,admin)));
    }
    @Test
    @WithMockUser(username = "viewUser", authorities = { "ADMIN" }, password = "1234")
    public void test_changePassword() throws Exception {
        //Arange

        ResponseEntity<String> response = new ResponseEntity<>("Password changed sucessfully!", HttpStatus.OK);
        when(accountController.changePassword(any(), any())).thenReturn(response);

        //Act
        MvcResult result = mockMvc.perform(patch("/account/changepassword?oldPassword=1234&newPassword=admin2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Assert
        ObjectMapper mapper = new ObjectMapper();
        Assertions.assertEquals(result.getResponse().getContentAsString(), response.getBody());
    }
}
