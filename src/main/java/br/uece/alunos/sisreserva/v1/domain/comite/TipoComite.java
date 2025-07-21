package br.uece.alunos.sisreserva.v1.domain.comite;

public enum TipoComite {
    GESTOR(0),
    USUARIOS(1),
    TECNICOS(2),
    REPRESENTANTE_DISCENTE(3);

    private final int codigo;

    TipoComite(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoComite fromCodigo(int codigo) {
        for (TipoComite tipo : values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoComite: " + codigo);
    }
}