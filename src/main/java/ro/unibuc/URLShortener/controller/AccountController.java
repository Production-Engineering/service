package ro.unibuc.URLShortener.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.dto.AccountDTO;
import ro.unibuc.URLShortener.dto.LoginDTO;
import ro.unibuc.URLShortener.services.AccountService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody @Valid AccountDTO accountDTO) {

        return (ResponseEntity<String>) accountService.createAccount(accountDTO);

    }

    @PatchMapping("/account/changepassword")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword)
    {
        return accountService.changePassword(oldPassword, newPassword);
    }


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

