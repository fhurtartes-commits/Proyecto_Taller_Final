package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.ReportesPDF;
import com.mycompany.mvctaller.util.CrearPDF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Módulo de reportes: pantallas (reportes.html) y descargas PDF.
 */
@Controller
public class ReportesControlador {

    private final ReportesPDF reportesPDF;
    private final CrearPDF crearPDF;

    public ReportesControlador(ReportesPDF reportesPDF, CrearPDF crearPDF) {
        this.reportesPDF = reportesPDF;
        this.crearPDF = crearPDF;
    }

    /** Datos que necesita siempre la plantilla y nombre de la vista. */
    private String vista(Model model) {
        model.addAttribute("estados", reportesPDF.obtenerEstadosDisponibles());
        return "reportes";
    }

    // =====================================================================
    //  PANTALLAS
    // =====================================================================

    @GetMapping("/reportes")
    public String inicio(Model model) {
        return vista(model);
    }

    // 1. Órdenes por estado (TODOS = una sección por cada estado)
    @GetMapping("/reportes/ordenes-estado")
    public String ordenesPorEstado(@RequestParam(defaultValue = "TODOS") String estado, Model model) {
        model.addAttribute("estadoSeleccionado", estado);

        if ("TODOS".equalsIgnoreCase(estado)) {
            model.addAttribute("grupos", reportesPDF.obtenerOrdenesAgrupadasPorEstado());
            model.addAttribute("tipoReporte", "Órdenes agrupadas por estado");
        } else {
            model.addAttribute("grupos", reportesPDF.obtenerSeccionPorEstado(estado));
            model.addAttribute("tipoReporte", "Órdenes en estado: " + estado);
        }
        return vista(model);
    }

    // 2. Historial por placa
    @GetMapping("/reportes/historial-placa")
    public String historialPorPlaca(@RequestParam String noPlaca, Model model) {
        model.addAttribute("lista", reportesPDF.obtenerHistorialPorPlaca(noPlaca));
        model.addAttribute("tipoReporte", "Historial de reparaciones - Placa " + noPlaca);
        return vista(model);
    }

    // 3. Stock bajo
    @GetMapping("/reportes/stock-bajo")
    public String stockBajo(@RequestParam(defaultValue = "5") int limite, Model model) {
        model.addAttribute("lista", reportesPDF.obtenerRepuestosStockBajo(limite));
        model.addAttribute("tipoReporte", "Repuestos con existencia menor o igual a " + limite);
        return vista(model);
    }

    // 4. Órdenes por mecánico y estado
    @GetMapping("/reportes/ordenes-mecanico")
    public String ordenesPorMecanico(@RequestParam int idMecanico,
                                     @RequestParam(defaultValue = "TODOS") String estado,
                                     Model model) {
        model.addAttribute("lista", reportesPDF.obtenerOrdenesPorMecanicoYEstado(idMecanico, estado));
        model.addAttribute("tipoReporte", tituloMecanico(idMecanico, estado));
        return vista(model);
    }

    // 5. Ingresos por rango de fechas
    @GetMapping("/reportes/ingresos-fechas")
    public String ingresosPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        if (fechaInicio.isAfter(fechaFin)) {
            model.addAttribute("error", "La fecha de inicio no puede ser posterior a la fecha fin.");
            return vista(model);
        }

        model.addAttribute("lista",
                reportesPDF.obtenerIngresosPorFechas(Date.valueOf(fechaInicio), Date.valueOf(fechaFin)));
        model.addAttribute("tipoReporte", "Ingresos del " + fechaInicio + " al " + fechaFin);
        return vista(model);
    }

    // =====================================================================
    //  DESCARGAS PDF
    // =====================================================================

    @GetMapping("/reportes/pdf/estado")
    public ResponseEntity<byte[]> pdfPorEstado(@RequestParam(defaultValue = "TODOS") String estado) {
        byte[] contenido;

        if ("TODOS".equalsIgnoreCase(estado)) {
            contenido = crearPDF.generarSecciones("Órdenes agrupadas por estado",
                    reportesPDF.obtenerOrdenesAgrupadasPorEstado());
        } else {
            contenido = crearPDF.generarSecciones("Órdenes en estado: " + estado,
                    reportesPDF.obtenerSeccionPorEstado(estado));
        }
        return respuestaPdf(contenido, "ordenes-por-estado");
    }

    @GetMapping("/reportes/pdf/placa")
    public ResponseEntity<byte[]> pdfPorPlaca(@RequestParam String noPlaca) {
        byte[] contenido = crearPDF.generarTabla("Historial de reparaciones - Placa " + noPlaca,
                reportesPDF.obtenerHistorialPorPlaca(noPlaca), null);
        return respuestaPdf(contenido, "historial-placa");
    }

    @GetMapping("/reportes/pdf/stock-bajo")
    public ResponseEntity<byte[]> pdfStockBajo(@RequestParam(defaultValue = "5") int limite) {
        byte[] contenido = crearPDF.generarTabla("Repuestos con existencia menor o igual a " + limite,
                reportesPDF.obtenerRepuestosStockBajo(limite), null);
        return respuestaPdf(contenido, "stock-bajo");
    }

    @GetMapping("/reportes/pdf/mecanico")
    public ResponseEntity<byte[]> pdfPorMecanico(@RequestParam int idMecanico,
                                                 @RequestParam(defaultValue = "TODOS") String estado) {
        byte[] contenido = crearPDF.generarTabla(tituloMecanico(idMecanico, estado),
                reportesPDF.obtenerOrdenesPorMecanicoYEstado(idMecanico, estado), null);
        return respuestaPdf(contenido, "ordenes-mecanico");
    }

    @GetMapping("/reportes/pdf/ingresos")
    public ResponseEntity<byte[]> pdfIngresos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Map<String, Object>> filas = reportesPDF.obtenerIngresosPorFechas(
                Date.valueOf(fechaInicio), Date.valueOf(fechaFin));

        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> fila : filas) {
            Object v = fila.get("costoTotal");
            if (v instanceof BigDecimal) {
                total = total.add((BigDecimal) v);
            }
        }

        byte[] contenido = crearPDF.generarTabla(
                "Ingresos del " + fechaInicio + " al " + fechaFin,
                filas,
                "Total de ingresos: Q " + total.setScale(2, RoundingMode.HALF_UP).toPlainString());
        return respuestaPdf(contenido, "ingresos");
    }

    // =====================================================================
    //  UTILIDADES
    // =====================================================================

    private String tituloMecanico(int idMecanico, String estado) {
        return "Órdenes del mecánico #" + idMecanico +
                ("TODOS".equalsIgnoreCase(estado) ? " (todos los estados)" : " - estado: " + estado);
    }

    private ResponseEntity<byte[]> respuestaPdf(byte[] contenido, String nombre) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombre + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido);
    }
}