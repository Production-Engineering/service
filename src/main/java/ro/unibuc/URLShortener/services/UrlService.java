package ro.unibuc.URLShortener.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.unibuc.URLShortener.data.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UrlService {
    private static final int SHORT_URL_LENGTH = 10;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<Url> getAll() {
        return new ArrayList<>(urlRepository.findAll());
    }


    public String shortenUrl(String longUrl) {
        String shortUrl = shortenUrl();
        //keep generating new URL until we find one that is not taken
        //not a good long term solution
        // Change for hashing with salt?
        while (urlRepository.existsByShortUrl(shortUrl)) {
            shortUrl = shortenUrl();
        }
        Url newUrl = new Url(longUrl, shortUrl);

        saveUrlToRepository(newUrl);
        return shortUrl;
    }

    public Url findByShortUrl(String shortUrl) {
        if (urlRepository.existsByShortUrl(shortUrl)) {
            return urlRepository.findByShortUrl(shortUrl);
        }
        return null;
    }
    public Url findByShortUrl(String shortUrl, HttpServletRequest httpReq) {
        if (urlRepository.existsByShortUrl(shortUrl)) {
            Url url = urlRepository.findByShortUrl(shortUrl);
            Request req = parseHTTPReq(httpReq);
            url.addRequest(req);

            return urlRepository.save(url);
        }
        return null;
    }

    public Url findByLongUrl(String longUrl) {
        if (urlRepository.existsByLongUrl(longUrl)) {
            return urlRepository.findByLongUrl((longUrl));
        }
        return null;
    }

    private String shortenUrl() {
        return RandomStringUtils.randomAlphabetic(SHORT_URL_LENGTH);
    }

    private void saveUrlToRepository(Url url) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String email = (String) auth.getPrincipal();
            System.out.println(email);
            Optional<Account> authenticatedOptional = accountRepository.findByEmail(email);
            if (authenticatedOptional.isPresent()) {
                Account authenticatedAcc = authenticatedOptional.get();
                authenticatedAcc.addURL(url);
                accountRepository.save(authenticatedAcc);
            }

        }
        urlRepository.save(url);
    }

    private Request parseHTTPReq(HttpServletRequest httpReq)
    {
        String ip = getClientIpAddress(httpReq);
        String userAgent = httpReq.getHeader("user-agent");
        System.out.println(getHeadersInfo(httpReq));
        String browser = parseBrowserInfo(userAgent);
        String deviceType = getDeviceType(userAgent);
        Request req = new Request();
        req.setIP(ip);
        req.setBrowser(browser);
        req.setDeviceType(deviceType);
        req.setRequestDate(new Date());
        return req;
    }
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
    private String parseBrowserInfo(String browserInfo)
    {
        String browsername = "";
        String browserversion = "";
        if (browserInfo.contains("MSIE"))
        {
            String subsString = browserInfo.substring(browserInfo.indexOf("MSIE"));
            String[] info = (subsString.split(";")[0]).split(" ");
            browsername = info[0];
            browserversion = info[1];
        } else if (browserInfo.contains("Firefox"))
        {

            String subsString = browserInfo.substring(browserInfo.indexOf("Firefox"));
            String[] info = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if (browserInfo.contains("Chrome"))
        {

            String subsString = browserInfo.substring(browserInfo.indexOf("Chrome"));
            String[] info = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if (browserInfo.contains("Opera"))
        {

            String subsString = browserInfo.substring(browserInfo.indexOf("Opera"));
            String[] info = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if(browserInfo.contains("Safari"))
        { String subsString = browserInfo.substring(browserInfo.indexOf("Safari"));
            String[] info = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        } else if(browserInfo.contains("Postman")){
            String subsString = browserInfo.substring(browserInfo.indexOf("Postman"));
            String[] info = (subsString.split(" ")[0]).split("/");
            browsername = info[0];
            browserversion = info[1];
        }
        return browsername + "-" + browserversion;
    }
    private String getDeviceType(String userAgent) {
        if(userAgent.contains("Mobi")) {
            return "Mobile";
        } else {
           return "Desktop";
        }
    }
}
