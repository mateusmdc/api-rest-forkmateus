package br.uece.alunos.api_aluga_espacos.v1.infra.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
