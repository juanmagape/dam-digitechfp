package Vista;

import Modelo.Contacto;

import java.util.ArrayList;
import java.util.Scanner;

public class Vista {
    private Scanner print = new Scanner(System.in);

    public int mostrarMenu() {
        System.out.println("\n===================== AGENDA DE CONTACTOS =====================\n");
        System.out.println("Escoge un número (1 - 4)\n");
        System.out.println("1: Añadir contactos");
        System.out.println("2: Mostrar contactos");
        System.out.println("3: Buscar contacto");
        System.out.println("4: Modificar contacto");
        System.out.println("5: Salir");

        int opcionEscogida = print.nextInt();
        print.nextLine();
        return opcionEscogida;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public int pedirCantidadContactos() {
        System.out.println("Cuantos contactos quieres añadir? (MAX 100, MIN 1)");
        int num = print.nextInt();
        print.nextLine();
        return num;
    }

    public String pedirNombre() {
        System.out.println("Introduce el nombre:");
        return print.nextLine();
    }

    public String pedirApellido() {
        System.out.println("Introduce el apellido:");
        return print.nextLine();
    }

    public int pedirTelefono() {
        System.out.println("Introduce el teléfono:");
        int telf = print.nextInt();
        print.nextLine();
        return telf;
    }

    public void mostrarContactos(ArrayList<Contacto> contactos) {
        for (int i = 0; i < contactos.size(); i++) {
            System.out.println(contactos.get(i));
        }
    }

    public int pedirTipoBusqueda() {
        System.out.println("Quieres buscar el contacto por nombre o por número? (1 - 2)");
        int busq = print.nextInt();
        print.nextLine();
        return busq;
    }

    public String pedirNombreBusqueda() {
        System.out.println("Introduce el nombre a buscar");
        return print.nextLine();
    }

    public int pedirNumeroBusqueda() {
        System.out.println("Introduce el número de telefono");
        int num = print.nextInt();
        print.nextLine();
        return num;
    }

    public void mostrarContactosEditar(ArrayList<Contacto> contactos) {
        for (int i = 0; i < contactos.size(); i++) {
            System.out.println(i + ": " + contactos.get(i));
        }
    }

    public int pedirIndiceEditar() {
        System.out.println("Introduce el número del contacto que quieres editar:");
        int num = print.nextInt();
        print.nextLine();
        return num;
    }
}
