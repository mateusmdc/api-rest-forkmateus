package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoDTO;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.ProjetoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/projeto")
@Tag(name = "Rotas de projeto mapeadas no controller")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<ProjetoRetornoDTO>> criarProjeto(@RequestBody @Valid ProjetoDTO data) {
        var projetoRetornoDTO = projetoService.criarProjeto(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(projetoRetornoDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ProjetoRetornoDTO>>> obterProjetoPaginado(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "16") int size,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder,
        @RequestParam(required = false) String id,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String descricao,
        @RequestParam(required = false) LocalDate dataInicio,
        @RequestParam(required = false) LocalDate dataFim,
        @RequestParam(required = false) String usuarioResponsavel,
        @RequestParam(required = false) String instituicao
    ) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var projetosPaginados = projetoService.obterProjeto(pageable, id, nome, descricao, dataInicio, dataFim, usuarioResponsavel, instituicao);
        return ResponseEntity.ok(ApiResponseDTO.success(projetosPaginados));
    }

}
