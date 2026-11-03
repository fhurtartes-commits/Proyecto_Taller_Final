package com.mycompany.mvctaller.controlador;

import com.mycompany.mvctaller.dao.ServiciosDAO;
import com.mycompany.mvctaller.modelo.Servicios;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServiciosControlador {

    private final ServiciosDAO serviciosDAO = new ServiciosDAO();

    @GetMapping
    public String listarServicios(Model model) {
        List<Servicios> listaServicios = serviciosDAO.listarTodos();
        model.addAttribute("servicios", listaServicios);
        return "servicios";
    }
}
