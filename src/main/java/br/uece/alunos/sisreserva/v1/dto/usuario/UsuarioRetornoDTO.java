package br.uece.alunos.sisreserva.v1.dto.usuario;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.cargo.CargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;

import java.util.List;
import java.util.stream.Collectors;

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
    public UsuarioRetornoDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDocumentoFiscal(),
                usuario.getFotoPerfil(),
                usuario.getMatricula(),
                usuario.getTelefone(),
                new InstituicaoRetornoDTO(usuario.getInstituicao()),
                usuario.isRefreshTokenEnabled(),
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
                usuario.getDocumentoFiscal(),
                usuario.getFotoPerfil(),
                usuario.getMatricula(),
                usuario.getTelefone(),
                new InstituicaoRetornoDTO(usuario.getInstituicao()),
                usuario.isRefreshTokenEnabled(),
                usuarioCargos.stream().map(UsuarioCargoRetornoDTO::cargo).toList()
        );
    }
}
