
package Modelo;

import java.sql.Connection;//importamos la conexion de MySQL
import java.sql.DriverManager;//importamos la conexion de MySQL
import java.sql.SQLException;//importamos la conexion de MySQL


/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 24 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa:Clase para conectar una base de datos

 */
public class Conexion {
    Connection con;//importar conexion de mySQL
    //Creamos un metodo public de tipo conection para obtener la conexion
    public Connection getConnection(){
        //Para capturar las excepciones
        try {
            String myBD="jdbc:mysql://localhost:3307/sistemaventa?serverTimezone=UTC";//Creamos el nombre de la base de datos y sera igual al driver de la misma
            con = DriverManager.getConnection(myBD,"root","");//usamos la conexion con para obtener la conexion(nombre de la BD, usuario(root), contraseña)
            return con;//retornamos con
            
        } catch (SQLException e) {
            System.out.println(e.toString());//Vamos a capturar a e(error) 
        }
        return null;//Retornamos un null para matar el error
    }
    
}
