package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.GestorComplexoEspacosService;
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

/**
 * Controller REST para operações de gestores de complexos de espaços.
 * Endpoints para cadastrar, inativar e consultar gestores.
 */
@RestController
@RequestMapping("/complexo-espacos/gestor")
@Tag(name = "Rotas de gestor de complexo de espaços mapeadas no controller")
public class GestorComplexoEspacosController {
    @Autowired
    private GestorComplexoEspacosService gestorComplexoEspacosService;

    /**
     * Cadastra um novo gestor ou reativa um gestor inativo para um complexo de espaços.
     *
     * @param data DTO com usuarioGestorId e complexoEspacosId
     * @return Resposta com dados do gestor cadastrado/reativado
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<GestorComplexoEspacosRetornoDTO>> cadastrarGestorComplexoEspacos(
            @RequestBody @Valid GestorComplexoEspacosDTO data) {
        var gestorComplexoEspacosRetornoDTO = gestorComplexoEspacosService.cadastrarOuReativarGestorComplexoEspacos(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(gestorComplexoEspacosRetornoDTO));
    }

    /**
     * Inativa um gestor de complexo de espaços.
     *
     * @param id ID do gestor a ser inativado
     * @return Resposta sem conteúdo (204 No Content)
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<Void>> inativarGestorComplexoEspacos(@PathVariable String id) {
        gestorComplexoEspacosService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtém gestores de complexos de espaços com filtros e paginação.
     *
     * @param page número da página (padrão: 0)
     * @param size tamanho da página (padrão: 100)
     * @param sortField campo para ordenação (padrão: id)
     * @param sortOrder direção da ordenação (asc/desc, padrão: asc)
     * @param id filtro por ID do gestor
     * @param complexoEspacos filtro por ID do complexo
     * @param gestor filtro por ID do usuário gestor
     * @param todos se true, inclui gestores inativos (padrão: false)
     * @return Página com gestores encontrados
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<GestorComplexoEspacosRetornoDTO>>> obterGestorComplexoEspacosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String complexoEspacos,
            @RequestParam(required = false) String gestor,
            @RequestParam(defaultValue = "false") boolean todos) {
        var fieldToSort = switch (sortField) {
            case "gestor" -> "usuarioGestor.id";
            case "complexoEspacos" -> "complexoEspacos.id";
            default -> sortField;
        };
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), fieldToSort));
        var gestoresPaginados = gestorComplexoEspacosService.obter(pageable, id, complexoEspacos, gestor, todos);
        return ResponseEntity.ok(ApiResponseDTO.success(gestoresPaginados));
    }
}
