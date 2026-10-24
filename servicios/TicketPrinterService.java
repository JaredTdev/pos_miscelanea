package servicios;

import java.awt.*;
import java.awt.print.*;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class TicketPrinterService implements Printable{

    private final List<Object[]> productos;
    private final double total;
    
    // Pasamos los mismos datos de la venta para armar el ticket dinámicamente
    public TicketPrinterService(List<Object[]> productos, double total) {
        this.productos = productos;
        this.total = total;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Fuentes estándar para terminales POS (Monospaced asegura el mismo ancho de carácter)
        Font fontTitulo = new Font("Monospaced", Font.BOLD, 12);
        Font fontCuerpo = new Font("Monospaced", Font.PLAIN, 9);
        Font fontTotal = new Font("Monospaced", Font.BOLD, 11);

        int y = 15; // Coordenada vertical inicial

        // 1. Encabezado del Ticket
        g2d.setFont(fontTitulo);
        g2d.drawString("    MI TIENDITA POS    ", 5, y); y += 15;
        g2d.setFont(fontCuerpo);
        g2d.drawString("==========================", 5, y); y += 12;
        g2d.drawString("Cant  Descripción   Precio", 5, y); y += 12;
        g2d.drawString("--------------------------", 5, y); y += 12;

        // 2. Detalle de Productos dinámicos desde la tabla
        for (Object[] art : productos) {
            String descripcion = (String) art[1]; // Nombre/Descripción
            int cantidad = (int) art[2];          // Cantidad comprada
            double precio = (double) art[3];      // Precio Unitario

            // Limitar texto si la descripción es muy larga para que no rompa el ticket
            if (descripcion.length() > 14) {
                descripcion = descripcion.substring(0, 12) + "..";
            }

            String linea = String.format("%-4d  %-14s $%5.2f", cantidad, descripcion, precio);
            g2d.drawString(linea, 5, y);
            y += 12;
        }

        // 3. Cierre y Totales
        g2d.drawString("--------------------------", 5, y); y += 15;
        g2d.setFont(fontTotal);
        g2d.drawString(String.format("TOTAL:          $%7.2f", total), 5, y); y += 20;
        
        g2d.setFont(fontCuerpo);
        g2d.drawString(" ¡Gracias por su compra! ", 5, y); y += 12;
        g2d.drawString("  Conserve este ticket   ", 5, y);

        return PAGE_EXISTS;
    }

    /**
     * Envía a imprimir de manera silenciosa (segundo plano).
     * @param nombreImpresora Opcional: Nombre exacto de tu ticketera en Windows/Linux. 
     *                        Si se pasa "default" o vacío, usará la del sistema.
     */
    public void imprimirSilencioso(String nombreImpresora) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        // Configurar tamaño de página genérico para ticketera (Ancho fijo, alto variable)
        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        // 1 pulgada = 72 puntos. Un ticket de 58mm son aprox 164 puntos, el de 80mm son 226 puntos.
        paper.setSize(180, 500); 
        paper.setImageableArea(5, 5, 170, 490);
        pf.setPaper(paper);
        
        // Asignar el tamaño formateado al trabajo
        job.setPrintable(this, pf);

        // Buscar impresora específica por nombre
        if (nombreImpresora != null && !nombreImpresora.trim().isEmpty() && !nombreImpresora.equalsIgnoreCase("default")) {
            PrintService[] servicios = PrintServiceLookup.lookupPrintServices(null, null);
            for (PrintService service : servicios) {
                if (service.getName().toLowerCase().contains(nombreImpresora.toLowerCase())) {
                    try {
                        job.setPrintService(service);
                        break;
                    } catch (PrinterException e) {
                        System.out.println("No se pudo usar la impresora especificada: " + e.getMessage());
                    }
                }
            }
        }

        // Ejecutar en un hilo separado para que la interfaz gráfica no se congele ni un milisegundo
        new Thread(() -> {
            try {
                // LLAMADA DIRECTA: Ejecuta la impresión sin usar job.printDialog()
                job.print();
            } catch (PrinterException ex) {
                System.out.println("Error al imprimir ticket de forma silenciosa: " + ex.getMessage());
            }
        }).start();
    }
}
