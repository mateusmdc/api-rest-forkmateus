package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.TotaisPeriodoDTO;
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
 * Caso de uso para gerar PDF com estatísticas de uso dos equipamentos em um período.
 * 
 * <p>Gera um documento PDF formatado contendo as estatísticas detalhadas
 * de uso dos equipamentos em um período, incluindo estatísticas por mês,
 * mês com mais reservas e usuários que mais reservaram.</p>
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
     * Gera PDF com estatísticas de uso dos equipamentos em um período.
     * 
     * @param mesInicial mês inicial para filtrar reservas (opcional, padrão = mês atual)
     * @param anoInicial ano inicial para filtrar reservas (opcional, padrão = ano atual)
     * @param mesFinal mês final para filtrar reservas (opcional, padrão = mês atual)
     * @param anoFinal ano final para filtrar reservas (opcional, padrão = ano atual)
     * @param tombamentos lista de tombamentos de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtro para equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço para filtrar equipamentos vinculados (opcional)
     * @return array de bytes contendo o PDF gerado
     * @throws IOException se houver erro na geração do PDF
     */
    public byte[] gerarPDF(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> tombamentos, String tipoEquipamentoId, Boolean multiusuario, String espacoId) throws IOException {
        // Obtém as estatísticas
        EstatisticasGeralEquipamentoDTO estatisticas = obterEstatisticasEquipamentos.obterEstatisticas(mesInicial, anoInicial, mesFinal, anoFinal, tombamentos, tipoEquipamentoId, multiusuario, espacoId);
        
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
            float pageHeight = page.getMediaBox().getHeight();
            
            // Título principal
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_TITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(removerAcentos("Estatísticas de Uso - Equipamento"));
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Data de geração
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            String dataGeracao = removerAcentos("Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            contentStream.showText(dataGeracao);
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Tipo do equipamento
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(removerAcentos("Equipamento: " + equipamentoStats.equipamentoTombamento() + " (Tombamento)"));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            // Descrição do equipamento
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            String descricao = equipamentoStats.equipamentoDescricao() != null ? equipamentoStats.equipamentoDescricao() : "";
            contentStream.showText(removerAcentos(descricao));
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Estatísticas por mês do período
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(removerAcentos("Estatísticas por Mês"));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            // Preparar dados da tabela
            String[] headers = {"Mês/Ano", "Solicitadas", "Confirmadas"};
            String[][] data = new String[equipamentoStats.estatisticasPorMes().size()][3];
            int rowIndex = 0;
            for (ReservasMesDTO mes : equipamentoStats.estatisticasPorMes()) {
                data[rowIndex][0] = String.format("%02d/%d", mes.mes(), mes.ano());
                data[rowIndex][1] = String.valueOf(mes.reservasSolicitadas());
                data[rowIndex][2] = String.valueOf(mes.reservasConfirmadas());
                rowIndex++;
            }
            float[] columnWidths = {150f, 150f, 150f};
            
            yPosition = desenharTabela(contentStream, yPosition, headers, data, columnWidths);
            yPosition -= LINE_HEIGHT;
            
            // Totais do período
            yPosition -= LINE_HEIGHT * 0.5f;
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(removerAcentos("Totais do Período"));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            TotaisPeriodoDTO totais = equipamentoStats.totaisPeriodo();
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText(removerAcentos("Total Solicitadas: " + totais.totalReservasSolicitadas()));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText(removerAcentos("Total Aprovadas: " + totais.totalReservasAprovadas()));
            contentStream.endText();
            yPosition -= LINE_HEIGHT * 2;
            
            // Mês com mais solicitações de reservas (apenas se período > 1 mês)
            if (equipamentoStats.mesComMaisReservas() != null) {
                yPosition = adicionarSecaoReservasMes(contentStream, yPosition, 
                    removerAcentos("Mês com mais solicitações de reservas"), equipamentoStats.mesComMaisReservas());
                yPosition -= LINE_HEIGHT;
            }
            
            // Usuários que mais reservaram
            yPosition = adicionarSecaoUsuarios(contentStream, yPosition, 
                equipamentoStats.usuariosQueMaisReservaram(), pageHeight,
                "Usuários que Mais Reservaram", 10);
            yPosition -= LINE_HEIGHT;
            
            // Todos os usuários que solicitaram reserva
            yPosition = adicionarSecaoUsuarios(contentStream, yPosition, 
                equipamentoStats.todosUsuarios(), pageHeight,
                "Todos os Usuários que Solicitaram reserva", 0);
        }
    }
    
    /**
     * Adiciona uma seção de reservas de um mês.
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
        contentStream.showText(removerAcentos(String.format("Período: %02d/%d", reservas.mes(), reservas.ano())));
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN + 20, yPosition);
        contentStream.showText(removerAcentos("Total Solicitadas: " + reservas.reservasSolicitadas()));
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN + 20, yPosition);
        contentStream.showText(removerAcentos("Total Aprovadas: " + reservas.reservasConfirmadas()));
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        return yPosition;
    }
    
    /**
     * Adiciona a seção de usuários que mais reservaram.
     */
    private float adicionarSecaoUsuarios(PDPageContentStream contentStream, float yPosition, 
                                        List<UsuarioEstatisticaDTO> usuarios, float pageHeight, 
                                        String titulo, int limite) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(removerAcentos(titulo));
        contentStream.endText();
        yPosition -= LINE_HEIGHT;
        
        if (usuarios.isEmpty()) {
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText(removerAcentos("Nenhuma reserva encontrada"));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            return yPosition;
        }
        
        // Determinar quantos usuários mostrar
        int totalUsuarios = limite > 0 ? Math.min(limite, usuarios.size()) : usuarios.size();
        
        // Preparar dados da tabela
        String[] headers = {"Usuário", "Solicitadas", "Confirmadas"};
        String[][] data = new String[totalUsuarios][3];
        for (int i = 0; i < totalUsuarios; i++) {
            UsuarioEstatisticaDTO usuario = usuarios.get(i);
            data[i][0] = usuario.usuarioNome();
            data[i][1] = String.valueOf(usuario.reservasSolicitadas());
            data[i][2] = String.valueOf(usuario.reservasConfirmadas());
        }
        float[] columnWidths = {250f, 125f, 125f};
        
        yPosition = desenharTabela(contentStream, yPosition, headers, data, columnWidths);
        
        // Se houver mais usuários que o limite, adicionar nota
        if (limite > 0 && usuarios.size() > limite) {
            yPosition -= LINE_HEIGHT;
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText(removerAcentos(String.format("... e mais %d usuários", usuarios.size() - limite)));
            contentStream.endText();
        }
        
        return yPosition;
    }
    
    /**
     * Desenha uma tabela no PDF com colunas e linhas.
     * 
     * @param contentStream stream de conteúdo da página
     * @param yPosition posição Y inicial
     * @param headers cabeçalhos das colunas
     * @param data dados das linhas (cada linha é um array de strings)
     * @param columnWidths larguras de cada coluna
     * @return nova posição Y após a tabela
     */
    private float desenharTabela(PDPageContentStream contentStream, float yPosition, 
                                String[] headers, String[][] data, float[] columnWidths) throws IOException {
        float tableWidth = 0;
        for (float width : columnWidths) {
            tableWidth += width;
        }
        
        float rowHeight = LINE_HEIGHT + 5;
        float tableYStart = yPosition;
        float xPosition = MARGIN;
        
        // Desenhar cabeçalho
        contentStream.setLineWidth(1f);
        
        // Linha superior da tabela
        contentStream.moveTo(xPosition, yPosition);
        contentStream.lineTo(xPosition + tableWidth, yPosition);
        contentStream.stroke();
        
        yPosition -= rowHeight;
        
        // Texto do cabeçalho
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
        float currentX = xPosition;
        for (int i = 0; i < headers.length; i++) {
            contentStream.newLineAtOffset(currentX + 5, yPosition + 5);
            contentStream.showText(removerAcentos(headers[i]));
            contentStream.newLineAtOffset(-(currentX + 5), -(yPosition + 5));
            currentX += columnWidths[i];
        }
        contentStream.endText();
        
        // Linha após cabeçalho
        contentStream.moveTo(xPosition, yPosition);
        contentStream.lineTo(xPosition + tableWidth, yPosition);
        contentStream.stroke();
        
        // Linhas verticais do cabeçalho
        currentX = xPosition;
        for (int i = 0; i <= columnWidths.length; i++) {
            contentStream.moveTo(currentX, tableYStart);
            contentStream.lineTo(currentX, yPosition);
            contentStream.stroke();
            if (i < columnWidths.length) {
                currentX += columnWidths[i];
            }
        }
        
        // Desenhar dados
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
        for (String[] row : data) {
            yPosition -= rowHeight;
            
            // Texto da linha
            contentStream.beginText();
            currentX = xPosition;
            for (int i = 0; i < row.length && i < columnWidths.length; i++) {
                contentStream.newLineAtOffset(currentX + 5, yPosition + 5);
                contentStream.showText(removerAcentos(row[i]));
                contentStream.newLineAtOffset(-(currentX + 5), -(yPosition + 5));
                currentX += columnWidths[i];
            }
            contentStream.endText();
            
            // Linha horizontal
            contentStream.moveTo(xPosition, yPosition);
            contentStream.lineTo(xPosition + tableWidth, yPosition);
            contentStream.stroke();
            
            // Linhas verticais
            currentX = xPosition;
            for (int i = 0; i <= columnWidths.length; i++) {
                contentStream.moveTo(currentX, yPosition + rowHeight);
                contentStream.lineTo(currentX, yPosition);
                contentStream.stroke();
                if (i < columnWidths.length) {
                    currentX += columnWidths[i];
                }
            }
        }
        
        return yPosition - LINE_HEIGHT;
    }
    
    /**
     * Adiciona uma página vazia com mensagem de "sem dados".
     */
    private void adicionarPaginaVazia(PDDocument document) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float yPosition = page.getMediaBox().getHeight() / 2;
            
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(removerAcentos("Nenhum equipamento encontrado para as estatísticas."));
            contentStream.endText();
        }
    }
    
    /**
     * Remove acentos de uma string para compatibilidade com PDF.
     * Nota: PDFBox 3.x com WinAnsiEncoding suporta acentos portugueses.
     * 
     * @param texto texto original
     * @return texto original (preservando acentos)
     */
    private String removerAcentos(String texto) {
        // PDFBox 3.x com WinAnsiEncoding suporta caracteres acentuados
        // Retorna o texto original preservando os acentos
        return texto != null ? texto : "";
    }
}
