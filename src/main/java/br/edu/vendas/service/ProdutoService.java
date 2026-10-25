package br.edu.vendas.service;

import br.edu.vendas.dao.ProdutoDAO;
import br.edu.vendas.dao.MarcaDAO;
import br.edu.vendas.dao.CategoriaDAO;
import br.edu.vendas.model.Produto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ProdutoService {
    @Inject private ProdutoDAO dao;
    @Inject private MarcaDAO marcaDAO;
    @Inject private CategoriaDAO categoriaDAO;
    @Inject private UploadImagemService uploadService;
    public List<Produto> listar() { return dao.listar(); }
    public Produto buscar(Long id) { return dao.buscar(id); }
    public void salvar(Produto p) {
        if (p == null || p.getNome() == null || p.getNome().isBlank()) throw new IllegalArgumentException("Informe o nome do produto.");
        if (p.getPreco() == null || p.getPreco().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("O preço deve ser maior que zero.");
        if (p.getQuantidadeEstoque() == null || p.getQuantidadeEstoque() < 0) throw new IllegalArgumentException("O estoque não pode ser negativo.");
        if (p.getMarca() == null || p.getMarca().getId() == null) throw new IllegalArgumentException("Selecione uma marca.");
        if (p.getCategoria() == null || p.getCategoria().getId() == null) throw new IllegalArgumentException("Selecione uma categoria.");
        var marca = marcaDAO.buscar(p.getMarca().getId());
        if (marca == null) throw new IllegalArgumentException("A marca selecionada não existe.");
        if (!Boolean.TRUE.equals(marca.getAtivo())) throw new IllegalArgumentException("Não é possível vincular produto a uma marca inativa.");
        var categoria = categoriaDAO.buscar(p.getCategoria().getId());
        if (categoria == null) throw new IllegalArgumentException("A categoria selecionada não existe.");
        p.setMarca(marca); p.setCategoria(categoria); p.setNome(p.getNome().trim());
        dao.salvar(p);
    }
    public void excluir(Long id) {
        Produto existente = dao.buscar(id);
        if (existente != null) {
            String imagem = existente.getCaminhoImagem();
            dao.excluir(id);
            uploadService.excluir(imagem);
        }
    }
}
