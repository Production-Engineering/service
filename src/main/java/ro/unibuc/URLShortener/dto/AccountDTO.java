package ro.unibuc.URLShortener.dto;

import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


public class AccountDTO {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String matchingPassword;
    @NotNull
    @NotEmpty
    @Email
    private String email;

    public AccountDTO() {
    }

    public AccountDTO(@NotNull @NotEmpty String firstName, @NotNull @NotEmpty String lastName, @NotNull @NotEmpty String password,  @NotNull @NotEmpty String matchingPassword, @NotNull @NotEmpty @Email String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.matchingPassword = matchingPassword;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
