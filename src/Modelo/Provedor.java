/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 27 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa:Clase que resivira los parametros de la clase ProvedorDAO y los enviara mediante el uso de la clase Conexion  para guardar y consultar todos los campos de la tabla "provedor" del la base de datos "sistemaventa"

 */
public class Provedor {
    //Creamos las variables privadas para guardar los datos del cliente 
    private int id;
    private int ruc;
    private String nombre;
    private int telefono;
    private String direccion;
    private String razon;
    //Creamos un contructor vacio
    //Ayuda: Clic derecho, insert code, contructor,no seleccionamos nada, generate
    public Provedor() {
    }
    //Creamos un contructor con los parametros del Provedor
    //Ayuda: Clic derecho, insert code, contructor, seleccionamos todo, generate
    public Provedor(int id, int ruc, String nombre, int telefono, String direccion, String razon) {
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
