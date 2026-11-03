package com.mycompany.mvctaller.controlador;
 
import com.mycompany.mvctaller.dao.ClientesDAO;
import com.mycompany.mvctaller.modelo.Clientes;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/clientes")
public class ClientesControlador {
 
    @Autowired
    private ClientesDAO clientesDAO;
 
    @GetMapping
    public String listar(@RequestParam(required = false) String nit, Model model) {
        List<Clientes> clientes = new ArrayList<>();
        try {
            if (nit != null && !nit.trim().isEmpty()) {
                Clientes encontrado = clientesDAO.buscarPorNit(nit);
                if (encontrado == null) {
                    model.addAttribute("error", "No se encontró ningún cliente con el NIT " + nit.trim() + ".");
                } else {
                    clientes.add(encontrado);
                }
            } else {
                clientes = clientesDAO.listarTodos();
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("nitBuscado", nit);
        return "clientes";
    }
 
    @PostMapping("/crear")
    public String crear(@RequestParam String nombre, @RequestParam String nit,
                        @RequestParam(required = false) String telefono, RedirectAttributes ra) {
        try {
            boolean ok = clientesDAO.insertar(new Clientes(0, nombre, nit, telefono));
            if (!ok) {
                ra.addFlashAttribute("error", "Error al momento de registrar el cliente.");
            } else {
                ra.addFlashAttribute("mensaje", "Cliente registrado exitosamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }
 
    @PostMapping("/modificar")
    public String modificar(@RequestParam int idCliente, @RequestParam String nombre,
                            @RequestParam String nit, @RequestParam(required = false) String telefono,
                            RedirectAttributes ra) {
        try {
            boolean ok = clientesDAO.actualizar(new Clientes(idCliente, nombre, nit, telefono));
            if (!ok) {
                ra.addFlashAttribute("error", "No se pudo modificar el cliente (no existe o hubo un error).");
            } else {
                ra.addFlashAttribute("mensaje", "Cliente #" + idCliente + " modificado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }
 
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes ra) {
        try {
            boolean ok = clientesDAO.eliminar(id);
            if (!ok) {
                ra.addFlashAttribute("error", "No se pudo eliminar el cliente (no existe o hubo un error).");
            } else {
                ra.addFlashAttribute("mensaje", "Cliente #" + id + " eliminado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }
}