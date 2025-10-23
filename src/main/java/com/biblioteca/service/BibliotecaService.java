package com.biblioteca.service;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BibliotecaService {

    @Inject
    AutorRepository autorRepository;

    @Inject
    LivroRepository livroRepository;

    @Inject
    EmprestimoRepository emprestimoRepository;


    public List<Autor> listarTodosAutores() { return autorRepository.findAll(); }
    public List<Livro> listarTodosLivros() { return livroRepository.findAll(); }
    public List<Emprestimo> listarEmprestimosAtivos() { return emprestimoRepository.findAtivos(); }

    public long contarTotalLivros() { return livroRepository.count(); }
    public long contarLivrosDisponiveis() { return livroRepository.countByDisponivel(true); }
    public long contarEmprestimosAtivos() { return emprestimoRepository.countAtivos(); }
    public long contarTotalAutores() { return autorRepository.count(); }


    @Transactional
    public void salvarAutor(Autor autor) {
        autorRepository.persistir(autor);
    }

    @Transactional
    public void excluirAutor(Long id) {
        autorRepository.excluirPorId(id);
    }


    @Transactional
    public void salvarLivro(Livro livro, Long idAutor) {
        Autor autor = autorRepository.buscarPorId(idAutor);
        livro.setAutor(autor);
        livroRepository.persistir(livro);
    }

    @Transactional
    public void excluirLivro(Long id) {
        livroRepository.excluirPorId(id);
    }
}
