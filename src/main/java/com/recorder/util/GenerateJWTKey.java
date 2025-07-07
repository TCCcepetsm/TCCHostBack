package com.recorder.util; // Altere para o pacote onde você deseja que esta classe esteja

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Encoders;

public class GenerateJWTKey {
    public static void main(String[] args) {
        // Gera uma chave HMAC SHA-512 (512 bits) usando a biblioteca JJWT.
        // Essa é uma chave robusta e recomendada para segurança.
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // Codifica a chave gerada para o formato Base64, ideal para variáveis de
        // ambiente.
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("=== CHAVE SECRETA JWT GERADA COM SUCESSO! ===");
        System.out.println("Algoritmo de Assinatura: HS512");
        System.out.println(" "); // Espaço para clareza
        System.out.println(">>>> COPIE APENAS ESTA LINHA ABAIXO (INCLUINDO O '==' NO FINAL, SE HOUVER): <<<<");
        System.out.println(base64Key);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" "); // Espaço para clareza
        System.out.println("ATENÇÃO:");
        System.out.println("- Mantenha esta chave em **SEGREDO ABSOLUTO**.");
        System.out.println("- Adicione esta chave como o **VALOR** da variável de ambiente ");
        System.out.println("  `APPLICATION_SECURITY_JWT_SECRET_KEY` no seu serviço de Backend no Render.");
        System.out.println("- Remova este arquivo `GenerateJWTKey.java` do seu projeto após usá-lo.");
        System.out.println("--------------------------------------------------------------------------");

        // Adicionar uma verificação de comprimento básica, embora Keys.secretKeyFor já
        // garanta um bom tamanho.
        if (base64Key.length() < 64) {
            System.err.println(
                    "Aviso: O comprimento da chave gerada pode ser insuficiente (menos de 64 caracteres Base64).");
        }
    }
}