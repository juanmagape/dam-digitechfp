import java.io.*;
import java.util.Scanner;

public class tarea4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] listaEmpleados = {
                "1, Carlos Perez",
                "2, Maria Gomez",
                "3, Juan Lopez",
                "4, Ana Fernandez",
                "5, David Martinez",
                "6, Laura Sanchez",
                "7, Jorge Romero",
                "8, Elena Diaz",
                "9, Miguel Torres",
                "10, Sofia Ramirez"
        };

        File archivoEmpleados = new File("Empleados.txt");
        int opcion = 0;

        while (opcion != 4) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1 - FileWriter + FileReader");
            System.out.println("2 - BufferedWriter + BufferedReader");
            System.out.println("3 - Eliminar Archivo");
            System.out.println("4 - Salir");
            System.out.print("Selecciona una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\n=== FILEWRITER ===");
                    try (FileWriter escritorBasico = new FileWriter(archivoEmpleados)) {
                        for (String empleado : listaEmpleados) {
                            escritorBasico.write(empleado + "\n");
                        }
                        System.out.println("Fichero escrito con éxito.");
                    } catch (IOException excepcion) {
                        System.err.println(excepcion.getMessage());
                    }

                    System.out.println("\n=== FILEREADER ===");
                    try (FileReader lectorBasico = new FileReader(archivoEmpleados)) {
                        int codigoCaracter;
                        while ((codigoCaracter = lectorBasico.read()) != -1) {
                            System.out.print((char) codigoCaracter);
                        }
                    } catch (IOException excepcion) {
                        System.err.println(excepcion.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("\n=== BUFFEREDWRITER ===");
                    try (BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(archivoEmpleados))) {
                        for (String empleado : listaEmpleados) {
                            escritorBuffer.write(empleado);
                            escritorBuffer.newLine();
                        }
                        System.out.println("Fichero escrito con éxito.");
                    } catch (IOException excepcion) {
                        System.err.println(excepcion.getMessage());
                    }

                    System.out.println("\n=== BUFFEREDREADER ===");
                    try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(archivoEmpleados))) {
                        String lineaTexto;
                        while ((lineaTexto = lectorBuffer.readLine()) != null) {
                            System.out.println(lineaTexto);
                        }
                    } catch (IOException excepcion) {
                        System.err.println(excepcion.getMessage());
                    }
                    break;

                case 3:
                    if (archivoEmpleados.exists()) {
                        if (archivoEmpleados.delete()) {
                            System.out.println("Archivo 'Empleados.txt' eliminado con éxito.");
                        } else {
                            System.out.println("No se pudo eliminar el archivo.");
                        }
                    } else {
                        System.out.println("El archivo no existe.");
                    }
                    break;

                case 4:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }

        sc.close();
    }
}