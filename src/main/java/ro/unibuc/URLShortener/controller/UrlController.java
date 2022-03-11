package ro.unibuc.URLShortener.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.unibuc.URLShortener.services.UrlService;
import ro.unibuc.URLShortener.data.Url;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/getall")
    @ResponseBody
    public List<Url> getAll(){
        return urlService.getAll();
    }

    @PostMapping("/shorten")
    @ResponseBody
    public String shortenUrl(@RequestBody String url) {
        String escapedURL = null;
        System.out.println(url);
        try {
           escapedURL = new URL(url).toString();
        } catch (MalformedURLException e) {
            return "Invalid URL";
        }
        String shortened = urlService.shortenUrl(url);
        return "Url shortened to: " + shortened;

    }

    @GetMapping("/get/{shortUrl}")
    @ResponseBody
    public Url getShortUrl(@PathVariable String shortUrl){
        System.out.println("getShortURL received: " + shortUrl);
        return urlService.findByShortUrl(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ModelAndView redirect(@PathVariable String shortUrl, ModelMap model){
        Url url = urlService.findByShortUrl(shortUrl);
        if(url != null){
            return new ModelAndView("redirect:" + url.getLongUrl(), model);
        }
        else {
            return new ModelAndView("redirect:/notFound", model);
        }
    }

    @GetMapping("/notFound")
    @ResponseBody
    public String notFound(){
        return "The link was not found.";
    }
}
