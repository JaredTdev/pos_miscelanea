package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;

public class VentaDAO {

    // 1. REGISTRAR UNA VENTA COMPLETA (Cabecera y Detalle con Transacción)
    public boolean registrarVenta(double total, List<Object[]> detallesCarrito) {
        String sqlVenta = "INSERT INTO ventas (total) VALUES (?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        Connection con = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); // Iniciamos transacción manualmente

            // Insertar Cabecera de Venta
            psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setDouble(1, total);
            psVenta.executeUpdate();

            // Obtener el ID autogenerado de la venta
            ResultSet rsKeys = psVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rsKeys.next()) {
                idVenta = rsKeys.getInt(1);
            }

            // Preparar statements para el lote (Batch)
            psDetalle = con.prepareStatement(sqlDetalle);
            psStock = con.prepareStatement(sqlStock);

            // Recorrer el carrito de compras
            for (Object[] articulo : detallesCarrito) {
                int idProducto = (int) articulo[0];
                int cantidad = (int) articulo[1];
                double precioUnitario = (double) articulo[2];

                // Agregar al lote del Detalle
                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, idProducto);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precioUnitario);
                psDetalle.addBatch();

                // Agregar al lote de actualización de Stock
                psStock.setInt(1, cantidad);
                psStock.setInt(2, idProducto);
                psStock.addBatch();
            }

            // Ejecutar todas las inserciones en bloque
            psDetalle.executeBatch();
            psStock.executeBatch();

            con.commit(); // Si todo salió bien, guardamos cambios en la base de datos
            return true;

        } catch (SQLException e) {
            System.out.println("Error en transacción de venta: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            // Cerramos recursos de manera segura
            try {
                if (psVenta != null) psVenta.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                if (con != null) con.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 2. CONSULTAR HISTORIAL DE VENTAS (Para llenar una tabla de reportes)
    public List<Object[]> obtenerHistorialVentas() {
        List<Object[]> listaVentas = new ArrayList<>();
        String sql = "SELECT id, fecha, total FROM ventas ORDER BY fecha DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getTimestamp("fecha");
                fila[2] = rs.getDouble("total");
                listaVentas.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener historial: " + e.getMessage());
        }
        return listaVentas;
    }
    
}
