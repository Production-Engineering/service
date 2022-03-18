package ro.unibuc.URLShortener.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Role;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository userRepository) {
        this.accountRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account acc = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: "+ email));
        return new User(acc.getEmail(), acc.getPassword(), mapRolesToAuthorities(acc.getRoles()));
    }


    public List<UserDetails> loadAllUsers()
    {
        var accounts = accountRepository.findAll();
        return accounts.stream().map(acc -> new User(acc.getEmail(),
                acc.getPassword(), mapRolesToAuthorities(acc.getRoles()))).collect(Collectors.toList());

    }

    public Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}