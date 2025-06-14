package br.uece.alunos.sisreserva.v1.infra.exceptions;

public class DTOValidationException extends RuntimeException {
    public DTOValidationException(String message) {
        super(message);
    }
}
