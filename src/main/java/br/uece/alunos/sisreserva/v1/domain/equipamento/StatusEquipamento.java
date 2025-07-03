package br.uece.alunos.sisreserva.v1.domain.equipamento;

public enum StatusEquipamento {
    INATIVO(0),
    ATIVO(1),
    EM_MANUTENCAO(2);

    private final int codigo;

    StatusEquipamento(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusEquipamento fromCodigo(int codigo) {
        for (StatusEquipamento status : values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido para StatusEquipamento: " + codigo);
    }
}