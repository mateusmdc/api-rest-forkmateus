package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO para atualização de dados de usuário.
 * Todos os campos são opcionais, permitindo atualização parcial.
 */
public record AtualizarUsuarioDTO(
        String nome, 
        
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "A senha deve conter pelo menos uma letra maiúscula e um número")
        String senha,
        
        String fotoPerfil, 
        String matricula, 
        String telefone, 
        String instituicaoId,
        Boolean refreshTokenEnabled, 
        List<String> cargosId
) {
}
