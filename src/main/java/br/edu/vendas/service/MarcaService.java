package br.edu.vendas.service;

import br.edu.vendas.dao.MarcaDAO;
import br.edu.vendas.model.Marca;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MarcaService {
    @Inject private MarcaDAO dao;
    public List<Marca> listar() { return dao.listar(); }
    public List<Marca> listarAtivas() { return dao.listarAtivas(); }
    public Marca buscar(Long id) { return dao.buscar(id); }
    public void salvar(Marca marca) {
        if (marca == null || marca.getNome() == null || marca.getNome().isBlank()) throw new IllegalArgumentException("Informe o nome da marca.");
        if (dao.nomeExiste(marca.getNome(), marca.getId())) throw new IllegalArgumentException("Já existe uma marca com esse nome.");
        if (marca.getAtivo() == null) marca.setAtivo(Boolean.TRUE);
        dao.salvar(marca);
    }
    public void alternarAtivo(Long id) {
        Marca marca = dao.buscar(id);
        if (marca == null) throw new IllegalArgumentException("Marca não encontrada.");
        marca.setAtivo(!Boolean.TRUE.equals(marca.getAtivo()));
        dao.salvar(marca);
    }
    public void excluirFisicamente(Long id) {
        if (dao.contarProdutos(id) > 0) throw new IllegalStateException("Esta marca possui produtos vinculados. Inative-a em vez de excluir.");
        dao.excluirFisicamente(id);
    }
}
