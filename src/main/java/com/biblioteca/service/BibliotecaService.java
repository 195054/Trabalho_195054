package com.biblioteca.service;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BibliotecaService {

    @Inject
    AutorRepository autorRepository;

    @Inject
    LivroRepository livroRepository;

    @Inject
    EmprestimoRepository emprestimoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    /* ===================== AUTORES ===================== */

    public List<Autor> listarTodosAutores() {
        return autorRepository.findAll();
    }

    @Transactional
    public void salvarAutor(Autor autor) {
        autorRepository.persistir(autor);
    }

    @Transactional
    public void excluirAutor(Long id) {
        autorRepository.excluirPorId(id);
    }

    public long contarTotalAutores() {
        return autorRepository.count();
    }

    /* ===================== LIVROS ===================== */

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    @Transactional
    public void salvarLivro(Livro livro, Long idAutor) {
        if (livro == null || idAutor == null) return;
        Autor autor = autorRepository.buscarPorId(idAutor);
        livro.setAutor(autor);
        if (livro.getDisponivel() == null) {
            livro.setDisponivel(Boolean.TRUE);
        }
        livroRepository.persistir(livro);
    }

    @Transactional
    public void excluirLivro(Long id) {
        livroRepository.excluirPorId(id);
    }

    public long contarTotalLivros() {
        return livroRepository.count();
    }

    public long contarLivrosDisponiveis() {
        return livroRepository.countDisponiveis();
    }

    /* ===================== EMPRÉSTIMOS ===================== */

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findAtivos();
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return emprestimoRepository.findAll();
    }

    public long contarEmprestimosAtivos() {
        return emprestimoRepository.countAtivos();
    }

    @Transactional
    public void salvarEmprestimo(Emprestimo novoEmprestimo) {
        if (novoEmprestimo == null || novoEmprestimo.getLivro() == null) {
            return;
        }
        Livro livro = novoEmprestimo.getLivro();
        // qualquer empréstimo (pendente ou aprovado) já marca o livro como indisponível
        livro.setDisponivel(false);
        livroRepository.persistir(livro);
        emprestimoRepository.save(novoEmprestimo);
    }

    @Transactional
    public void aprovarEmprestimo(Long id, LocalDate dataDevolucaoPrevista) {
        if (id == null) return;
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo == null) return;

        emprestimo.setStatus("APROVADO");
        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }
        if (dataDevolucaoPrevista != null) {
            emprestimo.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        }

        Livro livro = emprestimo.getLivro();
        if (livro != null) {
            livro.setDisponivel(false);
            livroRepository.persistir(livro);
        }

        emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public void registrarDevolucao(Long id) {
        if (id == null) return;
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo == null) return;

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus("DEVOLVIDO");

        Livro livro = emprestimo.getLivro();
        if (livro != null) {
            livro.setDisponivel(true);
            livroRepository.persistir(livro);
        }

        emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public void excluirEmprestimo(Long id) {
        if (id == null) return;
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo != null) {
            Livro livro = emprestimo.getLivro();
            if (livro != null && !emprestimo.isAtivo()) {
                // se já devolvido, garantir que livro esteja disponível
                livro.setDisponivel(true);
                livroRepository.persistir(livro);
            }
            emprestimoRepository.excluir(emprestimo);
        }
    }

    @Transactional
    public void rejeitarEmprestimo(Long id) {
        if (id == null) return;
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo != null) {
            Livro livro = emprestimo.getLivro();
            if (livro != null) {
                livro.setDisponivel(true);
                livroRepository.persistir(livro);
            }
            emprestimoRepository.excluir(emprestimo);
        }
    }

    /* ===================== USUÁRIOS ===================== */

    public Usuario buscarUsuarioPorUsername(String username) {
        if (username == null) return null;
        return usuarioRepository.buscarPorUsername(username);
    }
}
