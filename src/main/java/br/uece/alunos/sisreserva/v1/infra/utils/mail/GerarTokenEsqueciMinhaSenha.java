package br.uece.alunos.sisreserva.v1.infra.utils.mail;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GerarTokenEsqueciMinhaSenha {
    public String gerarTokenEmail() {
        var tokenLength = 20;
        byte[] tokenBytes = new byte[tokenLength];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(tokenBytes);

        var tokenBuilder = new StringBuilder();
        for (byte b : tokenBytes) {
            tokenBuilder.append(String.format("%02x", b));
        }

        return tokenBuilder.toString();
    }
}
