# SlotBooker Hub API

Backend de uma plataforma SaaS de agendamento de serviços e reservas de horários para profissionais autônomos e estúdios, como barbearias, consultórios, clínicas e personal trainers.

O projeto foi desenvolvido para estudo e portfólio, com foco em práticas profissionais de desenvolvimento Java Full Stack. A primeira versão utiliza um **monólito modular com Layered Architecture**, mantendo os módulos de autenticação, prestadores, serviços, expediente e agendamentos organizados por funcionalidade.

A solução foi planejada para evoluir gradualmente para microsserviços, sem adicionar complexidade desnecessária ao MVP.

## Sumário

- [Objetivos](#objetivos)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Stack](#stack)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Modelo de dados](#modelo-de-dados)
- [Enums](#enums)
- [Regras de negócio](#regras-de-negócio)
- [Endpoints](#endpoints)
- [Segurança](#segurança)
- [Tratamento de erros](#tratamento-de-erros)
- [Como executar](#como-executar)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
- [Roadmap](#roadmap)
- [Critérios de conclusão do MVP](#critérios-de-conclusão-do-mvp)
- [Boas práticas](#boas-práticas)
- [Convenção de commits](#convenção-de-commits)
- [Contribuição](#contribuição)
- [Referências](#referências)
- [Licença](#licença)

## Objetivos

O projeto tem como objetivos:

- praticar Java 17 e Spring Boot 3;
- construir REST APIs com contratos claros e tipagem estrita;
- implementar autenticação e autorização com Spring Security e JWT;
- aplicar persistência com Spring Data JPA e PostgreSQL;
- dominar cálculos temporais e validação de sobreposição com a API `java.time`;
- tratar concorrência de reservas para evitar *double-booking*;
- organizar o código em camadas e módulos por funcionalidade;
- implementar validação e tratamento centralizado de erros com `@RestControllerAdvice`;
- escrever unit tests e integration tests com Testcontainers;
- documentar a API com Swagger/OpenAPI;
- executar a aplicação e suas dependências com Docker Compose;
- preparar uma base sólida para uma futura migração para microsserviços.

## Funcionalidades

### MVP

- cadastro de prestador de serviços, que será o usuário gestor;
- autenticação por e-mail e senha;
- emissão de access token JWT;
- consulta e atualização do perfil autenticado;
- cadastro e gestão do catálogo de serviços, incluindo nome, preço e duração;
- configuração da grade semanal de expediente;
- consulta pública de slots disponíveis por prestador, serviço e data;
- criação pública de agendamento com validação de disponibilidade;
- listagem paginada de agendamentos do prestador com filtros;
- consulta de agendamento por identificador;
- alteração de status para `CONFIRMED`, `COMPLETED` ou `CANCELED`;
- cancelamento com liberação imediata do slot;
- isolamento dos dados por prestador autenticado;
- validação de entrada de dados e horários;
- respostas de erro padronizadas.

### Fora do escopo inicial

- microsserviços;
- gateway de pagamento e retenção de sinal;
- integração com Google Calendar ou Outlook;
- notificações em tempo real por WhatsApp ou SMS;
- múltiplos profissionais no mesmo estabelecimento;
- fila de espera automática;
- cancelamento com retenção de taxa;
- refresh token e revogação de sessão via Redis.

Esses itens aparecem no [Roadmap](#roadmap) como evoluções posteriores.

## Arquitetura

A aplicação começa como um **monólito modular**, organizado por funcionalidade e dividido internamente em camadas.

```text
Cliente
   |
   | HTTP / JSON
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
PostgreSQL
```

### Responsabilidades

- **Controller**: recebe requisições HTTP, valida DTOs e define status codes.
- **Service**: concentra regras de negócio, cálculo de horários livres e controle transacional.
- **Repository**: executa operações de persistência e consultas customizadas com Spring Data JPA.
- **Entity**: representa o modelo persistido no banco relacional.
- **DTO**: define contratos de entrada e saída da API, utilizando Java Records quando apropriado.
- **Mapper**: converte entities em DTOs e vice-versa.
- **Security**: configura autenticação, autorização e validação stateless do JWT.
- **Exception**: centraliza exceções de negócio, validação e segurança.

## Stack

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 |
| Web | Spring Web MVC |
| Segurança | Spring Security + JWT |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL |
| Migrações | Flyway |
| Validação | Jakarta Bean Validation |
| Documentação | Springdoc OpenAPI + Swagger UI |
| Unit tests | JUnit 5 + Mockito |
| Integration tests | Spring Boot Test + Testcontainers |
| Build | Maven |
| Containers | Docker + Docker Compose |
| CI | GitHub Actions, evolução planejada |

> As versões exatas das dependências devem permanecer centralizadas no `pom.xml`.

## Estrutura do projeto

```text
slotbooker-api/
├── src/
│   ├── main/
│   │   ├── java/com/slotbooker/
│   │   │   ├── SlotBookerApplication.java
│   │   │   ├── auth/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── service/
│   │   │   │   └── security/
│   │   │   ├── provider/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── serviceitem/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── businesshour/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── booking/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── common/
│   │   │   │   ├── exception/
│   │   │   │   └── response/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   │           ├── V1__create_users_table.sql
│   │           ├── V2__create_service_items_table.sql
│   │           ├── V3__create_business_hours_table.sql
│   │           └── V4__create_bookings_table.sql
│   └── test/
│       └── java/com/slotbooker/
│           ├── auth/
│           ├── provider/
│           ├── serviceitem/
│           ├── businesshour/
│           └── booking/
├── .env.example
├── .gitignore
├── compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## Modelo de dados

### User (Prestador)

| Campo | Tipo | Regras |
|---|---|---|
| `id` | UUID | chave primária |
| `name` | varchar(100) | obrigatório |
| `email` | varchar(150) | obrigatório e único |
| `password` | varchar(255) | hash da senha |
| `phone` | varchar(20) | opcional |
| `role` | varchar(30) | `PROVIDER` ou `ADMIN` |
| `created_at` | timestamp | preenchimento automático |
| `updated_at` | timestamp | atualização automática |

### ServiceItem (Serviço ofertado)

| Campo | Tipo | Regras |
|---|---|---|
| `id` | UUID | chave primária |
| `name` | varchar(100) | obrigatório |
| `description` | text | opcional |
| `price` | numeric(10,2) | obrigatório e não negativo |
| `duration_minutes` | integer | obrigatório e maior que zero |
| `provider_id` | UUID | referência ao prestador |
| `active` | boolean | padrão `true` |
| `created_at` | timestamp | preenchimento automático |
| `updated_at` | timestamp | atualização automática |

### BusinessHour (Grade de expediente)

| Campo | Tipo | Regras |
|---|---|---|
| `id` | UUID | chave primária |
| `day_of_week` | varchar(20) | `MONDAY`, `TUESDAY`, etc. |
| `open_time` | time | horário de início |
| `close_time` | time | horário de encerramento |
| `provider_id` | UUID | referência ao prestador |

### Booking (Agendamento)

| Campo | Tipo | Regras |
|---|---|---|
| `id` | UUID | chave primária |
| `client_name` | varchar(100) | obrigatório |
| `client_email` | varchar(150) | obrigatório |
| `client_phone` | varchar(20) | obrigatório |
| `start_time` | timestamp | início do atendimento |
| `end_time` | timestamp | calculado pela duração do serviço |
| `status` | varchar(30) | obrigatório |
| `notes` | text | observações do cliente |
| `service_id` | UUID | referência ao serviço |
| `provider_id` | UUID | referência ao prestador |
| `created_at` | timestamp | preenchimento automático |
| `updated_at` | timestamp | atualização automática |

## Enums

```java
public enum BookingStatus {
    CONFIRMED,
    COMPLETED,
    CANCELED
}
```

```java
public enum Role {
    PROVIDER,
    ADMIN
}
```

## Regras de negócio

1. O e-mail do prestador deve ser único no sistema.
2. A senha nunca deve ser armazenada ou retornada em texto puro.
3. Somente prestadores autenticados podem gerenciar seus serviços, expediente e listagem completa de agendamentos.
4. Um prestador só pode consultar, alterar ou cancelar agendamentos vinculados a ele.
5. A consulta de slots e a criação de agendamento são públicas para permitir reservas sem cadastro prévio de cliente.
6. A duração do agendamento é inferida do `ServiceItem`.
7. O `end_time` é calculado como `start_time + duration_minutes`.
8. Não é permitido criar agendamentos no passado.
9. Não é permitido criar agendamentos fora do expediente configurado.
10. Não pode haver sobreposição de agendamentos para o mesmo prestador, exceto quando o agendamento conflitante estiver `CANCELED`.
11. A criação da reserva deve usar controle transacional para evitar *double-booking* em requisições simultâneas.
12. Um agendamento `COMPLETED` ou `CANCELED` não pode ter seus horários editados.
13. Recursos inexistentes devem resultar em `404 Not Found`.
14. Requisições inválidas devem resultar em `400 Bad Request`.
15. Conflitos de horário devem resultar em `409 Conflict`.

### Regra de sobreposição

Dois intervalos se sobrepõem quando:

```text
existing.startTime < requested.endTime
AND
existing.endTime > requested.startTime
```

Agendamentos com status `CANCELED` não participam da verificação de conflito.

## Endpoints

Prefixo sugerido da API:

```text
/api/v1
```

### Autenticação

#### Registrar prestador

```http
POST /api/v1/auth/register
Content-Type: application/json
```

```json
{
  "name": "Barbearia Vintage",
  "email": "contato@barbearia.com",
  "password": "StrongPassword123",
  "phone": "+5581999998888"
}
```

Resposta esperada:

```http
201 Created
```

#### Autenticar

```http
POST /api/v1/auth/login
Content-Type: application/json
```

```json
{
  "email": "contato@barbearia.com",
  "password": "StrongPassword123"
}
```

Exemplo de resposta:

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer"
}
```

### Perfil do prestador

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/providers/me` | consulta o perfil autenticado |
| `PUT` | `/api/v1/providers/me` | atualiza o perfil autenticado |

### Serviços do prestador

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/services` | cadastra um serviço |
| `GET` | `/api/v1/services` | lista os serviços do prestador |
| `GET` | `/api/v1/services/{id}` | consulta um serviço |
| `PUT` | `/api/v1/services/{id}` | atualiza um serviço |
| `DELETE` | `/api/v1/services/{id}` | desativa ou remove um serviço |

### Grade de expediente

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/business-hours` | cadastra uma faixa de expediente |
| `GET` | `/api/v1/business-hours` | lista o expediente semanal |
| `PUT` | `/api/v1/business-hours/{id}` | atualiza uma faixa |
| `DELETE` | `/api/v1/business-hours/{id}` | remove uma faixa |

### Disponibilidade e agendamentos públicos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/providers/{providerId}/available-slots` | lista os horários livres |
| `POST` | `/api/v1/bookings` | cria uma reserva |

#### Consultar horários livres

```http
GET /api/v1/providers/6f8e77a2-7212-4c22-b5f7-66a7b7a66f01/available-slots?serviceId=c3d2e1b0-4567-489a-bcde-9876543210ab&date=2026-10-15
```

Exemplo de resposta:

```json
{
  "date": "2026-10-15",
  "providerId": "6f8e77a2-7212-4c22-b5f7-66a7b7a66f01",
  "serviceId": "c3d2e1b0-4567-489a-bcde-9876543210ab",
  "slots": [
    "09:00:00",
    "09:45:00",
    "10:30:00",
    "14:00:00",
    "14:45:00"
  ]
}
```

#### Criar agendamento

```http
POST /api/v1/bookings
Content-Type: application/json
```

```json
{
  "providerId": "6f8e77a2-7212-4c22-b5f7-66a7b7a66f01",
  "serviceId": "c3d2e1b0-4567-489a-bcde-9876543210ab",
  "clientName": "Lucas Andrade",
  "clientEmail": "lucas@example.com",
  "clientPhone": "+5581988887777",
  "startTime": "2026-10-15T09:00:00",
  "notes": "Primeira visita"
}
```

Exemplo de resposta:

```json
{
  "id": "7b399ac8-144a-4e2b-a01c-6d8ec5a1b32e",
  "clientName": "Lucas Andrade",
  "clientEmail": "lucas@example.com",
  "clientPhone": "+5581988887777",
  "serviceName": "Corte de Cabelo Clássico",
  "price": 45.00,
  "startTime": "2026-10-15T09:00:00",
  "endTime": "2026-10-15T09:45:00",
  "status": "CONFIRMED",
  "createdAt": "2026-09-19T20:30:00",
  "updatedAt": "2026-09-19T20:30:00"
}
```

### Gestão de agendamentos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/bookings` | lista os agendamentos do prestador |
| `GET` | `/api/v1/bookings/{id}` | consulta os detalhes de uma reserva |
| `PATCH` | `/api/v1/bookings/{id}/status` | altera o status da reserva |

Filtros sugeridos:

```http
GET /api/v1/bookings?startDate=2026-10-01T00:00:00&endDate=2026-10-31T23:59:59&status=CONFIRMED&page=0&size=20&sort=startTime,asc
```

Exemplo de alteração de status:

```http
PATCH /api/v1/bookings/7b399ac8-144a-4e2b-a01c-6d8ec5a1b32e/status
Authorization: Bearer <access-token>
Content-Type: application/json
```

```json
{
  "status": "CANCELED"
}
```

## Segurança

O fluxo inicial utiliza autenticação stateless:

```text
Credenciais
    |
    v
POST /auth/login
    |
    v
Validação do prestador
    |
    v
Emissão do JWT
    |
    v
Authorization: Bearer <token>
    |
    v
SecurityFilterChain
    |
    v
Endpoint protegido
```

### Endpoints públicos

- `/api/v1/auth/**`;
- `GET /api/v1/providers/*/available-slots`;
- `POST /api/v1/bookings`.

### Endpoints protegidos

Os endpoints de perfil, serviços, expediente e gestão de agendamentos exigem um usuário autenticado com o papel adequado.

### Diretrizes

- utilizar `PasswordEncoder` com BCrypt;
- nunca versionar chaves JWT ou senhas;
- receber segredos por variáveis de ambiente;
- configurar a expiração do access token;
- validar assinatura e expiração em cada requisição protegida;
- não registrar senhas ou tokens completos nos logs;
- restringir CORS às origens autorizadas;
- utilizar HTTPS fora do ambiente local.

## Tratamento de erros

As exceções devem ser capturadas e formatadas por um `@RestControllerAdvice`.

Exemplo:

```json
{
  "timestamp": "2026-09-19T20:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "O horário selecionado já foi reservado ou está indisponível",
  "path": "/api/v1/bookings"
}
```

Exceções iniciais:

- `BookingConflictException`, HTTP 409;
- `ServiceNotFoundException`, HTTP 404;
- `BookingNotFoundException`, HTTP 404;
- `OutsideBusinessHoursException`, HTTP 400;
- `EmailAlreadyExistsException`, HTTP 400;
- `InvalidCredentialsException`, HTTP 401;
- `ForbiddenOperationException`, HTTP 403;
- `BusinessException`, HTTP 400.

## Como executar

### Pré-requisitos

- JDK 17;
- Maven 3.9 ou superior, ou Maven Wrapper incluído no projeto;
- Docker e Docker Compose;
- Git.

### 1. Clonar o repositório

```bash
git clone https://github.com/SEU-USUARIO/slotbooker-api.git
cd slotbooker-api
```

### 2. Configurar variáveis de ambiente

Crie o arquivo `.env` a partir do exemplo:

```bash
cp .env.example .env
```

Exemplo:

```env
POSTGRES_DB=slotbooker
POSTGRES_USER=slotbooker
POSTGRES_PASSWORD=slotbooker
JWT_SECRET=replace-with-a-long-random-secret
JWT_EXPIRATION=3600000
```

> O arquivo `.env` não deve ser versionado.

### 3. Subir o PostgreSQL

```bash
docker compose up -d postgres
```

### 4. Executar a aplicação

Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### Executar tudo com Docker Compose

```bash
docker compose up --build
```

Para encerrar:

```bash
docker compose down
```

Para encerrar e remover os volumes locais:

```bash
docker compose down -v
```

## Testes

### Unit tests

Foco inicial:

- cálculo do motor de disponibilidade em `SlotService`;
- validação de sobreposição no `BookingService`;
- transições de status;
- autenticação e regras do `AuthService`;
- operações temporais com `java.time`.

Execução:

```bash
./mvnw test
```

### Integration tests

Foco inicial:

- endpoints REST e validação de payload;
- concorrência e isolamento no PostgreSQL com Testcontainers;
- migrações do Flyway executadas do zero;
- autenticação, geração e validação do JWT;
- respostas HTTP de erro e sucesso.

Execução:

```bash
./mvnw verify
```

### Cenários mínimos

- cadastrar e autenticar um prestador;
- impedir cadastro com e-mail duplicado;
- rejeitar reserva fora do expediente;
- rejeitar reserva no passado;
- calcular slots livres descontando horários reservados;
- impedir *double-booking* por requisições concorrentes;
- retornar uma confirmação e um erro 409 quando duas requisições disputarem o mesmo slot;
- liberar o slot após cancelamento;
- impedir que um prestador acesse ou cancele reservas de outro;
- retornar 404 para recursos inexistentes.

## Documentação da API

Com a aplicação em execução:

```text
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
```

A documentação deve apresentar:

- schemas dos contratos de entrada e saída;
- descrições dos parâmetros de consulta e headers;
- exemplos de requisições e respostas;
- códigos de status HTTP;
- requisitos de autenticação Bearer;
- respostas de erro relevantes.

## Roadmap

### Fase 1: Fundação

- [ ] criar o projeto Spring Boot 3 com Java 17;
- [ ] configurar Maven, dependências e profiles;
- [ ] configurar PostgreSQL;
- [ ] configurar Docker Compose;
- [ ] adicionar Flyway;
- [ ] criar as migrations iniciais.

### Fase 2: Autenticação e prestadores

- [ ] modelar a entity e o repository de `User`;
- [ ] implementar cadastro e login de prestadores;
- [ ] configurar Spring Security;
- [ ] emitir e validar JWT;
- [ ] implementar endpoints de perfil;
- [ ] adicionar testes de autenticação.

### Fase 3: Serviços e expediente

- [ ] criar o CRUD de `ServiceItem`;
- [ ] validar preço e duração;
- [ ] implementar a grade semanal de `BusinessHour`;
- [ ] impedir faixas de expediente inválidas;
- [ ] adicionar testes unitários.

### Fase 4: Motor de agendamento e concorrência

- [ ] desenvolver o algoritmo de geração de slots;
- [ ] implementar a criação pública de reservas;
- [ ] implementar validações temporais;
- [ ] impedir sobreposição de horários;
- [ ] aplicar proteção transacional contra *double-booking*;
- [ ] implementar transições de status.

### Fase 5: Qualidade e confiabilidade

- [ ] centralizar respostas de erro;
- [ ] adicionar integration tests com Testcontainers;
- [ ] testar requisições concorrentes;
- [ ] documentar endpoints com Swagger/OpenAPI;
- [ ] adicionar logs estruturados;
- [ ] configurar GitHub Actions;
- [ ] publicar relatório de cobertura.

### Fase 6: Evoluções

- [ ] adicionar múltiplos profissionais por estabelecimento;
- [ ] integrar pagamentos para cobrança de sinal;
- [ ] enviar lembretes automáticos com RabbitMQ;
- [ ] integrar Google Calendar e Outlook;
- [ ] adicionar fila de espera;
- [ ] adicionar refresh token e revogação via Redis;
- [ ] separar os módulos em microsserviços;
- [ ] adicionar API Gateway;
- [ ] integrar o frontend Angular 17;
- [ ] avaliar micro front-end após o MVP.

## Critérios de conclusão do MVP

O MVP será considerado concluído quando:

- [ ] um prestador puder se cadastrar e autenticar;
- [ ] o prestador puder configurar serviços e expediente;
- [ ] endpoints de gestão exigirem JWT válido;
- [ ] o endpoint público calcular slots disponíveis corretamente;
- [ ] clientes puderem agendar sem sobreposição;
- [ ] duas requisições simultâneas para o mesmo slot gerarem somente uma confirmação;
- [ ] a requisição concorrente rejeitada retornar HTTP 409;
- [ ] o cancelamento liberar imediatamente o horário;
- [ ] dados de prestadores diferentes permanecerem isolados;
- [ ] as migrations criarem o banco do zero;
- [ ] `./mvnw verify` finalizar com sucesso;
- [ ] a aplicação iniciar com Docker Compose;
- [ ] o Swagger UI documentar contratos públicos e privados;
- [ ] nenhuma credencial ou chave JWT estiver exposta no repositório.

## Boas práticas

- injeção de dependência por construtor;
- Java Records para DTOs imutáveis quando apropriado;
- separação entre DTOs e entities JPA;
- uso da API `java.time` para manipulação temporal;
- enums tipados para papéis e estados;
- validação declarativa com Jakarta Bean Validation;
- regras de negócio centralizadas na camada Service;
- controllers pequenos e focados em HTTP;
- migrations versionadas;
- consultas por prestador, status e intervalo de tempo indexadas conforme a necessidade;
- validação de autorização no backend;
- logs sem dados sensíveis;
- commits pequenos e descritivos;
- revisão humana e testes antes de qualquer publicação.

## Convenção de commits

Sugestão baseada em Conventional Commits:

```text
feat: implementa algoritmo gerador de slots disponíveis
fix: corrige validação de sobreposição de horários
test: adiciona teste de concorrência com Testcontainers
docs: adiciona exemplos de agendamento ao README
refactor: migra DTOs de serviço para Java Records
chore: adiciona migration V4 do Flyway
```

## Contribuição

1. Crie uma branch a partir de `main`:

   ```bash
   git checkout -b feature/nome-da-feature
   ```

2. Realize alterações pequenas e com responsabilidade única.
3. Adicione ou atualize os testes.
4. Execute a verificação local:

   ```bash
   ./mvnw verify
   ```

5. Atualize a documentação quando necessário.
6. Abra um Pull Request descrevendo motivação, solução e evidências de teste.

## Referências

### Documentação oficial

- [Spring Boot](https://docs.spring.io/spring-boot/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
- [Jakarta Bean Validation](https://beanvalidation.org/)
- [Springdoc OpenAPI](https://springdoc.org/)
- [PostgreSQL](https://www.postgresql.org/docs/)
- [Flyway](https://documentation.red-gate.com/fd)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html)
- [Testcontainers](https://java.testcontainers.org/)
- [Docker Compose](https://docs.docker.com/compose/)

## Licença

Este projeto está disponível para fins de estudo e portfólio sob a licença MIT.

Consulte o arquivo [`LICENSE`](LICENSE) para mais informações.