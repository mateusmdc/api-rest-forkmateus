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