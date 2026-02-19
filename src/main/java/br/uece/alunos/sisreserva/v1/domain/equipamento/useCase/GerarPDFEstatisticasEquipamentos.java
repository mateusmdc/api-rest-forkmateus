package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.UsuarioEstatisticaDTO;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Caso de uso para gerar PDF com estatísticas de uso dos equipamentos.
 * 
 * <p>Gera um documento PDF formatado contendo as estatísticas detalhadas
 * de uso dos equipamentos, incluindo reservas do mês, mês com mais reservas
 * e usuários que mais reservaram.</p>
 * 
 * <p>Utiliza Apache PDFBox para geração do documento.</p>
 */
@Component
@RequiredArgsConstructor
public class GerarPDFEstatisticasEquipamentos {
    
    private final ObterEstatisticasEquipamentos obterEstatisticasEquipamentos;
    
    private static final float MARGIN = 50;
    private static final float FONT_SIZE_TITLE = 18;
    private static final float FONT_SIZE_SUBTITLE = 14;
    private static final float FONT_SIZE_NORMAL = 10;
    private static final float LINE_HEIGHT = 15;
    
    /**
     * Gera PDF com estatísticas de uso dos equipamentos.
     * 
     * @param mes mês para filtrar reservas (opcional, padrão = mês atual)
     * @param ano ano para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @return array de bytes contendo o PDF gerado
     * @throws IOException se houver erro na geração do PDF
     */
    public byte[] gerarPDF(Integer mes, Integer ano, List<String> equipamentoIds) throws IOException {
        // Obtém as estatísticas
        EstatisticasGeralEquipamentoDTO estatisticas = obterEstatisticasEquipamentos.obterEstatisticas(mes, ano, equipamentoIds);
        
        // Cria o documento PDF
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            // Adiciona páginas com as estatísticas
            for (EstatisticasEquipamentoDTO equipamentoStats : estatisticas.equipamentos()) {
                adicionarPaginaEquipamento(document, equipamentoStats);
            }
            
            // Se não houver equipamentos, adiciona página vazia com mensagem
            if (estatisticas.equipamentos().isEmpty()) {
                adicionarPaginaVazia(document);
            }
            
            // Salva o documento no ByteArrayOutputStream
            document.save(baos);
            return baos.toByteArray();
        }
    }
    
    /**
     * Adiciona uma página ao documento com as estatísticas de um equipamento.
     */
    private void adicionarPaginaEquipamento(PDDocument document, EstatisticasEquipamentoDTO equipamentoStats) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float yPosition = page.getMediaBox().getHeight() - MARGIN;
            
            // Título principal
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_TITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Estatisticas de Uso - Equipamento");
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Data de geração
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            String dataGeracao = "Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            contentStream.showText(dataGeracao);
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Informações do equipamento
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Equipamento: " + equipamentoStats.equipamentoTombamento());
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Tipo: " + equipamentoStats.equipamentoTipo());
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("ID: " + equipamentoStats.equipamentoId());
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Reservas do mês
            yPosition = adicionarSecaoReservasMes(contentStream, yPosition, "Reservas do Mes", equipamentoStats.reservasDoMes());
            yPosition -= LINE_HEIGHT;
            
            // Mês com mais reservas
            yPosition = adicionarSecaoReservasMes(contentStream, yPosition, "Mes com Mais Reservas", equipamentoStats.mesComMaisReservas());
            yPosition -= LINE_HEIGHT;
            
            // Usuários que mais reservaram
            yPosition = adicionarSecaoUsuarios(contentStream, yPosition, equipamentoStats.usuariosQueMaisReservaram(), page.getMediaBox().getHeight());
        }
    }
    
    /**
     * Adiciona seção com informações de reservas de um mês.
     */
    private float adicionarSecaoReservasMes(PDPageContentStream contentStream, float yPosition, 
                                            String titulo, ReservasMesDTO reservas) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(titulo);
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN + 20, yPosition);
        contentStream.showText("Periodo: " + String.format("%02d/%d", reservas.mes(), reservas.ano()));
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN + 20, yPosition);
        contentStream.showText("Total de Reservas Solicitadas: " + reservas.reservasSolicitadas());
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN + 20, yPosition);
        contentStream.showText("Total de Reservas Confirmadas: " + reservas.reservasConfirmadas());
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        return yPosition;
    }
    
    /**
     * Adiciona seção com lista de usuários que mais reservaram.
     */
    private float adicionarSecaoUsuarios(PDPageContentStream contentStream, float yPosition,
                                        List<UsuarioEstatisticaDTO> usuarios, float pageHeight) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Usuarios que Mais Reservaram");
        contentStream.endText();
        yPosition -= LINE_HEIGHT * 1.5f;
        
        if (usuarios.isEmpty()) {
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText("Nenhuma reserva encontrada.");
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
        } else {
            int count = 0;
            for (UsuarioEstatisticaDTO usuario : usuarios) {
                // Limita a 10 usuários para não estourar a página
                if (count >= 10 || yPosition < MARGIN + 50) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), FONT_SIZE_NORMAL);
                    contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                    contentStream.showText("... e mais usuarios");
                    contentStream.endText();
                    break;
                }
                
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                String linha = String.format("%d. %s - %d reservas (%d confirmadas)",
                    count + 1,
                    usuario.usuarioNome(),
                    usuario.reservasSolicitadas(),
                    usuario.reservasConfirmadas()
                );
                contentStream.showText(linha);
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
                count++;
            }
        }
        
        return yPosition;
    }
    
    /**
     * Adiciona uma página vazia com mensagem quando não há dados.
     */
    private void adicionarPaginaVazia(PDDocument document) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float yPosition = page.getMediaBox().getHeight() / 2;
            
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Nenhum equipamento encontrado com os filtros especificados.");
            contentStream.endText();
        }
    }
}
