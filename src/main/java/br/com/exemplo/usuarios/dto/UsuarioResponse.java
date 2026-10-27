package br.com.exemplo.usuarios.dto;

public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    String endereco
) {
}
