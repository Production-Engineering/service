package ro.unibuc.hello.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import ro.unibuc.hello.data.Account;
import ro.unibuc.hello.data.AccountRepository;
import ro.unibuc.hello.data.Role;
import ro.unibuc.hello.data.RoleRepository;
import ro.unibuc.hello.dto.AccountDTO;
import ro.unibuc.hello.dto.Greeting;
import ro.unibuc.hello.dto.LoginDTO;


import javax.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
        if(accountRepository.existsByEmail(accountDTO.getEmail()))
        {
            return new ResponseEntity<>("Email already used!", HttpStatus.BAD_REQUEST);
        }
        else
        {
            Account newAcc = new Account(accountDTO.getEmail(), accountDTO.getFirstName(), accountDTO.getLastName(), accountDTO.getPassword());
            Optional<Role> defaultRole = roleRepository.findByName("DEFAULT");
            defaultRole.ifPresent(newAcc::addRole);
            accountRepository.save(newAcc);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        }
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<?> listAll(@RequestParam(name="title", required=false, defaultValue="Overview") String title) {
        List<Account> entity = accountRepository.findAll();
        List<Greeting> greetings = new ArrayList<>();

        return new ResponseEntity<>(greetings, HttpStatus.OK);
    }

}

