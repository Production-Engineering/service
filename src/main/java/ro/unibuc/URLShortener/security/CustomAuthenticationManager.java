package ro.unibuc.URLShortener.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Role;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        System.out.println("Trying to authenticate: " + email +" " +password);
        Optional<Account> user = accountRepository.findByEmail(email);

        if (user.isEmpty()) {
            System.out.println("Didn't find the user");
            throw new BadCredentialsException("1000");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            System.out.println("Passwords don't match: "+ password + " " + user.get().getPassword());
            throw new BadCredentialsException("1000");
        }

        Set<Role> userRoles = user.get().getRoles();
        return new UsernamePasswordAuthenticationToken(email, null, userRoles.stream().map(x -> new SimpleGrantedAuthority(x.getName())).collect(Collectors.toList()));
    }

}