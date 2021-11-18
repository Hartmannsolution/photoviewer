package security;

import java.security.SecureRandom;

/* This generates a secure random per execution of the server
 * A server restart, will generate a new key, making all existing tokens invalid
 * For production (and if a load-balancer is used) come up with a persistent key strategy */
public class SharedSecret {
    private static byte[] secret;

    public static byte[] getSharedKey() {

        String jwtSecret = System.getenv("JWT_SECRET");
        if (jwtSecret == null) {
//            secret = new byte[32];
//            new SecureRandom().nextBytes(secret);
//            return secret;
            return "ALKJHUIOSWW=ILDEOKLDWEDLKSKDLKJHEHDDHDDHSKSLSNCBXMXMSLDKKDNXMXNDNDFDKSLEIDJDKSLCSLSÆAJDIEIFKDSKEJDI".getBytes();
        } else {
            return jwtSecret.getBytes();
        }
    }
}