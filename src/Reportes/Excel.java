package Reportes;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Modelo.Conexion;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author: Angel Gabriel Mendez Diaz
 * Fecha de creacion: 02 de Enero del 2023
 * Fecha de modificacion:
 * Nombre del programa: Clase con metodo para poder generar reportes en excel

 */
 
public class Excel {
    //Creamos un metodo para poder crear un reporte en excel
    public static void reporte() {
        //Workbook "nombre del libro" = new XSSFWorkbook();
        Workbook book = new XSSFWorkbook();//Crear un constructor de libro de trabajo en blanco
        Sheet sheet = book.createSheet("Productos");//Creamos una nueva hoja en el libro creado con el nombre de "productos" 
        //Creamos un try para intentar hacer lo siguiente en excel 
        try {
            //Insertamos el icono de la empresa
            InputStream is = new FileInputStream("src/Imagenes/logo.png");// CONTRUCTOR PARA INSERTAR IMAGENInputStream "nombre del objeto donde estara el icono" = new FileInputStream("Direccion de el icono")
            byte[] bytes = IOUtils.toByteArray(is);//Guardamos en un arreglo de tipo "byte" llamado "bytes" para guardar los bites del icono
            // int "NOMBRE VARIABLE PARA GUARDAR EL INDICE DE LA IMGAGEN" = book.addPicture("NOMBRE DEL ARRGLO DONDE SE GUARDO SU TAMAÑO", Workbook.PICTURE_TYPE_PNG);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);//Agregamos la imagen al libro y lo guardamos en una variable entera para su indice
            //"nombre del objeto de InputStream".close();
            is.close();//Cerramos el objeto "is" 
 
            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();
            //nos ubicamos en la fila 1 columna 0
            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(1);
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);
            //Colocamos el tipo de fuente(Letra)
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);
            //Colocamos el titulo del reporte
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Productos");
 
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
           //Creamos los encabezados 
            String[] cabecera = new String[]{"Código", "Nombre", "Precio", "Existencia"};
 
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
 
            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
 
            Row filaEncabezados = sheet.createRow(4);
            //Utilizamos el ciclo for para recorrer la cabezera
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i);
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }
 
            Conexion con = new Conexion();
            PreparedStatement ps;
            ResultSet rs;
            Connection conn = con.getConnection();
 
            int numFilaDatos = 5;
 
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            //Hacemos la consulta a la base de datos
            ps = conn.prepareStatement("SELECT codigo, nombre, precio, stock FROM productos");
            rs = ps.executeQuery();
 
            int numCol = rs.getMetaData().getColumnCount();
            //Ciclo wahile para recorrer por los resultados que trajo la consulta
            while (rs.next()) {
                Row filaDatos = sheet.createRow(numFilaDatos);
               //Vamos agregando cada resultado a su respectiva celda en excel
                for (int a = 0; a < numCol; a++) {
 
                    Cell CeldaDatos = filaDatos.createCell(a);
                    CeldaDatos.setCellStyle(datosEstilo);
                    CeldaDatos.setCellValue(rs.getString(a + 1));
                }
 
 
                numFilaDatos++;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            //metodo para hacer una descarga del reporte 
            sheet.setZoom(150);
            String fileName = "productos";//nombre de la descarga
            String home = System.getProperty("user.home");//Proprietario del de la pc
            File file = new File(home + "/Downloads/" + fileName + ".xlsx");//nombre del lugar a guargdar y con extension .xlsx(nuevo formato de excel)
            //Abrimos el archivo descargado
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Reporte Generado");
 
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException | SQLException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
}