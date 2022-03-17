package ro.unibuc.URLShortener.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class RequestTest {
    Request req = new Request();
    @Test
    public void test_date(){
        Date testDate = new Date();
        req.setRequestDate(testDate);
        Assertions.assertSame(testDate, req.getRequestDate());
    }
    @Test
    public void test_IP(){
        String ip = "192.168.0.0";
        req.setIP(ip);
        Assertions.assertSame(req.getIP(), ip);
    }
    @Test
    public void test_browser(){
            String browser = "Chrome";
            req.setBrowser(browser);
            Assertions.assertSame(req.getBrowser(), browser);
    }
    @Test
    public void test_deviceType(){
        String device = "Mobile";
        req.setDeviceType(device);
        Assertions.assertSame(req.getDeviceType(), device);
    }
}
