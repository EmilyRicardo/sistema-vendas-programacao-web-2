package br.edu.vendas.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.primefaces.model.file.UploadedFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class UploadImagemService {
    public static final long TAMANHO_MAXIMO = 5L * 1024 * 1024;
    public Path diretorio() {
        String configurado = System.getProperty("vendas.upload.dir");
        Path path = (configurado == null || configurado.isBlank())
                ? Path.of(System.getProperty("user.home"), ".sistema-vendas", "uploads")
                : Path.of(configurado);
        return path.toAbsolutePath().normalize();
    }
    public String salvar(UploadedFile arquivo) throws IOException {
        if (arquivo == null || arquivo.getSize() <= 0) throw new IllegalArgumentException("Selecione uma imagem válida.");
        if (arquivo.getSize() > TAMANHO_MAXIMO) throw new IllegalArgumentException("A imagem deve ter no máximo 5 MB.");
        String nomeOriginal = arquivo.getFileName() == null ? "" : arquivo.getFileName();
        String extensao = extensao(nomeOriginal);
        String tipo = arquivo.getContentType() == null ? "" : arquivo.getContentType().toLowerCase(Locale.ROOT);
        boolean extensaoEConteudoCompativeis = ((extensao.equals("jpg") || extensao.equals("jpeg")) && tipo.equals("image/jpeg"))
                || (extensao.equals("png") && tipo.equals("image/png"))
                || (extensao.equals("webp") && tipo.equals("image/webp"));
        if (!extensaoEConteudoCompativeis) {
            throw new IllegalArgumentException("Formato não permitido. Use JPG, PNG ou WEBP e mantenha a extensão coerente com o tipo do arquivo.");
        }
        byte[] bytes;
        try (InputStream in = arquivo.getInputStream()) { bytes = in.readNBytes((int) TAMANHO_MAXIMO + 1); }
        if (bytes.length == 0 || bytes.length > TAMANHO_MAXIMO || !assinaturaValida(bytes, tipo)) throw new IllegalArgumentException("O arquivo não parece ser uma imagem JPG, PNG ou WEBP válida.");
        if (extensao.equals("jpeg")) extensao = "jpg";
        String nome = UUID.randomUUID().toString() + "." + extensao;
        Path dir = diretorio();
        Files.createDirectories(dir);
        Files.write(dir.resolve(nome), bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        return nome;
    }
    public void excluir(String nome) {
        if (nome == null || !nome.matches("[a-fA-F0-9-]{36}\\.(jpg|png|webp)")) return;
        try { Files.deleteIfExists(diretorio().resolve(nome).normalize()); } catch (IOException ignored) { /* limpeza não deve impedir o CRUD */ }
    }
    private String extensao(String nome) {
        int ponto = nome.lastIndexOf('.');
        return ponto >= 0 && ponto < nome.length() - 1 ? nome.substring(ponto + 1).toLowerCase(Locale.ROOT) : "";
    }
    private boolean assinaturaValida(byte[] b, String tipo) {
        if (tipo.equals("image/png")) return b.length >= 8 && (b[0] & 255)==137 && b[1]==80 && b[2]==78 && b[3]==71 && b[4]==13 && b[5]==10 && b[6]==26 && b[7]==10;
        if (tipo.equals("image/jpeg")) return b.length >= 3 && (b[0]&255)==255 && (b[1]&255)==216 && (b[2]&255)==255;
        return b.length >= 12 && b[0]=='R' && b[1]=='I' && b[2]=='F' && b[3]=='F' && b[8]=='W' && b[9]=='E' && b[10]=='B' && b[11]=='P';
    }
}
