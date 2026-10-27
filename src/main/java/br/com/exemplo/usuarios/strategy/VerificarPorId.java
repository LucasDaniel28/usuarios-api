package br.com.exemplo.usuarios.strategy;

import br.com.exemplo.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component("id")
public class VerificarPorId implements VerificacaoUsuarioStrategy {

    private final UsuarioRepository repository;

    public VerificarPorId(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existe(String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        try {
            long id = Long.parseLong(valor);
            return id > 0 && repository.existsById(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
