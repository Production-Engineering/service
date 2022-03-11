package ro.unibuc.URLShortener.data;

import com.mongodb.lang.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "accounts")
public class Account {


    @Id
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String password;

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public Date getDateCreated() {
        return dateCreated;
    }

    @NonNull
    private Date dateCreated;
    private Set<Role> roles = new HashSet<>();

    private List<Url> urlList = new ArrayList<>();

    public Account(@NotNull String email, @NotNull String firstName, @NotNull String lastName, @NotNull String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateCreated = new Date();
        this.password = password;
    }

    public void addURL(Url url) {
        urlList.add(url);
        url.setUserEmail(this.email);
    }
    public List<Url> getUrlList(){
        return urlList;
    }
    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }



    public String getEmail() {
        return this.email;
    }
}