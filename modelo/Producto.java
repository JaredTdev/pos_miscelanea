package modelo;

public class Producto {

    private int id;
    private String codigoBarras;
    private String nombre;
    private double precioVenta;
    private int stock;
    
    //Constructor vacio
    public Producto() {}

    //Constructor completo
    public Producto(int id, String codigoBarras, String nombre, double precioVenta, int stock) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    
}
