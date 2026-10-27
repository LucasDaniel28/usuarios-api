package br.com.exemplo.usuarios.controller;

import br.com.exemplo.usuarios.dto.UsuarioRequest;
import br.com.exemplo.usuarios.dto.UsuarioResponse;
import br.com.exemplo.usuarios.service.UsuarioFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioFacade facade;

    public UsuarioController(UsuarioFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(
            @Valid @RequestBody UsuarioRequest request) {
        return facade.cadastrar(request);
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return facade.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return facade.buscarPorId(id);
    }

    @GetMapping("/verificar")
    public Map<String, Object> verificar(
            @RequestParam String tipo,
            @RequestParam String valor) {

        boolean existe = facade.verificar(tipo, valor);

        return Map.of(
                "tipo", tipo,
                "valor", valor,
                "existe", existe
        );
    }
}
