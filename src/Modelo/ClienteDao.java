/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
//Importamos lo necesario
import java.sql.Connection;//Sirve para crear la conexion con MySQL
import java.sql.PreparedStatement;//Para preparar operaciones en MySQL
import java.sql.ResultSet;//Guardar los datos que arroja MySQL
import java.sql.SQLException;//Guardar os errorers que arroje MySQL
import java.util.ArrayList;
import javax.swing.JOptionPane;//Mostrar mensajes en pantalla
import java.util.List;//Sirve para enlistar

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 27 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa:Clase que servira para capturar los datos del formulario sistema y posteriormente los mandara a la clase Cliente

 */
public class ClienteDao {
    Connection con;//importar conexion de mySQL y le damos un nombre 
    Conexion cn = new Conexion();//Mandamos a llamar a la clase conexion y creamos un contructor
    PreparedStatement ps;//importar conexion de mySQL y le damos un nombre 
    ResultSet rs;//importar conexion de mySQL y le damos un nombre 
    
    //Crearemos nuestro metodo para registrar a los clientes de tipo boleano
    public boolean RegistrarCliente(Cliente cl){//Resibira todo los campos de cliente y los almacenara en un cl
         String sql = "INSERT INTO clientes (dni, nombre, telefono, direccion, razon) VALUES (?,?,?,?,?)";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un cliente en la tabla "cliente" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1, por que el 0 es el id en la tabla cliente y en este caso, no es necesario llenarlo ya que es autoincrementado
            ps.setInt(1, cl.getDni());//vamos a enviar lo que se halla capturado en Dni
            ps.setString(2, cl.getNombre());//vamos a enviar lo que se halla capturado en Nombre
            ps.setInt(3, cl.getTelefono());//vamos a enviar lo que se halla capturado en Telefono
            ps.setString(4, cl.getDireccion());//vamos a enviar lo que se halla capturado en Direccion
            ps.setString(5, cl.getRazon());//vamos a enviar lo que se halla capturado en Razon
            ps.execute();//Sirve para poder ejecutar la consulta
            return true;//Si se pudo realizar la accion, retornara un true
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            JOptionPane.showMessageDialog(null,e.toString());//Mostrar en pantalla un mensaje con el error de MySQL
            return false;//Si no se pudo realizar la accion, retornara un false
        }
         //finally{} servira para cerrar la conexion con la base de datos 
         finally{//Cerraremos la conexion 
             try {//Intentar cerrar  
                 con.close();//Este comando nos permite cerrar la conexion
                 
             } catch (Exception e) {
                 JOptionPane.showMessageDialog(null,e.toString());//Mostrar en pantalla un mensaje con el error de MySQL
             }
             
         }
        
    }
    
    //Creamos un metodo list para enlistar a los clientes
    public List ListarCliente (){
       //CONTRUCTOR PARA ENLISTAR VARIABLES:  List<"nombre de la variable que se va a enlistar "> "nombre que se le va a dar a la lista" = new ArrayList(); 
        List<Cliente> ListaCl = new ArrayList();//Sirve para poder enlistar a la variable cliente 
        String sql = "SELECT * FROM clientes";//Creamos una variable String para guardar la consulta para MySQL
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un bucle while para poder mostrar lo que se obtubo de la consulta
            while(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                Cliente cl = new Cliente();//Creamos un contructor para guardar los resultados
                //"nombre del objeto".set"nombre del campo a mostrar"("nombre del objeto de resulset".get"tipo de dato a obtener"("nombre del campo en MySQL"));
                cl.setId(rs.getInt("id"));//Mostramos en "setId" lo que obtuvo "rs" del campo de MySQL "id"
                cl.setDni(rs.getInt("dni"));//Mostramos en "setDni" lo que obtuvo "rs" del campo de MySQL "dni"
                cl.setNombre(rs.getString("nombre"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                cl.setTelefono(rs.getInt("telefono"));//Mostramos en "setTelefono" lo que obtuvo "rs" del campo de MySQL "telefono"
                cl.setDireccion(rs.getString("direccion"));//Mostramos en "setDireccion" lo que obtuvo "rs" del campo de MySQL "direccion"
                cl.setRazon(rs.getString("razon"));//Mostramos en "setRazon" lo que obtuvo "rs" del campo de MySQL "razon"
                ListaCl.add(cl);//Agregamos nuestro resultado a la lista
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            JOptionPane.showMessageDialog(null,e.toString());//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
        return ListaCl;//retornamos la lista 
        
    }
    
    //Creamos un metodo para eliminar a los clientes de tipo boleano
    public boolean EliminarCliente(int id){//Eliminara todos los registros mediante su id
         String sql = "DELETE FROM clientes WHERE id = ?";//Creamos una variable "sql" para guardar un comando de sql y borrar el registro de un cliente en la tabla "cliente" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1
            ps.setInt(1, id);//vamos a enviar lo que se halla capturado en Dni
            ps.execute();//Sirve para poder ejecutar la consulta
            return true;//Si se pudo realizar la accion, retornara un true
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            JOptionPane.showMessageDialog(null,e.toString());//Mostrar en pantalla un mensaje con el error de MySQL
            return false;//Si no se pudo realizar la accion, retornara un false
        }
         //finally{} servira para cerrar la conexion con la base de datos 
         finally{//Cerraremos la conexion 
             try {//Intentar cerrar  
                 con.close();//Este comando nos permite cerrar la conexion
                 
             } catch (Exception ex) {
                 JOptionPane.showMessageDialog(null,ex.toString());//Mostrar en pantalla un mensaje con el error de MySQL
             }
             
         }
        
    }
    
    //Crearemos nuestro metodo para Modificar  los registros de los clientes de tipo boleano
    public boolean ModificarCliente(Cliente cl){//Resibira todo los campos de cliente y los almacenara en un cl
         String sql = "UPDATE clientes SET dni=?, nombre=?, telefono=?, direccion=?, razon=? WHERE id=?";//Creamos una variable "sql" para guardar un comando de sql y actualizar el registro de un cliente en la tabla "cliente" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1, por que el 0 es el id en la tabla cliente y en este caso, no es necesario llenarlo ya que es autoincrementado
            ps.setInt(1, cl.getDni());//vamos a enviar lo que se halla capturado en Dni
            ps.setString(2, cl.getNombre());//vamos a enviar lo que se halla capturado en Nombre
            ps.setInt(3, cl.getTelefono());//vamos a enviar lo que se halla capturado en Telefono
            ps.setString(4, cl.getDireccion());//vamos a enviar lo que se halla capturado en Direccion
            ps.setString(5, cl.getRazon());//vamos a enviar lo que se halla capturado en Razon
            ps.setInt(6, cl.getId());//vamos a enviar lo que se halla capturado en Dni
            ps.execute();//Sirve para poder ejecutar la consulta
            return true;//Si se pudo realizar la accion, retornara un true
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            JOptionPane.showMessageDialog(null,e.toString());//Mostrar en pantalla un mensaje con el error de MySQL
            return false;//Si no se pudo realizar la accion, retornara un false
        }
         //finally{} servira para cerrar la conexion con la base de datos 
         finally{//Cerraremos la conexion 
             try {//Intentar cerrar  
                 con.close();//Este comando nos permite cerrar la conexion
                 
             } catch (Exception e) {
                 JOptionPane.showMessageDialog(null,e.toString());//Mostrar en pantalla un mensaje con el error de MySQL
             }
             
         }
        
    }
    
    //Creamos un metodo para buscar el cliente para una nueva venta
    public Cliente BuscarCliente(int dni){
        Cliente cl = new Cliente();//Creamos un contructor para instanciar la clase cliente
        String sql = "SELECT * FROM clientes WHERE dni = ?";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
        //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //"nombre del objeto".set"Tipo de dato a obtener"(1, codigo);
            ps.setInt(1, dni);//vamos a enviar el codigo a la instrucsion de arriba 
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un if para poder mostrar lo que se obtubo de la consulta
            if(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                //"nombre del objeto".set"nombre del campo a mostrar"("nombre del objeto de resulset".get"tipo de dato a obtener"("nombre del campo en MySQL"));
                cl.setNombre(rs.getString("nombre"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                cl.setTelefono(rs.getInt("telefono"));//Mostramos en "setTelefono" lo que obtuvo "rs" del campo de MySQL "telefono"
                cl.setDireccion(rs.getString("direccion"));//Mostramos en "setDireccion" lo que obtuvo "rs" del campo de MySQL "direccion"
                cl.setRazon(rs.getString("razon"));
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
        return cl;
    }
    
}
