import java.sql.*;
import java.util.Scanner;

public class CrudJdbc {
    // Cambia estos datos según tu MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/crud_java";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n=== CRUD JDBC ===");
            System.out.println("1. Crear producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Actualizar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> crear();
                case 2 -> listar();
                case 3 -> actualizar();
                case 4 -> eliminar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void crear() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Precio: ");
        double precio = sc.nextDouble();

        String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.executeUpdate();
            System.out.println("Producto agregado ✅");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listar() {
        String sql = "SELECT * FROM productos";
        try (Connection con = conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nLista de productos:");
            while (rs.next()) {
                System.out.printf("%d - %s ($%.2f)%n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizar() {
        listar();
        System.out.print("ID a actualizar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo precio: ");
        double precio = sc.nextDouble();

        String sql = "UPDATE productos SET nombre=?, precio=? WHERE id=?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Producto actualizado y nuevo ✅");
            else System.out.println("No se encontró el producto ❌");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminar() {
        listar();
        System.out.print("ID a eliminar: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Producto eliminado ✅");
            else System.out.println("No se encontró el producto ❌");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
