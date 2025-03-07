package mk.ukim.finki.ib.filesharing.encryption;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {
    public static SecretKey getSecretKey() {
        String encodedKey = System.getenv("ENCRYPTION_KEY");
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
