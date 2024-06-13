package br.com.heycheff.api.config.auth;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.data.model.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.security.PublicKey;
import java.util.Date;

@Service
public class TokenService {

    private static final String ISSUER = "HEYCHEFF";
    public static final String BEARER = "Bearer";

    @Value("${heycheff.jwt.expiration}")
    String expiration;

    final KeyStore.PrivateKeyEntry keyEntry;

    public TokenService(KeyStore.PrivateKeyEntry keyEntry) {
        this.keyEntry = keyEntry;
    }

    public TokenDTO generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + Long.parseLong(expiration));

        String token = Jwts.builder().issuer(ISSUER)
                .subject(user.getId())
                .issuedAt(issuedDate).expiration(expirationDate)
                .signWith(keyEntry.getPrivateKey(), Jwts.SIG.RS512)
                .compact();

        return new TokenDTO(token, BEARER, user.getId(), issuedDate, expirationDate);
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private PublicKey getPublicKey() {
        return keyEntry.getCertificate().getPublicKey();
    }
}
