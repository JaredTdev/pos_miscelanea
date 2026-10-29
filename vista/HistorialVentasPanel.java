package vista;

import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.util.List;

import dao.VentaDAO;

public class HistorialVentasPanel extends JPanel{

    private JTable tblHistorial;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalAcumulado;
    private VentaDAO ventaDAO;
    private SimpleDateFormat formateadorFecha;



    /**
     * Consulta la Base de Datos mediante el VentaDAO y refresca las filas de la tabla.
     */
    public void cargarHistorial() {
        modeloTabla.setRowCount(0); // Limpiar datos previos
        List<Object[]> historial = ventaDAO.obtenerHistorialVentas();
        double sumatoriaTotal = 0.0;

        for (Object[] fila : historial) {
            int id = (int) fila[0];
            Timestamp fechaRaw = (Timestamp) fila[1];
            double total = (double) fila[2];

            sumatoriaTotal += total;

            // Formatear la fecha SQL a un texto más amigable (dd/MM/yyyy HH:mm:ss)
            String fechaFormateada = formateadorFecha.format(fechaRaw);

            modeloTabla.addRow(new Object[]{id, fechaFormateada, String.format("$%.2f", total)});
        }

        lblTotalAcumulado.setText(String.format("Total Histórico: $%.2f", sumatoriaTotal));
    }
}
