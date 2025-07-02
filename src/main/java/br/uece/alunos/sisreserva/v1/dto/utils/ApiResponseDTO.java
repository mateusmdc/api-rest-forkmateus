package br.uece.alunos.sisreserva.v1.dto.utils;

public record ApiResponseDTO<T>(
        ErrorResponse error,
        T data
) {
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(null, data);
    }

    public static <T> ApiResponseDTO<T> failure(String name, String message) {
        return new ApiResponseDTO<>(new ErrorResponse(name, message), null);
    }

    public record ErrorResponse(String name, String message) {}
}