# CritiqueHub Platform

[![Quarkus](https://img.shields.io/badge/Quarkus-3.6.0-blue.svg)](https://quarkus.io)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-red.svg)](https://jakarta.ee/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📖 Sobre o Projeto

O **CritiqueHub** é uma plataforma social de descoberta cultural onde usuários podem registrar, avaliar e compartilhar suas experiências com filmes, livros e jogos. A plataforma combina elementos sociais com algoritmos inteligentes de recomendação, construída com as melhores práticas do desenvolvimento web enterprise usando Jakarta EE e Quarkus.

### 🎯 Principais Funcionalidades

- **Gestão de Usuários**: Cadastro, autenticação e perfis personalizados
- **Catálogo Cultural**: Base abrangente de filmes, livros e jogos
- **Sistema de Avaliações**: Notas de 1-10 com resenhas detalhadas
- **Recomendações Inteligentes**: Algoritmos baseados em similaridade de usuários
- **Rede Social**: Sistema de seguir usuários e feed de atividades
- **Listas Personalizadas**: Coleções customizadas de itens culturais

## 🚀 Tecnologias Utilizadas

### Core Framework
- **[Quarkus](https://quarkus.io/)** - Framework Java supersônico e subatômico
- **[Jakarta EE 10](https://jakarta.ee/)** - Especificações enterprise padrão

### Principais Extensões
- **Jakarta REST (JAX-RS)** - APIs RESTful
- **Jakarta Persistence (JPA)** - Mapeamento objeto-relacional
- **Jakarta CDI** - Injeção de dependência e contextos
- **Jakarta Bean Validation** - Validação de dados
- **Hibernate ORM** - Implementação JPA

### Banco de Dados
- **PostgreSQL** - Produção
- **H2 Database** - Desenvolvimento e testes

### Ferramentas de Build
- **Maven 3.9+** - Gerenciamento de dependências e build
- **Docker** - Containerização

## 🏗️ Arquitetura

A aplicação segue uma arquitetura em camadas bem definida:

```
┌─────────────────┐
│   REST Layer    │  ← Endpoints JAX-RS
├─────────────────┤
│  Service Layer  │  ← Lógica de negócio + @Transactional
├─────────────────┤
│Repository Layer │  ← Acesso aos dados + JPA
├─────────────────┤
│  Entity Layer   │  ← Modelo de domínio + Anotações JPA
└─────────────────┘
```


### Principais Entidades
- **Usuario** - Usuários da plataforma
- **ItemCultural** - Classe base para filmes, livros e jogos
- **Avaliacao** - Avaliações e resenhas dos usuários
- **Genero** - Categorização de itens culturais

## ⚡ Quick Start

### Pré-requisitos
- JDK 21+
- Maven 3.9+
- Docker (opcional, para banco de dados)

### 1. Clone o Repositório
```shell script
git clone https://github.com/diegopatr/critiquehub.git
cd critiquehub
```


### 2. Execute com Banco H2 (Desenvolvimento)
```shell script
# Modo desenvolvimento com live coding
./mvnw quarkus:dev
```

A aplicação estará disponível em `http://localhost:8080`

### 3. Execute com PostgreSQL (Produção)
```shell script
# Subir banco PostgreSQL com Docker
docker-compose up -d postgres

# Executar aplicação em modo produção
./mvnw quarkus:dev -Dquarkus.profile=prod
```


### 4. Acessar Interfaces de Desenvolvimento
- **Aplicação**: http://localhost:8080
- **Dev UI**: http://localhost:8080/q/dev-ui
- **Health Check**: http://localhost:8080/q/health
- **OpenAPI/Swagger**: http://localhost:8080/q/swagger-ui

## 🔧 Configuração

### Perfis de Ambiente

#### Desenvolvimento (dev)
```yaml
quarkus:
  datasource:
    db-kind: h2
    jdbc.url: jdbc:h2:mem:critiquehub
  hibernate-orm:
    database.generation: drop-and-create
    log.sql: true
```


#### Teste (test)
```yaml
quarkus:
  datasource:
    db-kind: h2
    jdbc.url: jdbc:h2:mem:test-critiquehub
  hibernate-orm:
    database.generation: drop-and-create
```


#### Produção (prod)
```yaml
quarkus:
  datasource:
    db-kind: postgresql
    jdbc.url: jdbc:postgresql://localhost:5432/critiquehub
  hibernate-orm:
    database.generation: validate
```


### Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_PORT` | Porta do PostgreSQL | `5432` |
| `DB_NAME` | Nome do banco | `critiquehub` |
| `DB_PASSWORD` | Senha do banco | `prodpassword` |

## 🧪 Testes

### Executar Testes Unitários
```shell script
./mvnw test
```


### Executar Testes de Integração
```shell script
./mvnw integration-test
```


### Testes com Testcontainers
Os testes de integração utilizam Testcontainers para garantir ambiente isolado:
```shell script
# Testes automaticamente sobem containers PostgreSQL
./mvnw verify
```


## 📊 Monitoramento

### Health Checks
```shell script
curl http://localhost:8080/q/health
```


### Métricas (Micrometer)
```shell script
curl http://localhost:8080/q/metrics
```


## 🐳 Docker

### Build da Imagem
```shell script
./mvnw clean package -Dquarkus.package.type=uber-jar
docker build -f src/main/docker/Dockerfile.jvm -t critiquehub/platform .
```


### Executar Container
```shell script
docker run -i --rm -p 8080:8080 critiquehub/platform
```


### Compilação Nativa (GraalVM)
```shell script
./mvnw package -Dnative
docker build -f src/main/docker/Dockerfile.native -t critiquehub/platform-native .
```


## 🚀 Deploy

### Desenvolvimento Local
```shell script
./mvnw quarkus:dev
```


### Produção
```shell script
java -jar target/quarkus-app/quarkus-run.jar
```


### Native Binary
```shell script
./target/critiquehub-platform-1.0.0-SNAPSHOT-runner
```


## 📚 API Documentation

Com a aplicação rodando, acesse:
- **Swagger UI**: http://localhost:8080/q/swagger-ui
- **OpenAPI Spec**: http://localhost:8080/q/openapi

### Principais Endpoints

```
GET    /usuarios              # Listar usuários
POST   /usuarios              # Criar usuário
GET    /usuarios/{id}         # Buscar usuário
PUT    /usuarios/{id}         # Atualizar usuário

GET    /itens-culturais       # Listar itens
POST   /filmes                # Criar filme
POST   /livros                # Criar livro
POST   /jogos                 # Criar jogo

GET    /avaliacoes            # Listar avaliações
POST   /avaliacoes            # Criar avaliação
```


## 🤝 Contribuição

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📈 Performance

### Métricas Típicas (JVM Mode)
- **Startup**: ~2 segundos
- **Memory**: ~150MB RSS
- **Throughput**: ~20k req/s

### Métricas Típicas (Native Mode)
- **Startup**: ~0.05 segundos
- **Memory**: ~30MB RSS
- **Throughput**: ~25k req/s

## 🔍 Troubleshooting

### Problemas Comuns

**Erro de Conexão com Banco**
```shell script
# Verificar se PostgreSQL está rodando
docker-compose ps

# Ver logs do container
docker-compose logs postgres
```


**Porta 8080 já em uso**
```shell script
# Mudar porta da aplicação
./mvnw quarkus:dev -Dquarkus.http.port=8081
```


**Problemas com Native Build**
```shell script
# Instalar GraalVM
sdk install java 21.0.1-graal
sdk use java 21.0.1-graal
```


## 🤖 Uso de IA Generativa

### Transparência no Desenvolvimento

Este projeto foi desenvolvido com o auxílio de **Inteligência Artificial Generativa** para fins educacionais e de produtividade. O uso de IA foi aplicado nas seguintes áreas:

#### ✅ Uso Apropriado de IA
- **Geração de código boilerplate**: Entidades JPA, métodos CRUD básicos, configurações Maven/Gradle
- **Documentação técnica**: JavaDoc, comentários de código, documentação de APIs
- **Exemplos e templates**: Estruturas de projeto, arquivos de configuração, scripts de deploy
- **Revisão e otimização**: Identificação de melhores práticas, sugestões de refatoração
- **Resolução de problemas**: Debugging, análise de logs, troubleshooting

#### 🎯 Valor Educacional Mantido
- **Arquitetura e design**: Decisões arquiteturais tomadas com entendimento humano
- **Lógica de negócio**: Regras específicas do domínio desenvolvidas com compreensão contextual
- **Integração de conceitos**: Aplicação prática dos princípios de Jakarta EE e Quarkus
- **Resolução criativa**: Soluções inovadoras para desafios específicos do projeto

#### 📚 Processo de Aprendizagem
O uso de IA **não substitui** o entendimento dos conceitos fundamentais, mas serve como:
- **Acelerador de produtividade** para tarefas repetitivas
- **Fonte de referência** para melhores práticas da indústria
- **Assistente de documentação** para manter código bem documentado
- **Ferramenta de revisão** para identificar possíveis melhorias

#### ⚠️ Limitações e Considerações
- **Validação humana**: Todo código gerado por IA foi revisado e validado
- **Contexto específico**: Adaptações foram feitas para atender requisitos específicos do projeto
- **Responsabilidade**: A responsabilidade final pelo código e suas implicações permanece humana
- **Evolução**: O projeto pode evoluir além das sugestões iniciais de IA conforme necessidades emergem

### 🎓 Recomendações para Estudantes

Se você está utilizando este projeto para aprendizado:

1. **Entenda antes de usar**: Não copie código sem compreender sua função
2. **Experimente variações**: Modifique implementações para testar seu entendimento
3. **Questione decisões**: Analise por que certas abordagens foram escolhidas
4. **Implemente do zero**: Tente recriar partes do projeto sem consultar o código existente

### 🔧 Ferramentas de IA Utilizadas

- **GitHub Copilot** - Assistência na escrita de código
- **Claude 3.5** - Revisão de arquitetura e documentação
- **ChatGPT** - Geração de exemplos e troubleshooting

> **Nota**: O uso transparente de IA visa demonstrar como essas ferramentas podem ser integradas produtivamente no desenvolvimento de software, mantendo qualidade, aprendizado e responsabilidade profissional.

## 📝 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Autores

- **Prof. Me. Diego Patricio** - *Arquitetura e Design* - [@diegopatr](https://github.com/diegopatr)

## 🙏 Agradecimentos

- [Quarkus Community](https://quarkus.io/community/) - Framework incrível
- [Jakarta EE](https://jakarta.ee/) - Especificações robustas
- [Red Hat](https://redhat.com/) - Patrocinador do Quarkus
- Comunidade de desenvolvedores que contribui com feedback e melhorias

---

**CritiqueHub** - Descubra, Avalie, Compartilhe 🎬📚🎮
