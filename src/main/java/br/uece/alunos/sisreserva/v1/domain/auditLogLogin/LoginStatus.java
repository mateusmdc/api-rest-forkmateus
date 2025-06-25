package br.uece.alunos.sisreserva.v1.domain.auditLogLogin;

public enum LoginStatus {
    SUCESSO("sucesso"),
    FALHA("falha");

    private String status;

    LoginStatus(String status) {
        this.status = status;
    }
}
