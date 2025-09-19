package Reportes;

import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author: Angel Gabriel Mendez Diaz Fecha de creacion: 09 de Enero del 2023
 * Fecha de modificacion: Nombre del programa: Clase con metodo para poder
 * generar reportes en graficos Librerias a utilizar : jfreechart-1.0.1.9,
 * jcommon-1.0.23
 *
 */
public class Grafico {

    //Creamos un metodo para hacer la graficacion de la venta
    public static void Graficar(String fecha) {
        Connection con;//importar conexion de mySQL y le damos un nombre 
        Conexion cn = new Conexion();//Mandamos a llamar a la clase conexion y creamos un contructor
        PreparedStatement ps;//importar conexion de mySQL y le damos un nombre 
        ResultSet rs;//importar conexion de mySQL y le damos un nombre 
        //Un try y catch es como un if y un else re´pectivamente para MySQL, con try haremos algo, y si no puede hacerlo utilizara una catch
        try {//Intentar hacer lo siguiente 
            String sql = "SELECT total FROM ventas WHERE fecha = ?";//Creamos una variable "sql" para guardar un comando de sql y meter el registro de un provedor en la tabla "provedor" de la BD "sistemaventa"        
            con = cn.getConnection();//Sirve para establecer la conexion con la base de datos
            ps = con.prepareStatement(sql);//Sirve para mandar lavariable en la que tenemos guardada la consulta de MySQL
            ps.setString(1, fecha);
            DefaultPieDataset dataset = new DefaultPieDataset();//Sirve para ir guardando  datos para posteriormente hacer una grafica
            //Sirve para poder ejecutar la consulta pero que devolvera algo
            rs = ps.executeQuery();//ps.executeQuery() devolvera un resultado a mostrar (Resulset)por lo que lo guardamos en la importacion de "Resulset"
            //Creamos un bucle while para poder mostrar lo que se obtubo de la consulta
            while(rs.next()){//rs.next() sirve para que pase al siguiente espacio, como el (i+) del ciclo for
                //"Nombre objeto tipo mostrar dato".setValue("nombre objeto almaceanr resultado".get"tipo de dato a obtener"("Campo de la base de datos"));
                dataset.setValue(rs.getString("total"), rs.getDouble("total"));//Vamos a adañir a un combobox lo que se obtenga del registro de nombre     
            }
            //JFreeChart "nombre objeto tipo grafico"= ChartFactory.create"Tipo de grafico"("titulo del grafico", "nombre objeto tipo dataset")
            JFreeChart jf= ChartFactory.createPieChart("Reporte de venta", dataset);//Vamos a realizar el grafico
            //vamos a agregarlo al frame
            //ChartFrame "nombre objeto tipo chartframe" = new ChartFrame("Titulo", "nombre objeto tipo grafico")
            ChartFrame f = new ChartFrame("Total de ventas por dia", jf);//creamos un frame para fuardar el grafico y le ponemos un titulo
            f.setSize(1000, 500);//le damos el tamaño al frame 
            f.setLocationRelativeTo(null);//picisionamos el frame en el centro
            f.setVisible(true);//hacemos el frame visible
            
        } catch (SQLException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara MsSQL y los mostrara aqui)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error de MySQL
            
        }

    }
}
