package com.mycompany.sistemataller;

import dao.OrdenDAO;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OrdenDAO ordenDAO = new OrdenDAO();
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 8) {
            System.out.println("\nSISTEMA TALLER,GESTION DE ORDENES");
            System.out.println("1. Crear Orden");
            System.out.println("2. Iniciar Reparación");
            System.out.println("3. Registrar Servicio a Orden");
            System.out.println("4. Asignar Repuesto");
            System.out.println("5. Retirar Repuesto");
            System.out.println("6. Finalizar Orden y Generar Factura");
            System.out.println("7. Cancelar Orden");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese No. de Placa del Vehículo: ");
                    String placa = scanner.nextLine();
                    System.out.print("Ingrese ID del Mecánico: ");
                    int idMecanico = Integer.parseInt(scanner.nextLine());
                    ordenDAO.crearOrden(placa, idMecanico);
                    break;

                case 2:
                    System.out.print("Ingrese ID de la Orden a Iniciar: ");
                    int idOrdenInit = Integer.parseInt(scanner.nextLine());
                    ordenDAO.iniciarReparacion(idOrdenInit);
                    break;

                case 3:
                    System.out.print("Ingrese ID de la Orden: ");
                    int idOrdServ = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ingrese ID del Servicio: ");
                    int idServ = Integer.parseInt(scanner.nextLine());
                    ordenDAO.registrarServicio(idOrdServ, idServ);
                    break;

                case 4:
                    System.out.print("Ingrese ID de la Orden: ");
                    int idOrdRep = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ingrese ID del Repuesto (Bodega): ");
                    int idBodega = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ingrese Cantidad a Asignar: ");
                    int cantidad = Integer.parseInt(scanner.nextLine());
                    ordenDAO.asignarRepuesto(idOrdRep, idBodega, cantidad);
                    break;

                case 5:
                    System.out.print("Ingrese ID del Detalle de Repuesto (id_DetalleRepuesto): ");
                    int idDetalle = Integer.parseInt(scanner.nextLine());
                    ordenDAO.retirarRepuesto(idDetalle);
                    break;

                case 6:
                    System.out.print("Ingrese ID de la Orden a Finalizar: ");
                    int idOrdFin = Integer.parseInt(scanner.nextLine());
                    ordenDAO.finalizarOrden(idOrdFin);
                    break;

                case 7:
                    System.out.print("Ingrese ID de la Orden a Cancelar: ");
                    int idOrdCanc = Integer.parseInt(scanner.nextLine());
                    ordenDAO.cancelarOrden(idOrdCanc);
                    break;

                case 8:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }
}