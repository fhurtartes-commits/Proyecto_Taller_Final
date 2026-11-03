package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.RepuestoDAO;
import com.mycompany.mvctaller.modelo.Repuesto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/repuestos")
public class RepuestoControlador {

    private final RepuestoDAO repuestoDAO = new RepuestoDAO();

    @GetMapping
    public String listarRepuestos(Model model) {
        List<Repuesto> lista = repuestoDAO.listarTodos();
        model.addAttribute("repuestos", lista);
        model.addAttribute("repuesto", new Repuesto());
        return "repuesto"; 
    }

    @PostMapping("/guardar")
    public String guardarRepuesto(@ModelAttribute Repuesto repuesto) {
        repuestoDAO.insertar(repuesto);
        return "redirect:/repuestos";
    }

    @PostMapping("/actualizar")
    public String actualizarRepuesto(@ModelAttribute Repuesto repuesto) {
        repuestoDAO.actualizar(repuesto);
        return "redirect:/repuestos";
    }

    @PostMapping("/abastecer")
    public String abastecerRepuesto(@RequestParam("idRepuesto") int idRepuesto, @RequestParam("cantidad") int cantidad) {
        repuestoDAO.abastecer(idRepuesto, cantidad);
        return "redirect:/repuestos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRepuesto(@PathVariable("id") int id) {
        repuestoDAO.eliminar(id);
        return "redirect:/repuestos";
    }

    @GetMapping("/agotados")
    public String listarAgotados(Model model) {
        List<Repuesto> lista = repuestoDAO.listarAgotados();
        model.addAttribute("repuestos", lista);
        model.addAttribute("repuesto", new Repuesto());
        model.addAttribute("filtro", "Agotados");
        return "repuesto";
    }

    @GetMapping("/bajo-inventario")
    public String listarBajoInventario(Model model) {
        int limitePorDefecto = 5; 
        List<Repuesto> lista = repuestoDAO.listarBajoInventario(limitePorDefecto);
        model.addAttribute("repuestos", lista);
        model.addAttribute("repuesto", new Repuesto());
        model.addAttribute("filtro", "Bajo Inventario (<= " + limitePorDefecto + ")");
        return "repuesto";
    }
}