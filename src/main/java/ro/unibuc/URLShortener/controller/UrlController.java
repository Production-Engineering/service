package ro.unibuc.URLShortener.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ro.unibuc.URLShortener.data.Request;
import ro.unibuc.URLShortener.services.UrlService;
import ro.unibuc.URLShortener.data.Url;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

@Controller
public class UrlController {

    @Autowired
    private UrlService urlService;

    @Autowired
    private HttpServletRequest httpReq;

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
    public void redirect(HttpServletResponse response, @PathVariable String shortUrl){
        Url url = urlService.findByShortUrl(shortUrl, httpReq);
        try {
            if (url != null) {
                response.sendRedirect(url.getLongUrl());
            }
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect");
        }
    }

}
