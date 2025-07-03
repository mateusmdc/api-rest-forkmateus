package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva;

public enum StatusSolicitacao {
    PENDENTE(0),
    APROVADO(1),
    RECUSADO(2),
    PENDENTE_AJUSTE(3);

    private final int codigo;

    StatusSolicitacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusSolicitacao fromCodigo(int codigo) {
        for (StatusSolicitacao status : values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido para StatusSolicitacao: " + codigo);
    }
}

