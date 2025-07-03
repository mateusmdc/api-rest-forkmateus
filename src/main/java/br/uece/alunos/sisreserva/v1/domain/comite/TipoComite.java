package br.uece.alunos.sisreserva.v1.domain.comite;

public enum TipoComite {
    GESTOR(1),
    USUARIOS(2),
    TECNICOS(3),
    REPRESENTANTE_DISCENTE(4);

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