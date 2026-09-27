import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class tarea2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Rutas de carpetas
        File ruta = new File("C:/AD/Ejercicios");
        File miDirectorio = new File(ruta, "miDirectorio");
        File nuevoDirectorio = new File(ruta, "nuevoDirectorio");

        // Fihceros
        File archivo1 = new File(miDirectorio, "fichero_de_texto.txt");
        File archivo2 = new File(nuevoDirectorio, "fichero_de_texto2.txt");

        String[] provincias = {
                "Almería",
                "Cádiz",
                "Córdoba",
                "Granada",
                "Huelva",
                "Jaén",
                "Málaga",
                "Sevilla"
        };

        int opcion = 0;

        // Creación de bucle para las diferentes opciones con el meenú.
        while (opcion != 5) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("[1] Crear directorio 'nuevoDirectorio' en C:\\AD\\Ejercicios");
            System.out.println("[2] Crear 'fichero_de_texto2.txt' en 'nuevoDirectorio'");
            System.out.println("[3] Eliminar 'fichero_de_texto.txt'");
            System.out.println("[4] Eliminar directorio 'nuevoDirectorio'");
            System.out.println("[5] Salir");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    if (nuevoDirectorio.mkdir()) {
                        System.out.println("Directorio 'nuevoDirectorio' creado con éxito.");
                    } else {
                        System.out.println("No se pudo crear o el directorio ya existía.");
                    }
                    break;

                case 2:
                    try {
                        if (archivo2.createNewFile()) {
                            System.out.println("Fichero 'fichero_de_texto2.txt' creado con éxito.");
                        } else {
                            System.out.println("El fichero ya existía.");
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    break;

                case 3:
                    if (archivo1.delete()) {
                        System.out.println("Fichero 'fichero_de_texto.txt' eliminado con éxito.");
                    } else {
                        System.out.println("No se pudo eliminar: el archivo no existe.");
                    }
                    break;

                case 4:
                    //IF para eliminar los archivos internos y que así no salga error con el delete
                    if (nuevoDirectorio.exists()) {
                        File[] elementos = nuevoDirectorio.listFiles();

                        if (elementos != null) {
                            for (File f : elementos) {
                                f.delete();
                            }
                        }
                    }

                    if (nuevoDirectorio.delete()) {
                        System.out.println("Directorio 'nuevoDirectorio' eliminado con éxito.");
                    } else {
                        System.out.println("No se pudo eliminar: el directorio no existe.");
                    }
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }

        sc.close();
    }
}
