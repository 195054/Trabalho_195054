package com.biblioteca.controller;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.BibliotecaService;
import jakarta.annotation.PostConstruct;
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

    private List<Autor> autores = Collections.emptyList();
    private List<Livro> livros = Collections.emptyList();
    private List<Emprestimo> emprestimosAtivos = Collections.emptyList();

    private long totalLivros;
    private long livrosDisponiveis;
    private long emprestimosAtivosCount;
    private long totalAutores;

    private Autor novoAutor = new Autor();
    private Livro novoLivro = new Livro();
    private Emprestimo novoEmprestimo = new Emprestimo(); // ✅ NOVO
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

    /* ===================== AUTORES ===================== */
    public void novoAutorDialog() {
        novoAutor = new Autor();
    }

    public void editarAutor(Autor autor) {
        this.novoAutor = autor;
    }

    public void salvarAutor() {
        try {
            if (novoAutor.getNome() == null || novoAutor.getNome().isBlank()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe o nome do autor!", null));
                return;
            }

            service.salvarAutor(novoAutor);
            autores = service.listarTodosAutores();
            totalAutores = autores.size();
            novoAutor = new Autor();

            org.primefaces.PrimeFaces.current().ajax().update("formPrincipal");

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Autor salvo com sucesso!", null));

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar autor: " + e.getMessage(), null));
        }
    }

    public void excluirAutor(Long id) {
        service.excluirAutor(id);
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== LIVROS ===================== */
    public void novoLivroDialog() {
        novoLivro = new Livro();
        idAutorSelecionado = null;
    }

    public void editarLivro(Livro livro) {
        this.novoLivro = livro;
        this.idAutorSelecionado = (livro.getAutor() != null) ? livro.getAutor().getId() : null;
    }

    public void salvarLivro() {
        if (idAutorSelecionado == null || novoLivro.getTitulo() == null || novoLivro.getTitulo().isBlank()) return;
        service.salvarLivro(novoLivro, idAutorSelecionado);
        novoLivro = new Livro();
        idAutorSelecionado = null;
        carregarDados();
        carregarEstatisticas();
    }

    public void excluirLivro(Long id) {
        service.excluirLivro(id);
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== EMPRÉSTIMOS ===================== */
    public void novoEmprestimoDialog() {
        novoEmprestimo = new Emprestimo();
        novoEmprestimo.setDataEmprestimo(LocalDate.now());
    }

    public void salvarEmprestimo() {
        try {
            if (novoEmprestimo.getNomeUsuario() == null || novoEmprestimo.getNomeUsuario().isBlank()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe o nome do usuário!", null));
                return;
            }
            if (novoEmprestimo.getLivro() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione um livro!", null));
                return;
            }

            service.salvarEmprestimo(novoEmprestimo);
            emprestimosAtivos = service.listarEmprestimosAtivos();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Empréstimo salvo com sucesso!", null));

            org.primefaces.PrimeFaces.current().ajax().update("formEmprestimos");
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar empréstimo: " + e.getMessage(), null));
        }
    }

    public void editarEmprestimo(Emprestimo e) {
        this.novoEmprestimo = e;
    }

    public void devolverEmprestimo(Emprestimo e) {
        e.setDataDevolucao(LocalDate.now());
        carregarDados();
        carregarEstatisticas();
    }

    public void excluirEmprestimo(Long id) {
        service.excluirEmprestimo(id);
        carregarDados();
        carregarEstatisticas();
    }

    /* ===================== OUTROS MÉTODOS ===================== */
    public void recarregarDados() {
        init();
    }

    public List<Livro> getLivrosFiltrados() {
        if (filtro == null || filtro.isBlank()) return livros;
        String f = filtro.toLowerCase();
        return livros.stream()
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

    /* ===================== GETTERS E SETTERS ===================== */
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

    public Emprestimo getNovoEmprestimo() { return novoEmprestimo; } // ✅ NOVO
    public void setNovoEmprestimo(Emprestimo novoEmprestimo) { this.novoEmprestimo = novoEmprestimo; } // ✅ NOVO

    public Long getIdAutorSelecionado() { return idAutorSelecionado; }
    public void setIdAutorSelecionado(Long idAutorSelecionado) { this.idAutorSelecionado = idAutorSelecionado; }

    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }
}
