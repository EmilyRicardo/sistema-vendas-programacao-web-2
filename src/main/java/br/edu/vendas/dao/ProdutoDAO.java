package br.edu.vendas.dao;

import br.edu.vendas.model.Produto;
import br.edu.vendas.util.JpaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProdutoDAO {
    @Inject private JpaUtil jpa;
    public List<Produto> listar() { return jpa.consultar(em -> {
        List<Produto> lista = em.createQuery("select p from Produto p join fetch p.marca join fetch p.categoria order by p.nome", Produto.class).getResultList();
        lista.forEach(p -> p.getCategoria().getCaminhoHierarquico());
        return lista;
    }); }
    public Produto buscar(Long id) { return id == null ? null : jpa.consultar(em -> {
        Produto produto = em.createQuery("select p from Produto p join fetch p.marca join fetch p.categoria where p.id = :id", Produto.class).setParameter("id", id).getResultStream().findFirst().orElse(null);
        if (produto != null) produto.getCategoria().getCaminhoHierarquico();
        return produto;
    }); }
    public void salvar(Produto p) { jpa.transacionar(em -> { if (p.getId() == null) em.persist(p); else em.merge(p); return null; }); }
    public void excluir(Long id) { jpa.transacionar(em -> { Produto p = em.find(Produto.class, id); if (p != null) em.remove(p); return null; }); }
}
