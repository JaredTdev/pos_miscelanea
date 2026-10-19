package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
