# Usuários API

API REST de cadastro, consulta e verificação de usuários, desenvolvida com Java 21, Spring Boot, Spring Data JPA, Bean Validation e banco H2.

## Requisitos

- JDK 21 instalado
- Maven 3.6.3 ou superior (ou Maven Wrapper, se você adicioná-lo pelo seu ambiente)

Confira as instalações:

```bash
java -version
mvn -version
```

## Executar

Na pasta que contém `pom.xml`:

```bash
mvn spring-boot:run
```

A API será iniciada em `http://localhost:8080`.

Também é possível compilar e executar o JAR:

```bash
mvn clean package
java -jar target/usuarios-api-0.0.1-SNAPSHOT.jar
```

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/api/usuarios` | Cadastra usuário |
| `GET` | `/api/usuarios` | Lista usuários |
| `GET` | `/api/usuarios/{id}` | Busca por ID |
| `GET` | `/api/usuarios/verificar?tipo=id&valor=1` | Verifica ID |
| `GET` | `/api/usuarios/verificar?tipo=email&valor=ana@exemplo.com` | Verifica e-mail |

### Exemplo de cadastro

Envie `POST http://localhost:8080/api/usuarios` com `Content-Type: application/json`:

```json
{
  "nome": "Ana Silva",
  "email": "ana@exemplo.com",
  "endereco": "Rua das Flores, 123"
}
```

Uma resposta bem-sucedida tem status `201 Created`:

```json
{
  "id": 1,
  "nome": "Ana Silva",
  "email": "ana@exemplo.com",
  "endereco": "Rua das Flores, 123"
}
```

### Exemplo de verificação

`GET http://localhost:8080/api/usuarios/verificar?tipo=email&valor=ana@exemplo.com`

Resposta:

```json
{
  "tipo": "email",
  "valor": "ana@exemplo.com",
  "existe": true
}
```

O parâmetro `tipo` aceita `id` ou `email`. Se o usuário não existir, `existe` será `false`. Um cadastro com e-mail já utilizado retorna `409 Conflict`.

## Banco de dados

O H2 está configurado para persistir em arquivo na pasta `data/usuariosdb`, relativa ao diretório de execução. O Hibernate cria/atualiza o esquema durante o desenvolvimento.

Console H2: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:file:./data/usuariosdb`
- User Name: `sa`
- Password: deixe em branco

Use esta configuração apenas localmente. Antes de produção, configure credenciais seguras, migrações de esquema, proteção de endpoints e tratamento dos conflitos de unicidade.

## Padrões e conceitos aplicados

- **Facade:** `UsuarioFacade` coordena cadastro, consulta e verificação.
- **Strategy:** `VerificarPorId` e `VerificarPorEmail` implementam `VerificacaoUsuarioStrategy`.
- **Singleton:** componentes Spring usam, por padrão, escopo singleton gerenciado pelo container.
- **SOLID/SRP:** controller, DTOs, entidade, repositório e estratégias têm responsabilidades separadas.
- **Injeção de dependência:** dependências são fornecidas pelo construtor.

## Estrutura

```text
src/main/java/br/com/exemplo/usuarios/
├── UsuariosApiApplication.java
├── controller/
│   └── UsuarioController.java
├── dto/
│   ├── UsuarioRequest.java
│   └── UsuarioResponse.java
├── model/
│   └── Usuario.java
├── repository/
│   └── UsuarioRepository.java
├── service/
│   └── UsuarioFacade.java
└── strategy/
    ├── VerificacaoUsuarioStrategy.java
    ├── VerificarPorId.java
    └── VerificarPorEmail.java
```

## Observações

- Esta é uma base didática, não uma API pronta para produção.
- O endpoint de listagem retorna todos os usuários; paginação é recomendada para conjuntos maiores.
- A checagem de e-mail antes de salvar melhora a mensagem de erro, mas não elimina uma corrida entre requisições simultâneas. A restrição `unique` do banco continua necessária e o conflito deve ser tratado explicitamente em uma versão de produção.
- O console H2 está habilitado para desenvolvimento e deve ser desabilitado ou protegido em ambientes expostos.
