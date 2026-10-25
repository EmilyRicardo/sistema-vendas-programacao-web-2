package br.edu.vendas.dao;

import br.edu.vendas.model.Categoria;
import br.edu.vendas.util.JpaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoriaDAO {
    @Inject private JpaUtil jpa;
    public List<Categoria> listar() { return jpa.consultar(em -> {
        List<Categoria> lista = em.createQuery("select c from Categoria c order by c.nome", Categoria.class).getResultList();
        // Inicializa o caminho de cada categoria antes de fechar o EntityManager (relação pai é LAZY).
        lista.forEach(Categoria::getCaminhoHierarquico);
        return lista;
    }); }
    public Categoria buscar(Long id) { return id == null ? null : jpa.consultar(em -> {
        Categoria categoria = em.find(Categoria.class, id);
        if (categoria != null) categoria.getCaminhoHierarquico();
        return categoria;
    }); }
    public void salvar(Categoria c) { jpa.transacionar(em -> { if (c.getId() == null) em.persist(c); else em.merge(c); return null; }); }
    public long contarFilhos(Long id) { return jpa.consultar(em -> em.createQuery("select count(c) from Categoria c where c.categoriaPai.id = :id", Long.class).setParameter("id", id).getSingleResult()); }
    public long contarProdutos(Long id) { return jpa.consultar(em -> em.createQuery("select count(p) from Produto p where p.categoria.id = :id", Long.class).setParameter("id", id).getSingleResult()); }
    public void excluir(Long id) { jpa.transacionar(em -> { Categoria c = em.find(Categoria.class, id); if (c != null) em.remove(c); return null; }); }
}
