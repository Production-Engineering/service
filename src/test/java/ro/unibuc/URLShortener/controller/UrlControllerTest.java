package ro.unibuc.URLShortener.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ro.unibuc.URLShortener.data.Url;
import ro.unibuc.URLShortener.data.UrlRepository;
import ro.unibuc.URLShortener.services.UrlService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTest {
    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    @Mock
    private UrlRepository urlRepository;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Test
    public void givenLongUrlReturnStatusOk() throws Exception {
        String longUrl = "https://github.com/Production-Engineering/service/pull/4/files";

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(longUrl))
                .andExpect(status().isOk());
    }

    @Test
    public void givenShortUrlReturnOk() throws Exception {
        String longUrl = "https://example.com/foo";
        String shortUrl = urlController.shortenUrl(longUrl).getBody();

        mockMvc.perform(get("/get/{shortUrl}", shortUrl))
                .andExpect(status().isOk());
    }

//    @Test
//    public void shouldNotInsertLongUrlIfAlreadyExists() throws Exception {
//        String longUrl = "https://example.com/foo";
//
//        String shortUrl1 = mockMvc.perform(post("/shorten")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(longUrl))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//
//        String shortUrl2 = mockMvc.perform(post("/shorten")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(longUrl))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//
//        System.out.println("RESULT1 " + shortUrl1);
//        System.out.println("RESULT2 " + shortUrl1);
//
//        Assertions.assertEquals(shortUrl1, shortUrl2);
//    }


    @Test
    public void shouldNotInsertShortUrlIfDoesExist() throws Exception {
        String longUrl1 = "https://example.com/foosdfs1";
        String longUrl2 = "https://example.com/foosfs2";

        MvcResult result1 = mockMvc.perform(post("/shorten")
                        .content(longUrl1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        when(urlRepository.existsByLongUrl(longUrl1)).thenReturn(true);
        when(urlRepository.existsByShortUrl(result1.getResponse().getContentAsString())).thenReturn(true);

        MvcResult result2 = mockMvc.perform(post("/shorten")
                        .content(longUrl2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("RESULT1 " + result1.getResponse().getContentAsString());
        System.out.println("RESULT2 " + result2.getResponse().getContentAsString());

        Assertions.assertNotEquals(result1.getResponse().getContentAsString(), result2.getResponse().getContentAsString());
    }

}
