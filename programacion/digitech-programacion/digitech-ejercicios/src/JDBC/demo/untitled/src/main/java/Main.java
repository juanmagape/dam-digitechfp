import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String url = "jdbc:mysql://localhost:3306/hipermercado";
        String usuario = "root";
        String contrasena = "root";
        Connection conexion;

        try {
            conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("Conectado a la base de datos: Hipermercado");
        } catch (SQLException e) {
            System.out.println("Error en la conexión con la base de datos: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("1. Devolver un producto por su ID");
            System.out.println("2. Crear un producto nuevo");
            System.out.println("3. Filtrar productos por su categoría");
            System.out.println("4. Eliminar un producto");
            System.out.println("5. Mostrar los productos de una marca en concreto");
            System.out.println("6. Mostrar productos a punto de caducar");
            System.out.println("7. Mostrar las novedades");
            System.out.println("8. Mostrar productos sin gluten");
            System.out.println("9. Mostrar productos veganos");
            System.out.println("10. Mostrar productos agotados");
            System.out.println("11. Salir");
            System.out.println("=======================================================\n");

            System.out.print("Elige una opción: ");
            int opcion;
            try {
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Por favor, introduce un número válido.");
                sc.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    BusquedaID(sc, conexion);
                    break;
                case 2:
                    CrearProducto(sc, conexion);
                    break;
                case 3:
                    FiltrarCategoria(sc, conexion);
                    break;
                case 4:
                    EliminarProducto(sc, conexion);
                    break;
                case 5:
                    FiltrarMarca(sc, conexion);
                    break;
                case 6:
                    ProductosPorCaducar(sc, conexion);
                    break;
                case 7:
                    MostrarNovedades(sc, conexion);
                    break;
                case 8:
                    MostrarSinGluten(sc, conexion);
                    break;
                case 9:
                    MostrarVeganos(sc, conexion);
                    break;
                case 10:
                    MostrarAgotados(sc, conexion);
                    break;
                case 11:
                    System.out.println("Saliendo de la aplicación...");
                    try { if (conexion != null) conexion.close(); } catch (SQLException e) {}
                    System.exit(0);
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }

    public static void BusquedaID(Scanner sc, Connection conexion) {
        try {
            System.out.println("Introduce el ID del producto que quieres buscar:");
            int idProducto = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT * FROM productos WHERE id_producto = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);

            ResultSet rs = ps.executeQuery();

            System.out.println("=================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
            System.out.println("=================================================================");

            while (rs.next()) {
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=================================================================");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar el producto: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error de entrada de datos. Comprueba los valores introducidos.");
            sc.nextLine();
        }
    }

    public static void CrearProducto(Scanner sc, Connection conexion) {
        try {
            System.out.println("--- Crear Nuevo Producto ---");

            System.out.print("Nombre del producto: ");
            String nombre = sc.nextLine();
            System.out.print("Descripción: ");
            String descripcion = sc.nextLine();
            System.out.print("Precio: ");
            double precio = sc.nextDouble();
            System.out.print("ID de la sección: ");
            int id_seccion = sc.nextInt();
            System.out.print("ID de la marca: ");
            int id_marca = sc.nextInt();
            System.out.print("Precio por Kg o Litro: ");
            int precio_kg_l = sc.nextInt();
            System.out.print("Peso o volumen (ml): ");
            double peso_ml = sc.nextDouble();
            System.out.print("Unidad de medida (ID): ");
            int unidad_medida = sc.nextInt();
            System.out.print("Stock inicial: ");
            int stock = sc.nextInt();
            System.out.print("Descuento (%): ");
            int descuento_pct = sc.nextInt();
            System.out.print("¿Es novedad? (true/false): ");
            boolean es_novedad = sc.nextBoolean();
            System.out.print("¿Es bio? (true/false): ");
            boolean es_bio = sc.nextBoolean();
            System.out.print("¿Sin gluten? (true/false): ");
            boolean sin_gluten = sc.nextBoolean();
            System.out.print("¿Es vegano? (true/false): ");
            boolean vegano = sc.nextBoolean();
            System.out.print("Calorías por 100g: ");
            int calorias_100g = sc.nextInt();
            sc.nextLine();
            System.out.print("Fecha de caducidad (AAAA-MM-DD): ");
            String fecha_caducidad = sc.nextLine();
            System.out.print("Código de barras: ");
            int codigo_barras = sc.nextInt();
            sc.nextLine();

            String sql = "INSERT INTO productos (nombre, descripcion, precio, id_seccion, id_marca, precio_kg_l, peso_ml, unidad_medida, stock, descuento_pct, es_novedad, es_bio, sin_gluten, vegano, calorias_100g, fecha_caducidad, codigo_barras, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setInt(4, id_seccion);
            ps.setInt(5, id_marca);
            ps.setInt(6, precio_kg_l);
            ps.setDouble(7, peso_ml);
            ps.setInt(8, unidad_medida);
            ps.setInt(9, stock);
            ps.setInt(10, descuento_pct);
            ps.setBoolean(11, es_novedad);
            ps.setBoolean(12, es_bio);
            ps.setBoolean(13, sin_gluten);
            ps.setBoolean(14, vegano);
            ps.setInt(15, calorias_100g);
            ps.setString(16, fecha_caducidad);
            ps.setInt(17, codigo_barras);

            if (ps.executeUpdate() > 0) {
                System.out.println("Producto insertado correctamente.");
            } else {
                System.out.println("Error al insertar el producto.");
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en la base de datos al crear el producto: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error de entrada de datos. Comprueba los valores introducidos.");
            sc.nextLine();
        }
    }

    public static void FiltrarCategoria(Scanner sc, Connection conexion) {
        try {
            System.out.println("Introduce el nombre de la sección por la que quieres buscar:");
            String nombreSeccion = sc.nextLine();

            String sql = "SELECT p.id_producto, p.nombre, p.precio, p.stock, p.descripcion, s.nombre AS nombre_seccion FROM productos p INNER JOIN secciones s ON p.id_seccion = s.id_seccion WHERE s.nombre = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreSeccion);
            ResultSet rs = ps.executeQuery();

            System.out.println("=========================================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s | %-15s%n", "ID Producto", "Nombre", "Precio", "Stock", "Sección");
            System.out.println("=========================================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d | %-15s%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"), rs.getString("nombre_seccion"));
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=========================================================================================");
            }

            if (!hayResultados) {
                System.out.println("No se han encontrado productos para la sección: " + nombreSeccion);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al filtrar por categoría: " + e.getMessage());
        }
    }

    public static void EliminarProducto(Scanner sc, Connection conexion) {
        try {
            System.out.println("Introduce el ID del producto que quieres eliminar:");
            int idProducto = sc.nextInt();
            sc.nextLine();

            String sqlSelect = "SELECT * FROM productos WHERE id_producto = ?";
            PreparedStatement psSelect = conexion.prepareStatement(sqlSelect);
            psSelect.setInt(1, idProducto);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                System.out.println("=========================================================================================");
                System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
                System.out.println("=========================================================================================");
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", idProducto, rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=========================================================================================");

                System.out.println("¿Estás seguro de que quieres eliminar este producto? (S/N):");
                if (sc.nextLine().equalsIgnoreCase("S")) {
                    String sqlDelete = "DELETE FROM productos WHERE id_producto = ?";
                    PreparedStatement psDelete = conexion.prepareStatement(sqlDelete);
                    psDelete.setInt(1, idProducto);

                    if (psDelete.executeUpdate() > 0) {
                        System.out.println("Producto eliminado correctamente.");
                    } else {
                        System.out.println("Hubo un problema y no se pudo eliminar el producto.");
                    }
                    psDelete.close();
                } else {
                    System.out.println("Operación cancelada. El producto no ha sido eliminado.");
                }
            } else {
                System.out.println("No se ha encontrado ningún producto con el ID: " + idProducto);
            }
            rs.close();
            psSelect.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error de entrada de datos.");
            sc.nextLine();
        }
    }

    public static void FiltrarMarca(Scanner sc, Connection conexion) {
        try {
            System.out.println("Introduce el nombre de la marca por la que quieres buscar:");
            String nombreMarca = sc.nextLine();

            String sql = "SELECT p.id_producto, p.nombre, p.precio, p.stock, p.descripcion, m.nombre AS nombre_marca FROM productos p INNER JOIN marcas m ON p.id_marca = m.id_marca WHERE m.nombre = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreMarca);
            ResultSet rs = ps.executeQuery();

            System.out.println("=========================================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s | %-15s%n", "ID Producto", "Nombre", "Precio", "Stock", "Marca");
            System.out.println("=========================================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d | %-15s%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"), rs.getString("nombre_marca"));
                System.out.println("-----------------------------------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=========================================================================================");
            }

            if (!hayResultados) {
                System.out.println("No se han encontrado productos para la marca: " + nombreMarca);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al filtrar por marca: " + e.getMessage());
        }
    }

    public static void ProductosPorCaducar(Scanner sc, Connection conexion) {
        try {
            System.out.println("Introduce la fecha límite de caducidad (AAAA-MM-DD):");
            String fecha = sc.nextLine();

            String sql = "SELECT * FROM productos WHERE fecha_caducidad <= ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, fecha);
            ResultSet rs = ps.executeQuery();

            System.out.println("=================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
            System.out.println("=================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Caducidad: " + rs.getString("fecha_caducidad") + " | Descripción: " + rs.getString("descripcion"));
                System.out.println("=================================================================");
            }

            if (!hayResultados) {
                System.out.println("No se han encontrado productos que caduquen antes de esa fecha.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    public static void MostrarNovedades(Scanner sc, Connection conexion) {
        try {
            String sql = "SELECT * FROM productos WHERE es_novedad = true";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("=================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
            System.out.println("=================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=================================================================");
            }

            if (!hayResultados) {
                System.out.println("No hay productos marcados como novedad.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    public static void MostrarSinGluten(Scanner sc, Connection conexion) {
        try {
            String sql = "SELECT * FROM productos WHERE sin_gluten = true";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("=================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
            System.out.println("=================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=================================================================");
            }

            if (!hayResultados) {
                System.out.println("No hay productos marcados sin gluten.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    public static void MostrarVeganos(Scanner sc, Connection conexion) {
        try {
            String sql = "SELECT * FROM productos WHERE vegano = true";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("=================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
            System.out.println("=================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=================================================================");
            }

            if (!hayResultados) {
                System.out.println("No hay productos marcados como veganos.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    public static void MostrarAgotados(Scanner sc, Connection conexion) {
        try {
            String sql = "SELECT * FROM productos WHERE stock = 0";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("=================================================================");
            System.out.printf("%-12s | %-30s | %-8s | %-5s%n", "ID Producto", "Nombre", "Precio", "Stock");
            System.out.println("=================================================================");

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-12d | %-30s | %-8.2f | %-5d%n", rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("=================================================================");
            }

            if (!hayResultados) {
                System.out.println("No hay productos agotados actualmente.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
    }
}