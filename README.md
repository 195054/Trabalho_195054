
# Biblioteca Digital

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-red.svg)](https://jakarta.ee/)
[![PrimeFaces](https://img.shields.io/badge/PrimeFaces-13-blue.svg)](https://www.primefaces.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## Autor

**Guilherme Cigognini dos Santos**  
Matrícula: **195054**

---

## Sobre o Projeto

O **Biblioteca Digital** é um sistema web desenvolvido em **Jakarta EE**, utilizando **JSF (Jakarta Faces)** e **PrimeFaces**, projetado para gerenciar de forma prática o acervo de uma biblioteca — incluindo autores, livros e empréstimos.

A aplicação oferece um painel administrativo moderno e responsivo, permitindo o cadastro, consulta, edição e exclusão de registros, além de métricas em tempo real sobre o estado da biblioteca.

---

## Principais Funcionalidades

- Gestão de Autores  
  - Cadastro, edição e exclusão de autores.  
  - Exibição de biografia e número de livros publicados.

- Gestão de Livros  
  - Cadastro completo de livros (título, ISBN, número de páginas, autor, disponibilidade).  
  - Controle de livros disponíveis e emprestados.

- Controle de Empréstimos  
  - Registro de novos empréstimos.  
  - Cálculo automático de atraso e multa.  
  - Devolução e exclusão de empréstimos.

- Dashboard Interativo  
  - Exibe estatísticas de livros, autores e empréstimos.  
  - Navegação rápida entre módulos via botões e menus.

- Interface Moderna (PrimeFaces + CSS3)  
  - Painel visual intuitivo com cards e tabelas interativas.  
  - Layout responsivo e estilizado.

---

## Tecnologias Utilizadas

### Frameworks e APIs
- Jakarta EE 10
- Jakarta Faces (JSF)
- Jakarta Persistence (JPA)
- Jakarta CDI (Contexts and Dependency Injection)
- Jakarta Annotations
- PrimeFaces 13 – componentes de interface modernos

### Banco de Dados
- H2 Database (Desenvolvimento/Testes)  
- MySQL ou PostgreSQL (Produção, configurável via persistence.xml)

### Servidor de Aplicação
- Apache TomEE / Payara / WildFly compatível com Jakarta EE 10

### Ferramentas de Build
- Maven 3.9+ – gerenciamento de dependências e build  
- JDK 21+ – compilação e execução

---

## Arquitetura do Sistema

O sistema segue o padrão MVC (Model-View-Controller), utilizando camadas bem definidas:

```
┌──────────────────────────┐
│        VIEW (JSF)        │ ← Páginas .xhtml + PrimeFaces
├──────────────────────────┤
│    CONTROLLER (Bean)     │ ← Managed Beans @Named e @ViewScoped
├──────────────────────────┤
│    SERVICE (Regras)      │ ← Lógica de negócio e @Transactional
├──────────────────────────┤
│   REPOSITORY (JPA)       │ ← EntityManager + Queries
├──────────────────────────┤
│     ENTITY (Modelo)      │ ← @Entity Autor, Livro, Emprestimo
└──────────────────────────┘
```

---

## Estrutura de Pacotes

```
src/
 └── com.biblioteca
      ├── controller/
      │    └── BibliotecaBean.java
      ├── entity/
      │    ├── Autor.java
      │    ├── Livro.java
      │    └── Emprestimo.java
      ├── repository/
      │    ├── AutorRepository.java
      │    ├── LivroRepository.java
      │    └── EmprestimoRepository.java
      └── service/
           └── BibliotecaService.java
```

E na camada de apresentação:

```
webapp/
 ├── index.xhtml
 ├── autores.xhtml
 ├── livros.xhtml
 ├── emprestimos.xhtml
 └── templates/
      └── template.xhtml
```



## Entidades Principais

| Entidade | Descrição |
|-----------|------------|
| Autor | Representa escritores e mantém relação com seus livros |
| Livro | Contém título, ISBN, número de páginas e disponibilidade |
| Emprestimo | Controla os empréstimos de livros com data e status |

---

## Testes e Debug

### Logs SQL
Ative logs SQL no `persistence.xml`:
```xml
<property name="jakarta.persistence.schema-generation.scripts.action" value="drop-and-create"/>
<property name="jakarta.persistence.jdbc.show_sql" value="true"/>
```

---

## Interface

A aplicação utiliza PrimeFaces com um template base (`template.xhtml`) e páginas:
- `index.xhtml` → Dashboard com métricas e navegação  
- `autores.xhtml` → CRUD completo de autores  
- `livros.xhtml` → Gerenciamento de acervo  
- `emprestimos.xhtml` → Controle de empréstimos ativos  

---

## Créditos

- Desenvolvimento e Arquitetura — Guilherme Cigognini dos Santos  
- Professor Orientador — Prof. Me. Diego Patricio

---

Biblioteca Digital - Sistema de Gerenciamento de Acervo
