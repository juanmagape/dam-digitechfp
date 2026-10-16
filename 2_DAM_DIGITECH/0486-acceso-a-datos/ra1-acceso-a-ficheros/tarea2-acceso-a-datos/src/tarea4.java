import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class tarea4 {
    public static void main(String[] args) {
        File fichero = new File("nombres.bin");
        String[] nombres = {"Jhon Pork", "Sahur", "BigY"};

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            raf.setLength(0);

            // Escribir los 3 nombres con writeUTF
            for (String nom : nombres) {
                raf.writeUTF(nom);
            }

            // Volver al inicio
            raf.seek(0);

            System.out.println("Nombres recuperados secuencialmente:");
            while (raf.getFilePointer() < raf.length()) {
                System.out.println(raf.readUTF());
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Explicación:
    // No se puede saltar directamente porque los registros NO tienen un tamaño fijo.
    // "Ana" ocupa 11 bytes (2 de cabecera + 9 del nombre) y "Sahur" ocupa 7 bytes (2 de cabecera + 5 letras).
    // Al medir cada nombre distinto, no existe una fórmula fija para calcular el offset con seek(),
    // por lo que estás obligado a leer el primero para saber dónde empieza el segundo.
}