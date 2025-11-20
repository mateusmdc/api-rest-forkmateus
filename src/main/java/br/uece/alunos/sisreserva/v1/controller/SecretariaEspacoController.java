package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.SecretariaEspacoService;
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
 * Controller REST para gerenciar as operações relacionadas à secretaria de espaços.
 * Fornece endpoints para cadastro, inativação e consulta de secretarias.
 */
@RestController
@RequestMapping("/espaco/secretaria")
@Tag(name = "Rotas de secretaria espaço mapeadas no controller")
public class SecretariaEspacoController {
    
    @Autowired
    private SecretariaEspacoService secretariaEspacoService;

    /**
     * Cadastra uma nova secretaria de espaço ou reativa uma existente.
     * 
     * @param data Dados do usuário e espaço a serem vinculados
     * @return Resposta com os dados da secretaria cadastrada/reativada
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<SecretariaEspacoRetornoDTO>> cadastrarSecretariaEspaco(
            @RequestBody @Valid SecretariaEspacoDTO data) {
        var secretariaEspacoRetornoDTO = secretariaEspacoService.cadastrarOuReativarSecretariaEspaco(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(secretariaEspacoRetornoDTO));
    }

    /**
     * Inativa uma secretaria de espaço.
     * 
     * @param id ID da secretaria a ser inativada
     * @return Resposta sem conteúdo (204 No Content)
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<Void>> inativarSecretariaEspaco(@PathVariable String id) {
        secretariaEspacoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtém secretarias de espaço com paginação e filtros.
     * 
     * @param page Número da página (padrão: 0)
     * @param size Tamanho da página (padrão: 100)
     * @param sortField Campo para ordenação (padrão: id)
     * @param sortOrder Direção da ordenação: asc ou desc (padrão: asc)
     * @param id Filtro por ID da secretaria (opcional)
     * @param espaco Filtro por ID do espaço (opcional)
     * @param secretaria Filtro por ID do usuário da secretaria (opcional)
     * @param todos Se true, inclui registros inativos (padrão: false)
     * @return Resposta com a página de secretarias encontradas
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<SecretariaEspacoRetornoDTO>>> obterSecretariaEspacoPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String espaco,
            @RequestParam(required = false) String secretaria,
            @RequestParam(defaultValue = "false") boolean todos) {
        
        // Mapeia os campos de ordenação para os campos da entidade
        var fieldToSort = switch (sortField) {
            case "secretaria" -> "usuarioSecretaria.id";
            case "espaco" -> "espaco.id";
            default -> sortField;
        };
        
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), fieldToSort));
        var secretariasPaginadas = secretariaEspacoService.obter(pageable, id, espaco, secretaria, todos);
        return ResponseEntity.ok(ApiResponseDTO.success(secretariasPaginadas));
    }
}
