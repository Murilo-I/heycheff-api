package br.com.heycheff.api.app.service;

import br.com.heycheff.api.config.TestConfiguration;
import br.com.heycheff.api.config.auth.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.heycheff.api.data.helper.DataHelper.user;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class TokenServiceTest {

    @Autowired
    TokenService service;

    @Test
    void shouldGenerateAndValidateToken() {
        var token = service.generateToken(user());
        assertNotNull(token);
        assertTrue(service.isValidToken(token.getToken()));
        assertEquals(user().getId(), service.getSubject(token.getToken()));
    }
}
