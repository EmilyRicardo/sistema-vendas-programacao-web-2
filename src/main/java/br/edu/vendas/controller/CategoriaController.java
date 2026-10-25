package br.edu.vendas.controller;

import br.edu.vendas.model.Categoria;
import br.edu.vendas.service.CategoriaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named @SessionScoped
public class CategoriaController implements Serializable {
    @Inject private CategoriaService service;
    private List<Categoria> categorias;
    private Categoria categoria;
    @PostConstruct public void init() { atualizar(); }
    public void atualizar() { categorias = service.listar(); }
    public void novo() { categoria = new Categoria(); }
    public void editar(Categoria c) { categoria = service.buscar(c.getId()); }
    public void salvar() {
        try { service.salvar(categoria); atualizar(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso","Categoria salva com sucesso."); }
        catch (Exception e) { erro(e); }
    }
    public void excluir(Categoria c) {
        try { service.excluir(c.getId()); atualizar(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso","Categoria excluída."); }
        catch (Exception e) { erro(e); }
    }
    public List<SelectItem> getOpcoesPai() {
        if (categorias == null) atualizar();
        List<SelectItem> itens = new ArrayList<>(); itens.add(new SelectItem(null,"— Categoria raiz —"));
        Set<Long> bloqueadas = new HashSet<>();
        if (categoria != null && categoria.getId() != null) {
            for (Categoria candidata : categorias) {
                Categoria atual = candidata;
                int limite = 0;
                while (atual != null && limite++ < 100) {
                    if (categoria.getId().equals(atual.getId())) { bloqueadas.add(candidata.getId()); break; }
                    atual = atual.getCategoriaPai();
                }
            }
        }
        for (Categoria c : categorias) if (c.getId() == null || !bloqueadas.contains(c.getId())) itens.add(new SelectItem(c,c.getCaminhoHierarquico()));
        return itens;
    }
    private void mensagem(FacesMessage.Severity s,String t,String d) { FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(s,t,d)); }
    private void erro(Exception e) { FacesContext.getCurrentInstance().validationFailed(); mensagem(FacesMessage.SEVERITY_ERROR,"Não foi possível concluir", e.getMessage() == null ? "Verifique os dados e tente novamente." : e.getMessage()); }
    public List<Categoria> getCategorias() { return categorias; }
    public Categoria getCategoria() { if (categoria == null) novo(); return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
