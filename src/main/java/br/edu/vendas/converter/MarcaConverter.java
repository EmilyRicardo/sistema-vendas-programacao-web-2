package br.edu.vendas.converter;

import br.edu.vendas.model.Marca;
import br.edu.vendas.service.MarcaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "marcaConverter", managed = true)
public class MarcaConverter implements Converter<Marca> {
    @Inject private MarcaService service;
    @Override public Marca getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        try { return service.buscar(Long.valueOf(value)); } catch (NumberFormatException e) { return null; }
    }
    @Override public String getAsString(FacesContext context, UIComponent component, Marca value) { return value == null || value.getId() == null ? "" : value.getId().toString(); }
}
