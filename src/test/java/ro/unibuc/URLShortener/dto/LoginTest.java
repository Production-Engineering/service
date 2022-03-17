package ro.unibuc.URLShortener.dto;

import  org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginTest {
    private static final String email = "radu.ndlcu@gmail.com";
    private static final String password = "1234";
    LoginDTO loginDTO = new LoginDTO(email,password);

    @Test
    public void test_email(){
        Assertions.assertSame(loginDTO.getEmail(), email);
    }
    @Test
    public void test_password(){
        Assertions.assertSame(loginDTO.getPassword(),password);
    }
    @Test
    public void test_setEmail(){
        LoginDTO dto = new LoginDTO();
        dto.setEmail("test");
        Assertions.assertSame("test", dto.getEmail());
    }
}
