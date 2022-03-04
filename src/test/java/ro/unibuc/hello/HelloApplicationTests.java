package ro.unibuc.hello;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.unibuc.hello.data.AccountRepository;


@SpringBootTest
class HelloApplicationTests {

	@MockBean
	AccountRepository mockRepository;

	@Test
	void contextLoads() {
	}

}
