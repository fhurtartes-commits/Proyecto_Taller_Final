package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.OrdenDAO;
import com.mycompany.mvctaller.modelo.Orden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ordenes")
public class OrdenControlador {

    @Autowired
    private OrdenDAO ordenDAO;

    @GetMapping
    public String listar(Model model) {
        if (!model.containsAttribute("orden")) {
            model.addAttribute("orden", new Orden());
        }
        return "ordenes";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam String noPlaca, @RequestParam int idMecanico, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.crearOrden(noPlaca, idMecanico);
            if (!ok) {
                ra.addFlashAttribute("error", "Error al momento de crear la orden");
            } else {
                ra.addFlashAttribute("mensaje", "Orden creada exitosamente en estado PENDIENTE.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/iniciar/{id}")
    public String iniciarReparacion(@PathVariable int id, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.iniciarReparacion(id);
            if (!ok) {
                ra.addFlashAttribute("error", "No se pudo iniciar la reparación (la orden no está pendiente o es inmutable).");
            } else {
                ra.addFlashAttribute("mensaje", "La orden #" + id + " cambió a estado EN PROCESO.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/registrar-servicio")
    public String registrarServicio(@RequestParam int idOrden, @RequestParam int idServicio, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.registrarServicio(idOrden, idServicio);
            if (!ok) {
                ra.addFlashAttribute("error", "Error al registrar el servicio en la orden.");
            } else {
                ra.addFlashAttribute("mensaje", "Servicio asignado correctamente a la orden #" + idOrden + ".");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/asignar-repuesto")
    public String asignarRepuesto(@RequestParam int idOrden, @RequestParam int idBodega, @RequestParam int cantidad, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.asignarRepuesto(idOrden, idBodega, cantidad);
            if (!ok) {
                ra.addFlashAttribute("error", "Stock insuficiente en bodega o la orden es inmutable.");
            } else {
                ra.addFlashAttribute("mensaje", "Repuesto asignado y stock descontado exitosamente.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/retirar-repuesto/{idDetalle}")
    public String retirarRepuesto(@PathVariable int idDetalle, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.retirarRepuesto(idDetalle);
            if (!ok) {
                ra.addFlashAttribute("error", "Error al retirar el repuesto del detalle.");
            } else {
                ra.addFlashAttribute("mensaje", "Repuesto retirado y devuelto al inventario exitosamente.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/finalizar/{id}")
    public String finalizarOrden(@PathVariable int id, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.finalizarOrden(id);
            if (!ok) {
                ra.addFlashAttribute("error", "No se pudo finalizar: Debe registrar al menos un servicio o la orden es inmutable.");
            } else {
                ra.addFlashAttribute("mensaje", "Orden #" + id + " finalizada y factura generada correctamente.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarOrden(@PathVariable int id, RedirectAttributes ra) {
        try {
            boolean ok = ordenDAO.cancelarOrden(id);
            if (!ok) {
                ra.addFlashAttribute("error", "No se pudo cancelar la orden.");
            } else {
                ra.addFlashAttribute("mensaje", "Orden #" + id + " CANCELADA y repuestos devueltos al inventario.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenes";
    }
}