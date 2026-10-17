# BRIEF — portal-cliente-oauth2-rbac

**Projeto:** portal-cliente-oauth2-rbac (Full-Stack)
**Local:** `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\portal-cliente-oauth2-rbac\`
**Stack:** Java 21 + Spring Boot 3 + JJWT 0.12.3 (backend); React 18 + TypeScript + Tailwind CSS 3 + Vite (frontend)
**Validação:** workflow manager (o dono desta sessão)

## Entregas

### Backend (`src/main/java/com/kelvinoliveis/portalrbac/`)
- `PortalClienteOauth2RbacApplication.java` — ponto de entrada Spring Boot
- `domain/` — `Papel.java`, `Recurso.java`, `Usuario.java` (records imutáveis)
- `repository/` — interfaces + implementações em memória determinísticas (`PapelRepository/Impl`, `RecursoRepository/Impl`, `UsuarioRepository/Impl`)
- `dto/` — `PapelDto/Request`, `RecursoDto/Request`, `UsuarioDto/Request`, `LoginRequest`, `AuthResponse`, `MensagemDto`
- `error/` — `EntidadeNaoEncontradaException`, `RegraNegocioException`, `CredenciaisInvalidasException`, `UsuarioInativoException`
- `service/` — `PapelService`, `RecursoService`, `UsuarioService` (cadastro com senha codificada, atribuição/remoção de papéis), `AuthService` (login OAuth2 + JWT), `TokenService` (emissão/validação JWT determinístico)
- `security/` — `SecurityConfig` (filtro stateless, RBAC: `/api/admin/**` = ADMIN, `/api/cliente/**` = CLIENTE ou ADMIN), `JwtAuthenticationFilter`
- `controller/` — `AuthController`, `UsuarioController`, `PapelController`, `RecursoController`, `GlobalExceptionHandler`
- `SeedRunner.java` — seed determinístico (papéis ADMIN/CLIENTE, recursos, usuários `admin`/`admin123` e `cliente`/`cliente123`)
- `pom.xml`, `src/main/resources/application.properties`

### Testes (`src/test/java/com/kelvinoliveis/portalrbac/`)
- `ApplicationContextTest.java` — contexto sobe (1 teste)
- `controller/AuthControllerTest.java` — login com mock (2 testes)
- `service/AuthServiceTest.java` — autenticação/sucesso/falha/inativo (4 testes)
- `service/PapelServiceTest.java` — CRUD de papéis (6 testes)
- `service/RecursoServiceTest.java` — CRUD de recursos (5 testes)
- `service/TokenServiceTest.java` — geração/validação de JWT (3 testes)
- `service/UsuarioServiceTest.java` — cadastro/duplicata/papeis/desativação (7 testes)

### Frontend (`frontend/`)
`package.json`, `vite.config.ts`, `tailwind.config.cjs`, `postcss.config.cjs`, `tsconfig.json`, `index.html`, `src/main.tsx`, `src/index.css`, `src/types.ts`, `src/api.ts`, `src/App.tsx`, `src/components/LoginForm.tsx`, `src/components/NavBar.tsx`, `src/pages/Dashboard.tsx`, `dist/` (build de produção)

### Documentação
- `README.md` — bilíngue PT-BR + EN (sobre, funcionalidades, como usar backend/frontend, testes, endpoints, licença MIT, autor Kelvin Oliveira)
- `BRIEF.md` — este arquivo

## Como verificar

### Backend
Comando de teste (Maven disponível no host):
```
mvn test
```
Resultado obtido: **28 testes, 0 falhas, 0 erros** — BUILD SUCCESS (todos os 7 suites verdes).

Compilação:
```
mvn -q compile
```
Sem erros de compilação.

Execução da aplicação:
```
mvn spring-boot:run
```
Porta `8080`. Usuários de demonstração: `admin`/`admin123` (ADMIN) e `cliente`/`cliente123` (CLIENTE).

### Frontend
```
cd frontend
npm install
npm run build        # tsc --noEmit + vite build: OK
```

## Caminhos dos documentos
- README: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\portal-cliente-oauth2-rbac\README.md`
- BRIEF: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\portal-cliente-oauth2-rbac\BRIEF.md`
