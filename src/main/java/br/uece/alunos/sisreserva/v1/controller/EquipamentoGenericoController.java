package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoGenericoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de equipamentos genéricos.
 * Disponibiliza endpoints para operações CRUD de equipamentos genéricos.
 * 
 * Equipamentos genéricos são itens utilizados em grande quantidade em espaços,
 * como cadeiras, lixeiras, lousas, etc.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@RestController
@RequestMapping("/equipamento-generico")
@AllArgsConstructor
@Tag(name = "Equipamento Genérico", description = "Endpoints para gerenciamento de equipamentos genéricos")
@SecurityRequirement(name = "bearer-key")
public class EquipamentoGenericoController {

    private final EquipamentoGenericoService service;

    /**
     * Cria um novo equipamento genérico.
     * Requer permissão de ADMIN.
     * 
     * @param dto DTO contendo os dados do equipamento genérico
     * @return DTO com os dados do equipamento genérico criado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar equipamento genérico", description = "Cria um novo equipamento genérico no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipamento genérico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<EquipamentoGenericoRetornoDTO> criar(
            @Valid @RequestBody EquipamentoGenericoDTO dto) {
        var equipamento = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * Obtém equipamentos genéricos com filtros e paginação.
     * Acessível por todos os usuários autenticados.
     * 
     * @param pageable Configurações de paginação
     * @param id Filtro opcional por ID
     * @param nome Filtro opcional por nome (busca parcial)
     * @return Página com os equipamentos genéricos encontrados
     */
    @GetMapping
    @Operation(summary = "Listar equipamentos genéricos", description = "Obtém lista paginada de equipamentos genéricos com filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<EquipamentoGenericoRetornoDTO>> obter(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable,
            @Parameter(description = "ID do equipamento genérico") @RequestParam(required = false) String id,
            @Parameter(description = "Nome do equipamento genérico (busca parcial)") @RequestParam(required = false) String nome) {
        var page = service.obter(pageable, id, nome);
        return ResponseEntity.ok(page);
    }

    /**
     * Obtém um equipamento genérico específico por ID.
     * Acessível por todos os usuários autenticados.
     * 
     * @param id ID do equipamento genérico
     * @return DTO com os dados do equipamento genérico
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter equipamento genérico por ID", description = "Retorna os dados de um equipamento genérico específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamento genérico encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipamento genérico não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<EquipamentoGenericoRetornoDTO> obterPorId(
            @Parameter(description = "ID do equipamento genérico") @PathVariable String id) {
        var equipamento = service.obterPorId(id);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * Atualiza um equipamento genérico existente.
     * Requer permissão de ADMIN.
     * 
     * @param id ID do equipamento genérico a ser atualizado
     * @param dto DTO contendo os novos dados
     * @return DTO com os dados do equipamento genérico atualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar equipamento genérico", description = "Atualiza os dados de um equipamento genérico existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamento genérico atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Equipamento genérico não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<EquipamentoGenericoRetornoDTO> atualizar(
            @Parameter(description = "ID do equipamento genérico") @PathVariable String id,
            @Valid @RequestBody EquipamentoGenericoDTO dto) {
        var equipamento = service.atualizar(id, dto);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * Deleta um equipamento genérico.
     * Requer permissão de ADMIN.
     * 
     * @param id ID do equipamento genérico a ser deletado
     * @return ResponseEntity sem conteúdo
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar equipamento genérico", description = "Remove um equipamento genérico do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipamento genérico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipamento genérico não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do equipamento genérico") @PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
