package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.VehiculosDAO;
import com.mycompany.mvctaller.modelo.Vehiculos;
import com.mycompany.mvctaller.modelo.Orden;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vehiculos")
public class VehiculosControlador {

    private final VehiculosDAO vehiculosDAO = new VehiculosDAO();
    
    @GetMapping
    public String listarVehiculos(Model model) {
        List<Vehiculos> listaVehiculos = vehiculosDAO.listarTodos();
        model.addAttribute("vehiculos", listaVehiculos);
        model.addAttribute("vehiculo", new Vehiculos());
        
        return "vehiculos"; 
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculos vehiculo, RedirectAttributes redirectAttrs) {
        boolean exito = vehiculosDAO.insertar(vehiculo);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Vehículo registrado exitosamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo registrar el vehículo. Verifique si la placa ya existe o si el ID del cliente es válido.");
        }
        return "redirect:/vehiculos";
    }

    @PostMapping("/actualizar")
    public String actualizarVehiculo(@ModelAttribute Vehiculos vehiculo, RedirectAttributes redirectAttrs) {
        boolean exito = vehiculosDAO.actualizar(vehiculo);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Vehículo actualizado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Error al actualizar los datos del vehículo.");
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable("id") String noPlaca, RedirectAttributes redirectAttrs) {
        boolean exito = vehiculosDAO.eliminar(noPlaca);
        if (exito) {
            redirectAttrs.addFlashAttribute("exito", "Vehículo eliminado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar el vehículo porque tiene órdenes de servicio registradas.");
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/historial/{id}")
    public String verHistorialOrdenes(@PathVariable("id") String noPlaca, Model model) {
        Vehiculos vehiculo = vehiculosDAO.buscarporID(noPlaca);
        List<Orden> historial = vehiculosDAO.obtenerHistorialOrdenes(noPlaca);
        
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("historialOrdenes", historial);
        
        return "historial_vehiculo";
    }
}