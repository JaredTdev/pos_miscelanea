package vista;

import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dao.VentaDAO;
import servicios.TicketPrinterService;

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

    /**
     * Recupera la venta seleccionada y simula una reimpresión utilizando el servicio físico.
     */
    private void accionarReimpresion() {
        int filaSeleccionada = tblHistorial.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la lista para reimprimir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos básicos de la fila seleccionada
        String idVentaStr = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        String totalStr = modeloTabla.getValueAt(filaSeleccionada, 2).toString().replace("$", "");
        double total = Double.parseDouble(totalStr);

        // Crear una lista ficticia con formato de ticket para la reimpresión rápida de la cabecera
        List<Object[]> detallesReimpresion = new ArrayList<>();
        detallesReimpresion.add(new Object[]{"REIMPRESIÓN TICKET #" + idVentaStr, 1, total});

        // Despachar a la ticketera de forma silenciosa
        TicketPrinterService imp = new TicketPrinterService(detallesReimpresion, total);
        imp.imprimirSilencioso("default");

        JOptionPane.showMessageDialog(this, "Orden de reimpresión enviada para la venta #" + idVentaStr, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
