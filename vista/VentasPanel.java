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

    private void configurarEscaner() {
        // Al dar "Enter" con el escáner se procesa el artículo
        txtEscaner.addActionListener(e -> {
            String codigo = txtEscaner.getText().trim();
            if (!codigo.isEmpty()) {
                agregarProductoAlCarrito(codigo);
            }
            txtEscaner.setText("");
        });
    }

    private void agregarProductoAlCarrito(String codigo) {
        Producto prod = productoDAO.buscarPorCodigo(codigo);

        if (prod == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (prod.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Sin stock disponible de: " + prod.getNombre(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaExistente = -1;
        // Comprobar si el producto ya está en la lista actual de la tabla
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 1).toString().equals(codigo)) {
                filaExistente = i;
                break;
            }
        }

        if (filaExistente != -1) {
            // El artículo ya existe, aumentamos cantidad si el stock lo permite
            int cantActual = (int) modeloTabla.getValueAt(filaExistente, 3);
            if (cantActual + 1 > prod.getStock()) {
                JOptionPane.showMessageDialog(this, "No puedes agregar más. Límite de stock alcanzado.", "Límite", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int nuevaCant = cantActual + 1;
            double precio = (double) modeloTabla.getValueAt(filaExistente, 4);
            double nuevoSubtotal = nuevaCant * precio;

            modeloTabla.setValueAt(nuevaCant, filaExistente, 3);
            modeloTabla.setValueAt(nuevoSubtotal, filaExistente, 5);
        } else {
            // Producto nuevo en el carrito, se añade fila por primera vez
            Object[] nuevaFila = {
                prod.getId(),
                prod.getCodigoBarras(),
                prod.getNombre(),
                1, // Cantidad inicial
                prod.getPrecioVenta(),
                prod.getPrecioVenta() // Subtotal inicial
            };
            modeloTabla.addRow(nuevaFila);
        }

        calcularTotal();
    }

    private void quitarArticuloSeleccionado() {
        int filaSeleccionada = tblCarrito.getSelectedRow();
        if (filaSeleccionada >= 0) {
            modeloTabla.removeRow(filaSeleccionada);
            calcularTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un artículo de la tabla para quitarlo.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void calcularTotal() {
        totalVenta = 0.0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            totalVenta += (double) modeloTabla.getValueAt(i, 5);
        }
        lblTotal.setText(String.format("$%.2f", totalVenta));
    }

    private void procesarCobro() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito de compras está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Estructurar los datos para enviarlos a VentaDAO de forma transaccional
        List<Object[]> detallesCarrito = new ArrayList<>();
        
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            int idProd = (int) modeloTabla.getValueAt(i, 0);
            int cantidad = (int) modeloTabla.getValueAt(i, 3);
            double precioUnitario = (double) modeloTabla.getValueAt(i, 4);
            
            // Estructura idéntica a la que espera el bucle for de tu VentaDAO: Object[] { id, cantidad, precio }
            detallesCarrito.add(new Object[]{ idProd, cantidad, precioUnitario });
        }

        // Llamada a la capa de persistencia transaccional
        boolean exito = ventaDAO.registrarVenta(totalVenta, detallesCarrito);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Venta registrada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // TODO: Lanzar la impresión del ticket aquí en el siguiente paso.

            limpiarPantallaCompleta();
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un error al procesar la transacción.\nVerifique stock e intente nuevamente.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
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
