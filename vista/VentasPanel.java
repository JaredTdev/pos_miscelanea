package vista;

import dao.ProductoDAO;
import dao.VentaDAO;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentasPanel extends JPanel {
    
    private JTextField txtEscaner;
    private JTable tblCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    private double totalVenta = 0.0;

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        //ENCABEZADO (Area de escaneo)
        JPanel pnlNorte = new JPanel(new BorderLayout(10, 10));
        pnlNorte.setOpaque(false);

        JLabel lblEscaner = new JLabel("Escanear Código de Barras:");
        lblEscaner.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        txtEscaner = new JTextField();
        txtEscaner.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtEscaner.setPreferredSize(new Dimension(300, 40));

        pnlNorte.add(lblEscaner, BorderLayout.NORTH);
        pnlNorte.add(txtEscaner, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

    }
}
