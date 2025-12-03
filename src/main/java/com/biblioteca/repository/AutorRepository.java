package com.biblioteca.repository;

import com.biblioteca.entity.Autor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class AutorRepository {

    @PersistenceContext
    EntityManager em;

    public List<Autor> findAll() {
        return em.createQuery("SELECT a FROM Autor a ORDER BY a.nome", Autor.class)
                .getResultList();
    }

    public Autor buscarPorId(Long id) {
        if (id == null) return null;
        return em.find(Autor.class, id);
    }

    public void persistir(Autor autor) {
        if (autor == null) return;
        if (autor.getId() == null) {
            em.persist(autor);
        } else {
            em.merge(autor);
        }
    }

    public void excluirPorId(Long id) {
        if (id == null) return;
        Autor a = em.find(Autor.class, id);
        if (a != null) {
            em.remove(a);
        }
    }

    public long count() {
        try {
            return em.createQuery("SELECT COUNT(a) FROM Autor a", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }
}
