package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexion.Conexion;
import modelo.Producto;

public class ProductoDAO {

    //Metodo para registrar un nuevo producto
    public boolean registrarProducto(Producto prod){

        String sql = "INSERT INTO productos (codigo_barras, nombre, precio_venta, stock) VALUES (?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, prod.getCodigoBarras());
            ps.setString(2, prod.getNombre());
            ps.setDouble(3, prod.getPrecioVenta());
            ps.setInt(4, prod.getStock());
            
            int resultado = ps.executeUpdate();
            return resultado > 0; // Retorna true si se insertó con éxito
            
        } catch (SQLException e) {
            System.out.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }

    // 2. Método para BUSCAR un producto (Sirve para Pricechecker y para agregar a Ventas)
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo_barras = ?";
        Producto prod = null;
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prod = new Producto();
                    prod.setId(rs.getInt("id"));
                    prod.setCodigoBarras(rs.getString("codigo_barras"));
                    prod.setNombre(rs.getString("nombre"));
                    prod.setPrecioVenta(rs.getDouble("precio_venta"));
                    prod.setStock(rs.getInt("stock"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return prod; // Retorna el objeto Producto o null si no lo encontró
    }
}
