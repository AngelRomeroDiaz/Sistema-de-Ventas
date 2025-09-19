
package Modelo;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 6 de enero de 2023
 * Fecha de modificacion:
 * Nombre del programa:Clase para guardar los datos de Config(datos de la empresa) en MySQL

 */
public class Config {
    private int id;
    private int ruc;
    private String nombre;
    private int telefono;
    private String direccion;
    private String razon; 
    //Creamos un contructor vacio
    //Ayuda: Clic derecho, insert code, contructor,no seleccionamos nada, generate
    public Config() {
    }
    //Creamos un contructor con los parametros del cliente
    //Ayuda: Clic derecho, insert code, contructor, seleccionamos todo, generate

    public Config(int id, int ruc, String nombre, int telefono, String direccion, String razon) {
        this.id = id;
        this.ruc = ruc;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.razon = razon;
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

    public int getRuc() {
        return ruc;
    }

    public void setRuc(int ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

}
