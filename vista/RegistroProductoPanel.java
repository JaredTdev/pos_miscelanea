package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import dao.ProductoDAO;
import modelo.Producto;

public class RegistroProductoPanel extends JPanel{

    private JTextField txtCodigo, txtNombre, txtPrecio, txtStock;
    private JButton btnGuardar, btnLimpiar;
    private ProductoDAO productoDAO;
    
    public RegistroProductoPanel() {
        productoDAO = new ProductoDAO();
        configurarValidaciones();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
    }
    
    private void configurarValidaciones() {
        // Validación en tiempo de escritura para que Precio admita solo números y un punto decimal
        txtPrecio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // Cancela la pulsación de la tecla
                }
                if (c == '.' && txtPrecio.getText().contains(".")) {
                    e.consume(); // Evita más de un punto decimal
                }
            }
        });

        // Validación para que Stock admita exclusivamente números enteros
        txtStock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }

    private void guardarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String stockStr = txtStock.getText().trim();

        // Validar campos vacíos antes de procesar
        if (codigo.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número mayor a cero.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar si el código ya existe para evitar errores de clave duplicada en la base de datos
            if (productoDAO.buscarPorCodigo(codigo) != null) {
                JOptionPane.showMessageDialog(this, "El código de barras ya se encuentra registrado por otro artículo.", "Código Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Armar el objeto e insertar a través del DAO
            Producto nuevoProducto = new Producto(0, codigo, nombre, precio, stock);
            boolean exito = productoDAO.registrarProducto(nuevoProducto);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente en el inventario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el artículo. Intente de nuevo.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor introduzca valores numéricos válidos en Precio y Stock.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCodigo.requestFocusInWindow();
    }

    // Método de utilidad para enfocar el primer campo al abrir la pestaña
    public void solicitarFocoInicial() {
        txtCodigo.requestFocusInWindow();
    }
}
