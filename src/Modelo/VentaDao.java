
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 4 de enero del 2023
 * Fecha de modificacion:
 * Nombre del programa:Clase con  metodos que utilizara para la tabla "ventas" de la BD

 */
public class VentaDao {
    Connection con;//importar conexion de mySQL y le damos un nombre 
    Conexion cn = new Conexion();//Mandamos a llamar a la clase conexion y creamos un contructor
    PreparedStatement ps;//importar conexion de mySQL y le damos un nombre 
    ResultSet rs;//importar conexion de mySQL y le damos un nombre 
    int r;//Creamos la variable para guardar el resultado del metodo RegistrarVenta()
    //Crearemos un metodo de tipo entero por que se utilizara el id de la venta en el sistema
    public int RegistrarVenta(Venta v){//indicamos que se utilizara la clase venta y tenfra un objeto llamado v
        String sql = "INSERT INTO ventas (cliente, Vendedor, total, fecha) VALUES (?,?,?,?)";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de una venta en la tabla "ventas" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1 siempre
            ps.setString(1, v.getCliente());//vamos a enviar lo que se halla capturado en cliente
            ps.setString(2, v.getVendedor());//vamos a enviar lo que se halla capturado en vendedor
            ps.setDouble(3, v.getTotal());//vamos a enviar lo que se halla capturado en total
            ps.setString(4, v.getFecha());//vamos a enviar lo que se halla capturado en total            
            ps.execute();//Sirve para poder ejecutar la consulta
            
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
             System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
         //finally{} servira para cerrar la conexion con la base de datos 
         finally{//Cerraremos la conexion 
             try {//Intentar cerrar  
                 con.close();//Este comando nos permite cerrar la conexion
                 
             } catch (Exception e) {
                 System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
             }
             
         }
         return r;//Si no se pudo realizar la accion, retornara un false
    }
    
    //Creamos un metodo para registrar la tabla detalle de la BD 
     public int RegistrarDetalle(Detalle Dv){//indicamos que se utilizara la clase venta y tenfra un objeto llamado v
        String sql = "INSERT INTO detalle (cod_pro, cantidad, precio, id_venta) VALUES (?,?,?,?)";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de una venta en la tabla "ventas" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1 siempre
            ps.setString(1, Dv.getCodigo_Producto());//vamos a enviar lo que se halla capturado en Detalle
            ps.setInt(2, Dv.getCantidad());//vamos a enviar lo que se halla capturado en Detalle
            ps.setDouble(3, Dv.getPrecio());//vamos a enviar lo que se halla capturado en total
            ps.setInt(4, Dv.getId_venta());//vamos a enviar lo que se halla capturado en vendedor           
            ps.execute();//Sirve para poder ejecutar la consulta
            
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
             System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
         //finally{} servira para cerrar la conexion con la base de datos 
         finally{//Cerraremos la conexion 
             try {//Intentar cerrar  
                 con.close();//Este comando nos permite cerrar la conexion
                 
             } catch (Exception e) {
                 System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
             }
             
         }
         return r;//retornamos el resultado
    }
     //Creamos un metodo para Consultar ID máximo de la venta
     public int IdVenta(){//indicamos que se utilizara la clase venta y tenfra un objeto llamado v
         int id=0;//creamos la variable para 
         String sql = "SELECT MAX(id) FROM ventas";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de una venta en la tabla "ventas" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1 siempre
            rs= ps.executeQuery();//Sirve para poder ejecutar la consulta y resibira algo
            //Creamos un if para poder mostrar lo que se obtubo de la consulta
            if(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                id= rs.getInt(1);
            }
            
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
             System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
         
         return id;//retornamos el id maximo de la tabla ventas  
    }
     
     //Creamos un metodo para actualizar el stock cuando se realize una venta
     public boolean ActualizarStock(int cantidad, String codigo){//Resibira  la cantidad y el codigo
         String sql = "UPDATE productos SET stock = ? WHERE codigo = ?";//Creamos una variable "sql" para guardar un comando de sql y actualizar el registro de un cliente en la tabla "cliente" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1, por que el 0 es el id en la tabla cliente y en este caso, no es necesario llenarlo ya que es autoincrementado
            ps.setInt(1, cantidad);//vamos a enviar el valor de cantidad
            ps.setString(2, codigo);//vamos a enviar el valor de codigo
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
     
     //Creamos un metodo list para enlistar   las ventas del boton 5 del sistema
    public List ListarVentas (){
       //CONTRUCTOR PARA ENLISTAR VARIABLES:  List<"nombre de la variable que se va a enlistar "> "nombre que se le va a dar a la lista" = new ArrayList(); 
        List<Venta> ListaVenta = new ArrayList();//Sirve para poder enlistar a la variable Producto 
        String sql = "SELECT * FROM ventas";//Creamos una variable String para guardar la consulta para MySQL
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un bucle while para poder mostrar lo que se obtubo de la consulta
            while(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                Venta vent = new Venta();//Creamos un contructor para guardar los resultados
                //"nombre del objeto".set"nombre del campo a mostrar"("nombre del objeto de resulset".get"tipo de dato a obtener"("nombre del campo en MySQL"));
                vent.setId(rs.getInt("id"));//Mostramos en "setId" lo que obtuvo "rs" del campo de MySQL "id"
                vent.setCliente(rs.getString("cliente"));//Mostramos en "setRuc" lo que obtuvo "rs" del campo de MySQL "codigo"
                vent.setVendedor(rs.getString("Vendedor"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                vent.setTotal(rs.getDouble("total"));//Mostramos en "setTelefono" lo que obtuvo "rs" del campo de MySQL "provedor"
                ListaVenta.add(vent);//Agregamos nuestro resultado a la lista
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
        return ListaVenta;//retornamos la lista 
        
    }
}
