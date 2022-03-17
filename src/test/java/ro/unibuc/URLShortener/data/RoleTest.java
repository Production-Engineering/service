package ro.unibuc.URLShortener.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleTest {
    @Test
    public void test_name(){
        String name = "ADMIN";
        Role role = new Role(1, name);
        Assertions.assertSame(name,role.getName());
    }
}
