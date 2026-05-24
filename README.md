# Tech do Bem — Backend Java (Sprint 4)

API **REST** em **Java 21 + Quarkus** que serve o domínio da plataforma **Tech do Bem**, conectando-se ao Oracle FIAP via JDBC e expondo todos os recursos consumidos pelo frontend React. Entrega da disciplina **Domain Driven Design Using Java** — 1TDSPR / FIAP.

## Integrantes

| RM       | Nome                          |
|----------|-------------------------------|
| RM568542 | Hugo Souza de Jesus           |
| RM566815 | Lucas Campanhã dos Santos     |
| RM567010 | Lucas Marcelino Pompeu        |

## Conteúdo do repositório

| Caminho | Descrição |
|---|---|
| `src/main/java/br/com/fiap/entities/` | 9 classes modelo (Colaborador, Dentista, Paciente, Campanha, Atendimento, Exame, Notificacao, Solicitacao, Anotacao) |
| `src/main/java/br/com/fiap/dao/` | 13 DAOs JDBC com CRUD completo, incluindo associativas |
| `src/main/java/br/com/fiap/bo/` | 12 BOs com regras de negócio + `SqlConflictDetector` |
| `src/main/java/br/com/fiap/dto/` | DTOs (`record`) de Request / Response |
| `src/main/java/br/com/fiap/resource/` | 12 endpoints REST (JAX-RS) |
| `src/main/java/br/com/fiap/conexoes/` | `ConexaoFactory` para o Oracle FIAP |
| `src/main/java/br/com/fiap/exceptions/` | `DadoInvalidoException`, `PersistenciaException` |
| `src/main/resources/seed/rebuild.sql` | Script de rebuild executado pelo endpoint admin |
| `pom.xml` | Quarkus 3.15.1 + Oracle JDBC 23.26 + Jackson |
| `Dockerfile` | Build multi-stage com Eclipse Temurin 21 |
| `DOCUMENTACAO.md` | Documentação atendendo à rubrica da Sprint 4 |
| `README.md` | Este arquivo |

## Sobre o projeto

A **Tech do Bem** é uma ONG que oferta atendimento odontológico gratuito a populações em vulnerabilidade. Este módulo Java implementa o backend principal da plataforma de gestão: autenticação, CRUD completo de todos os recursos do domínio, regras de negócio (aprovação de solicitações externas → criação de colaborador, notificações com dois fluxos, anotações polimórficas), além de endpoints especiais para reconstrução do banco (`/api/admin/rebuild`) e relatórios consolidados (`/api/relatorios`).

O sistema completo é composto por quatro repositórios:

- [`Tech-do-Bem-FIAP/Backend_Java`](https://github.com/Tech-do-Bem-FIAP/Backend_Java) — este repositório (API Quarkus)
- [`Tech-do-Bem-FIAP/Frontend`](https://github.com/Tech-do-Bem-FIAP/Frontend) — SPA React + Vite + TypeScript
- [`Tech-do-Bem-FIAP/Backend_AI`](https://github.com/Tech-do-Bem-FIAP/Backend_AI) — API Flask com modelo preditivo
- [`Tech-do-Bem-FIAP/Database`](https://github.com/Tech-do-Bem-FIAP/Database) — schema e seed Oracle

## Como executar

### Pré-requisitos

- **JDK 21** (Eclipse Temurin recomendado).
- **Maven 3.9+** (ou usar `./mvnw` se incluído).
- Acesso ao **Oracle FIAP** (`oracle.fiap.com.br:1521/orcl`) com seu RM.

### Local (modo dev Quarkus)

```bash
git clone https://github.com/Tech-do-Bem-FIAP/Backend_Java.git
cd Backend_Java

# credenciais Oracle como variáveis de ambiente
export DB_USER=RM568542
export DB_PASSWORD=080206

# modo dev com hot-reload
mvn quarkus:dev
```

A API sobe em `http://localhost:8080`. Quarkus Dev UI em `http://localhost:8080/q/dev`.

### Produção (uber-jar)

```bash
mvn package -DskipTests
java -jar target/quarkus-app/quarkus-run.jar
```

### Docker

```bash
docker build -t backend-java .
docker run -p 8080:8080 \
  -e DB_USER=RM568542 \
  -e DB_PASSWORD=080206 \
  backend-java
```

### Teste rápido

```bash
curl http://localhost:8080/api/dentistas
curl -X POST http://localhost:8080/api/login \
     -H 'Content-Type: application/json' \
     -d '{"email":"admin@admin.com","senha":"admin"}'
```

## Atendimento à rubrica Sprint 4 (Domain Driven Design Using Java)

| Critério                                          | Pontuação | Localização                                            |
|---------------------------------------------------|-----------|--------------------------------------------------------|
| Documentação (capa, escopo, endpoints, MER, classes, execução) | 15 pts | `DOCUMENTACAO.md`                                      |
| Camada Modelo (entities/beans, mínimo 6)          | 10 pts    | `src/main/java/br/com/fiap/entities/` (9 classes)      |
| Conexão BD + métodos de lógica (mínimo 4)         | 30 pts    | `conexoes/ConexaoFactory.java` + `bo/*BO.java` (12 BOs)|
| Exceções, DAO e BO — CRUD completo                | 10 pts    | `exceptions/`, `dao/` (13), `bo/` (12)                 |
| API Restful (todos os endpoints, princípios REST) | 35 pts    | `resource/` (12 resources, ~45 endpoints)              |
| **Total**                                         | **100**   | **Pontuação máxima esperada**                          |

Detalhes em [`DOCUMENTACAO.md`](DOCUMENTACAO.md).
