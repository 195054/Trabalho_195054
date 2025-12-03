package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class EmprestimoRepository {

    @PersistenceContext
    EntityManager em;

    public List<Emprestimo> findAll() {
        try {
            return em.createQuery(
                    "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro LEFT JOIN FETCH e.livro.autor",
                    Emprestimo.class)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Emprestimo> findAtivos() {
        try {
            return em.createQuery(
                    "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro WHERE e.dataDevolucao IS NULL",
                    Emprestimo.class)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void save(Emprestimo emprestimo) {
        if (emprestimo == null) return;
        if (emprestimo.getId() == null) {
            em.persist(emprestimo);
        } else {
            em.merge(emprestimo);
        }
    }

    public Emprestimo buscarPorId(Long id) {
        if (id == null) return null;
        return em.find(Emprestimo.class, id);
    }

    public void excluir(Emprestimo emprestimo) {
        if (emprestimo == null) return;
        Emprestimo managed = emprestimo;
        if (!em.contains(emprestimo)) {
            managed = em.find(Emprestimo.class, emprestimo.getId());
        }
        if (managed != null) {
            em.remove(managed);
        }
    }

    public long count() {
        try {
            return em.createQuery("SELECT COUNT(e) FROM Emprestimo e", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }

    public long countAtivos() {
        try {
            return em.createQuery(
                    "SELECT COUNT(e) FROM Emprestimo e WHERE e.dataDevolucao IS NULL", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }
}
