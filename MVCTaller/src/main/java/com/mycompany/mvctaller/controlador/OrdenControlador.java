package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.OrdenDAO;
import com.mycompany.mvctaller.modelo.Orden;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

prueba

@Controller
@RequestMapping("/ordenes")
public class OrdenControlador {

    private final OrdenDAO ordenDAO = new OrdenDAO();

    // Convierte el true/false que devuelve el DAO en un mensaje para la vista.
    private void mensaje(RedirectAttributes flash, boolean ok, String exito, String fallo) {
        if (ok) {
            flash.addFlashAttribute("ok", exito);
        } else {
            flash.addFlashAttribute("error", fallo + " (el motivo exacto sale en la consola de NetBeans)");
        }
    }

    // GET /ordenes  ó  /ordenes?estado=PENDIENTE  -> tabla de órdenes
    @GetMapping
    public String listar(@RequestParam(value = "estado", required = false) String estado, Model model) {
        model.addAttribute("ordenes", ordenDAO.listar(estado));
        model.addAttribute("estadoSel", estado == null ? "" : estado);
        return "ordenes/lista";
    }

    // GET /ordenes/nueva  -> formulario
    @GetMapping("/nueva")
    public String formNueva() {
        return "ordenes/nueva";
    }

    // POST /ordenes/nueva  -> DAO.crearOrden
    @PostMapping("/nueva")
    public String crear(@RequestParam("placa") String placa,
                        @RequestParam("idMecanico") int idMecanico,
                        RedirectAttributes flash) {
        if (placa.isBlank()) {
            flash.addFlashAttribute("error", "La placa es obligatoria.");
            return "redirect:/ordenes/nueva";
        }
        boolean ok = ordenDAO.crearOrden(placa.trim().toUpperCase(), idMecanico);
        mensaje(flash, ok, "Orden creada en estado PENDIENTE.",
                "No se pudo crear la orden (¿existen la placa y el mecánico?)");
        return ok ? "redirect:/ordenes" : "redirect:/ordenes/nueva";
    }

    // GET /ordenes/5  -> detalle de la orden con sus servicios y repuestos
    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") int id, Model model, RedirectAttributes flash) {
        Orden orden = ordenDAO.buscarPorId(id);
        if (orden == null) {
            flash.addFlashAttribute("error", "La orden " + id + " no existe.");
            return "redirect:/ordenes";
        }
        model.addAttribute("orden", orden);
        model.addAttribute("servicios", ordenDAO.serviciosDeOrden(id));
        model.addAttribute("repuestos", ordenDAO.repuestosDeOrden(id));
        return "ordenes/detalle";
    }

    // POST /ordenes/5/iniciar  -> DAO.iniciarReparacion  (PENDIENTE -> EN PROCESO)
    @PostMapping("/{id}/iniciar")
    public String iniciar(@PathVariable("id") int id, RedirectAttributes flash) {
        mensaje(flash, ordenDAO.iniciarReparacion(id),
                "La orden pasó a EN PROCESO.", "No se pudo iniciar la reparación");
        return "redirect:/ordenes/" + id;
    }

    // POST /ordenes/5/servicio  -> DAO.registrarServicio
    @PostMapping("/{id}/servicio")
    public String agregarServicio(@PathVariable("id") int id,
                                  @RequestParam("idServicio") int idServicio,
                                  RedirectAttributes flash) {
        mensaje(flash, ordenDAO.registrarServicio(id, idServicio),
                "Servicio registrado.", "No se pudo registrar el servicio (¿existe ese ID de servicio?)");
        return "redirect:/ordenes/" + id;
    }

    // POST /ordenes/5/repuesto  -> DAO.asignarRepuesto (valida stock y descuenta, en una transacción)
    @PostMapping("/{id}/repuesto")
    public String agregarRepuesto(@PathVariable("id") int id,
                                  @RequestParam("idBodega") int idBodega,
                                  @RequestParam("cantidad") int cantidad,
                                  RedirectAttributes flash) {
        if (cantidad <= 0) {
            flash.addFlashAttribute("error", "La cantidad debe ser mayor a cero.");
        } else {
            mensaje(flash, ordenDAO.asignarRepuesto(id, idBodega, cantidad),
                    "Repuesto asignado y descontado del inventario.",
                    "No se pudo asignar el repuesto (¿stock insuficiente o repuesto inexistente?)");
        }
        return "redirect:/ordenes/" + id;
    }

    // POST /ordenes/5/repuesto/12/retirar  -> DAO.retirarRepuesto (devuelve unidades al inventario)
    @PostMapping("/{id}/repuesto/{idDetalle}/retirar")
    public String retirarRepuesto(@PathVariable("id") int id,
                                  @PathVariable("idDetalle") int idDetalle,
                                  RedirectAttributes flash) {
        mensaje(flash, ordenDAO.retirarRepuesto(idDetalle),
                "Repuesto retirado; las unidades volvieron al inventario.",
                "No se pudo retirar el repuesto");
        return "redirect:/ordenes/" + id;
    }

    // POST /ordenes/5/finalizar  -> DAO.finalizarOrden (calcula el total y genera la factura)
    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable("id") int id, RedirectAttributes flash) {
        mensaje(flash, ordenDAO.finalizarOrden(id),
                "Orden FINALIZADA. Se generó el comprobante.",
                "No se pudo finalizar (¿tiene al menos un servicio registrado?)");
        return "redirect:/ordenes/" + id;
    }

    // POST /ordenes/5/cancelar  -> DAO.cancelarOrden (reintegra repuestos al inventario)
    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable("id") int id, RedirectAttributes flash) {
        mensaje(flash, ordenDAO.cancelarOrden(id),
                "Orden CANCELADA; los repuestos regresaron al inventario.",
                "No se pudo cancelar la orden");
        return "redirect:/ordenes/" + id;
    }
}