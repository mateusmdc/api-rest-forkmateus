package br.uece.alunos.sisreserva.v1.infra.utils.validators;

/**
 * Classe utilitária para validação e formatação de telefone.
 * Fornece métodos para normalizar, validar e formatar números de telefone brasileiros.
 */
public class TelefoneUtils {

    /**
     * Normaliza um telefone removendo todos os caracteres não numéricos.
     * 
     * @param telefone Telefone a ser normalizado
     * @return Telefone contendo apenas dígitos
     */
    public static String normalizarTelefone(String telefone) {
        if (telefone == null) {
            return null;
        }
        return telefone.replaceAll("[^0-9]", "");
    }

    /**
     * Valida se um telefone tem o tamanho correto.
     * Permite de 10 a 11 dígitos (DDD + 8 ou 9 dígitos do número).
     * 
     * @param telefone Telefone a ser validado (pode conter formatação)
     * @return true se o telefone é válido, false caso contrário
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return true; // Telefone é opcional
        }

        // Remove formatação
        telefone = normalizarTelefone(telefone);

        // Verifica se tem entre 10 e 11 dígitos
        return telefone.length() >= 10 && telefone.length() <= 11;
    }

    /**
     * Formata um telefone no padrão (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.
     * 
     * @param telefone Telefone a ser formatado (apenas dígitos)
     * @return Telefone formatado ou o valor original se inválido
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return telefone;
        }

        // Remove formatação existente
        telefone = normalizarTelefone(telefone);

        // Formata conforme o tamanho
        if (telefone.length() == 11) {
            // (XX) XXXXX-XXXX
            return String.format("(%s) %s-%s",
                    telefone.substring(0, 2),
                    telefone.substring(2, 7),
                    telefone.substring(7, 11));
        } else if (telefone.length() == 10) {
            // (XX) XXXX-XXXX
            return String.format("(%s) %s-%s",
                    telefone.substring(0, 2),
                    telefone.substring(2, 6),
                    telefone.substring(6, 10));
        }

        // Retorna sem formatação se não tiver tamanho válido
        return telefone;
    }
}
