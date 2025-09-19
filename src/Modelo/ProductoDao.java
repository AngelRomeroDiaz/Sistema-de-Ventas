
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 27 de Diciembre del 2022
 * Fecha de modificacion:
 * Nombre del programa:Clase que servira para capturar los datos del formulario sistema y posteriormente los mandara a la clase Producto

 */
public class ProductoDao {
    Connection con;//importar conexion de mySQL y le damos un nombre 
    Conexion cn = new Conexion();//Mandamos a llamar a la clase conexion y creamos un contructor
    PreparedStatement ps;//importar conexion de mySQL y le damos un nombre 
    ResultSet rs;//importar conexion de mySQL y le damos un nombre 
    
    //Crearemos nuestro metodo para registrar a los Productos de tipo boleano
    public boolean RegistrarProducto(Producto pro){//Resibira todo los campos de Producto y los almacenara en un pro
         String sql = "INSERT INTO productos (codigo, nombre, provedor, stock, precio) VALUES (?,?,?,?,?)";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un producto en la tabla "producto" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1 siempre
            ps.setString(1, pro.getCodigo());//vamos a enviar lo que se halla capturado en codigo
            ps.setString(2, pro.getNombre());//vamos a enviar lo que se halla capturado en Nombre
            ps.setString(3, pro.getProvedor());//vamos a enviar lo que se halla capturado en Provedor
            ps.setInt(4, pro.getStock());//vamos a enviar lo que se halla capturado en Stock
            ps.setDouble(5, pro.getPrecio());//vamos a enviar lo que se halla capturado en Precio
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
    
    //Creamos un metodo para consultar a los provedores mediante un combobox
    public void ConsultarProvedor(JComboBox provedor){//Va a recibir un parametro de tipo combobox y se llamara provedor
        String sql = "SELECT nombre FROM provedor";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
        //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un bucle while para poder mostrar lo que se obtubo de la consulta
            while(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                //"Nombre del comobox".addItem("nombre del objeto que arroja el resultado".get"tipo de dato a obtener"("Campo de la base de datos"));
                provedor.addItem(rs.getString("nombre"));//Vamos a adañir a un combobox lo que se obtenga del registro de nombre     
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        } 
        
    }

    //Creamos un metodo list para enlistar  los productos
    public List ListarProducto (){
       //CONTRUCTOR PARA ENLISTAR VARIABLES:  List<"nombre de la variable que se va a enlistar "> "nombre que se le va a dar a la lista" = new ArrayList(); 
        List<Producto> Listapro = new ArrayList();//Sirve para poder enlistar a la variable Producto 
        String sql = "SELECT * FROM productos";//Creamos una variable String para guardar la consulta para MySQL
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un bucle while para poder mostrar lo que se obtubo de la consulta
            while(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                Producto pro = new Producto();//Creamos un contructor para guardar los resultados
                //"nombre del objeto".set"nombre del campo a mostrar"("nombre del objeto de resulset".get"tipo de dato a obtener"("nombre del campo en MySQL"));
                pro.setId(rs.getInt("id"));//Mostramos en "setId" lo que obtuvo "rs" del campo de MySQL "id"
                pro.setCodigo(rs.getString("codigo"));//Mostramos en "setRuc" lo que obtuvo "rs" del campo de MySQL "codigo"
                pro.setNombre(rs.getString("nombre"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                pro.setProvedor(rs.getString("provedor"));//Mostramos en "setTelefono" lo que obtuvo "rs" del campo de MySQL "provedor"
                pro.setStock(rs.getInt("stock"));//Mostramos en "setDireccion" lo que obtuvo "rs" del campo de MySQL "stock"
                pro.setPrecio(rs.getDouble("precio"));//Mostramos en "setRazon" lo que obtuvo "rs" del campo de MySQL "precio"
                Listapro.add(pro);//Agregamos nuestro resultado a la lista
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
        return Listapro;//retornamos la lista 
        
    }
    
    //Creamos un metodo para eliminar  los productos de tipo boleano
    public boolean EliminarProducto(int id){//Eliminara todos los registros mediante su id
         String sql = "DELETE FROM productos WHERE id = ?";//Creamos una variable "sql" para guardar un comando de sql y borrar el registro de un cliente en la tabla "cliente" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1 
            ps.setInt(1, id);//vamos a enviar lo que se halla capturado en id
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
                 
             } catch (Exception ex) {
                 System.out.println(ex.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
             }
             
         }
        
    }

    //Crearemos nuestro metodo para Modificar  los registros de los provedores de tipo boleano
    public boolean ModificarProducto(Producto pro){//Resibira todo los campos de cliente y los almacenara en un cl
         String sql = "UPDATE productos SET codigo=?, nombre=?, provedor=?, stock=?, precio=? WHERE id=?";//Creamos una variable "sql" para guardar un comando de sql y actualizar el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1
            ps.setString(1, pro.getCodigo());//vamos a enviar lo que se halla capturado en Dni
            ps.setString(2, pro.getNombre());//vamos a enviar lo que se halla capturado en Nombre
            ps.setString(3, pro.getProvedor());//vamos a enviar lo que se halla capturado en Telefono
            ps.setInt(4, pro.getStock());//vamos a enviar lo que se halla capturado en Direccion
            ps.setDouble(5, pro.getPrecio());//vamos a enviar lo que se halla capturado en Razon
            ps.setInt(6, pro.getId());//vamos a enviar lo que se halla capturado en Dni
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
                 System.out.println(e.toString());;//Mostrar  un mensaje con el error de MySQL
            }
             
         }
        
    }

    //Creamos un metodo para buscar productos por su codigo
    public Producto BuscarProducto(String codigo){
        Producto producto = new Producto();//Creamos un contructor para instanciar la clase productos
        String sql = "SELECT * FROM productos WHERE codigo = ?";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
        //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //"nombre del objeto".set"Tipo de dato a obtener"(1, codigo);
            ps.setString(1, codigo);//vamos a enviar el codigo a la instrucsion de arriba 
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un ife para poder mostrar lo que se obtubo de la consulta
            if(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                //"nombre del objeto".set"nombre del campo a mostrar"("nombre del objeto de resulset".get"tipo de dato a obtener"("nombre del campo en MySQL"));
                producto.setNombre(rs.getString("nombre"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                producto.setPrecio(rs.getDouble("precio"));//Mostramos en "setRazon" lo que obtuvo "rs" del campo de MySQL "precio"
                producto.setStock(rs.getInt("stock"));//Mostramos en "setDireccion" lo que obtuvo "rs" del campo de MySQL "stock"
                
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
        return producto;
    }
    
     //Creamos un metodo para buscar los datos de la empresa config
    public Config BuscarDatos(){
        Config config = new Config();//Creamos un contructor para instanciar la clase productos
        String sql = "SELECT * FROM config";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
        //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
        try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un ife para poder mostrar lo que se obtubo de la consulta
            if(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                //"nombre del objeto".set"nombre del campo a mostrar"("nombre del objeto de resulset".get"tipo de dato a obtener"("nombre del campo en MySQL"));
                config.setId(rs.getInt("id"));//Mostramos en "setId" lo que obtuvo "rs" del campo de MySQL "id"
                config.setNombre(rs.getString("nombre"));//Mostramos en "setId" lo que obtuvo "rs" del campo de MySQL "id"
                config.setRuc(rs.getInt("ruc"));//Mostramos en "setId" lo que obtuvo "rs" del campo de MySQL "id"                              
                config.setTelefono(rs.getInt("telefono"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                config.setDireccion(rs.getString("direccion"));//Mostramos en "setRazon" lo que obtuvo "rs" del campo de MySQL "precio"
                config.setRazon(rs.getString("razon"));//Mostramos en "setNombre" lo que obtuvo "rs" del campo de MySQL "nombre"
                
            }
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }
        return config;
    }
    
    //Creamos un metodo para actualizar los datos de la empresa config
    public boolean ModificarDatos(Config conf){//Resibira todo los campos de cliente y los almacenara en un cl
         String sql = "UPDATE config SET ruc=?, nombre=?, telefono=?, direccion=?, razon=? WHERE id=?";//Creamos una variable "sql" para guardar un comando de sql y actualizar el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"
         //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
         try {//Intentar hacer lo siguiente 
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta
            //Inicia en 1
            ps.setInt(1, conf.getRuc());//vamos a enviar lo que se halla capturado en Dni
            ps.setString(2, conf.getNombre());//vamos a enviar lo que se halla capturado en Nombre
            ps.setInt(3, conf.getTelefono());//vamos a enviar lo que se halla capturado en Telefono
            ps.setString(4, conf.getDireccion());//vamos a enviar lo que se halla capturado en Direccion
            ps.setString(5, conf.getRazon());//vamos a enviar lo que se halla capturado en Razon
            ps.setInt(6, conf.getId());//vamos a enviar lo que se halla capturado en Dni
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
                 System.out.println(e.toString());;//Mostrar  un mensaje con el error de MySQL
            }
             
         }
        
    }
}
