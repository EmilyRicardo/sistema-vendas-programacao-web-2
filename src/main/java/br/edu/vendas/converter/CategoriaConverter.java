package br.edu.vendas.converter;

import br.edu.vendas.model.Categoria;
import br.edu.vendas.service.CategoriaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "categoriaConverter", managed = true)
public class CategoriaConverter implements Converter<Categoria> {
    @Inject private CategoriaService service;
    @Override public Categoria getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        try { return service.buscar(Long.valueOf(value)); } catch (NumberFormatException e) { return null; }
    }
    @Override public String getAsString(FacesContext context, UIComponent component, Categoria value) { return value == null || value.getId() == null ? "" : value.getId().toString(); }
}
