package br.edu.vendas.service;

import br.edu.vendas.dao.CategoriaDAO;
import br.edu.vendas.model.Categoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoriaService {
    @Inject private CategoriaDAO dao;
    public List<Categoria> listar() { return dao.listar(); }
    public Categoria buscar(Long id) { return dao.buscar(id); }
    public void salvar(Categoria categoria) {
        if (categoria == null || categoria.getNome() == null || categoria.getNome().isBlank()) throw new IllegalArgumentException("Informe o nome da categoria.");
        Categoria pai = categoria.getCategoriaPai();
        if (pai != null && pai.getId() != null) {
            pai = dao.buscar(pai.getId());
            if (pai == null) throw new IllegalArgumentException("A categoria pai selecionada não existe mais.");
            if (categoria.getId() != null && pai.getId().equals(categoria.getId())) throw new IllegalArgumentException("Uma categoria não pode ser pai dela mesma.");
            Categoria atual = pai;
            int limite = 0;
            while (atual != null && limite++ < 100) {
                if (categoria.getId() != null && categoria.getId().equals(atual.getId())) throw new IllegalArgumentException("Essa alteração criaria um ciclo na hierarquia.");
                atual = atual.getCategoriaPai();
            }
            categoria.setCategoriaPai(pai);
        } else categoria.setCategoriaPai(null);
        categoria.setNome(categoria.getNome().trim());
        dao.salvar(categoria);
    }
    public void excluir(Long id) {
        if (dao.contarFilhos(id) > 0) throw new IllegalStateException("Não é possível excluir uma categoria que possui subcategorias. Mova ou exclua as subcategorias primeiro.");
        if (dao.contarProdutos(id) > 0) throw new IllegalStateException("Não é possível excluir uma categoria que possui produtos vinculados.");
        dao.excluir(id);
    }
}
