package ro.unibuc.URLShortener.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.dto.AccountDTO;
import ro.unibuc.URLShortener.dto.LoginDTO;
import ro.unibuc.URLShortener.services.AccountService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MeterRegistry metricsRegistry;

    @Timed(value = "urlshortener_create_account_time", description = "Time taken to create a new account")
    @Counted(value = "urlshortener_create_account_count", description = "Times a new account was created")
    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody @Valid AccountDTO accountDTO) {

        return (ResponseEntity<String>) accountService.createAccount(accountDTO);

    }

    @Timed(value = "urlshortener_change_password_time", description = "Time taken to change user's password")
    @Counted(value = "urlshortener_change_password_count", description = "Times a password was changed")
    @PatchMapping("/account/changepassword")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword)
    {
        return accountService.changePassword(oldPassword, newPassword);
    }


    @Timed(value = "urlshortener_login_time", description = "Time taken to log in an user")
    @Counted(value = "urlshortener_login_count", description = "Times an user has logged in")
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {

        return accountService.authenticateUser(loginDTO);
    }

    @GetMapping("/admin/info")
    @ResponseBody
    public ResponseEntity<List<Account>> listAll() {
        return (ResponseEntity<List<Account>>) accountService.listAll();
    }

}

