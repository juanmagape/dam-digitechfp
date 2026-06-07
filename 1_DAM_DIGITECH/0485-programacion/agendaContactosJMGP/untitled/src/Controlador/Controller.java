package Controlador;

import Modelo.Contacto;
import Vista.Vista;
import java.util.ArrayList;

public class Controller {
    private Vista vista;
    private ArrayList<Contacto> agenda;
    private int cantidadContactos;

    public Controller(Vista vista) {
        this.vista = vista;
        this.agenda = new ArrayList<>();
        this.cantidadContactos = 0;
    }

    public void Menu() {
        boolean salir = false;

        while (!salir) {
            int opcion = vista.mostrarMenu();

            switch (opcion) {
                case 1:
                    AñadirContacto();
                    break;
                case 2:
                    MostrarContactos();
                    break;
                case 3:
                    BuscarContacto();
                    break;
                case 4:
                    ModificarContacto();
                    break;
                case 5:
                    salir = true;
                    break;
            }
        }
    }

    public void AñadirContacto() {
        int numCon = vista.pedirCantidadContactos();

        if (cantidadContactos + numCon > 100) {
            System.out.println("No hay espacio para tantos contactos.");
            return;
        }

        for (int i = 0; i < numCon; i++) {
            String nombre = vista.pedirNombre();
            String apellido = vista.pedirApellido();
            int numTelf = vista.pedirTelefono();

            agenda.add(new Contacto(nombre, apellido, numTelf));
            cantidadContactos++;
        }
    }

    public void MostrarContactos() {
        vista.mostrarContactos(agenda);
    }

    public void BuscarContacto() {
        int busq = vista.pedirTipoBusqueda();

        if (busq == 1) {
            String busqNom = vista.pedirNombreBusqueda();
            for (Contacto c : agenda) {
                if (c.getName().equalsIgnoreCase(busqNom)) {
                    vista.mostrarMensaje(c.toString());
                }
            }
        } else {
            int busqNum = vista.pedirNumeroBusqueda();
            for (Contacto c : agenda) {
                if (c.getNumero() == busqNum) {
                    vista.mostrarMensaje(c.toString());
                }
            }
        }
    }

    public void ModificarContacto() {
        if (agenda.isEmpty()) {
            vista.mostrarMensaje("No hay contactos para modificar.");
            return;
        }

        vista.mostrarContactosEditar(agenda);
        int contactoEditar = vista.pedirIndiceEditar();

        if (contactoEditar >= 0 && contactoEditar < agenda.size()) {
            Contacto contacto = agenda.get(contactoEditar);

            vista.mostrarMensaje("Nuevo nombre:");
            contacto.setName(vista.pedirNombre());

            vista.mostrarMensaje("Nuevo apellido:");
            contacto.setApellido(vista.pedirApellido());

            vista.mostrarMensaje("Nuevo teléfono:");
            contacto.setNumero(vista.pedirTelefono());
        } else {
            vista.mostrarMensaje("Número de contacto no válido.");
        }
    }
}