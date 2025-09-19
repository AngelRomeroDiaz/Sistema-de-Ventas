/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 30 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa:Clase que resivira los parametros de la clase Producto DAO y los enviara mediante el uso de la clase Conexion  para guardar y consultar todos los campos de la tabla "productos" del la base de datos "sistemaventa"

 */
public class Producto {
    //Creamos las variables privadas para guardar los datos de los productos 
    private int id;
    private String codigo;
    private String nombre;
    private String provedor;
    private int stock;
    private double precio;
    //Creamos un contructor vacio
    //Ayuda: Clic derecho, insert code, contructor,no seleccionamos nada, generate
    public Producto() {
    }
    //Creamos un contructor con los parametros del Producto
    //Ayuda: Clic derecho, insert code, contructor, seleccionamos todo, generate
    public Producto(int id, String codigo, String nombre, String provedor, int stock, double precio) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.provedor = provedor;
        this.stock = stock;
        this.precio = precio;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvedor() {
        return provedor;
    }

    public void setProvedor(String provedor) {
        this.provedor = provedor;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    
    
}
