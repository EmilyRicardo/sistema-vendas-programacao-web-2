package br.edu.vendas.web;

import br.edu.vendas.service.UploadImagemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

@WebServlet("/imagens/*")
public class ImagemServlet extends HttpServlet {
    private static final Pattern NOME_VALIDO = Pattern.compile("[a-fA-F0-9-]{36}\\.(jpg|png|webp)");
    private Path diretorio;
    @Override public void init() throws ServletException {
        String configurado = System.getProperty("vendas.upload.dir");
        diretorio = (configurado == null || configurado.isBlank()) ? Path.of(System.getProperty("user.home"), ".sistema-vendas", "uploads") : Path.of(configurado);
        diretorio = diretorio.toAbsolutePath().normalize();
    }
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nome = req.getPathInfo();
        if (nome == null || nome.length() < 2) { resp.sendError(404); return; }
        nome = nome.substring(1);
        if (!NOME_VALIDO.matcher(nome).matches()) { resp.sendError(404); return; }
        Path arquivo = diretorio.resolve(nome).normalize();
        if (!arquivo.startsWith(diretorio) || !Files.isRegularFile(arquivo)) { resp.sendError(404); return; }
        String ext = nome.substring(nome.lastIndexOf('.') + 1);
        resp.setContentType(ext.equals("png") ? "image/png" : ext.equals("webp") ? "image/webp" : "image/jpeg");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("Cache-Control", "private, max-age=3600");
        resp.setContentLengthLong(Files.size(arquivo));
        try (var in = Files.newInputStream(arquivo); var out = resp.getOutputStream()) { in.transferTo(out); }
    }
}
