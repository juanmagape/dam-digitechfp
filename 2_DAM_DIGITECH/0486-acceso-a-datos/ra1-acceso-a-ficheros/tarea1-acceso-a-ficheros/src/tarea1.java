import java.io.File;
import java.io.IOException;

public class tarea1 {
    public static void main(String[] args) {

        // Marco la ruta del directorio que voy a crear + donde estará el archivo
        File carpeta = new File("C:/AD/Ejercicios/miDirectorio");

        // Creo el la carpeta
        if (carpeta.mkdir()) {
            System.out.println("Carpeta creada con éxito");
        } else {
            System.out.println("Se ha producido un error o la carpeta ya existia");
        }

        // Se guarda el archivo, con la ruta y el nombre
        File archivo = new File(carpeta, "fichero_de_texto.txt");

        // Creamos el archivo
        try {
            if (archivo.createNewFile()) {
                System.out.println("archivo creado con éxito");
            } else {
                System.out.println("el archivo ya existia");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

