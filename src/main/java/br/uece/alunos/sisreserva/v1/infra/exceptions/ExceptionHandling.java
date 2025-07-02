package br.uece.alunos.sisreserva.v1.infra.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;

import static br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO.failure;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(failure("EntityNotFoundException", "Entidade não encontrada."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(failure("IllegalArgumentException", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(failure("ValidationException", String.join("; ", errors)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife &&
                ife.getTargetType().equals(java.time.LocalDate.class)) {
            return ResponseEntity.badRequest()
                    .body(failure("InvalidFormatException", "Formato de data inválido. Use o formato AAAA-MM-DD."));
        }

        return ResponseEntity.badRequest()
                .body(failure("HttpMessageNotReadableException", "Erro ao ler a mensagem da requisição."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(failure("BadCredentialsException", "Credenciais inválidas."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(failure("AuthenticationException", "Erro ao autenticar o usuário."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(failure("AccessDeniedException", "Acesso negado."));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(failure("LockedException", "Conta bloqueada."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof SQLException sqlEx && "23505".equals(sqlEx.getSQLState())) {
            if (sqlEx.getMessage().contains("usuario_email_key")) {
                return ResponseEntity.badRequest()
                        .body(failure("DuplicateEmail", "Já existe um usuário com este email."));
            }
        }

        return ResponseEntity.badRequest()
                .body(failure("DataIntegrityViolationException", "Erro de integridade de dados."));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.badRequest()
                .body(failure("MaxUploadSizeExceededException", "Tamanho máximo de upload excedido. Envie um arquivo menor."));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<?> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(failure("InternalAuthenticationServiceException", "Login ou senha incorretos."));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(failure("ValidationException", ex.getMessage()));
    }

    @ExceptionHandler(DTOValidationException.class)
    public ResponseEntity<?> handleDTOValidationException(DTOValidationException ex) {
        return ResponseEntity.badRequest().body(failure("DTOValidationException", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(failure("RuntimeException", "Erro interno do servidor: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(failure("Exception", "Erro inesperado: " + ex.getLocalizedMessage()));
    }

    private record DataValidationError(String field, String message) {
        public DataValidationError(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}