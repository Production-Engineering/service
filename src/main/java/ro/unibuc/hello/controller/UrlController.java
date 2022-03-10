package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import ro.unibuc.hello.UrlService;
import ro.unibuc.hello.data.Url;
import ro.unibuc.hello.data.UrlRepository;

import java.util.List;

@Controller
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    @GetMapping("/getall")
    public List<Url> getAll(){
        return UrlService.getAll();
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestBody Url url){
        if(UrlService.shortenUrl(url)){
            return "Url shortened to: " + UrlService.findByLongUrl(url.getLongUrl()).getShortUrl();
        }
        return "Url shortened to: " + UrlService.findByLongUrl(url.getLongUrl()).getShortUrl();
    }

    @GetMapping("/get/{shortUrl}")
    public Url getShortUrl(@PathVariable String shortUrl){
        return UrlService.findByShortUrl(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ModelAndView redirect(@PathVariable String shortUrl, ModelMap model){
        Url url = UrlService.findByShortUrl(shortUrl);
        if(url != null){
            return new ModelAndView("redirect:/" + url.getLongUrl(), model);
        }
        else {
            return new ModelAndView("redirect:/notFound", model);
        }
    }

    @GetMapping("/notFound")
    public String notFound(){
        return "The link was not found.";
    }
}
