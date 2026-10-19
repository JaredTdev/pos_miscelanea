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

    // 3. Método para ACTUALIZAR el stock tras una venta
    public boolean actualizarStock(int idProducto, int cantidadVendida) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, cantidadVendida);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidadVendida); // Evita que el stock quede en negativo
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
}
