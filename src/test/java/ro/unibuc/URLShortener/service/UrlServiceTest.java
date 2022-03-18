package ro.unibuc.URLShortener.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.URLShortener.data.Url;
import ro.unibuc.URLShortener.data.UrlRepository;
import ro.unibuc.URLShortener.services.UrlService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UrlServiceTest {
    @Mock
    UrlRepository urlRepository;

    @InjectMocks
    UrlService urlService = new UrlService();

    @Test
    public void test_shortUrl() {
        String longUrl = "https://emojipedia.org/clapping-hands/";
        Assertions.assertEquals(urlService.shortenUrl(longUrl).length(), 10);
    }

    @Test
    public void test_findByShortUrl_null(){
        String shortUrl = "aaaaaaaaaa";
        Assertions.assertNull(urlService.findByShortUrl(shortUrl));
    }


    @Test
    public void test_findByLongUrl_null(){
        String longUrl = "aaaaaaaaaa";
        Assertions.assertNull(urlService.findByLongUrl(longUrl));
    }
    

    @Test
    public void test_getAll(){
        String longUrl1 = "https://emojipedia.org/clapping-hands/";
        String longUrl2 = "https://emojipedia.org/clapping-hands/121";
        Url url1 = new Url(longUrl1, "abbbbbbbbb");
        Url url2 = new Url(longUrl2, "abbbbbbbbc");

        when(urlRepository.findByLongUrl(longUrl1)).thenReturn(url1);
        when(urlRepository.findByLongUrl(longUrl2)).thenReturn(url2);

        List<Url> list = List.of(url1, url2);

        when(urlRepository.findAll()).thenReturn(list);



        Assertions.assertEquals(urlService.getAll(), list);
    }

    @Test
    public void test_findByShortWithParsePostman()
    {
        Url url = new Url("www.google.com", "1234");
        String userAgent = "PostmanRuntime/7.29.0";
        String ip = "192.168.0.1";
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("user-agent")).thenReturn(userAgent);
        when(req.getRemoteAddr()).thenReturn(ip);
        when(urlRepository.existsByShortUrl(any())).thenReturn(true);
        when(urlRepository.findByShortUrl(any())).thenReturn(url);
        urlService.findByShortUrl("1234", req);

        Assertions.assertTrue(url.getRequestList().size()>0);
    }

    @Test
    public void test_findByShortWithParseMozilla()
    {
        Url url = new Url("www.google.com", "1234");
        String userAgent = "Mozilla/7.29.0";
        String ip = "192.168.0.1";
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("user-agent")).thenReturn(userAgent);
        when(req.getRemoteAddr()).thenReturn(ip);
        when(urlRepository.existsByShortUrl(any())).thenReturn(true);
        when(urlRepository.findByShortUrl(any())).thenReturn(url);
        urlService.findByShortUrl("1234", req);

        Assertions.assertTrue(url.getRequestList().size()>0);
    }
    @Test
    public void test_findByShortWithParseChrome()
    {
        Url url = new Url("www.google.com", "1234");
        String userAgent = "Chrome/7.29.0";
        String ip = "192.168.0.1";
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("user-agent")).thenReturn(userAgent);
        when(req.getRemoteAddr()).thenReturn(ip);
        when(urlRepository.existsByShortUrl(any())).thenReturn(true);
        when(urlRepository.findByShortUrl(any())).thenReturn(url);
        urlService.findByShortUrl("1234", req);

        Assertions.assertTrue(url.getRequestList().size()>0);

    }

}
