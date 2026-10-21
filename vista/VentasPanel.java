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

        // --- CENTRO (Carrito de Compras / Tabla) ---
        // Columnas: ID, Código, Descripción, Cantidad, Precio Unitario, Subtotal
        String[] columnas = {"ID", "Código", "Descripción", "Cantidad", "Precio U.", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celdas no editables directamente para evitar inconsistencias
            }
        };

        tblCarrito = new JTable(modeloTabla);
        tblCarrito.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tblCarrito.setRowHeight(28);
        tblCarrito.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Ocultar columna ID para que no sea visible al usuario pero accesible por código
        tblCarrito.getColumnModel().getColumn(0).setMinWidth(0);
        tblCarrito.getColumnModel().getColumn(0).setMaxWidth(0);
        tblCarrito.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollTabla = new JScrollPane(tblCarrito);
        add(scrollTabla, BorderLayout.CENTER);

        // --- SUR (Panel de Control de Pago) ---
        JPanel pnlSur = new JPanel(new BorderLayout(20, 20));
        pnlSur.setOpaque(false);

        // Bloque del Total Gigante
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTotal.setOpaque(false);
        JLabel lblTextoTotal = new JLabel("TOTAL A PAGAR: ");
        lblTextoTotal.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTotal.setForeground(new Color(40, 167, 69)); // Verde éxito
        
        pnlTotal.add(lblTextoTotal);
        pnlTotal.add(lblTotal);

        // Bloque de Acciones (Botones)
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlBotones.setOpaque(false);

        JButton btnEliminar = new JButton("Quitar Artículo");
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo peligro
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setPreferredSize(new Dimension(160, 45));

        JButton btnCobrar = new JButton("Cobrar Venta (F5)");
        btnCobrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCobrar.setBackground(new Color(0, 123, 255)); // Azul primario
        btnCobrar.setForeground(Color.WHITE);
        btnCobrar.setPreferredSize(new Dimension(200, 45));

        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnCobrar);

        pnlSur.add(pnlTotal, BorderLayout.NORTH);
        pnlSur.add(pnlBotones, BorderLayout.SOUTH);
        add(pnlSur, BorderLayout.SOUTH);

        
    

    }

    private void limpiarPantallaCompleta() {
        modeloTabla.setRowCount(0);
        totalVenta = 0.0;
        lblTotal.setText("$0.00");
        txtEscaner.requestFocusInWindow();
    }

    // Método para forzar foco desde el contenedor principal (JFrame o JTabbedPane)
    public void solicitarFocoEscaner() {
        txtEscaner.requestFocusInWindow();
    }
}
