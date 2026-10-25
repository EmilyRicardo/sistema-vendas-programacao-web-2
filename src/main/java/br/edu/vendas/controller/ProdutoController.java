package br.edu.vendas.controller;

import br.edu.vendas.model.Categoria;
import br.edu.vendas.model.Marca;
import br.edu.vendas.model.Produto;
import br.edu.vendas.service.CategoriaService;
import br.edu.vendas.service.MarcaService;
import br.edu.vendas.service.ProdutoService;
import br.edu.vendas.service.UploadImagemService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

@Named @SessionScoped
public class ProdutoController implements Serializable {
    @Inject private ProdutoService service;
    @Inject private MarcaService marcaService;
    @Inject private CategoriaService categoriaService;
    @Inject private UploadImagemService uploadService;
    private List<Produto> produtos;
    private Produto produto;
    private String imagemPersistidaInicial;
    private List<Marca> marcasAtivas;
    private List<SelectItem> categoriasHierarquicas;
    @PostConstruct public void init() { atualizar(); carregarOpcoes(); }
    public void atualizar() { produtos = service.listar(); }
    private void carregarOpcoes() {
        marcasAtivas = marcaService.listarAtivas();
        List<Categoria> categorias = categoriaService.listar();
        Map<Long,List<Categoria>> filhos = new HashMap<>();
        for (Categoria c : categorias) {
            Long paiId = c.getCategoriaPai() == null ? null : c.getCategoriaPai().getId();
            filhos.computeIfAbsent(paiId, k -> new ArrayList<>()).add(c);
        }
        filhos.values().forEach(lista -> lista.sort(Comparator.comparing(Categoria::getNome, String.CASE_INSENSITIVE_ORDER)));
        categoriasHierarquicas = new ArrayList<>();
        adicionarCategorias(filhos, null, 0, new HashSet<>());
    }
    private void adicionarCategorias(Map<Long,List<Categoria>> filhos, Long paiId, int nivel, Set<Long> vistos) {
        for (Categoria c : filhos.getOrDefault(paiId, Collections.emptyList())) {
            if (c.getId() != null && !vistos.add(c.getId())) continue;
            categoriasHierarquicas.add(new SelectItem(c, "— ".repeat(nivel) + c.getNome()));
            adicionarCategorias(filhos, c.getId(), nivel + 1, vistos);
        }
    }
    public void novo() { produto = new Produto(); produto.setQuantidadeEstoque(0); imagemPersistidaInicial = null; carregarOpcoes(); }
    public void editar(Produto p) { produto = service.buscar(p.getId()); imagemPersistidaInicial = produto == null ? null : produto.getCaminhoImagem(); carregarOpcoes(); }
    public void salvar() {
        try {
            service.salvar(produto);
            if (imagemPersistidaInicial != null && !imagemPersistidaInicial.equals(produto.getCaminhoImagem())) uploadService.excluir(imagemPersistidaInicial);
            imagemPersistidaInicial = produto.getCaminhoImagem();
            atualizar(); carregarOpcoes(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso","Produto salvo com sucesso.");
        } catch (Exception e) { erro(e); }
    }
    public void cancelarEdicao() {
        if (produto != null) {
            String atual = produto.getCaminhoImagem();
            if (atual != null && !atual.equals(imagemPersistidaInicial)) uploadService.excluir(atual);
            produto.setCaminhoImagem(imagemPersistidaInicial);
        }
        produto = null;
    }
    public void excluir(Produto p) {
        try { service.excluir(p.getId()); atualizar(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso","Produto excluído."); }
        catch (Exception e) { erro(e); }
    }
    public void upload(FileUploadEvent evento) {
        String anterior = produto.getCaminhoImagem();
        try {
            String novoNome = uploadService.salvar(evento.getFile());
            produto.setCaminhoImagem(novoNome);
            if (anterior != null && !anterior.equals(novoNome) && !anterior.equals(imagemPersistidaInicial)) uploadService.excluir(anterior);
            mensagem(FacesMessage.SEVERITY_INFO,"Imagem enviada","Imagem carregada com sucesso.");
        } catch (IOException | IllegalArgumentException e) { erro(e); }
    }
    public String formatarPreco(java.math.BigDecimal valor) { return valor == null ? "—" : NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(valor); }
    private void mensagem(FacesMessage.Severity s,String t,String d) { FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(s,t,d)); }
    private void erro(Exception e) { FacesContext.getCurrentInstance().validationFailed(); mensagem(FacesMessage.SEVERITY_ERROR,"Não foi possível concluir", e.getMessage() == null ? "Verifique os dados e tente novamente." : e.getMessage()); }
    public List<Produto> getProdutos() { return produtos; }
    public Produto getProduto() { if (produto == null) novo(); return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public List<Marca> getMarcasAtivas() { return marcasAtivas; }
    public List<SelectItem> getCategoriasHierarquicas() { return categoriasHierarquicas; }
}
