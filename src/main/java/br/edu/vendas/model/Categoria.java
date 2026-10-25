package br.edu.vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_categoria")
public class Categoria implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome da categoria.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_categoria_pai"))
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL)
    private List<Categoria> subcategorias = new ArrayList<>();

    public Categoria() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome == null ? null : nome.trim(); }
    public Categoria getCategoriaPai() { return categoriaPai; }
    public void setCategoriaPai(Categoria categoriaPai) { this.categoriaPai = categoriaPai; }
    public List<Categoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<Categoria> subcategorias) { this.subcategorias = subcategorias == null ? new ArrayList<>() : subcategorias; }
    public String getCaminhoHierarquico() {
        java.util.Deque<String> partes = new java.util.ArrayDeque<>();
        java.util.Set<Long> vistos = new java.util.HashSet<>();
        Categoria atual = this;
        int limite = 0;
        while (atual != null && limite++ < 100) {
            if (atual.getId() != null && !vistos.add(atual.getId())) { partes.addFirst("[ciclo]"); break; }
            partes.addFirst(atual.getNome() == null ? "" : atual.getNome());
            atual = atual.getCategoriaPai();
        }
        return String.join(" / ", partes);
    }
    @Override public boolean equals(Object obj) { if (this == obj) return true; if (!(obj instanceof Categoria other)) return false; return id != null && id.equals(other.id); }
    @Override public int hashCode() { return getClass().hashCode(); }
    @Override public String toString() { return getCaminhoHierarquico(); }
}
