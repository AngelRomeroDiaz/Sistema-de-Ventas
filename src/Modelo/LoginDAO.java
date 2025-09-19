/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 24 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa:Clase que servira para capturar los datos del formulario login y posteriormente los mandara a la clase login

 */
public class LoginDAO {
    Connection con;//importar conexion de mySQL y le damos un nombre 
    PreparedStatement ps;//importar conexion de mySQL y le damos un nombre 
    ResultSet rs;//importar conexion de mySQL y le damos un nombre 
    Conexion cn = new Conexion();//Mandamos a llamar a la clase conexion y creamos un contructor
    
    //Mandamos a llamar a la clase login
    public login log (String correo, String pass){
    login l = new login();//creamos el contructor de la clase login para que valla guardando los datos que se obtengan de esta clase
    String sql = "SELECT * FROM usuarios WHERE correo = ? AND pass = ?";//Creamos una variable "sql"para guardar un comando de sql y hacer una consulta de un correo con su contraseña
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            ps.setString(1, correo);//Sirve para mostrar lo que obtenga la base de datos respecto al correro y lo mostrara en un 1er apartado
            ps.setString(2, pass);//Sirve para mostrar lo que obtenga la base de datos respecto a la contraseña y lo mostrara en un 2do apartado
            //executeQuery devuelve un resultado a mostrar por lo que ocupamos la liibreria resultset
            rs = ps.executeQuery();//Sirve para poder ejecutar la consulta
            //Creamos un if para validar y guardar los datos que se obtengan de la tabla usuarios usuarios base de datos sistemaventa
            if(rs.next()){
                l.setId(rs.getInt("id"));//Llamamos a la clase login.mostratremos en el campo Id( el resultado de obtener el entero (campo de la tabla usuarios"id"))
                l.setNombre(rs.getString("nombre"));//Llamamos a la clase login.mostratremos en el campo Nombre( el resultado de obtener la cadena (campo de la tabla usuarios "nombre"))
                l.setCorreo(rs.getString("correo"));//Llamamos a la clase login.mostratremos en el campo Correo( el resultado de obtener la cadena (campo de la tabla usuarios "correo"))
                l.setPass(rs.getString("pass"));//Llamamos a la clase login.mostratremos en el campo Pass( el resultado de obtener la cadena (campo de la tabla usuarios "pass"))
                l.setRol(rs.getString("rol"));//Llamamos a la clase login.mostratremos en el campo rol( el resultado de obtener la cadena (campo de la tabla usuarios "rol"))
                
            }
            
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());//Vamos a capturar a e(error) que pueda arrojar la base de datos
        }
        return l;//Retornamos la clase loging con la que trabajamos 
    } 

    //Crearemos nuestro metodo para registrar a los usuarios
    public boolean Registrar(login reg){//Resibira todo los campos de Provedor y los almacenara en un pr
         String sql = "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?,?,?,?)";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1 siempre
            ps.setString(1, reg.getNombre());//vamos a enviar lo que se halla capturado en Nombre
            ps.setString(2, reg.getCorreo());//vamos a enviar lo que se halla capturado en correo
            ps.setString(3, reg.getPass());//vamos a enviar lo que se halla capturado en pass
            ps.setString(4, reg.getRol());//vamos a enviar lo que se halla capturado en rol
            ps.execute();//Sirve para poder ejecutar la consulta
            return true;//Si se pudo realizar la accion, retornara un true
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
             System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            return false;//Si no se pudo realizar la accion, retornara un false
        }
         //finally{} servira para cerrar la conexion con la base de datos 
         finally{//Cerraremos la conexion 
             try {//Intentar cerrar  
                 con.close();//Este comando nos permite cerrar la conexion
                 
             } catch (Exception e) {
                 System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
             }
             
         }
        
    }


}
