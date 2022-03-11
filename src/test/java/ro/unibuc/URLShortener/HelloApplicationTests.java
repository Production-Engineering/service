package ro.unibuc.URLShortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.unibuc.URLShortener.data.AccountRepository;
import ro.unibuc.URLShortener.data.RoleRepository;


@SpringBootTest
class HelloApplicationTests {

	@MockBean
	AccountRepository mockRepository;
	@MockBean
	RoleRepository mockRoleRepository;


}
