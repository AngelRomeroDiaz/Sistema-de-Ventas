
package Modelo;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 4 de enero del  2023
 * Fecha de modificacion:
 * Nombre del programa: Clase para guardar la tabla "detalle" de MySQL
 */
public class Detalle {
    //Creamos las variables privadas para guardar los datos de la tabla "detalle" de MySQL
    private int id;
    private String codigo_Producto;
    private int cantidad;
    private double precio;
    private int id_venta;
    //Creamos un contructor vacio
    //Ayuda: Clic derecho, insert code, contructor,no seleccionamos nada, generate
    public Detalle() {
    }
    //Creamos un contructor con los parametros del detalle de compra
    //Ayuda: Clic derecho, insert code, contructor, seleccionamos todo, generate
    public Detalle(int id, String codigo_Producto, int cantidad, double precio, int id_venta) {
        this.id = id;
        this.codigo_Producto = codigo_Producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.id_venta = id_venta;
    }
    //Creamos metodos setter y getter
    //Gett Sirve para obtener informacion ingresada
    //Sett Sirve para mostrar la informacion ingresada
    //Ayuda: Clic derecho, insert code, getter and setter, seleccionamos todo, generate
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo_Producto() {
        return codigo_Producto;
    }

    public void setCodigo_Producto(String codigo_Producto) {
        this.codigo_Producto = codigo_Producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    
   
}
