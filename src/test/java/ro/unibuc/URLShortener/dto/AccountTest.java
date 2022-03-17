package ro.unibuc.URLShortener.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountTest {
    AccountDTO accountDTO = new AccountDTO("Radu", "Nedelcu", "1234", "1234","radu.ndlcu@gmail.com");
    @Test
    public void test_firstName(){
        Assertions.assertSame("Radu", accountDTO.getFirstName());
    }
    @Test
    public void test_lastName(){
        Assertions.assertSame("Nedelcu", accountDTO.getLastName());
    }
    @Test
    public void test_password(){
        Assertions.assertSame("1234", accountDTO.getPassword());
    }

    @Test
    public void test_matchingPass(){
        Assertions.assertSame(accountDTO.getMatchingPassword(), accountDTO.getPassword());
    }
    @Test
    public void test_email(){
        Assertions.assertSame("radu.ndlcu@gmail.com", accountDTO.getEmail());
    }
    @Test
    public void test_setEmail(){
        AccountDTO dto = new AccountDTO();
        dto.setEmail("test");
        Assertions.assertSame("test", dto.getEmail());
    }
}
