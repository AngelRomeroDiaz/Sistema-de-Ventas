
package Modelo;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 24 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa://Clase que utilizara el  LoginDAO para guardar y consultar todos los campos de la tabla usuarios del la base de datos "sistemaventa"

 */

public class login {
    //Creamos las variables privadas para guardar los datos del usuario 
    private int id;
    private String nombre;
    private String correo;
    private String pass;//Contraseña
    private String rol;
    
    //Creamos un contructor vacio
    //Ayuda: Clic derecho, insert code, contructor,no seleccionamos nada, generate
    public login() {
    }
    //Creamos un contructor con los parametros del usuario
    //Ayuda: Clic derecho, insert code, contructor, seleccionamos todo, generate
    public login(int id, String nombre, String correo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.pass = pass;
        this.rol=rol;
    }
    //Creamos metodos setter y getter
    //Ayuda: Clic derecho, insert code, getter and setter, seleccionamos todo, generate
    //Gett Sirve para obtener informacion ingresada
    //Sett Sirve para mostrar la informacion ingresada
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
    
}
