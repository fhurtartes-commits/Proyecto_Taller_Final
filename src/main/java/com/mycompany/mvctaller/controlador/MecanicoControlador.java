package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.MecanicoDAO;
import com.mycompany.mvctaller.modelo.Mecanico;
import com.mycompany.mvctaller.modelo.Orden;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mecanicos")
public class MecanicoControlador {

    private final MecanicoDAO mecanicoDAO = new MecanicoDAO();

    @GetMapping
    public String listarMecanicos(Model model) {
        List<Mecanico> listaMecanicos = mecanicoDAO.listarTodos();
        model.addAttribute("mecanicos", listaMecanicos);
        model.addAttribute("mecanico", new Mecanico()); 
        
        return "mecanico";
    }

    @PostMapping("/guardar")
    public String guardarMecanico(@ModelAttribute Mecanico mecanico, RedirectAttributes redirectAttrs) {
        boolean exito = mecanicoDAO.insertar(mecanico);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Mecánico registrado exitosamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo registrar el mecánico. Verifique los datos e intente de nuevo.");
        }
        return "redirect:/mecanicos";
    }

    @PostMapping("/actualizar")
    public String actualizarMecanico(@ModelAttribute Mecanico mecanico, RedirectAttributes redirectAttrs) {
        boolean exito = mecanicoDAO.actualizar(mecanico);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Mecánico actualizado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Error al actualizar los datos del mecánico.");
        }
        return "redirect:/mecanicos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMecanico(@PathVariable("id") int idMecanico, RedirectAttributes redirectAttrs) {
        boolean exito = mecanicoDAO.eliminar(idMecanico);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Mecánico eliminado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar el mecánico porque tiene órdenes de servicio asociadas.");
        }
        return "redirect:/mecanicos";
    }

    @GetMapping("/ordenes/{id}")
    public String verOrdenesAsignadas(@PathVariable("id") int idMecanico, Model model) {
        Mecanico mecanico = mecanicoDAO.buscarporID(idMecanico);
        List<Orden> ordenesAsignadas = mecanicoDAO.obtenerOrdenesAsignadas(idMecanico);
        
        model.addAttribute("mecanico", mecanico);
        model.addAttribute("ordenesAsignadas", ordenesAsignadas);
        
        return "ordenes_mecanico";
    }
}