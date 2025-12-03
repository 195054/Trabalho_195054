package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class LivroRepository {

    @PersistenceContext
    EntityManager em;

    public List<Livro> findAll() {
        return em.createQuery("SELECT l FROM Livro l LEFT JOIN FETCH l.autor ORDER BY l.titulo", Livro.class)
                .getResultList();
    }

    public Livro buscarPorId(Long id) {
        if (id == null) return null;
        return em.find(Livro.class, id);
    }

    public void persistir(Livro livro) {
        if (livro == null) return;
        if (livro.getId() == null) {
            em.persist(livro);
        } else {
            em.merge(livro);
        }
    }

    public void excluirPorId(Long id) {
        if (id == null) return;
        Livro l = em.find(Livro.class, id);
        if (l != null) {
            em.remove(l);
        }
    }

    public long count() {
        try {
            return em.createQuery("SELECT COUNT(l) FROM Livro l", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }

    public long countDisponiveis() {
        try {
            return em.createQuery("SELECT COUNT(l) FROM Livro l WHERE l.disponivel = true", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }
}
