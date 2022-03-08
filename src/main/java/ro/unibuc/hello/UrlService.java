package ro.unibuc.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.data.Url;
import ro.unibuc.hello.data.UrlRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    public static List<Url> getAll(){
        List<Url> all = new ArrayList<>();
        urlRepository.findAll().forEach(all::add);
        return all;
    }

    public static boolean shortenUrl(String longUrl){
        if(!urlRepository.existsByLongUrl(longUrl)){
            Url url = new Url(longUrl);
            url.shortenUrl();
            //keep generating new URL until we find one that is not taken
            //not a good long term solution
            while(urlRepository.existsByShortUrl(url.getShortUrl())){
                url.shortenUrl();
            }
            urlRepository.save(url);
            return true;
        }
        return false;
    }

    public static Url findByShortUrl(String shortUrl){
        if(urlRepository.existsByShortUrl(shortUrl)){
            return urlRepository.findByShortUrl(shortUrl);
        }
        return null;
    }

    public static Url findByLongUrl(String longUrl){
        if(urlRepository.existsByLongUrl(longUrl)){
            return urlRepository.findByLongUrl((longUrl));
        }
        return null;
    }

}
