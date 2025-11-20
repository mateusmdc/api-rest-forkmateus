package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.TipoRecorrencia;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validador de regras de negócio para solicitações de reserva.
 * 
 * <p>Valida conflitos de horários, regras de recorrência e permissões de acesso.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Slf4j
@Component
public class SolicitacaoReservaValidator {

    @Autowired
    private SolicitacaoReservaRepository repository;
    
    @Autowired
    private EspacoRepository espacoRepository;
    
    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Valida se já existe uma solicitação de reserva aprovada para o mesmo espaço e período informado.
     * 
     * @param espacoId identificador do espaço
     * @param dataInicio data e hora de início da reserva
     * @param dataFim data e hora de fim da reserva
     * @throws IllegalArgumentException se houver conflito de horários
     */
    public void validarConflitoReserva(String espacoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeConflito = repository.existsByEspacoIdAndPeriodoConflitanteAprovado(espacoId, dataInicio, dataFim);
        if (existeConflito) {
            throw new IllegalArgumentException("Já existe uma solicitação de reserva aprovada para este espaço no período informado.");
        }
    }

    /**
     * Valida as datas de início e fim de uma reserva.
     * 
     * <p>Verifica se:</p>
     * <ul>
     *   <li>A data de início não é anterior à data/hora atual</li>
     *   <li>A data de fim não é anterior ou igual à data de início</li>
     *   <li>As datas não são nulas</li>
     * </ul>
     * 
     * @param dataInicio data e hora de início da reserva
     * @param dataFim data e hora de fim da reserva
     * @throws IllegalArgumentException se as datas forem inválidas
     */
    public void validarDatasReserva(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início da reserva não pode ser nula");
        }

        if (dataFim == null) {
            throw new IllegalArgumentException("Data de fim da reserva não pode ser nula");
        }

        LocalDateTime agora = LocalDateTime.now();

        // Validar que a data de início não é no passado
        if (dataInicio.isBefore(agora)) {
            throw new IllegalArgumentException(
                "Não é possível criar reservas para datas passadas. " +
                "A data de início deve ser igual ou posterior à data/hora atual."
            );
        }

        // Validar que a data de fim é posterior à data de início
        if (dataFim.isBefore(dataInicio) || dataFim.isEqual(dataInicio)) {
            throw new IllegalArgumentException(
                "A data de fim da reserva deve ser posterior à data de início. " +
                "Data início: " + dataInicio + ", Data fim: " + dataFim
            );
        }
    }

    /**
     * Valida os dados de recorrência de uma reserva.
     * 
     * <p>Verifica se a data fim de recorrência foi informada quando necessária
     * e se está em um período válido.</p>
     * 
     * @param tipoRecorrencia tipo de recorrência da reserva
     * @param dataFimRecorrencia data até quando a recorrência deve se repetir
     * @throws IllegalArgumentException se os dados de recorrência forem inválidos
     */
    public void validarDadosRecorrencia(TipoRecorrencia tipoRecorrencia, LocalDateTime dataFimRecorrencia) {
        if (tipoRecorrencia == null) {
            throw new IllegalArgumentException("Tipo de recorrência não pode ser nulo");
        }

        // Se é uma reserva recorrente, data fim de recorrência é obrigatória
        if (tipoRecorrencia != TipoRecorrencia.NAO_REPETE) {
            if (dataFimRecorrencia == null) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência é obrigatória para reservas recorrentes. " +
                    "Tipo de recorrência informado: " + tipoRecorrencia.getDescricao()
                );
            }

            // Validar que a data fim de recorrência não está muito distante (máximo 1 ano)
            LocalDateTime maxDataFim = LocalDateTime.now().plusYears(1);
            if (dataFimRecorrencia.isAfter(maxDataFim)) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência não pode ser superior a 1 ano a partir de hoje"
                );
            }
        }
    }

    /**
     * Valida se um usuário externo tem permissão para reservar o espaço informado.
     * 
     * <p>Usuários externos só podem reservar espaços marcados como multiusuário.
     * Administradores e usuários internos não possuem essa restrição.</p>
     * 
     * @param espacoId identificador do espaço a ser reservado
     * @throws ValidationException se o usuário externo tentar reservar um espaço não-multiusuário
     */
    public void validarPermissaoUsuarioExterno(String espacoId) {
        // Verifica se o usuário autenticado é externo e não é admin
        if (!usuarioAutenticadoService.deveRestringirEspacos()) {
            return; // Usuário não tem restrições
        }

        // Busca o espaço para verificar se é multiusuário
        var espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado com o ID: " + espacoId));

        // Se o espaço não é multiusuário, usuário externo não pode reservar
        if (!espaco.getMultiusuario()) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            
            // Log de auditoria: registra tentativa de acesso negado
            if (usuario != null) {
                log.warn("[AUDIT] ACESSO_NEGADO - Usuário externo '{}' (ID: {}) tentou reservar espaço não-multiusuário '{}' (ID: {}, multiusuario: false)",
                        usuario.getEmail(), usuario.getId(), espaco.getNome(), espaco.getId());
            }
            
            throw new ValidationException(
                "Usuários externos só podem solicitar reservas para espaços multiusuário. " +
                "O espaço selecionado não está disponível para usuários externos."
            );
        }
        
        // Log de auditoria: registra validação bem-sucedida
        var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        if (usuario != null) {
            log.info("[AUDIT] VALIDACAO_SUCESSO - Usuário externo '{}' (ID: {}) validado para reservar espaço multiusuário '{}' (ID: {})",
                    usuario.getEmail(), usuario.getId(), espaco.getNome(), espaco.getId());
        }
    }
}