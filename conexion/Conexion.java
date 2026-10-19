package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Ajusta credenciales para la conexion a DB
    private static final String URL = "jdbc:mysql://localhost:3306/DBPOS?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static Connection getConexion() {
        Connection con = null;
        try {
            // Registrar el Driver (Opcional en versiones modernas de Java, pero recomendado)
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
        return con;
    }
}
