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
}
