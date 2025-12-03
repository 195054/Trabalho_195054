package com.biblioteca.controller;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.BibliotecaService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named("bibliotecaBean")
@ViewScoped
public class BibliotecaBean implements Serializable {

    @Inject
    private BibliotecaService service;

    @Inject
    private SecurityIdentity securityIdentity;

    private List<Autor> autores = Collections.emptyList();
    private List<Livro> livros = Collections.emptyList();
    private List<Emprestimo> emprestimosAtivos = Collections.emptyList();

    private long totalLivros;
    private long livrosDisponiveis;
    private long emprestimosAtivosCount;
    private long totalAutores;

    private Autor novoAutor = new Autor();
    private Livro novoLivro = new Livro();
    private Emprestimo novoEmprestimo = new Emprestimo();
    private Long idAutorSelecionado;
    private String filtro = "";

    @PostConstruct
    public void init() {
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== DADOS E ESTATÍSTICAS ===================== */

    public void carregarDados() {
        try {
            autores = service.listarTodosAutores();
            livros = service.listarTodosLivros();
            emprestimosAtivos = service.listarEmprestimosAtivos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carregarEstatisticas() {
        try {
            totalLivros = service.contarTotalLivros();
            livrosDisponiveis = service.contarLivrosDisponiveis();
            emprestimosAtivosCount = service.contarEmprestimosAtivos();
            totalAutores = service.contarTotalAutores();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== AUTORES (ADMIN) ===================== */

    @RolesAllowed("ADMIN")
    public void novoAutorDialog() {
        novoAutor = new Autor();
    }

    @RolesAllowed("ADMIN")
    public void editarAutor(Autor autor) {
        this.novoAutor = autor;
    }

    @RolesAllowed("ADMIN")
    public void salvarAutor() {
        try {
            if (novoAutor.getNome() == null || novoAutor.getNome().isBlank()) {
                adicionarMensagem(FacesMessage.SEVERITY_WARN, "Validação", "Informe o nome do autor.");
                return;
            }

            service.salvarAutor(novoAutor);
            autores = service.listarTodosAutores();
            totalAutores = autores.size();
            novoAutor = new Autor();

            org.primefaces.PrimeFaces.current().ajax().update("formPrincipal");

            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Autor salvo com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar autor: " + e.getMessage());
        }
    }

    @RolesAllowed("ADMIN")
    public void excluirAutor(Long id) {
        service.excluirAutor(id);
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== LIVROS (ADMIN) ===================== */

    @RolesAllowed("ADMIN")
    public void novoLivroDialog() {
        novoLivro = new Livro();
        novoLivro.setDisponivel(Boolean.TRUE);
        idAutorSelecionado = null;
    }

    @RolesAllowed("ADMIN")
    public void editarLivro(Livro livro) {
        this.novoLivro = livro;
        this.idAutorSelecionado = (livro.getAutor() != null) ? livro.getAutor().getId() : null;
    }

    @RolesAllowed("ADMIN")
    public void salvarLivro() {
        if (idAutorSelecionado == null || novoLivro.getTitulo() == null || novoLivro.getTitulo().isBlank()) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Validação", "Informe título e autor do livro.");
            return;
        }
        service.salvarLivro(novoLivro, idAutorSelecionado);

        novoLivro = new Livro();
        idAutorSelecionado = null;

        carregarDados();
        carregarEstatisticas();
    }

    @RolesAllowed("ADMIN")
    public void excluirLivro(Long id) {
        service.excluirLivro(id);
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== EMPRÉSTIMOS (ADMIN) ===================== */

    @RolesAllowed("ADMIN")
    public void novoEmprestimoDialog() {
        novoEmprestimo = new Emprestimo();
        novoEmprestimo.setDataEmprestimo(LocalDate.now());
    }

    @RolesAllowed("ADMIN")
    public void salvarEmprestimo() {
        try {
            if (novoEmprestimo.getNomeUsuario() == null || novoEmprestimo.getNomeUsuario().isBlank()) {
                adicionarMensagem(FacesMessage.SEVERITY_WARN, "Validação", "Informe o nome do usuário.");
                return;
            }
            if (novoEmprestimo.getLivro() == null) {
                adicionarMensagem(FacesMessage.SEVERITY_WARN, "Validação", "Selecione um livro.");
                return;
            }

            if (novoEmprestimo.getStatus() == null) {
                novoEmprestimo.setStatus("APROVADO");
            }

            service.salvarEmprestimo(novoEmprestimo);
            emprestimosAtivos = service.listarEmprestimosAtivos();

            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Empréstimo salvo com sucesso.");
            org.primefaces.PrimeFaces.current().ajax().update("formEmprestimos");
        } catch (Exception e) {
            e.printStackTrace();
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar empréstimo: " + e.getMessage());
        }
    }

    @RolesAllowed("ADMIN")
    public void aprovarEmprestimo(Emprestimo emprestimo) {
        service.aprovarEmprestimo(emprestimo.getId(), LocalDate.now().plusDays(7));
        carregarDados();
        carregarEstatisticas();
        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Empréstimo aprovado com sucesso.");
    }

    @RolesAllowed("ADMIN")
    public void rejeitarEmprestimo(Emprestimo emprestimo) {
        service.rejeitarEmprestimo(emprestimo.getId());
        carregarDados();
        carregarEstatisticas();
        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Rejeitado", "Solicitação de empréstimo rejeitada com sucesso.");
    }

    @RolesAllowed("ADMIN")
    public void devolverEmprestimo(Emprestimo emprestimo) {
        service.registrarDevolucao(emprestimo.getId());
        carregarDados();
        carregarEstatisticas();
    }

    @RolesAllowed("ADMIN")
    public void excluirEmprestimo(Long id) {
        service.excluirEmprestimo(id);
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== PROTEÇÃO DE PÁGINA ADMIN ===================== */

    @RolesAllowed("ADMIN")
    public void verificarAcessoAdmin() {
        // Se não for ADMIN, o Quarkus automaticamente retorna 403.
        // Se não estiver logado, o Quarkus redireciona para o login.
    }

    /* ===================== USER: SOLICITAÇÃO DE EMPRÉSTIMO ===================== */

    @RolesAllowed("USER")
    public void emprestarLivro(Livro livro) {

        if (livro == null || Boolean.FALSE.equals(livro.getDisponivel())) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Indisponível", "Livro indisponível para empréstimo.");
            return;
        }

        try {
            String username = getUsuarioLogado();
            String nome = username;
            String email = username + "@biblioteca.com";

            var usuario = service.buscarUsuarioPorUsername(username);
            if (usuario != null) {
                nome = usuario.getNome();
                email = usuario.getEmail();
            }

            Emprestimo emprestimo = new Emprestimo(nome, email, livro);
            emprestimo.setStatus("PENDENTE");
            service.salvarEmprestimo(emprestimo);

            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Solicitação registrada",
                    "Empréstimo solicitado para " + nome + ". Aguarde aprovação do bibliotecário.");
            carregarDados();
            carregarEstatisticas();
        } catch (Exception e) {
            e.printStackTrace();
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao solicitar empréstimo: " + e.getMessage());
        }
    }

    /* ===================== SEGURANÇA ===================== */

    public String getUsuarioLogado() {
        if (securityIdentity == null || securityIdentity.isAnonymous()) {
            return "Anônimo";
        }
        return securityIdentity.getPrincipal().getName();
    }

    public boolean isAdmin() {
        return securityIdentity != null && securityIdentity.getRoles().contains("ADMIN");
    }

    public boolean isUser() {
        return securityIdentity != null && securityIdentity.getRoles().contains("USER");
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    /* ===================== AUXILIARES ===================== */

    public List<Livro> getLivrosFiltrados() {
        List<Livro> base = livros;
        if (isUser()) {
            base = livros.stream()
                    .filter(l -> Boolean.TRUE.equals(l.getDisponivel()))
                    .collect(Collectors.toList());
        }
        if (filtro == null || filtro.isBlank()) return base;
        String f = filtro.toLowerCase();
        return base.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(f)
                        || (l.getAutor() != null && l.getAutor().getNome().toLowerCase().contains(f)))
                .collect(Collectors.toList());
    }

    public List<Emprestimo> getEmprestimosAtrasados() {
        return emprestimosAtivos.stream()
                .filter(e -> e.isAtivo() && e.getDataDevolucaoPrevista() != null
                        && e.getDataDevolucaoPrevista().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public long getDiasAtraso(Emprestimo e) {
        if (e.getDataDevolucaoPrevista() == null || !e.isAtivo()) return 0;
        long dias = ChronoUnit.DAYS.between(e.getDataDevolucaoPrevista(), LocalDate.now());
        return Math.max(dias, 0);
    }

    public double getMulta(Emprestimo e) {
        return getDiasAtraso(e) * 2.5;
    }

    private void adicionarMensagem(FacesMessage.Severity severity, String resumo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, resumo, detalhe));
    }


    public List<Autor> getAutores() { return autores; }
    public List<Livro> getLivros() { return livros; }
    public List<Emprestimo> getEmprestimosAtivos() { return emprestimosAtivos; }

    public long getTotalLivros() { return totalLivros; }
    public long getLivrosDisponiveis() { return livrosDisponiveis; }
    public long getEmprestimosAtivosCount() { return emprestimosAtivosCount; }
    public long getTotalAutores() { return totalAutores; }

    public Autor getNovoAutor() { return novoAutor; }
    public void setNovoAutor(Autor novoAutor) { this.novoAutor = novoAutor; }

    public Livro getNovoLivro() { return novoLivro; }
    public void setNovoLivro(Livro novoLivro) { this.novoLivro = novoLivro; }

    public Emprestimo getNovoEmprestimo() { return novoEmprestimo; }
    public void setNovoEmprestimo(Emprestimo novoEmprestimo) { this.novoEmprestimo = novoEmprestimo; }

    public Long getIdAutorSelecionado() { return idAutorSelecionado; }
    public void setIdAutorSelecionado(Long idAutorSelecionado) { this.idAutorSelecionado = idAutorSelecionado; }

    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }
}
