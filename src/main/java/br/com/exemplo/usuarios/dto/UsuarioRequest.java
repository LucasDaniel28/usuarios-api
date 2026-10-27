package br.com.exemplo.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter até 100 caracteres")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 150, message = "O e-mail deve ter até 150 caracteres")
    String email,

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 250, message = "O endereço deve ter até 250 caracteres")
    String endereco

) {
}
