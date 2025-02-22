package mk.ukim.finki.ib.filesharing.encryption;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {
    public static SecretKey getSecretKey() {
        String encodedKey = System.getenv("ENCRYPTION_SECRET_KEY");
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
