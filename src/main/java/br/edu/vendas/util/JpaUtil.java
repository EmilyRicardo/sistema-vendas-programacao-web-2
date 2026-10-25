package br.edu.vendas.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.function.Function;

@ApplicationScoped
public class JpaUtil {
    private EntityManagerFactory emf;

    @PostConstruct
    public void iniciar() {
        emf = Persistence.createEntityManagerFactory("VendasPU");
    }

    @PreDestroy
    public void encerrar() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    public <T> T consultar(Function<EntityManager, T> operacao) {
        EntityManager em = emf.createEntityManager();
        try { return operacao.apply(em); }
        finally { em.close(); }
    }

    public <T> T transacionar(Function<EntityManager, T> operacao) {
        EntityManager em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            T resultado = operacao.apply(em);
            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }
}
