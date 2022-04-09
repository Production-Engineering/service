package ro.unibuc.URLShortener.controller;


import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ro.unibuc.URLShortener.services.UrlService;
import ro.unibuc.URLShortener.data.Url;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
public class UrlController {

    Logger logger = LoggerFactory.getLogger(UrlController.class);
    @Autowired
    private MeterRegistry metricsRegistry;
    @Autowired
    private UrlService urlService;

    @Autowired
    private HttpServletRequest httpReq;

    @GetMapping("/getall")
    @ResponseBody
    public List<Url> getAll(){
        return urlService.getAll();
    }



    @Timed(value = "urlshortener_shorten_time", description = "Time taken to shorten an URL")
    @Counted(value = "urlshortener_shorten_count", description = "Times an URL was shortened")
    @PostMapping("/shorten")
    @ResponseBody
    public ResponseEntity<String> shortenUrl(@RequestBody String url) {
        logger.info("/shorten called with "+ url);
        String shortened = urlService.shortenUrl(url);
        return new ResponseEntity<>(shortened, HttpStatus.OK);

    }


    @GetMapping("/get/{shortUrl}")
    @ResponseBody
    public ResponseEntity<Url> getShortUrl(@PathVariable String shortUrl, HttpServletRequest httpReq){
        logger.info("getShortURL received: " + shortUrl);
        return new ResponseEntity<Url>(urlService.findByShortUrl(shortUrl), HttpStatus.OK);
    }

    @Timed(value = "urlshortener_shortened_redirect_time", description = "Time taken to redirect from a shortened URL")
    @Counted(value = "urlshortener_shortened_redirect_count", description = "Times a shortened URL was used")
    @GetMapping("/{shortUrl}")
    public void redirect(HttpServletResponse response, @PathVariable String shortUrl){
        Url url = urlService.findByShortUrl(shortUrl, httpReq);
        try {
            if (url != null) {
                response.sendRedirect(url.getLongUrl());
            }
        } catch (IOException e){
            logger.error("Could not redirect from "+ shortUrl);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect");
        }
    }

}
