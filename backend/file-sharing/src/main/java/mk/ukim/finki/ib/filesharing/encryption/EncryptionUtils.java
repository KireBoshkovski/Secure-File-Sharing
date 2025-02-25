package mk.ukim.finki.ib.filesharing.encryption;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {
    public static SecretKey getSecretKey() {
        String encodedKey = "wOksO08zF/jPNP9A0gr0G2Mj6b0hBGsXM2lWx4lN0nI=";
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
