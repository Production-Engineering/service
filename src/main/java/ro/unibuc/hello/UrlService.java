package ro.unibuc.hello;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.data.Account;
import ro.unibuc.hello.data.AccountRepository;
import ro.unibuc.hello.data.Url;
import ro.unibuc.hello.data.UrlRepository;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private AccountRepository accountRepository;
    public List<Url> getAll(){
        return new ArrayList<>(urlRepository.findAll());
    }

    public boolean shortenUrl(Url url){
        if(!urlRepository.existsByLongUrl(url.getLongUrl())){

           String shortUrl = shortenUrl();
            //keep generating new URL until we find one that is not taken
            //not a good long term solution
            while(urlRepository.existsByShortUrl(shortUrl)){
                shortUrl = shortenUrl();
            }

            //Check if any user is authenticated
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null)
            { Account acc = accountRepository.findByEmail( (String) auth.getPrincipal()).get(); // User is logged in so it exists

            }
            urlRepository.save(url);
            return true;
        }
        return false;
    }
    public boolean shortenUrl(String longUrl){
        if(!urlRepository.existsByLongUrl(longUrl)){

            String shortUrl = shortenUrl();
            //keep generating new URL until we find one that is not taken
            //not a good long term solution
            while(urlRepository.existsByShortUrl(shortUrl)){
                shortUrl = shortenUrl();
            }
            Url newUrl = new Url(longUrl);
            newUrl.setShortUrl(shortUrl);
            urlRepository.save(newUrl);
            return true;
        }
        return false;
    }
    public Url findByShortUrl(String shortUrl){
        if(urlRepository.existsByShortUrl(shortUrl)){
            return urlRepository.findByShortUrl(shortUrl);
        }
        return null;
    }

    public Url findByLongUrl(String longUrl){
        if(urlRepository.existsByLongUrl(longUrl)){
            return urlRepository.findByLongUrl((longUrl));
        }
        return null;
    }
    private String shortenUrl(){
        return RandomStringUtils.randomAlphabetic(10);
    }

}
