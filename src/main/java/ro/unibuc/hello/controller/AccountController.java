package ro.unibuc.hello.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.unibuc.hello.config.CustomAuthenticationManager;
import ro.unibuc.hello.data.Account;
import ro.unibuc.hello.data.AccountRepository;
import ro.unibuc.hello.data.Role;
import ro.unibuc.hello.data.RoleRepository;
import ro.unibuc.hello.dto.AccountDTO;
import ro.unibuc.hello.dto.LoginDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomAuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountDTO accountDTO) {

        if (!accountDTO.getPassword().equals(accountDTO.getMatchingPassword()))
            return new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);

        if (accountRepository.existsByEmail(accountDTO.getEmail()))
            return new ResponseEntity<>("Email already used!", HttpStatus.BAD_REQUEST);

        Account newAcc = new Account(accountDTO.getEmail(), accountDTO.getFirstName(), accountDTO.getLastName(), passwordEncoder.encode(accountDTO.getPassword()));
        Optional<Role> defaultRole = roleRepository.findByName("USER");
        defaultRole.ifPresent(newAcc::addRole);
        accountRepository.save(newAcc);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);

    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
    //TO DO: This always returns 403 Forbidden because of line 65, why?y7

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               loginDTO.getEmail(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }

    @GetMapping("/admin/info")
    @ResponseBody
    public ResponseEntity<?> listAll() {
        List<Account> entity = accountRepository.findAll();


        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

}

