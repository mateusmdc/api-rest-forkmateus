package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase.CadastraOuReativaSecretariaEspaco;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase.InativarSecretariaEspaco;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase.ObterSecretariaEspaco;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase.ValidadorSecretariaEspaco;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.SecretariaEspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de SecretariaEspaco.
 * Coordena os casos de uso para gerenciar as secretarias de espaços.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SecretariaEspacoServiceImpl implements SecretariaEspacoService {
    
    private final CadastraOuReativaSecretariaEspaco cadastraOuReativaSecretariaEspaco;
    private final InativarSecretariaEspaco inativarSecretariaEspaco;
    private final ObterSecretariaEspaco obterSecretariaEspaco;
    private final ValidadorSecretariaEspaco validadorSecretariaEspaco;

    @Override
    public SecretariaEspacoRetornoDTO cadastrarOuReativarSecretariaEspaco(SecretariaEspacoDTO data) {
        return cadastraOuReativaSecretariaEspaco.executar(data);
    }

    @Override
    public SecretariaEspacoRetornoDTO inativar(String secretariaEspacoId) {
        return inativarSecretariaEspaco.inativar(secretariaEspacoId);
    }

    @Override
    public Page<SecretariaEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String secretariaId, boolean todos) {
        return obterSecretariaEspaco.obter(pageable, id, espacoId, secretariaId, todos);
    }

    @Override
    public void validarSecretariaAtiva(String usuarioId, String espacoId) {
        validadorSecretariaEspaco.validarSecretariaAtiva(usuarioId, espacoId);
    }
}
