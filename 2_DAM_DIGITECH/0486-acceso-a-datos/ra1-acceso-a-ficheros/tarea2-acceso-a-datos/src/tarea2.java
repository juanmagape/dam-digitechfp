import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class tarea2 {
    public static void main(String[] args) {
        File fichero = new File("nums.bin");
        Scanner sc = new Scanner(System.in);

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            raf.setLength(0);

            System.out.println("Introduce 5 números enteros:");
            for (int i = 0; i < 5; i++) {
                System.out.print("Número " + (i + 1) + ": ");
                int num = sc.nextInt();
                raf.writeInt(num);
            }

            // getFilePointer() tras escribir 5 enteros
            long posFinal = raf.getFilePointer();
            System.out.println("\nValor de getFilePointer() tras escribir los 5 números: " + posFinal + " bytes.");

            // devolver puntero al inicio
            raf.seek(0);

            System.out.println("\nLeyendo los 5 números del fichero desde el inicio:");
            for (int i = 0; i < 5; i++) {
                int valorLeido = raf.readInt();
                System.out.println("Número " + (i + 1) + ": " + valorLeido);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    //EL getfilerpointer tras escribir 5 ints, quedaría en el byte 20, ya que 5*4=20
}