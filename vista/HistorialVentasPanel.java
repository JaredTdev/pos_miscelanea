package vista;

import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.VentaDAO;

public class HistorialVentasPanel extends JPanel{

    private JTable tblHistorial;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalAcumulado;
    private VentaDAO ventaDAO;
    private SimpleDateFormat formateadorFecha;

}
