package br.edu.vendas.controller;

import br.edu.vendas.model.Marca;
import br.edu.vendas.service.MarcaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named @SessionScoped
public class MarcaController implements Serializable {
    @Inject private MarcaService service;
    private List<Marca> marcas;
    private Marca marca;
    @PostConstruct public void init() { atualizar(); }
    public void atualizar() { marcas = service.listar(); }
    public void novo() { marca = new Marca(); marca.setAtivo(true); }
    public void editar(Marca m) { marca = service.buscar(m.getId()); }
    public void salvar() {
        try { service.salvar(marca); atualizar(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso", "Marca salva com sucesso."); }
        catch (Exception e) { erro(e); }
    }
    public void alternarAtivo(Marca m) {
        try { service.alternarAtivo(m.getId()); atualizar(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso", Boolean.TRUE.equals(m.getAtivo()) ? "Marca inativada." : "Marca ativada."); }
        catch (Exception e) { erro(e); }
    }
    public void excluirFisicamente(Marca m) {
        try { service.excluirFisicamente(m.getId()); atualizar(); mensagem(FacesMessage.SEVERITY_INFO,"Sucesso", "Marca excluída."); }
        catch (Exception e) { erro(e); }
    }
    private void mensagem(FacesMessage.Severity s,String t,String d) { FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(s,t,d)); }
    private void erro(Exception e) { FacesContext.getCurrentInstance().validationFailed(); mensagem(FacesMessage.SEVERITY_ERROR,"Não foi possível concluir", e.getMessage() == null ? "Verifique os dados e tente novamente." : e.getMessage()); }
    public List<Marca> getMarcas() { return marcas; }
    public Marca getMarca() { if (marca == null) novo(); return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }
}
