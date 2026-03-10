package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.AtualizarQuantidadeDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoGenericoEspacoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipamento-generico-espaco")
@RequiredArgsConstructor
@Tag(name = "Equipamento Genérico - Espaço", description = "Endpoints para gerenciamento do relacionamento entre equipamentos genéricos e espaços")
@SecurityRequirement(name = "bearer-key")
public class EquipamentoGenericoEspacoController {

    private final EquipamentoGenericoEspacoService service;

    @PostMapping
    @Transactional
    @Operation(
        summary = "Vincular equipamento genérico ao espaço",
        description = "Vincula um equipamento genérico a um espaço informando a quantidade. " +
                     "Requer permissão de administrador, gestor ou secretaria do espaço."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vínculo criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "409", description = "Equipamento já vinculado ao espaço")
    })
    public ResponseEntity<ApiResponseDTO<EquipamentoGenericoEspacoRetornoDTO>> vincular(
            @Valid @RequestBody EquipamentoGenericoEspacoDTO dto) {
        var vinculo = service.vincular(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(vinculo));
    }

    @PutMapping("/{vinculoId}/quantidade")
    @Transactional
    @Operation(
        summary = "Atualizar quantidade",
        description = "Atualiza a quantidade de um equipamento genérico em um espaço. " +
                     "Requer permissão de administrador, gestor ou secretaria do espaço."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quantidade atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    public ResponseEntity<ApiResponseDTO<EquipamentoGenericoEspacoRetornoDTO>> atualizarQuantidade(
            @Parameter(description = "ID do vínculo equipamento-espaço") @PathVariable String vinculoId,
            @Valid @RequestBody AtualizarQuantidadeDTO dto) {
        var vinculo = service.atualizarQuantidade(vinculoId, dto);
        return ResponseEntity.ok(ApiResponseDTO.success(vinculo));
    }

    @DeleteMapping("/{vinculoId}")
    @Transactional
    @Operation(
        summary = "Desvincular equipamento genérico do espaço",
        description = "Remove o vínculo entre equipamento genérico e espaço. " +
                     "Requer permissão de administrador, gestor ou secretaria do espaço."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vínculo removido com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> desvincular(
            @Parameter(description = "ID do vínculo equipamento-espaço") @PathVariable String vinculoId) {
        service.desvincular(vinculoId);
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @GetMapping("/espaco/{espacoId}")
    @Operation(
        summary = "Listar equipamentos do espaço",
        description = "Obtém todos os equipamentos genéricos vinculados a um espaço com suas quantidades."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ApiResponseDTO<List<EquipamentoGenericoEspacoRetornoDTO>>> obterPorEspaco(
            @Parameter(description = "ID do espaço") @PathVariable String espacoId) {
        var equipamentos = service.obterPorEspaco(espacoId);
        return ResponseEntity.ok(ApiResponseDTO.success(equipamentos));
    }

    @GetMapping("/equipamento/{equipamentoGenericoId}")
    @Operation(
        summary = "Listar espaços com o equipamento",
        description = "Obtém todos os espaços que possuem um equipamento genérico específico com suas quantidades."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ApiResponseDTO<List<EquipamentoGenericoEspacoRetornoDTO>>> obterPorEquipamento(
            @Parameter(description = "ID do equipamento genérico") @PathVariable String equipamentoGenericoId) {
        var espacos = service.obterPorEquipamento(equipamentoGenericoId);
        return ResponseEntity.ok(ApiResponseDTO.success(espacos));
    }
}
