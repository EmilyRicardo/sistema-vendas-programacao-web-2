package br.edu.vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "tb_marca")
public class Marca implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome da marca.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    public Marca() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome == null ? null : nome.trim(); }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    @Override public boolean equals(Object obj) { if (this == obj) return true; if (!(obj instanceof Marca other)) return false; return id != null && id.equals(other.id); }
    @Override public int hashCode() { return getClass().hashCode(); }
    @Override public String toString() { return nome == null ? "" : nome; }
}
