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
        return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(a) FROM Autor a", Long.class).getSingleResult();
    }

    public Autor buscarPorId(Long id) {
        return em.find(Autor.class, id);
    }

    public void persistir(Autor autor) {
        if (autor.getId() == null) {
            em.persist(autor);
        } else {
            em.merge(autor);
        }
        em.flush(); // força o commit no banco
        em.clear(); // limpa o cache do contexto
    }




    public void excluirPorId(Long id) {
        Autor a = em.find(Autor.class, id);
        if (a != null) em.remove(a);
    }
}
