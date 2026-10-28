package br.com.exemplo.usuarios;

import br.com.exemplo.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:usuarios-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.h2.console.enabled=false"
})
@AutoConfigureMockMvc
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveCadastrarUsuario() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Silva",
                                  "email": "ana@exemplo.com",
                                  "endereco": "Rua das Flores, 123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Ana Silva"))
                .andExpect(jsonPath("$.email").value("ana@exemplo.com"))
                .andExpect(jsonPath("$.endereco").value("Rua das Flores, 123"));
    }

    @Test
    void deveRejeitarEmailDuplicadoIgnorandoMaiusculas() throws Exception {
        cadastrarAna();

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Souza",
                                  "email": "ANA@EXEMPLO.COM",
                                  "endereco": "Avenida Central, 10"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRejeitarDadosInvalidos() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": " ",
                                  "email": "email-invalido",
                                  "endereco": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarUsuariosCadastrados() throws Exception {
        cadastrarAna();

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Ana Silva"));
    }

    @Test
    void deveRetornar404AoBuscarUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/api/usuarios/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveVerificarEmailExistente() throws Exception {
        cadastrarAna();

        mockMvc.perform(get("/api/usuarios/verificar")
                        .param("tipo", "email")
                        .param("valor", "ANA@EXEMPLO.COM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("email"))
                .andExpect(jsonPath("$.existe").value(true));
    }

    @Test
    void deveRejeitarTipoDeVerificacaoDesconhecido() throws Exception {
        mockMvc.perform(get("/api/usuarios/verificar")
                        .param("tipo", "telefone")
                        .param("valor", "11999999999"))
                .andExpect(status().isBadRequest());
    }

    private void cadastrarAna() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Silva",
                                  "email": "ana@exemplo.com",
                                  "endereco": "Rua das Flores, 123"
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
