package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame{
    
    private JTabbedPane panelPestañas;
    private VentasPanel moduloVentas;
    private PriceCheckerPanel moduloPriceChecker;
    private RegistroProductoPanel moduloRegistro;

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
}
