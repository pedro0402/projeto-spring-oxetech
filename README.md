# Projeto Base — API com Spring Boot + JWT

## Funcionalidades

- Autenticação com JWT
- Controle de acesso por perfis (roles)
- Criptografia de senhas com BCrypt
- Cadastro e login de usuários
- Estrutura de DTOs, services, controllers e repositórios

## Tecnologias

- Java 21
- Maven
- Docker (PostgreSQL + Pgadmin)
- Spring Boot
- Postman
- JUnit

## Setup

1. Clone o repositório
2. Rode o banco com:
```bash  
docker compose up -d
```
3. Rode o projeto com:
```bash  
./mvnw spring-boot:run
```
---  
**Sistema de Gestão de Usuários e Relatórios**
   - Roles: ADMIN, GERENTE, COLABORADOR
   - Permissões: visualizar, editar, controlar acesso a relatórios
   - UML:
   - ![proj1uml.png](UMLs/proj1uml.png)
  ---
---
