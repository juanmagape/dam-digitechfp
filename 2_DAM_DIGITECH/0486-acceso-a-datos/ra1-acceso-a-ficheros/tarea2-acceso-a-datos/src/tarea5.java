import java.io.RandomAccessFile;

public class tarea5 {
    public static void main(String[] args) throws Exception {
        try (RandomAccessFile raf = new RandomAccessFile("personas_fijo.bin", "rw")) {
            raf.setLength(0);

            // Escribir 3 registros (4 bytes int + 20 bytes char = 24 bytes por registro)
            String[] nombres = {"Lucia", "Mateo", "Valentina"};

            for (int i = 0; i < 3; i++) {
                raf.writeInt(i + 1);
                StringBuffer sb = new StringBuffer(nombres[i]);
                sb.setLength(10);
                raf.writeChars(sb.toString());
            }

            //Saltar directo al 3er registro: (3 - 1) * 24 = 48
            raf.seek(2 * 24);

            // Leer id y los 10 caracteres del nombre
            int id = raf.readInt();
            String nombre = "";
            for (int i = 0; i < 10; i++) nombre += raf.readChar();

            System.out.println("ID: " + id + " | Nombre: " + nombre.trim());
        }
    }
}