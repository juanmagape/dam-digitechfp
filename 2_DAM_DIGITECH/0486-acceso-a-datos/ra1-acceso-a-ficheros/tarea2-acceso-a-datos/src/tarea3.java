import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class tarea3 {
    private static final int BYTES_POR_SLOT = 4;

    public static void main(String[] args) {
        File fichero = new File("slots.bin");
        int[] numeros = {10, 20, 30, 40, 50};

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            // Escribimos los números en slots de 4 bytes
            for (int num : numeros) {
                raf.writeInt(num);
            }

            // slot número 2 (índice base 0: slot 1)
            int slotBuscado = 2;
            long offset = (long) (slotBuscado - 1) * BYTES_POR_SLOT; // (2 - 1) * 4 = byte 4

            // Saltamos directamente sin leer el primer entero
            raf.seek(offset);
            int segundoValor = raf.readInt();

            System.out.println("Lectura directa del slot " + slotBuscado + ": " + segundoValor);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// explicación:
//como cada slot son 4 bytes siempre, el offset se consigue con la formula:

// offset = (numero slot - 1) * 4