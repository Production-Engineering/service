package ro.unibuc.URLShortener.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class URLTest {
    Url testURL = new Url("www.google.com", "123");
    @Test
    public void test_shortURL(){
        String shortURL = "1";
        testURL.setShortUrl(shortURL);
        Assertions.assertSame(shortURL, testURL.getShortUrl());
    }
    @Test
    public void test_getLongURL(){
        Assertions.assertSame("www.google.com", testURL.getLongUrl());
    }
    @Test
    public void test_dateCreated(){
        Url test = new Url("www.github.com", "asd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String urlDate = dateFormat.format(test.getDateCreated());
        String testDate = dateFormat.format(new Date());
        Assertions.assertEquals(testDate,urlDate);
    }
    @Test
    public void test_userEmailEmpty(){
        Assertions.assertNull(testURL.getUserEmail());
    }
    @Test
    public void test_userEmail(){
        String email = "email@gmail.com";
        Account test = new Account(email, "Radu", "Nedelcu", "1234");
        Url testURL = new Url("www.github.com", "asd");
        test.addURL(testURL);
        Assertions.assertEquals(testURL.getUserEmail(), email);
    }
    @Test
    public void test_getEmptyRequests(){
        Url testURL = new Url("www.github.com", "asd");
        Assertions.assertEquals(0, testURL.getRequestList().size());
    }
    @Test
    public void test_getAddRequests(){
        Url testURL = new Url("www.github.com", "asd");
        Assertions.assertEquals(0, testURL.getRequestList().size());
        testURL.addRequest(new Request());
        Assertions.assertEquals(1, testURL.getRequestList().size());
    }


}
