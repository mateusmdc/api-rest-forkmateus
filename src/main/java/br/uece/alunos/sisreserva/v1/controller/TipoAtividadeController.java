package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.useCase.ObterTiposAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.ObterTiposEspaco;
import br.uece.alunos.sisreserva.v1.dto.tipoAtividade.TipoAtividadeRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEspacoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atividade/tipo")
public class TipoAtividadeController {
    @Autowired
    private ObterTiposAtividade obterTiposAtividade;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<TipoAtividadeRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nome
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var tipoDeAtividade = obterTiposAtividade.obter(pageable, id, nome);
        return ResponseEntity.ok(ApiResponseDTO.success(tipoDeAtividade));
    }
}
