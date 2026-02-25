package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoVincularComplexosDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasGeralDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.EspacoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/espaco")
@Tag(name = "Rotas de espaço mapeadas no controller")
public class EspacoController {
    @Autowired
    private EspacoService espacoService;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<EspacoRetornoDTO>> criarEspaco(@RequestBody @Valid EspacoDTO data) {
        var espacoRetornoDTO = espacoService.criarEspaco(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(espacoRetornoDTO));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<EspacoRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody EspacoAtualizarDTO data) {
        var atualizado = espacoService.atualizar(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(atualizado));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<EspacoRetornoDTO>>> obterEspacosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) String tipoEspaco,
            @RequestParam(required = false) String tipoAtividade,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean multiusuario,
            @RequestParam(required = false) Boolean reservavel) {

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var espacosPaginados = espacoService.obterEspacos(pageable, id, departamento, localizacao, tipoEspaco, tipoAtividade, nome, multiusuario, reservavel);
        return ResponseEntity.ok(ApiResponseDTO.success(espacosPaginados));
    }

    /**
     * Obtém espaços reserváveis com filtros e paginação.
     * 
     * <p>Este endpoint retorna apenas espaços com o campo 'reservavel' definido como true.</p>
     * 
     * <p>Aplica automaticamente restrições de visualização baseadas no cargo do usuário autenticado:</p>
     * <ul>
     *   <li><strong>Usuários externos (USUARIO_EXTERNO)</strong>: Visualizam apenas espaços reserváveis 
     *       que também possuem o campo 'multiusuario' como true.</li>
     *   <li><strong>Usuários internos e administradores</strong>: Visualizam todos os espaços reserváveis, 
     *       independentemente do campo 'multiusuario'.</li>
     * </ul>
     * 
     * @param page Número da página (padrão: 0)
     * @param size Tamanho da página (padrão: 100)
     * @param sortField Campo para ordenação (padrão: nome)
     * @param sortOrder Direção da ordenação - asc ou desc (padrão: asc)
     * @param id Filtro por ID do espaço (opcional)
     * @param departamento Filtro por ID do departamento (opcional)
     * @param localizacao Filtro por ID da localização (opcional)
     * @param tipoEspaco Filtro por ID do tipo de espaço (opcional)
     * @param tipoAtividade Filtro por ID do tipo de atividade (opcional)
     * @param nome Filtro por nome do espaço (opcional)
     * @param multiusuario Filtro explícito por espaços multiusuário (opcional)
     * @return Página com os espaços reserváveis encontrados
     */
    @GetMapping("/reservaveis")
    @Operation(summary = "Listar espaços reserváveis", 
               description = "Retorna apenas espaços com reservavel=true. Usuários externos veem apenas espaços multiusuário.")
    public ResponseEntity<ApiResponseDTO<Page<EspacoRetornoDTO>>> obterEspacosReservaveis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) String tipoEspaco,
            @RequestParam(required = false) String tipoAtividade,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean multiusuario) {

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var espacosReservaveis = espacoService.obterEspacosReservaveis(pageable, id, departamento, localizacao, tipoEspaco, tipoAtividade, nome, multiusuario);
        return ResponseEntity.ok(ApiResponseDTO.success(espacosReservaveis));
    }

    @GetMapping("/{id}/horarios-ocupados")
    public ResponseEntity<ApiResponseDTO<HorariosOcupadosPorMesDTO>> obterHorariosOcupadosDoEspaco(
        @PathVariable String id,
        @RequestParam(required = false) Integer mes,
        @RequestParam(required = false) Integer ano
    ) {
        var horariosOcupados = espacoService.obterHorariosOcupadosPorEspaco(id, mes, ano);
        return ResponseEntity.ok(ApiResponseDTO.success(horariosOcupados));
    }

    @PostMapping("/{id}/complexos")
    @Transactional
    public ResponseEntity<ApiResponseDTO<EspacoRetornoDTO>> atribuirComplexos(
            @PathVariable String id,
            @RequestBody @Valid EspacoVincularComplexosDTO data) {
        var espacoAtualizado = espacoService.atribuirComplexos(id, data.complexoIds());
        return ResponseEntity.ok(ApiResponseDTO.success(espacoAtualizado));
    }

    @DeleteMapping("/{id}/complexos")
    @Transactional
    public ResponseEntity<ApiResponseDTO<EspacoRetornoDTO>> desatribuirComplexos(
            @PathVariable String id,
            @RequestBody @Valid EspacoVincularComplexosDTO data) {
        var espacoAtualizado = espacoService.desatribuirComplexos(id, data.complexoIds());
        return ResponseEntity.ok(ApiResponseDTO.success(espacoAtualizado));
    }

    @GetMapping("/{id}/complexos")
    public ResponseEntity<ApiResponseDTO<List<ComplexoEspacosRetornoDTO>>> listarComplexos(@PathVariable String id) {
        var complexos = espacoService.listarComplexos(id);
        return ResponseEntity.ok(ApiResponseDTO.success(complexos));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<Void>> deletar(@PathVariable String id) {
        espacoService.deletar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    /**
     * Obtém estatísticas de uso dos espaços em um período.
     * 
     * <p>Retorna estatísticas detalhadas sobre o uso dos espaços, incluindo:</p>
     * <ul>
     *   <li>Estatísticas por mês do período</li>
     *   <li>Mês com mais reservas (do período ou do ano se período for 1 mês)</li>
     *   <li>Usuários que mais reservaram no período</li>
     *   <li>Todos os usuários que reservaram no período</li>
     *   <li>Totais do período (reservas solicitadas e aprovadas)</li>
     * </ul>
     * 
     * <p>As estatísticas são agrupadas por espaço e podem ser filtradas
     * por espaços específicos, departamento, localização ou tipo de espaço.</p>
     * 
     * @param mesInicial mês inicial para filtrar reservas (1-12), padrão = mês atual
     * @param anoInicial ano inicial para filtrar reservas, padrão = ano atual
     * @param mesFinal mês final para filtrar reservas (1-12), padrão = mês atual
     * @param anoFinal ano final para filtrar reservas, padrão = ano atual
     * @param espacoIds lista de IDs de espaços para filtrar (opcional, padrão = todos os espaços)
     * @param departamentoId ID do departamento para filtrar espaços (opcional)
     * @param localizacaoId ID da localização para filtrar espaços (opcional)
     * @param tipoEspacoId ID do tipo de espaço para filtrar espaços (opcional)
     * @return estatísticas agrupadas por espaço
     */
    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas de uso dos espaços",
               description = "Retorna estatísticas detalhadas sobre o uso dos espaços em um período, incluindo estatísticas por mês, " +
                           "mês com mais reservas e usuários que mais reservaram. Pode ser filtrado por período e espaços específicos.")
    public ResponseEntity<ApiResponseDTO<EstatisticasGeralDTO>> obterEstatisticas(
            @RequestParam(required = false) Integer mesInicial,
            @RequestParam(required = false) Integer anoInicial,
            @RequestParam(required = false) Integer mesFinal,
            @RequestParam(required = false) Integer anoFinal,
            @RequestParam(required = false) List<String> espacoIds,
            @RequestParam(required = false) String departamentoId,
            @RequestParam(required = false) String localizacaoId,
            @RequestParam(required = false) String tipoEspacoId) {
        var estatisticas = espacoService.obterEstatisticas(mesInicial, anoInicial, mesFinal, anoFinal, espacoIds, departamentoId, localizacaoId, tipoEspacoId);
        return ResponseEntity.ok(ApiResponseDTO.success(estatisticas));
    }

    /**
     * Gera PDF com estatísticas de uso dos espaços em um período.
     * 
     * <p>Retorna um arquivo PDF contendo as mesmas informações do endpoint de estatísticas,
     * formatado para impressão ou download.</p>
     * 
     * @param mesInicial mês inicial para filtrar reservas (1-12), padrão = mês atual
     * @param anoInicial ano inicial para filtrar reservas, padrão = ano atual
     * @param mesFinal mês final para filtrar reservas (1-12), padrão = mês atual
     * @param anoFinal ano final para filtrar reservas, padrão = ano atual
     * @param espacoIds lista de IDs de espaços para filtrar (opcional, padrão = todos os espaços)
     * @param departamentoId ID do departamento para filtrar espaços (opcional)
     * @param localizacaoId ID da localização para filtrar espaços (opcional)
     * @param tipoEspacoId ID do tipo de espaço para filtrar espaços (opcional)
     * @return arquivo PDF com as estatísticas
     */
    @GetMapping("/estatisticas/pdf")
    @Operation(summary = "Gerar PDF com estatísticas de uso dos espaços",
               description = "Gera um documento PDF contendo as estatísticas detalhadas de uso dos espaços em um período.")
    public ResponseEntity<byte[]> gerarPDFEstatisticas(
            @RequestParam(required = false) Integer mesInicial,
            @RequestParam(required = false) Integer anoInicial,
            @RequestParam(required = false) Integer mesFinal,
            @RequestParam(required = false) Integer anoFinal,
            @RequestParam(required = false) List<String> espacoIds,
            @RequestParam(required = false) String departamentoId,
            @RequestParam(required = false) String localizacaoId,
            @RequestParam(required = false) String tipoEspacoId) throws java.io.IOException {
        byte[] pdfBytes = espacoService.gerarPDFEstatisticas(mesInicial, anoInicial, mesFinal, anoFinal, espacoIds, departamentoId, localizacaoId, tipoEspacoId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "estatisticas-espacos.pdf");
        headers.setContentLength(pdfBytes.length);
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}

