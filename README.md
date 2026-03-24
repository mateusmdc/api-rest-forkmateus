docker-compose --profile dev up -d

## 🐳 Docker

Para rodar a aplicação utilizando Docker, é **OBRIGATÓRIO** configurar as variáveis de ambiente antes de iniciar os containers.

> ⚠️ **IMPORTANTE**: O arquivo `.env.docker` é obrigatório. Se as variáveis não forem definidas, o Docker Compose falhará com mensagens de erro indicando quais variáveis estão faltando.

### 1. Configuração (`.env.docker`) - OBRIGATÓRIA

Crie um arquivo chamado `.env.docker` na raiz do projeto (no mesmo nível do `docker-compose.yml`). Copie e cole o conteúdo abaixo:
(caso precise, já existe um arquivo de exemplo dentro projeto chamado '.env.example', na pasta raíz do projeto)

```properties
# Banco de Dados
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/sisreserva?currentSchema=dev
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=admin

# Configuração do Servidor
SERVER_PORT=8080

# Segurança (JWT e CORS)
API_SECURITY_TOKEN_SECRET=123456
API_SECURITY_ACCESS_SECRET=123456
API_SECURITY_REFRESH_SECRET=123456
CORS_ALLOWED_ORIGINS=http://localhost:3000

# E-mail (SMTP)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=andredev.invasao@gmail.com
SPRING_MAIL_PASSWORD=ceuxqdpkkaygfmzl
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true

# Configuração do Container Postgres
POSTGRES_DB=sisreserva
```

### 2. Execução

Após criar e configurar o arquivo `.env.docker` com todos os valores necessários, execute o comando abaixo para compilar a aplicação e subir os containers (API e Banco de Dados):

```bash
docker-compose up --build
```

O Docker irá:
1. **Validar** que todas as variáveis de ambiente obrigatórias estão definidas no `.env`
2. Criar o container do PostgreSQL (`sisreserva-db`)
3. Compilar o projeto Java utilizando Maven (num container multi-stage)
4. Iniciar a API (`sisreserva-api`) na porta especificada
5. **Executar automaticamente** as migrations do Flyway para criar e popular o banco de dados

> 📋 **Migrations Automáticas**: O Flyway está configurado para executar automaticamente:
> - Scripts de migração (`db/migration/V*.sql`) - Criam a estrutura do banco
> - Scripts de dados (`db/data/R*.sql`) - Populam o banco com dados iniciais

### 3. Acesso

- **API:** http://localhost:8080 (ou a porta definida em `SERVER_PORT`)
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **Banco de Dados:** Acessível externamente na porta `5432` (se mapeada) ou internamente via network do docker.

Para parar os serviços:
```bash
docker-compose down
```

### 4. Ambientes Específicos (Teste/Produção)

Para diferentes ambientes, crie arquivos `.env` específicos:

**Ambiente de Teste:**
```bash
# Copie o .env.example e ajuste os valores
cp .env.example .env.test
# Edite .env.test com as configurações de teste
# Execute com:
docker-compose --env-file .env.test up --build
```

**Ambiente de Produção:**
```bash
# Copie o .env.example e ajuste os valores
cp .env.example .env.prod
# Edite .env.prod com as configurações de produção
# Execute com:
docker-compose --env-file .env.prod up --build
```

**Valores importantes para ajustar por ambiente:**
- `SPRING_DATASOURCE_URL`: URL do banco de dados do ambiente
- `CORS_ALLOWED_ORIGINS`: URL do frontend (ex: `https://sisreserva-teste.uece.br`)
- `API_SECURITY_*_SECRET`: Use secrets fortes e únicos para cada ambiente
- `SPRING_MAIL_*`: Configurações SMTP do ambiente

### 5. Troubleshooting

**Erro: "ERROR: variável não definida"**
- Verifique se o arquivo `.env` existe na raiz do projeto
- Confirme que todas as variáveis listadas no `.env.example` estão presentes
- Certifique-se de que não há linhas em branco ou comentários mal formatados

**Migrations não executaram:**
- Verifique os logs do container: `docker logs sisreserva-api`
- Confirme que a URL do banco está correta no `.env`
- O Flyway executa automaticamente na inicialização do Spring Boot

## 💻 Sobre

SisReserva API foi projetada para lidar com os pedidos de Reserva de espaços da UECE.

A API é construída utilizando Spring Boot e incorpora Flyway e Hibernate para o gerenciamento do banco de dados, seguindo as melhores práticas de comunicação com o banco.

## Documentation API

- Inicie a aplicação ao executar ApiSisReservaApplication
- Confira a interface visual da documentação em  http://localhost:8080/swagger-ui/index.html
- Acesse o JSON da documentação em http://localhost:8080/v3/api-docs
- Para buildar e rodar o projeto usando Maven:
```
mvn package
java -jar target/your-project-name.jar
```
Isso irá primeiro compilar o projeto, empacotá-lo em um arquivo JAR e, em seguida, você poderá executar o JAR usando o comando java -jar.
Substitua your-project-name.jar pelo nome real do seu arquivo JAR gerado.

---

## ⚙️ Funcionalidades

- [x] User custom system with different levels of permissions
- [x] Sign In system with JWT authentication
- [x] User Authentication Using a Refresh Token Scheme with User Experience Patterns
- [x] Access audit routines for application security logging
- [x] Cache-based User Management System to Minimize Database Queries for Logged-in Users
- [x] All endpoints mapped in the REST standard
- [x] Security schema implemented in the backend for requests in different layers
- [x] Vulnerability Protection through HTTP Cookies, Rate Limiting Filters, and IP Blocking
- [x] Cadastro de espaços com validação dos dados de entrada
- [x] Listagem de espaços com paginação, ordenação e filtros por atributos específicos
- [x] Cadastro e reativação de gestores de espaço vinculados a espaços específicos
- [x] Inativação de gestores de espaço
- [x] Listagem de gestores de espaço com suporte a paginação, ordenação e múltiplos filtros
- [x] Possibilidade de retornar todos os gestores (ativos e inativos) com flag de controle
- [x] Cadastro de projetos para atribuir a reservas
- [x] Solicitar reservas, com validação de horários conflitantes
- [x] Possibilidade de alterar status da solicitação de reserva para um dos status possíveis (APROVADO, PENDENTE, RECUSADO, PENDENTE_AJUSTE)
- [x] Listagem de horários ocupados de cada dia de um mês específico (para exibição no front)
---

## 🛠 Tecnologias

The following technologies were used in the development of the REST API project:

- **[Java 21](https://www.oracle.com/java)**
- **[Spring Boot 3.5](https://spring.io/projects/spring-boot)**
- **[Maven](https://maven.apache.org)**
- **[Postgresql](https://www.postgresql.org/)**
- **[Hibernate](https://hibernate.org)**
- **[Flyway](https://flywaydb.org)**
- **[Lombok](https://projectlombok.org)**
- **[JWT](https://jwt.io/)**
- **[Bucket4j](https://bucket4j.com/)**