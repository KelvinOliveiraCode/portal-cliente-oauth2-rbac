# Portal Cliente — OAuth2 + RBAC

Portal cliente com login OAuth2 (emissão de JWT) e controle de acesso baseado em papel (RBAC). Backend em Java 21 com Spring Boot 3; frontend em React + TypeScript + Tailwind CSS. Dados de demonstração são carregados na inicialização, sem banco de dados externo.

## Sobre o projeto

### PT-BR
Aplicação full-stack de mínimo produto viável (MVP) para controle de acesso de clientes. O backend expõe uma API REST protegida por Spring Security com JWT stateless. O frontend consome a API, armazena o token em `localStorage` e mostra ou esconde conteúdos de acordo com o papel do usuário (ADMIN ou CLIENTE).

### EN
MVP for client access control. The backend exposes a REST API protected by Spring Security with stateless JWT. The frontend consumes the API, stores the token in `localStorage`, and shows or hides content according to the user role (ADMIN or CLIENTE).

## Funcionalidades

### PT-BR
- Login OAuth2 com emissão de JWT (endpoint `/auth/login`)
- Papéis: `ADMIN` e `CLIENTE`
- Cadastro, listagem, busca, atualização e desativação de usuários
- Atribuição e remoção de papéis a usuários (RBAC)
- Endpoints protegidos por papel: `/api/admin/**` exige `ADMIN`; `/api/cliente/**` aceita `CLIENTE` ou `ADMIN`; demais `/api/**` exigem autenticação
- Tratamento padronizado de erros (400, 401, 403, 404, 500)
- Seed de dados na inicialização (usuários `admin`/`admin123` e `cliente`/`cliente123`)

### EN
- OAuth2 login with JWT issuance (`/auth/login`)
- Roles: `ADMIN` and `CLIENTE`
- Create, list, get, update and deactivate users
- Assign and revoke roles on users (RBAC)
- Role-protected endpoints: `/api/admin/**` requires `ADMIN`; `/api/cliente/**` accepts `CLIENTE` or `ADMIN`; other `/api/**` require authentication
- Standardized error handling (400, 401, 403, 404, 500)
- Seed data on startup (users `admin`/`admin123` and `cliente`/`cliente123`)

## Como usar

### Pré-requisitos
JDK 21, Maven 3.9+, Node.js 20+ e npm.

### Backend

#### PT-BR
Na raiz do projeto:

```
mvn spring-boot:run
```

A aplicação sobe na porta `8080`. Não é necessário configurar banco de dados; os repositórios são em memória e o seed carrega os papéis e usuários de demonstração ao iniciar.

#### EN
In the project root:

```
mvn spring-boot:run
```

The application starts on port `8080`. No database setup is required; repositories are in-memory and the seed loads roles and demo users on startup.

### Frontend

#### PT-BR
```
cd frontend
npm install
npm run dev
```

O servidor de desenvolvimento roda na porta `5173` e encaminha `/api` e `/auth` para o backend na porta `8080`.

#### EN
```
cd frontend
npm install
npm run dev
```

The dev server runs on port `5173` and proxies `/api` and `/auth` to the backend on port `8080`.

## Testes

### PT-BR
Executa a suíte JUnit 5 do backend:

```
mvn test
```

Resultado: 28 testes, 0 falhas, 0 erros.

### EN
Runs the backend JUnit 5 suite:

```
mvn test
```

Result: 28 tests, 0 failures, 0 errors.

### Build do frontend

#### PT-BR
```
cd frontend
npm run build
```

#### EN
```
cd frontend
npm run build
```

## Endpoints principais

| Método | Caminho | Acesso | Descrição (PT-BR) | Description (EN) |
|--------|---------|--------|-------------------|------------------|
| POST | `/auth/login` | público | Autentica e retorna JWT | Authenticates and returns JWT |
| GET | `/auth/me` | autenticado | Dados da sessão | Session data |
| GET | `/api/usuarios` | ADMIN | Lista usuários | Lists users |
| POST | `/api/usuarios` | ADMIN | Cadastra usuário | Creates a user |
| POST | `/api/usuarios/{id}/papeis` | ADMIN | Atribui papel | Assigns a role |
| GET | `/api/papeis` | autenticado | Lista papéis | Lists roles |
| GET | `/api/recursos` | autenticado | Lista recursos | Lists resources |

## Licença

MIT

## Autor

Kelvin Oliveira
