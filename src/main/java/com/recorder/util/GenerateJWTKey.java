import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Encoders;

public class GenerateJWTKey {
    public static void main(String[] args) {
        // Usar HS512 para maior segurança
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("=== JWT Key Generation ===");
        System.out.println("Algorithm: HS512");
        System.out.println("Secret Key: " + base64Key);
        System.out.println("=== Store this securely ===");

        // Adicionar verificação de comprimento
        if (base64Key.length() < 64) {
            System.err.println("Warning: Key length may be insufficient");
        }
    }
}