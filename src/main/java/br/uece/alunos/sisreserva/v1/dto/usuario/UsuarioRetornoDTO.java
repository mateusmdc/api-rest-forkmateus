package br.uece.alunos.sisreserva.v1.dto.usuario;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.cargo.CargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.DocumentoFiscalUtils;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.TelefoneUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO de retorno com os dados do usuário.
 * CPF e telefone são formatados para exibição.
 */
public record UsuarioRetornoDTO(
        String id,
        String nome,
        String email,
        String documentoFiscal,
        String fotoPerfil,
        String matricula,
        String telefone,
        InstituicaoRetornoDTO instituicao,
        boolean refreshTokenEnabled,
        List<CargoRetornoDTO> cargos
) {
    /**
     * Construtor que cria o DTO a partir da entidade Usuario.
     * Formata CPF e telefone para exibição.
     */
    public UsuarioRetornoDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                DocumentoFiscalUtils.formatarCPF(usuario.getDocumentoFiscal()),
                usuario.getFotoPerfil(),
                usuario.getMatricula(),
                TelefoneUtils.formatarTelefone(usuario.getTelefone()),
                new InstituicaoRetornoDTO(usuario.getInstituicao()),
                usuario.isRefreshTokenEnabled(),
                usuario.getUsuarioCargos()
                        .stream()
                        .map(uc -> new CargoRetornoDTO(uc.getCargo()))
                        .toList()
        );
    }

    /**
     * Construtor que cria o DTO a partir da entidade Usuario e lista de cargos.
     * Formata CPF e telefone para exibição.
     */
    public UsuarioRetornoDTO(Usuario usuario, List<UsuarioCargoRetornoDTO> usuarioCargos) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                DocumentoFiscalUtils.formatarCPF(usuario.getDocumentoFiscal()),
                usuario.getFotoPerfil(),
                usuario.getMatricula(),
                TelefoneUtils.formatarTelefone(usuario.getTelefone()),
                new InstituicaoRetornoDTO(usuario.getInstituicao()),
                usuario.isRefreshTokenEnabled(),
                usuarioCargos.stream().map(UsuarioCargoRetornoDTO::cargo).toList()
        );
    }
}
