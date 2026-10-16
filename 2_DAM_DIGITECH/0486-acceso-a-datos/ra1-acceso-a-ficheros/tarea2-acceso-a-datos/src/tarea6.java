import java.io.RandomAccessFile;
import java.util.Scanner;

public class tarea6 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        try (RandomAccessFile raf = new RandomAccessFile("personas.dat", "rw")) {
            //Si está vacío, escribir 3 registros de 48 bytes (4 id + 40 nombre [20 chars] + 4 edad)
            if (raf.length() == 0) {
                String[] nombres = {"Laura", "Marcos", "David"};
                int[] edades = {22, 35, 29};
                for (int i = 0; i < 3; i++) {
                    raf.writeInt(i + 1);
                    StringBuffer sb = new StringBuffer(nombres[i]);
                    sb.setLength(20);
                    raf.writeChars(sb.toString());
                    raf.writeInt(edades[i]);
                }
            }

            // Pedir datos
            System.out.print("ID (1-3) y nueva edad: ");
            int idBuscado = sc.nextInt();
            int nuevaEdad = sc.nextInt();

            //recorrido saltando con seek(regInicio + 48)
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                long regInicio = raf.getFilePointer();
                if (raf.readInt() == idBuscado) {
                    // Mover el puntero al campo edad (inicio + 4 id + 40 nombre)
                    raf.seek(regInicio + 44);
                    raf.writeInt(nuevaEdad);

                    // Rebobinar al inicio del mismo registro para verificarlo
                    raf.seek(regInicio);
                    int id = raf.readInt();
                    String nombre = "";
                    for (int i = 0; i < 20; i++) nombre += raf.readChar();
                    int edad = raf.readInt();

                    System.out.println("Actualizado -> ID: " + id + " | Nombre: " + nombre.trim() + " | Edad: " + edad);
                    return;
                }
                raf.seek(regInicio + 48); // Saltar al siguiente registro si no coincide
            }
            System.out.println("ID no encontrado.");
        }
    }
}