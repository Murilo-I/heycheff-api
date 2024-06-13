package br.com.heycheff.api.config;

import br.com.heycheff.api.app.service.FileService;
import br.com.heycheff.api.util.exception.LoadKeyStoreException;
import br.com.heycheff.api.util.exception.RetrievePrivateKeyEntryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.List;

@Configuration
public class SingletonTokenBean {

    private static final String PKCS_12 = "pkcs12";

    @Value("${heycheff.jwk.jks-file}")
    List<String> jksPath;
    @Value("${heycheff.jwk.password.key-store}")
    String keyStorePass;
    @Value("${heycheff.jwk.password.key-pass}")
    String keyPass;
    @Value("${heycheff.jwk.alias}")
    String alias;

    final FileService fileService;

    public SingletonTokenBean(FileService fileService) {
        this.fileService = fileService;
    }

    @Bean
    public KeyStore.PrivateKeyEntry getPrivateKeyEntry() {
        try {
            return (KeyStore.PrivateKeyEntry) getKeyStore().getEntry(
                    alias, new KeyStore.PasswordProtection(keyStorePass.toCharArray())
            );
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            throw new RetrievePrivateKeyEntryException(e);
        }
    }

    private KeyStore getKeyStore() {
        try (var jksInputStream = fileService.getJksFile(jksPath)) {
            var keyStore = KeyStore.getInstance(PKCS_12);
            keyStore.load(jksInputStream, keyStorePass.toCharArray());
            return keyStore;
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new LoadKeyStoreException(e);
        }
    }
}
