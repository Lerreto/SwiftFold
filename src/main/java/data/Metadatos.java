package data;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class Metadatos {

    public record Info(String originalName, String tipo, long sizeBytes, LocalDateTime modifiedAt) {}

    // formato para la fecha
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // === API principal ===
    public static Info leer(Path p) {
        try {
            BasicFileAttributes a = Files.readAttributes(p, BasicFileAttributes.class);
            String nombre = p.getFileName().toString();
            long size = a.size();
            LocalDateTime mod = LocalDateTime.ofInstant(a.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            String tipo = inferirTipo(nombre);

            Info info = new Info(nombre, tipo, size, mod);

            // imprime automáticamente un resumen en consola
            imprimir(info);
            return info;

        } catch (IOException e) {
            throw new RuntimeException("Leyendo atributos: " + e.getMessage(), e);
        }
    }

    // convierte bytes a tamaño legible
    public static String human(long bytes) {
        String[] u = {"B","KB","MB","GB","TB"};
        int i = 0;
        double v = bytes;
        while (v >= 1024 && i < u.length - 1) { v /= 1024.0; i++; }
        return String.format(java.util.Locale.US, "%.2f %s", v, u[i]);
    }

    // imprime bonito en consola
    public static void imprimir(Info i) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n============= METADATOS DEL ARCHIVO =============\n");
        sb.append(String.format("%-18s: %s%n", "Nombre", i.originalName()));
        sb.append(String.format("%-18s: %s%n", "Tipo", i.tipo()));
        sb.append(String.format("%-18s: %s (%d bytes)%n", "Tamaño", human(i.sizeBytes()), i.sizeBytes()));
        sb.append(String.format("%-18s: %s%n", "Últ. modificación", i.modifiedAt().format(FMT)));
        sb.append("=================================================\n");
        System.out.print(sb.toString());
    }

    // detecta tipo simple por extensión
    private static String inferirTipo(String name) {
        String s = name.toLowerCase();
        if (s.endsWith(".pdf")) return "PDF";
        if (s.endsWith(".doc") || s.endsWith(".docx")) return "WORD";
        if (s.endsWith(".xls") || s.endsWith(".xlsx") || s.endsWith(".csv")) return "EXCEL";
        if (s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".gif")) return "IMAGEN";
        return "OTRO";
    }

    private Metadatos() {}
}
