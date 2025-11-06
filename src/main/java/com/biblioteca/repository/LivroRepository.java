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
        return em.createQuery("SELECT l FROM Livro l LEFT JOIN FETCH l.autor", Livro.class).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(l) FROM Livro l", Long.class).getSingleResult();
    }

    public long countByDisponivel(boolean disponivel) {
        return em.createQuery("SELECT COUNT(l) FROM Livro l WHERE l.disponivel = :d", Long.class)
                .setParameter("d", disponivel)
                .getSingleResult();
    }

    public Livro buscarPorId(Long id) {
        return em.find(Livro.class, id);
    }

    public void persistir(Livro livro) {
        if (livro.getId() == null) em.persist(livro);
        else em.merge(livro);
    }

    public void excluirPorId(Long id) {
        Livro l = em.find(Livro.class, id);
        if (l != null) em.remove(l);
    }
}
