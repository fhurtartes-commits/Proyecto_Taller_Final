package com.mycompany.mvctaller.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CrearPDF {

    private static final Font F_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Font.BOLD, new BaseColor(33, 37, 41));
    private static final Font F_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);
    private static final Font F_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD, new BaseColor(13, 110, 253));
    private static final Font F_ENCABEZADO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, BaseColor.WHITE);
    private static final Font F_TEXTO = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
    private static final Font F_PIE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD, BaseColor.BLACK);

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** PDF con una sola tabla. 'pie' es opcional (por ejemplo, un total). */
    public byte[] generarTabla(String titulo, List<Map<String, Object>> filas, String pie) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            agregarEncabezado(doc, titulo);
            agregarTabla(doc, filas);

            if (pie != null && !pie.isEmpty()) {
                doc.add(new Paragraph(pie, F_PIE));
            }

            doc.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("No se pudo generar el PDF", e);
        }

        return out.toByteArray();
    }

    /** PDF con una sección (título + tabla) por cada clave del mapa, por ejemplo por estado. */
    public byte[] generarSecciones(String titulo, Map<String, List<Map<String, Object>>> grupos) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            agregarEncabezado(doc, titulo);

            if (grupos.isEmpty()) {
                doc.add(new Paragraph("Sin registros.", F_TEXTO));
            }

            for (Map.Entry<String, List<Map<String, Object>>> seccion : grupos.entrySet()) {
                Paragraph p = new Paragraph(
                        seccion.getKey() + "  (" + seccion.getValue().size() + " órdenes)", F_SECCION);
                p.setSpacingBefore(10);
                doc.add(p);
                agregarTabla(doc, seccion.getValue());
            }

            doc.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("No se pudo generar el PDF", e);
        }

        return out.toByteArray();
    }

    // ---------- Utilidades ----------

    private void agregarEncabezado(Document doc, String titulo) throws DocumentException {
        doc.add(new Paragraph(titulo, F_TITULO));
        Paragraph sub = new Paragraph("Sistema Taller Mecánico - Generado: " + LocalDateTime.now().format(FORMATO_FECHA), F_SUBTITULO);
        sub.setSpacingAfter(8);
        doc.add(sub);
    }

    private void agregarTabla(Document doc, List<Map<String, Object>> filas) throws DocumentException {
        if (filas == null || filas.isEmpty()) {
            Paragraph vacio = new Paragraph("No hay registros.", F_TEXTO);
            vacio.setSpacingAfter(8);
            doc.add(vacio);
            return;
        }

        List<String> columnas = new ArrayList<>(filas.get(0).keySet());

        PdfPTable tabla = new PdfPTable(columnas.size());
        tabla.setWidthPercentage(100);
        tabla.setHeaderRows(1);
        tabla.setSpacingBefore(6);
        tabla.setSpacingAfter(12);

        for (String col : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(col.replace('_', ' ').toUpperCase(), F_ENCABEZADO));
            celda.setBackgroundColor(new BaseColor(33, 37, 41));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(5);
            tabla.addCell(celda);
        }

        boolean gris = false;
        for (Map<String, Object> fila : filas) {
            for (String col : columnas) {
                PdfPCell celda = new PdfPCell(new Phrase(formatear(col, fila.get(col)), F_TEXTO));
                celda.setPadding(4);
                if (gris) {
                    celda.setBackgroundColor(new BaseColor(245, 245, 245));
                }
                tabla.addCell(celda);
            }
            gris = !gris;
        }

        doc.add(tabla);
    }

    private String formatear(String columna, Object valor) {
        if (valor == null) return "-";

        if (valor instanceof BigDecimal) {
            String c = columna.toLowerCase();
            if (c.contains("costo") || c.contains("precio")) {
                return "Q " + ((BigDecimal) valor).setScale(2, RoundingMode.HALF_UP).toPlainString();
            }
        }
        return valor.toString();
    }
}