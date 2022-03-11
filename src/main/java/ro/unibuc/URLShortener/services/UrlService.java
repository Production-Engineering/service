package ro.unibuc.URLShortener.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.unibuc.URLShortener.data.Account;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.Url;
import ro.unibuc.URLShortener.data.UrlRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
