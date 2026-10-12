import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class tarea1 {
    private static final int long_apellido = 10;
    private static final int long_reg = 4 + (long_apellido * 2) + 4 + 8;

    public static void main(String[] args) {
        File fichero = new File("empleados.dat");

        if (fichero.exists()) {
            fichero.delete();
        }

        String[] apellidos = {"RONALDO", "OLISE", "NEVES", "ALONSO"};
        int[] departamentos = {10, 20, 20, 30};
        double[] salarios = {2039.50, 1740.10, 950.75, 700.20};


        // ESCRITURA EN ARCHIVO
        try (RandomAccessFile randomFile = new RandomAccessFile(fichero, "rw")) {
            System.out.println("Escribiendo empleados...");

            for (int i = 0; i < apellidos.length; i++) {
                int id = i + 1;

                long inicioRegistro = randomFile.getFilePointer();
                StringBuffer buffer = new StringBuffer(apellidos[i]);
                buffer.setLength(long_apellido);

                randomFile.writeInt(id);
                randomFile.writeChars(buffer.toString());
                randomFile.writeInt(departamentos[i]);
                randomFile.writeDouble(salarios[i]);

                long finRegistro = randomFile.getFilePointer();
                long bytesOcupados = finRegistro - inicioRegistro;

                System.out.printf("Empleado ID %d escrito en bytes [%d - %d]. Tamaño: %d bytes.%n", id, inicioRegistro, finRegistro - 1, bytesOcupados);
            }

            System.out.println("\nTamaño total del archivo: " + randomFile.length() + " bytes.");
            System.out.println("Registros calculados: " + (randomFile.length() / long_reg));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // LECTURA DE ARCHVIO
        try (RandomAccessFile randomFile = new RandomAccessFile(fichero, "rw")) {
            System.out.println("Leyendo registros...");

            randomFile.seek(0);
            while (randomFile.getFilePointer() < randomFile.length()) {
                long posInicio = randomFile.getFilePointer();

                int id = randomFile.readInt();

                char[] charsApellido = new char[long_apellido];
                for (int c = 0; c < long_apellido; c++) {
                    charsApellido[c] = randomFile.readChar();
                }
                String apellido = new String(charsApellido).trim();

                int departamento = randomFile.readInt();
                double salario = randomFile.readDouble();

                System.out.printf("[Byte %03d] ID: %d | Apellido: %-10s | Dept: %d | Salario: %.2f €%n", posInicio, id, apellido, departamento, salario);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

