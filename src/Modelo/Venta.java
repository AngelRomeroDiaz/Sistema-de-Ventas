
package Modelo;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 4 de enero de 2023
 * Fecha de modificacion:
 * Nombre del programa:Clase para instansiar los parametros para la tabla "ventas" de la BD
 */
public class Venta {
    //Creamos las variables privadas para guardar los datos de las ventas 
    private int id;
    private String cliente;
    private String vendedor;
    private double total;
    private String fecha;
    //Creamos un contructor vacio
    //Ayuda: Clic derecho, insert code, contructor,no seleccionamos nada, generate
    public Venta() {
    }
    //Creamos un contructor con los parametros del Producto
    //Ayuda: Clic derecho, insert code, contructor, seleccionamos todo, generate
    public Venta(int id, String cliente, String vendedor, double total, String fecha) {
        this.id = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.total = total;
        this.fecha = fecha;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

   

}
