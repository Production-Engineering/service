package ro.unibuc.URLShortener.data;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class AccountTest {
    Account acc = new Account("radu.ndlcu@gmail.com", "Radu", "Nedelcu", "1234");
    @Test
    public void test_firstName(){
        Assertions.assertSame("Radu", acc.getFirstName());
    }
    @Test
    public void test_lastName(){
        Assertions.assertSame("Nedelcu", acc.getLastName());
    }
    @Test
    public void test_getPassword(){
        Assertions.assertSame("1234", acc.getPassword());
    }
    @Test
    public void test_setPassword(){
        acc.setPassword("newPass");
        Assertions.assertSame("newPass", acc.getPassword());
    }
    @Test
    public void test_email()
    {
        Assertions.assertSame("radu.ndlcu@gmail.com", acc.getEmail());
    }
    @Test
    public void test_dateCreated(){
        Account test = new Account("radu.ndlcu@gmail.com", "Radu", "Nedelcu", "1234");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String accDate = dateFormat.format(test.getDateCreated());
        String testDate = dateFormat.format(new Date());
        Assertions.assertEquals(testDate,accDate);
    }

    @Test
    public void test_getEmptyUrlList(){
        Assertions.assertEquals(0, acc.getUrlList().size());
    }
    @Test
    public void test_addURL(){
        Account test = new Account("Radu", "Nedelcu", "1234", "radu.ndlcu@gmail.com");
        Url url = new Url("123","1");
        test.addURL(url);
        Assertions.assertEquals(1, test.getUrlList().size());
    }
    @Test
    public void test_addRole(){
        Account test = new Account("Radu", "Nedelcu", "1234", "radu.ndlcu@gmail.com");
        Role admin = new Role(1,"ADMIN");
        test.addRole(admin);
        Assertions.assertEquals(1, test.getRoles().size());
        Assertions.assertTrue(test.getRoles().contains(admin));
    }
    @Test
    public void test_removeRole(){
        Account test = new Account("Radu", "Nedelcu", "1234", "radu.ndlcu@gmail.com");
        Role admin = new Role(1,"ADMIN");
        test.addRole(admin);
        Assertions.assertEquals(1, test.getRoles().size());
        Assertions.assertTrue(test.getRoles().contains(admin));
        test.removeRole(admin);
        Assertions.assertEquals(0, test.getRoles().size());
        Assertions.assertFalse(test.getRoles().contains(admin));
    }

    @Test
    public void test_getRolesEmptyWhenInstantiated(){
        Account test = new Account("Radu", "Nedelcu", "1234", "radu.ndlcu@gmail.com");
        Assertions.assertEquals(0, test.getRoles().size());
    }
    @Test
    public void test_getRolesWontAddDuplicates(){
        Account test = new Account("Radu", "Nedelcu", "1234", "radu.ndlcu@gmail.com");
        Role admin = new Role(1,"ADMIN");
        test.addRole(admin);
        test.addRole(admin);
        Assertions.assertEquals(1, test.getRoles().size());
        Assertions.assertTrue(test.getRoles().contains(admin));
    }
    @Test
    public void test_getRolesMultipleRoles(){
        Account test = new Account("Radu", "Nedelcu", "1234", "radu.ndlcu@gmail.com");
        Role admin = new Role(1,"ADMIN");
        Role user = new Role(2, "USER");
        test.addRole(admin);
        test.addRole(user);
        Assertions.assertEquals(2, test.getRoles().size());
        Assertions.assertTrue(test.getRoles().contains(admin));
        Assertions.assertTrue(test.getRoles().contains(user));
    }
}

