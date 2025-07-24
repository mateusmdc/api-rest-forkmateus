package br.uece.alunos.sisreserva.v1.domain.projeto.validation;

import br.uece.alunos.sisreserva.v1.domain.projeto.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProjetoValidator {
    @Autowired
    private ProjetoRepository repository;

    public void validarSeProjetoJaExiste(String nome, String usuarioResponsavelId, String instituicaoId) {
        String nomeNormalizado = nome.trim().toLowerCase();

        boolean jaExiste = repository.existsByNomeAndUsuarioResponsavelIdAndInstituicaoId(
                nomeNormalizado, usuarioResponsavelId, instituicaoId
        );

        if (jaExiste) {
            throw new IllegalArgumentException("Já existe um projeto com esse nome, usuário e instituição.");
        }
    }
}
