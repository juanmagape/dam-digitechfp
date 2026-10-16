import java.io.RandomAccessFile;
import java.util.Scanner;

public class tarea7 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        // Construir el índice personas.idx desde personas.dat
        try (RandomAccessFile dat = new RandomAccessFile("personas.dat", "r");
             RandomAccessFile idx = new RandomAccessFile("personas.idx", "rw")) {

            idx.setLength(0);
            int i = 0;

            while (dat.getFilePointer() < dat.length()) {
                int id = dat.readInt();
                long offset = i * 48L;

                idx.writeInt(id);
                idx.writeLong(offset);

                dat.seek(offset + 48);
                i++;
            }
        }

        System.out.print("Introduce el ID a buscar: ");
        int idBuscado = sc.nextInt();

        //Búsqueda lineal en el índice y salto directo a personas.dat
        try (RandomAccessFile idx = new RandomAccessFile("personas.idx", "r");
             RandomAccessFile dat = new RandomAccessFile("personas.dat", "r")) {

            boolean encontrado = false;

            while (idx.getFilePointer() < idx.length()) {
                int idActual = idx.readInt();
                long offset = idx.readLong();

                if (idActual == idBuscado) {
                    encontrado = true;

                    dat.seek(offset);
                    int id = dat.readInt();
                    String nombre = "";
                    for (int c = 0; c < 20; c++) nombre += dat.readChar();
                    int edad = dat.readInt();

                    System.out.printf("Registro en offset %d -> ID: %d | Nombre: %s | Edad: %d%n",
                            offset, id, nombre.trim(), edad);
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("ID no encontrado en el índice.");
            }
        }
    }
}