package ro.unibuc.URLShortener.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.unibuc.URLShortener.controller.UrlController;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Role;
import ro.unibuc.URLShortener.data.RoleRepository;
import ro.unibuc.URLShortener.dto.AccountDTO;
import ro.unibuc.URLShortener.dto.LoginDTO;
import ro.unibuc.URLShortener.security.CustomAuthenticationManager;

import java.util.List;
import java.util.Optional;

@Component
public class AccountService {
    Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomAuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> createAccount(AccountDTO accountDTO) {

        if (!accountDTO.getPassword().equals(accountDTO.getMatchingPassword()))
        {   logger.info("New account passwords don't match");
            return new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);}

        if (accountRepository.existsByEmail(accountDTO.getEmail()))
        {   logger.info("Email already used");
            return new ResponseEntity<>("Email already used!", HttpStatus.BAD_REQUEST);}

        Account newAcc = new Account(accountDTO.getEmail(), accountDTO.getFirstName(), accountDTO.getLastName(), passwordEncoder.encode(accountDTO.getPassword()));
        Optional<Role> defaultRole = roleRepository.findByName("USER");
        defaultRole.ifPresent(newAcc::addRole);
        accountRepository.save(newAcc);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);

    }


    public ResponseEntity<?> authenticateUser(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }

    public ResponseEntity<List<Account>> listAll() {
        List<Account> entity = accountRepository.findAll();
        return new ResponseEntity<List<Account>>(entity, HttpStatus.OK);
    }

    public ResponseEntity<String> changePassword(String oldPlainPass, String newPass) {
        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account authenticatedAcc = accountRepository.findByEmail(authenticatedEmail).get();
        String currentPassword = authenticatedAcc.getPassword();
        if (passwordEncoder.matches(oldPlainPass, currentPassword)) {
            String encodedPassword = passwordEncoder.encode(newPass);
            authenticatedAcc.setPassword(encodedPassword);
            accountRepository.save(authenticatedAcc);
            return new ResponseEntity<>("Password changed sucessfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
    }

}
