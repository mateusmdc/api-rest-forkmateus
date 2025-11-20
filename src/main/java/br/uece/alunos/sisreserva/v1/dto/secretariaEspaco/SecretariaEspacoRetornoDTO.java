package br.uece.alunos.sisreserva.v1.dto.secretariaEspaco;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;

/**
 * DTO de retorno com os dados completos de uma secretaria de espaço.
 * Inclui informações do espaço, do usuário e o status de ativação.
 */
public record SecretariaEspacoRetornoDTO(
        /**
         * ID único da secretaria de espaço
         */
        String id,
        
        /**
         * Dados completos do espaço
         */
        EspacoRetornoDTO espaco,
        
        /**
         * Dados completos do usuário da secretaria
         */
        UsuarioRetornoDTO secretaria,
        
        /**
         * Indica se a secretaria está ativa
         */
        Boolean estaAtivo
) {
    /**
     * Construtor que converte a entidade SecretariaEspaco para o DTO de retorno
     * 
     * @param secretariaEspaco Entidade a ser convertida
     */
    public SecretariaEspacoRetornoDTO(SecretariaEspaco secretariaEspaco) {
        this(
                secretariaEspaco.getId(),
                new EspacoRetornoDTO(secretariaEspaco.getEspaco()),
                new UsuarioRetornoDTO(secretariaEspaco.getUsuarioSecretaria()),
                secretariaEspaco.getEstaAtivo()
        );
    }
}
