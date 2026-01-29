package br.uece.alunos.sisreserva.v1.infra.utils.validators;

/**
 * Classe utilitária para validação e formatação de CPF.
 * Fornece métodos para normalizar, validar e formatar números de CPF.
 */
public class DocumentoFiscalUtils {

    /**
     * Normaliza um CPF removendo todos os caracteres não numéricos.
     * 
     * @param cpf CPF a ser normalizado
     * @return CPF contendo apenas dígitos
     */
    public static String normalizarCPF(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    /**
     * Valida se um CPF é válido segundo o algoritmo de validação oficial.
     * 
     * @param cpf CPF a ser validado (pode conter formatação)
     * @return true se o CPF é válido, false caso contrário
     */
    public static boolean validarCPF(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove formatação
        cpf = normalizarCPF(cpf);

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Calcula o primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }

            // Verifica o primeiro dígito
            if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
                return false;
            }

            // Calcula o segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }

            // Verifica o segundo dígito
            return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Formata um CPF no padrão XXX.XXX.XXX-XX.
     * 
     * @param cpf CPF a ser formatado (apenas dígitos)
     * @return CPF formatado ou o valor original se inválido
     */
    public static String formatarCPF(String cpf) {
        if (cpf == null) {
            return null;
        }

        // Remove formatação existente
        cpf = normalizarCPF(cpf);

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return cpf;
        }

        // Formata: XXX.XXX.XXX-XX
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }
}
