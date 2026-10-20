package vista;

import dao.ProductoDAO;
import modelo.Producto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PriceCheckerPanel extends JPanel {
    
    private JTextField txtEscaner;
    private JLabel lblNombreProducto;
    private JLabel lblPrecioProducto;
    private JLabel lblInstrucciones;
    private ProductoDAO productoDAO;
    private Timer temporizadorLimpieza;

    public PriceCheckerPanel() {
        productoDAO = new ProductoDAO();
        initComponents();
        configurarEscaner();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(240, 244, 248)); // Color de fondo limpio y moderno

        // Componente Invisible/Oculto para capturar el foco del escáner
        txtEscaner = new JTextField();
        txtEscaner.setPreferredSize(new Dimension(0, 0)); // No se ve en la pantalla
        add(txtEscaner, BorderLayout.NORTH);

        // Panel Central para los datos gigantes
        JPanel pnlCentro = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlCentro.setBackground(new Color(255, 255, 255));
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        lblInstrucciones = new JLabel("POR FAVOR, ESCANEE UN PRODUCTO", SwingConstants.CENTER);
        lblInstrucciones.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblInstrucciones.setForeground(new Color(64, 80, 100));

        lblNombreProducto = new JLabel("---", SwingConstants.CENTER);
        lblNombreProducto.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblNombreProducto.setForeground(new Color(33, 37, 41));

        lblPrecioProducto = new JLabel("$0.00", SwingConstants.CENTER);
        lblPrecioProducto.setFont(new Font("SansSerif", Font.BOLD, 72)); // Texto gigante para el precio
        lblPrecioProducto.setForeground(new Color(40, 167, 69)); // Verde éxito

        pnlCentro.add(lblInstrucciones);
        pnlCentro.add(lblNombreProducto);
        pnlCentro.add(lblPrecioProducto);

        add(pnlCentro, BorderLayout.CENTER);

        // Temporizador para limpiar la pantalla automáticamente tras 5 segundos de inactividad
        temporizadorLimpieza = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restablecerPantalla();
            }
        });
        temporizadorLimpieza.setRepeats(false); // Ejecutar solo una vez por escaneo
    }

    private void configurarEscaner() {
        // Evento que se dispara inmediatamente cuando el lector manda el caracter "Enter"
        txtEscaner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = txtEscaner.getText().trim();
                if (!codigo.isEmpty()) {
                    consultarPrecio(codigo);
                }
                txtEscaner.setText(""); // Limpiar para el siguiente escaneo
            }
        });

        // Truco de usabilidad: Asegura que el campo oculto nunca pierda el foco
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtEscaner.requestFocusInWindow();
            }
        });
    }

    private void consultarPrecio(String codigo) {
        temporizadorLimpieza.stop(); // Detener contador previo si escanean rápido otro artículo
        
        Producto prod = productoDAO.buscarPorCodigo(codigo);

        if (prod != null) {
            lblInstrucciones.setText("PRECIO DEL ARTÍCULO:");
            lblNombreProducto.setText(prod.getNombre().toUpperCase());
            lblPrecioProducto.setText(String.format("$%.2f", prod.getPrecioVenta()));
        } else {
            lblInstrucciones.setText("PRODUCTO NO ENCONTRADO");
            lblNombreProducto.setText("Código: " + codigo);
            lblPrecioProducto.setText("---");
        }

        temporizadorLimpieza.start(); // Iniciar cuenta regresiva para borrar la pantalla
    }

    private void restablecerPantalla() {
        lblInstrucciones.setText("POR FAVOR, ESCANEE UN PRODUCTO");
        lblNombreProducto.setText("---");
        lblPrecioProducto.setText("$0.00");
    }

    // Método público para llamar desde tu menú principal cuando abran esta sección
    public void solicitarFocoEscaner() {
        txtEscaner.requestFocusInWindow();
    }
}