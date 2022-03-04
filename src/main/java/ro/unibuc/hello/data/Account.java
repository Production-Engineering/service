package ro.unibuc.hello.data;

import com.mongodb.lang.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "accounts")
public class Account{


    @Id @NonNull private String email;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String password;
    @NonNull private Date dateCreated;
    private Set<Role> roles = new HashSet<>();
    public Account(@NotNull String email, @NotNull String firstName, @NotNull String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateCreated = new Date();
        this.password = new BCryptPasswordEncoder().encode(password);
    }
    public void changePassword(String oldPlainPass, String newPass)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(oldPlainPass,password))
        {
            this.password = passwordEncoder.encode(newPass);
        }
    }
    public void addRole(Role role)
    {
        roles.add(role);
    }
    public void removeRole(Role role)
    {
        roles.remove(role);
    }
    public Set<Role> getRoles()
    {
        return this.roles;
    }
    public String getPassword() {
        return this.password;
    }
    @Override
    public String toString() {
        return String.format(
                "Account[email='%s', firstName='%s', lastName='%s']",
                email,firstName,lastName);
    }


    public String getEmail() {
        return this.email;
    }
}