package vista;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

public class MenuPrincipal extends JFrame{
    
    private JTabbedPane panelPestañas;
    private VentasPanel moduloVentas;
    private PriceCheckerPanel moduloPriceChecker;
    private RegistroProductoPanel moduloRegistro;

    public MenuPrincipal() {
        setTitle("SISTEMA PUNTO DE VENTA (POS) - ESCRITORIO");
        setSize(1024, 700); // Resolución inicial ideal para pantallas estándar
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la aplicación al arrancar

        initComponents();
        configurarLógicaFoco();
    }

    private void initComponents() {
        // Inicializamos las pantallas individuales que creamos antes
        moduloVentas = new VentasPanel();
        moduloPriceChecker = new PriceCheckerPanel();
        moduloRegistro = new RegistroProductoPanel();

        // Creamos el contenedor de pestañas
        panelPestañas = new JTabbedPane();
        panelPestañas.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Añadimos cada panel como una pestaña independiente
        panelPestañas.addTab("Módulo de Ventas  ", moduloVentas);
        panelPestañas.addTab("Verificador de Precios  ", moduloPriceChecker);
        panelPestañas.addTab("Registrar Productos  ", moduloRegistro);

        // Añadir el contenedor a la ventana principal
        add(panelPestañas, BorderLayout.CENTER);
    }

    private void configurarLógicaFoco() {
        // Crítico para POS: Cuando el usuario cambie de pestaña, 
        // el cursor debe ir automáticamente al campo de texto del escáner.
        panelPestañas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int pestañaSeleccionada = panelPestañas.getSelectedIndex();
                switch (pestañaSeleccionada) {
                    case 0:
                        moduloVentas.solicitarFocoEscaner();
                        break;
                    case 1:
                        moduloPriceChecker.solicitarFocoEscaner();
                        break;
                    case 2:
                        moduloRegistro.solicitarFocoInicial();
                        break;
                }
            }
        });

        // Forzar el foco inicial en la pantalla de ventas al abrir el programa
        SwingUtilities.invokeLater(() -> moduloVentas.solicitarFocoEscaner());
    }

    /**
     * MÉTODO DE ARRANQUE PRINCIPAL (MAIN)
     */
    public static void main(String[] args) {
        // Aplicar el estilo visual nativo del sistema operativo (Look and Feel)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer el Look and Feel nativo: " + e.getMessage());
        }

        // Lanzar la aplicación en el hilo de eventos de Swing de forma segura
        EventQueue.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
