package br.com.exemplo.usuarios.strategy;

import br.com.exemplo.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component("email")
public class VerificarPorEmail implements VerificacaoUsuarioStrategy {

    private final UsuarioRepository repository;

    public VerificarPorEmail(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existe(String valor) {
        return valor != null
                && !valor.isBlank()
                && repository.existsByEmailIgnoreCase(valor.trim());
    }
}
