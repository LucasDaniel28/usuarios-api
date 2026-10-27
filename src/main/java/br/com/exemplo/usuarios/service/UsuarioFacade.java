package br.com.exemplo.usuarios.service;

import br.com.exemplo.usuarios.dto.UsuarioRequest;
import br.com.exemplo.usuarios.dto.UsuarioResponse;
import br.com.exemplo.usuarios.model.Usuario;
import br.com.exemplo.usuarios.repository.UsuarioRepository;
import br.com.exemplo.usuarios.strategy.VerificacaoUsuarioStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioFacade {

    private final UsuarioRepository repository;
    private final Map<String, VerificacaoUsuarioStrategy> estrategias;

    public UsuarioFacade(
            UsuarioRepository repository,
            Map<String, VerificacaoUsuarioStrategy> estrategias) {
        this.repository = repository;
        this.estrategias = estrategias;
    }

    public UsuarioResponse cadastrar(UsuarioRequest request) {
        String email = request.email().trim();

        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um usuário com esse e-mail"
            );
        }

        Usuario usuario = new Usuario(
                request.nome().trim(),
                email,
                request.endereco().trim()
        );

        return converter(repository.save(usuario));
    }

    public List<UsuarioResponse> listar() {
        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        return converter(usuario);
    }

    public boolean verificar(String tipo, String valor) {
        VerificacaoUsuarioStrategy estrategia = estrategias.get(tipo);

        if (estrategia == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo inválido. Use 'id' ou 'email'"
            );
        }

        return estrategia.existe(valor);
    }

    private UsuarioResponse converter(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco()
        );
    }
}
