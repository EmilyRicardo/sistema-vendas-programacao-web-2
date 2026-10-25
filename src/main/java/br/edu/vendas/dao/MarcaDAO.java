package br.edu.vendas.dao;

import br.edu.vendas.model.Marca;
import br.edu.vendas.util.JpaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MarcaDAO {
    @Inject private JpaUtil jpa;
    public List<Marca> listar() { return jpa.consultar(em -> em.createQuery("select m from Marca m order by m.nome", Marca.class).getResultList()); }
    public List<Marca> listarAtivas() { return jpa.consultar(em -> em.createQuery("select m from Marca m where m.ativo = true order by m.nome", Marca.class).getResultList()); }
    public Marca buscar(Long id) { return id == null ? null : jpa.consultar(em -> em.find(Marca.class, id)); }
    public boolean nomeExiste(String nome, Long ignorarId) {
        return jpa.consultar(em -> {
            String jpql = "select count(m) from Marca m where lower(m.nome) = :nome" + (ignorarId == null ? "" : " and m.id <> :id");
            var q = em.createQuery(jpql, Long.class).setParameter("nome", nome.trim().toLowerCase(java.util.Locale.ROOT));
            if (ignorarId != null) q.setParameter("id", ignorarId);
            return q.getSingleResult() > 0;
        });
    }
    public void salvar(Marca marca) { jpa.transacionar(em -> { if (marca.getId() == null) em.persist(marca); else em.merge(marca); return null; }); }
    public long contarProdutos(Long id) { return jpa.consultar(em -> em.createQuery("select count(p) from Produto p where p.marca.id = :id", Long.class).setParameter("id", id).getSingleResult()); }
    public void excluirFisicamente(Long id) { jpa.transacionar(em -> { Marca m = em.find(Marca.class, id); if (m != null) em.remove(m); return null; }); }
}
