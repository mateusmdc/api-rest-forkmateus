package br.uece.alunos.sisreserva.v1.dto.usuario;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.cargo.CargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.DocumentoFiscalUtils;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.TelefoneUtils;

import java.util.List;

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
        boolean usuarioInterno,
        List<CargoRetornoDTO> cargos
) {
    public UsuarioRetornoDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDocumentoFiscal() != null
                        ? DocumentoFiscalUtils.formatarCPF(usuario.getDocumentoFiscal())
                        : null,
                usuario.getFotoPerfil(),
                usuario.getMatricula(),
                usuario.getTelefone() != null
                        ? TelefoneUtils.formatarTelefone(usuario.getTelefone())
                        : null,
                new InstituicaoRetornoDTO(usuario.getInstituicao()),
                usuario.isRefreshTokenEnabled(),
                usuario.getRoles().contains("USUARIO_INTERNO"),
                usuario.getUsuarioCargos()
                        .stream()
                        .map(uc -> new CargoRetornoDTO(uc.getCargo()))
                        .toList()
        );
    }

    public UsuarioRetornoDTO(Usuario usuario, List<UsuarioCargoRetornoDTO> usuarioCargos) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDocumentoFiscal() != null
                        ? DocumentoFiscalUtils.formatarCPF(usuario.getDocumentoFiscal())
                        : null,
                usuario.getFotoPerfil(),
                usuario.getMatricula(),
                usuario.getTelefone() != null
                        ? TelefoneUtils.formatarTelefone(usuario.getTelefone())
                        : null,
                new InstituicaoRetornoDTO(usuario.getInstituicao()),
                usuario.isRefreshTokenEnabled(),
                usuario.getRoles().contains("USUARIO_INTERNO"),
                usuarioCargos.stream().map(UsuarioCargoRetornoDTO::cargo).toList()
        );
    }
}