/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Reportes.Excel;
import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.Config;
import Modelo.Detalle;
import Modelo.Eventos;
import Modelo.Producto;
import Modelo.ProductoDao;
import Modelo.Provedor;
import Modelo.ProvedorDao;
import Modelo.Venta;
import Modelo.VentaDao;
import Modelo.login;
import Reportes.Grafico;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfWriter;
import static com.itextpdf.text.pdf.XfaXpathConstructor.XdpPackage.Pdf;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import javax.swing.text.Position;
import javax.swing.text.Segment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Desktop;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Admin
 */
public class Sistema extends javax.swing.JFrame {
    Date FechaVenta = new Date();//Creamos el objeto de tipo fecha para registrar la venta
    String FechaActual = new SimpleDateFormat("dd/MM/yyyy").format(FechaVenta);//Vamos a enviar la fecha en un tipo de formato
    Cliente cl = new Cliente();//Mandamos a traer al la clase "Cliente" donde esta el metodo para mandar datos a la tabla "clientes" de la BD "sistemaventa" mediante la conexion de BD
    ClienteDao client = new ClienteDao();//Mandamos a traer al la clase "ClienteDao" donde esta el metodo para capturar los datos de la tabla "clientes" para la Base de datos "sistemaventa"
    Provedor pr = new Provedor();//Mandamos a traer al la clase "Provedor" donde esta el metodo para mandar datos a la tabla "provedor" de la BD "sistemaventa" mediante la conexion de BD
    ProvedorDao PrDao = new ProvedorDao();//Mandamos a traer al la clase "ProvedorDao" donde esta el metodo para capturar los datos de la tabla "provedor" para la Base de datos "sistemaventa"
    Producto pro = new Producto();//Mandamos a traer al la clase "Producto" donde esta el metodo para mandar datos a la tabla "producto" de la BD "sistemaventa" mediante la conexion de BD
    ProductoDao ProDao = new ProductoDao();//Mandamos a traer al la clase "ProductoDao" donde esta el metodo para capturar los datos de la tabla "producto" para la Base de datos "sistemaventa"
    DefaultTableModel modelo = new DefaultTableModel();//Importante para mostrar tablas en los formularios 
    DefaultTableModel tmp = new DefaultTableModel();//Importante para mostrar tablas en los formularios     
    int item = 0;//Variable que utilizara para agregar productos a a la tablaVenta
    double Totalpagar = 0.00; //Variable que se utilizara para el total a pagar
    Venta v = new Venta();//Mandamos a llamar a la clase Venta
    VentaDao Vdao = new VentaDao();//Mandamos a llamar a la clase VentaDao
    Detalle Dv = new Detalle();//Mandamos a llamar a la clase Detalle
    Config conf = new Config();//Mandamos a llamar la case config
    Eventos event = new Eventos();//Llamamos a la clase de eventos con sus metodos de validar

    /**
     * Creates new form Sistema
     */
    public Sistema() {
        //aqui podemos realizar acciones para que se haggan iniciando el programa
        initComponents();
        
    }
    
    //Crearemos un constructor que llevara una sobrecarga de metodos
    public Sistema (login priv){
        //Copiamos todos los constructores
        initComponents();
        this.setLocationRelativeTo(null);//Vamos a centrar nuestro formulario al ejecutarlo
        txtIdCliente.setVisible(false);//Ocultamos el id del cliente 
        txtIdProvedor.setVisible(false);//Ocultamos el id del Provedor
        txtIdPro.setVisible(false);//Ocultamos el id del producto
        txtIdProducto.setVisible(false);//Ocultamos el id del producto en la tabla ventas
        txtIdConfig.setVisible(false);//Ocultamos el id del Confi en la tabla ventas
        ListarConfig();
        //Utilizamos la libreria swingx-all-1.6.4 para poder auto acompletar los campos
        AutoCompleteDecorator.decorate(cbxProvedor);//Sirve para autoacompletar el combobox solamente
        ProDao.ConsultarProvedor(cbxProvedor);//Llamamos a nuestro metodo de consuldar el nombre del provedor para el combobox de producto

        //Creamos un if para verificar si es un administrador o un Asistente
        if (priv.getRol().equals("Asistente")) {//si lo que obtinen en rol de la clase login es igual a Asistente entonces
            //como prueba desabilitaremos los botones de productos y provedor
            //"nombre del boton".setEnabled("Verdadero (activar) o False (desacticar)) para activar o desactivar los botones
            btnGuardarProvedor.setEnabled(false);//Desactivamos el boton de guardar brovedor 
            btnEditarProvedor.setEnabled(false);//Desactivamos el boton de editar brovedor 
            btnEliminarProvedor.setEnabled(false);//Desactivamos el boton de eliminar brovedor 
            btnGuardarProductos.setEnabled(false);//Desactivamos el boton de guardar productos 
            btnEliminarProductos.setEnabled(false);//Desactivamos el boton de eliminar productos
            btnRegistrar.setEnabled(false);//Desactivamos el boton de usuarios que mostrara un form para registrarse
            LabelVendedor.setText(priv.getNombre());//mostrabos en label el nombre del ususario
        } else {
            LabelVendedor.setText(priv.getNombre());//mostrabos en label el nombre ususario
        }
    }

    //Creamos un metodo que se ejecutara cada que se presione el boton de cliente en el formulario. Sirve para poder enlistar a los Clientes
    public void ListarCliente() {
        List<Cliente> ListaCl = client.ListarCliente();//Sirve para poder enlistar a los clientes
        modelo = (DefaultTableModel) TableCliente.getModel(); //Darle forma a la tabla tiliente del formulario
        Object[] ob = new Object[6];//Creamos un arreglo llamado objeto para almacenar la informacion del un tamaño de 6
        //Creamos un for para ir llenando el arreglo
        for (int i = 0; i < ListaCl.size(); i++) {//ListaCl.size() sirve para saber el tamaño de una lista empezando por 0
            //"nombre del arreglo"["posicion"]="nombre del objeto de la lista".get("pocicion del cliclo for".get"Nombre del campo a obtener"
            ob[0] = ListaCl.get(i).getId();
            ob[1] = ListaCl.get(i).getDni();
            ob[2] = ListaCl.get(i).getNombre();
            ob[3] = ListaCl.get(i).getTelefono();
            ob[4] = ListaCl.get(i).getDireccion();
            ob[5] = ListaCl.get(i).getRazon();
            modelo.addRow(ob);//Agregamos el arreglo objeto a las filas del paquete modelo 
        }
        TableCliente.setModel(modelo);//Mostramos las filas de modelo a la tabla de clientes
    }

    //Creamos un metodo que se ejecutara cada que se presione el boton de cliente en el formulario. Sirve para poder enlistar a los provedores
    public void ListarProvedor() {
        List<Provedor> ListaPr = PrDao.ListarProvedor();//Sirve para poder enlistar a los clientes
        modelo = (DefaultTableModel) TableProvedor.getModel(); //Darle forma a la tabla tiliente del formulario
        Object[] ob = new Object[6];//Creamos un arreglo llamado objeto para almacenar la informacion del un tamaño de 6
        //Creamos un for para ir llenando el arreglo
        for (int i = 0; i < ListaPr.size(); i++) {//ListaCl.size() sirve para saber el tamaño de una lista empezando por 0
            //"nombre del arreglo"["posicion"]="nombre del objeto de la lista".get("pocicion del cliclo for".get"Nombre del campo a obtener"
            ob[0] = ListaPr.get(i).getId();
            ob[1] = ListaPr.get(i).getRuc();
            ob[2] = ListaPr.get(i).getNombre();
            ob[3] = ListaPr.get(i).getTelefono();
            ob[4] = ListaPr.get(i).getDireccion();
            ob[5] = ListaPr.get(i).getRazon();
            modelo.addRow(ob);//Agregamos el arreglo objeto a las filas del paquete modelo 
        }
        TableProvedor.setModel(modelo);//Mostramos las filas de modelo a la tabla de Provedor
    }

    //Creamos un metodo que se ejecutara cada que se presione el boton de cliente en el formulario. Sirve para poder enlistar a los productos
    public void ListarProducto() {
        List<Producto> ListaPro = ProDao.ListarProducto();//Creamos un cosntructor de tipo lista
        modelo = (DefaultTableModel) TableProducto.getModel(); //Darle forma a la tabla tiliente del formulario
        Object[] ob = new Object[6];//Creamos un arreglo llamado objeto para almacenar la informacion del un tamaño de 6
        //Creamos un for para ir llenando el arreglo
        for (int i = 0; i < ListaPro.size(); i++) {//ListaCl.size() sirve para saber el tamaño de una lista empezando por 0
            //"nombre del arreglo"["posicion"]="nombre del objeto de la lista".get("pocicion del cliclo for".get"Nombre del campo a obtener"
            ob[0] = ListaPro.get(i).getId();
            ob[1] = ListaPro.get(i).getCodigo();
            ob[2] = ListaPro.get(i).getNombre();
            ob[3] = ListaPro.get(i).getProvedor();
            ob[4] = ListaPro.get(i).getStock();
            ob[5] = ListaPro.get(i).getPrecio();
            modelo.addRow(ob);//Agregamos el arreglo objeto a las filas del paquete modelo 
        }
        TableProducto.setModel(modelo);//Mostramos las filas de modelo a la tabla de Producto
    }

    //Creamos el metodo para listar los datos de la empresa(Config)
    public void ListarConfig() {
        conf = ProDao.BuscarDatos();//Igualamos nuestro objeto de tipo Config a ProDaos con el metodo buscar datos
        //Pasamos lo que va a retornar de conf a los cuadros de texto
        txtIdConfig.setText("" + conf.getId());
        txtRucConfig.setText("" + conf.getRuc());
        txtNombreConfig.setText("" + conf.getNombre());
        txtTelefonoConfig.setText("" + conf.getTelefono());
        txtDireccionConfig.setText("" + conf.getDireccion());
        txtRazonConfig.setText("" + conf.getRazon());

    }

    //Creamos un metodo que se ejecutara cada que se presione el boton de cliente en el formulario. Sirve para poder enlistar a los productos
    public void ListarVentas() {
        List<Venta> ListaVenta = Vdao.ListarVentas();//Creamos un cosntructor de tipo lista
        modelo = (DefaultTableModel) TableVentas.getModel(); //Darle forma a la tabla tiliente del formulario
        Object[] ob = new Object[4];//Creamos un arreglo llamado objeto para almacenar la informacion del un tamaño de 6
        //Creamos un for para ir llenando el arreglo
        for (int i = 0; i < ListaVenta.size(); i++) {//ListaCl.size() sirve para saber el tamaño de una lista empezando por 0
            //"nombre del arreglo"["posicion"]="nombre del objeto de la lista".get("pocicion del cliclo for".get"Nombre del campo a obtener"
            ob[0] = ListaVenta.get(i).getId();
            ob[1] = ListaVenta.get(i).getCliente();
            ob[2] = ListaVenta.get(i).getVendedor();
            ob[3] = ListaVenta.get(i).getTotal();
            modelo.addRow(ob);//Agregamos el arreglo objeto a las filas del paquete modelo 
        }
        TableVentas.setModel(modelo);//Mostramos las filas de modelo a la tabla de Producto
    }
    
    //Creamos un metodo para limpiar las tablas
    public void LimpiarTable() {
        for (int i = 0; i < modelo.getRowCount(); i++) {//tamaño de la tabla del paquete modelo
            modelo.removeRow(i);//Este comando sirve para eliminar filas en las tablas del paquete modelo
            i = i - 1;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        BotonNuevaVenta = new javax.swing.JButton();
        BotonClientes = new javax.swing.JButton();
        BotonProductos = new javax.swing.JButton();
        BotonProvedor = new javax.swing.JButton();
        BotonVentas = new javax.swing.JButton();
        BotonConfiguracion = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoVenta = new javax.swing.JTextField();
        txtDescripcionVenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCantidadVenta = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtStockDisponibleVenta = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnEliminarVenta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableVenta = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtRucVenta = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNombreClienteVenta = new javax.swing.JTextField();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        LabelTotal = new javax.swing.JLabel();
        txtTelefonoClienteVenta = new javax.swing.JTextField();
        txtRazonClienteVenta1 = new javax.swing.JTextField();
        txtDireccionClienteVenta2 = new javax.swing.JTextField();
        txtIdVenta1 = new javax.swing.JTextField();
        txtIdProducto = new javax.swing.JTextField();
        LabelVendedor = new javax.swing.JLabel();
        btnGraficar = new javax.swing.JButton();
        MyDate = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        txtDniCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtRazonCliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableCliente = new javax.swing.JTable();
        btnGuardarCliente = new javax.swing.JButton();
        btnEditarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnNuevoCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtRucProvedor = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtNombreProvedor = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtTelefonoProvedor = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtDireccionProvedor = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtRazonProvedor = new javax.swing.JTextField();
        btnGuardarProvedor = new javax.swing.JButton();
        btnEditarProvedor = new javax.swing.JButton();
        btnEliminarProvedor = new javax.swing.JButton();
        btnNuevoProvedor = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableProvedor = new javax.swing.JTable();
        txtIdProvedor = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtPrecioProducto = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableProducto = new javax.swing.JTable();
        btnEditarProductos = new javax.swing.JButton();
        btnNuevoProductos = new javax.swing.JButton();
        btnEliminarProductos = new javax.swing.JButton();
        btnGuardarProductos = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        btnExcelProvedor = new javax.swing.JButton();
        cbxProvedor = new javax.swing.JComboBox<>();
        txtIdPro = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableVentas = new javax.swing.JTable();
        btnPdfVentas = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txtRucConfig = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtNombreConfig = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtTelefonoConfig = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtDireccionConfig = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtRazonConfig = new javax.swing.JTextField();
        btnActualizarConfig = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        txtIdConfig = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));

        BotonNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Nventa.png"))); // NOI18N
        BotonNuevaVenta.setText(" Nueva Venta");
        BotonNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonNuevaVentaActionPerformed(evt);
            }
        });

        BotonClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Clientes.png"))); // NOI18N
        BotonClientes.setText("Clientes");
        BotonClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonClientesActionPerformed(evt);
            }
        });

        BotonProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        BotonProductos.setText("Productos");
        BotonProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonProductosActionPerformed(evt);
            }
        });

        BotonProvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/proveedor.png"))); // NOI18N
        BotonProvedor.setText("Provedor");
        BotonProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonProvedorActionPerformed(evt);
            }
        });

        BotonVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/compras.png"))); // NOI18N
        BotonVentas.setText("Ventas");
        BotonVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonVentasActionPerformed(evt);
            }
        });

        BotonConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/config.png"))); // NOI18N
        BotonConfiguracion.setText("Configuracion");
        BotonConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonConfiguracionActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo.png"))); // NOI18N

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Clientes.png"))); // NOI18N
        btnRegistrar.setText("Usuarios");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BotonProvedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(2, 2, 2)
                .addComponent(BotonNuevaVenta)
                .addGap(18, 18, 18)
                .addComponent(btnRegistrar)
                .addGap(22, 22, 22)
                .addComponent(BotonClientes)
                .addGap(18, 18, 18)
                .addComponent(BotonProvedor)
                .addGap(18, 18, 18)
                .addComponent(BotonProductos)
                .addGap(20, 20, 20)
                .addComponent(BotonVentas)
                .addGap(18, 18, 18)
                .addComponent(BotonConfiguracion)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 720));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/encabezado.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 830, 160));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Codigo");

        txtCodigoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyTyped(evt);
            }
        });

        txtDescripcionVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionVentaKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Descripcion");

        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Cantidad");

        txtPrecioVenta.setEditable(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Precio");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Stook Disponible");

        btnEliminarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminarVenta.setText("Eliminar");
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        tableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION", "CANTIDAD", "PRECIO ", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(tableVenta);
        if (tableVenta.getColumnModel().getColumnCount() > 0) {
            tableVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("DNI/RUC");

        txtRucVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRucVentaKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("NOMBRE");

        txtNombreClienteVenta.setEditable(false);

        btnGenerarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/print.png"))); // NOI18N
        btnGenerarVenta.setText("Generar Venta");
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        jLabel10.setText("TOTAL A PAGAR");

        LabelTotal.setText("-----");

        txtTelefonoClienteVenta.setEditable(false);

        txtRazonClienteVenta1.setEditable(false);

        txtDireccionClienteVenta2.setEditable(false);

        LabelVendedor.setText("Hola mundo");

        btnGraficar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/torta.png"))); // NOI18N
        btnGraficar.setText("Graficar");
        btnGraficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficarActionPerformed(evt);
            }
        });

        jLabel11.setText("Seleccionar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRucVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIdVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addGap(38, 38, 38)
                        .addComponent(LabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 265, Short.MAX_VALUE))
                            .addComponent(txtDescripcionVenta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtStockDisponibleVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6))
                        .addGap(125, 125, 125))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnGraficar)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MyDate, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(72, 72, 72))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDireccionClienteVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtRazonClienteVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(LabelVendedor)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnGraficar)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(MyDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminarVenta)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStockDisponibleVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRucVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(LabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccionClienteVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRazonClienteVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelVendedor))
                .addGap(18, 18, 18))
        );

        jTabbedPane1.addTab("tab1", jPanel2);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("DNI/RUC");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("NOMBRE");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Telefono");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Direccion");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Razon Social");

        TableCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DNI/RUC", "NOMBRE", "Telefono", "Direccion", "Razon Social"
            }
        ));
        TableCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableClienteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableCliente);
        if (TableCliente.getColumnModel().getColumnCount() > 0) {
            TableCliente.getColumnModel().getColumn(0).setPreferredWidth(100);
            TableCliente.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableCliente.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableCliente.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableCliente.getColumnModel().getColumn(4).setPreferredWidth(80);
            TableCliente.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        btnGuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/GuardarTodo.png"))); // NOI18N
        btnGuardarCliente.setText("Guardar");
        btnGuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });

        btnEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Actualizar (2).png"))); // NOI18N
        btnEditarCliente.setText("Actualizar");
        btnEditarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        btnNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo.png"))); // NOI18N
        btnNuevoCliente.setText("Nuevo");
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRazonCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarCliente)
                            .addComponent(btnEliminarCliente))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNuevoCliente)
                            .addComponent(btnEditarCliente)))
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 94, Short.MAX_VALUE)
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(306, 306, 306))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane2))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtDniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtRazonCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardarCliente)
                            .addComponent(btnEditarCliente))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminarCliente)
                            .addComponent(btnNuevoCliente))
                        .addContainerGap(162, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("DNI/RUC");

        txtRucProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRucProvedorActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("NOMBRE");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Telefono");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("Direccion");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Razon Social");

        btnGuardarProvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/GuardarTodo.png"))); // NOI18N
        btnGuardarProvedor.setText("Guardar");
        btnGuardarProvedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProvedorActionPerformed(evt);
            }
        });

        btnEditarProvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Actualizar (2).png"))); // NOI18N
        btnEditarProvedor.setText("Actualizar");
        btnEditarProvedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProvedorActionPerformed(evt);
            }
        });

        btnEliminarProvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminarProvedor.setText("Eliminar");
        btnEliminarProvedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProvedorActionPerformed(evt);
            }
        });

        btnNuevoProvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo.png"))); // NOI18N
        btnNuevoProvedor.setText("Nuevo");
        btnNuevoProvedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProvedorActionPerformed(evt);
            }
        });

        TableProvedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DNI/RUC", "NOMBRE", "Telefono", "Direccion", "Razon Social"
            }
        ));
        TableProvedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProvedorMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableProvedor);
        if (TableProvedor.getColumnModel().getColumnCount() > 0) {
            TableProvedor.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProvedor.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableProvedor.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProvedor.getColumnModel().getColumn(3).setPreferredWidth(40);
            TableProvedor.getColumnModel().getColumn(4).setPreferredWidth(80);
            TableProvedor.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(37, 37, 37)
                        .addComponent(txtRucProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelefonoProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDireccionProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtRazonProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarProvedor)
                            .addComponent(btnEliminarProvedor))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEditarProvedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevoProvedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIdProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(312, 312, 312))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(txtIdProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtRucProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txtNombreProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtTelefonoProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtDireccionProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtRazonProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardarProvedor)
                            .addComponent(btnEditarProvedor))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminarProvedor)
                            .addComponent(btnNuevoProvedor))
                        .addContainerGap(167, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("tab3", jPanel4);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setText("Codigo");

        txtCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoProductoActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setText("Descripcion");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Cantidad");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Precio");

        txtPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoKeyTyped(evt);
            }
        });

        TableProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Codigo", "Descripcion", "Provedor", "Stok", "Precio"
            }
        ));
        TableProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProductoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TableProducto);
        if (TableProducto.getColumnModel().getColumnCount() > 0) {
            TableProducto.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProducto.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableProducto.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProducto.getColumnModel().getColumn(3).setPreferredWidth(60);
            TableProducto.getColumnModel().getColumn(4).setPreferredWidth(40);
            TableProducto.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        btnEditarProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Actualizar (2).png"))); // NOI18N
        btnEditarProductos.setText("Actualizar");
        btnEditarProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductosActionPerformed(evt);
            }
        });

        btnNuevoProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo.png"))); // NOI18N
        btnNuevoProductos.setText("Nuevo");
        btnNuevoProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEliminarProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminarProductos.setText("Eliminar");
        btnEliminarProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductosActionPerformed(evt);
            }
        });

        btnGuardarProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/GuardarTodo.png"))); // NOI18N
        btnGuardarProductos.setText("Guardar");
        btnGuardarProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductosActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Provedor");

        btnExcelProvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/excel.png"))); // NOI18N
        btnExcelProvedor.setText("Reporte");
        btnExcelProvedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelProvedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelProvedorActionPerformed(evt);
            }
        });

        cbxProvedor.setEditable(true);
        cbxProvedor.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(84, 84, 84)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        .addComponent(jLabel22)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel24)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel26)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(55, 55, 55)
                            .addComponent(btnExcelProvedor)))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(btnEliminarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnNuevoProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(btnGuardarProductos)
                            .addGap(18, 18, 18)
                            .addComponent(btnEditarProductos))))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 393, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(cbxProvedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardarProductos)
                            .addComponent(btnEditarProductos))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminarProductos)
                            .addComponent(btnNuevoProductos))
                        .addGap(18, 18, 18)
                        .addComponent(btnExcelProvedor))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab4", jPanel5);

        TableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "VENDEDOR", "TOTAL"
            }
        ));
        TableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TableVentas);
        if (TableVentas.getColumnModel().getColumnCount() > 0) {
            TableVentas.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableVentas.getColumnModel().getColumn(1).setPreferredWidth(60);
            TableVentas.getColumnModel().getColumn(2).setPreferredWidth(60);
            TableVentas.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        btnPdfVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        btnPdfVentas.setText("Reporte");
        btnPdfVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(btnPdfVentas)
                        .addGap(42, 42, 42)
                        .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPdfVentas)
                    .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab5", jPanel6);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("RUC");

        txtRucConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRucConfigActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("NOMBRE");

        txtNombreConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreConfigActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("TELEFONO");

        txtTelefonoConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoConfigActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("DIRECCION");

        txtDireccionConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionConfigActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("RAZON SOCIAL");

        txtRazonConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRazonConfigActionPerformed(evt);
            }
        });

        btnActualizarConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Actualizar (2).png"))); // NOI18N
        btnActualizarConfig.setText("Actualizar");
        btnActualizarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarConfigActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel32.setText("DATOS DE LA EMPRESA");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizarConfig)
                .addGap(302, 302, 302))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                        .addComponent(jLabel32)
                        .addGap(216, 216, 216))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel27)
                            .addComponent(txtRucConfig, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(jLabel30)
                            .addComponent(txtDireccionConfig))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28)
                            .addComponent(txtNombreConfig, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(jLabel31)
                            .addComponent(txtRazonConfig))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(txtTelefonoConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefonoConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRucConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31))
                .addGap(8, 8, 8)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRazonConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79)
                .addComponent(btnActualizarConfig)
                .addGap(85, 85, 85))
        );

        jTabbedPane1.addTab("tab6", jPanel7);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 830, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonNuevaVentaActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);//para mostrar siempre el panel 0 del formulario
    }//GEN-LAST:event_BotonNuevaVentaActionPerformed

    private void BotonClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonClientesActionPerformed
        // TODO add your handling code here:
        LimpiarTable();//llamamos el metodo para limpiar la tabla de cliente anter de verlos y no se repitan
        ListarCliente();//llamamos el metodo de enlistar a los clientes
        jTabbedPane1.setSelectedIndex(1);//para mostrar siempre el panel 2 del formulario

    }//GEN-LAST:event_BotonClientesActionPerformed

    private void BotonProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonProductosActionPerformed
        // TODO add your handling code here:
        LimpiarTable();//llamamos el metodo para limpiar la tabla de cliente anter de verlos y no se repitan
        ListarProducto();//Llamamos el metodo para que enliste los productos
        jTabbedPane1.setSelectedIndex(3);//para mostrar siempre el panel 4 del formulario
    }//GEN-LAST:event_BotonProductosActionPerformed

    private void BotonProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonProvedorActionPerformed
        // TODO add your handling code here:
        LimpiarTable();//llamamos el metodo para limpiar la tabla de cliente anter de verlos y no se repitan
        ListarProvedor();
        jTabbedPane1.setSelectedIndex(2);//para mostrar siempre el panel 3 del formulario
    }//GEN-LAST:event_BotonProvedorActionPerformed

    private void BotonVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonVentasActionPerformed
        // TODO add your handling code here:
        LimpiarTable();
        ListarVentas();        
        jTabbedPane1.setSelectedIndex(4);//para mostrar siempre el panel 4 del formulario
    }//GEN-LAST:event_BotonVentasActionPerformed

    private void BotonConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonConfiguracionActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(5);//para mostrar siempre el panel 4 del formulario
    }//GEN-LAST:event_BotonConfiguracionActionPerformed

    private void txtRucProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRucProvedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRucProvedorActionPerformed

    private void txtCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProductoActionPerformed

    private void txtRucConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRucConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRucConfigActionPerformed

    private void txtNombreConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConfigActionPerformed

    private void txtTelefonoConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoConfigActionPerformed

    private void txtDireccionConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionConfigActionPerformed

    private void txtRazonConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRazonConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRazonConfigActionPerformed

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed
        // TODO add your handling code here:
        //Creamos un if para verificar que los cuadros de texto no esten vacios
        if (!"".equals(txtDniCliente.getText()) || !"".equals(txtNombreCliente.getText()) || !"".equals(txtTelefonoCliente.getText()) || !"".equals(txtDireccionCliente.getText()) || !"".equals(txtRazonCliente.getText())) {//Si es diferente la compararcion de un espacio vacio con un caualquiera de los campos txt entonces
            cl.setDni(Integer.parseInt(txtDniCliente.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" convertido a entero
            cl.setNombre(txtNombreCliente.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" 
            cl.setTelefono(Integer.parseInt(txtTelefonoCliente.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtTelefonoCliente" convertido a entero
            cl.setDireccion(txtDireccionCliente.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDireccionCliente "
            cl.setRazon(txtRazonCliente.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtRazonCliente "
            client.RegistrarCliente(cl);//Llamamos  a la clase "ClienteDao" mediante el objeto "client" y utilizammos su metodo de registrar clientes con el paramettro cl
            LimpiarTable();
            ListarCliente();
            LimpiarCliente();

            JOptionPane.showMessageDialog(null, "Cliente Registrado");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    private void btnGuardarProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProvedorActionPerformed
        // TODO add your handling code here:
        //Creamos un if para verificar que los cuadros de texto no esten vacios
        if (!"".equals(txtRucProvedor.getText()) || !"".equals(txtNombreProvedor.getText()) || !"".equals(txtTelefonoProvedor.getText()) || !"".equals(txtDireccionProvedor.getText()) || !"".equals(txtRazonProvedor.getText())) {//Si es diferente la compararcion de un espacio vacio con un caualquiera de los campos txt entonces
            pr.setRuc(Integer.parseInt(txtRucProvedor.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" convertido a entero
            pr.setNombre(txtNombreProvedor.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" 
            pr.setTelefono(Integer.parseInt(txtTelefonoProvedor.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtTelefonoCliente" convertido a entero
            pr.setDireccion(txtDireccionProvedor.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDireccionCliente "
            pr.setRazon(txtRazonProvedor.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtRazonCliente "
            PrDao.RegistrarProvedor(pr);//Llamamos  a la clase "ClienteDao" mediante el objeto "client" y utilizammos su metodo de registrar clientes con el paramettro cl
            LimpiarTable();
            ListarProvedor();
            LimpiarProvedor();

            JOptionPane.showMessageDialog(null, "Povedor Registrado");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnGuardarProvedorActionPerformed
    // Sirve para mostrar lo que este en la tabla en los cuadros de texto cuando se selecionen con un clic del mouse 
    private void TableProvedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProvedorMouseClicked
        int fila = TableProvedor.rowAtPoint(evt.getPoint());//TableCliente.rowAtPoint(evt.getPoint()) Sirve oara poder obtener los datos de una tabla al seleccionarlos con el mouse y un clic
        //TableCliente.getValueAt(fila,0).toString() Sirve para obtener el valor de una fila en su respectiva pocision y pasarlo a  
        txtIdProvedor.setText(TableProvedor.getValueAt(fila, 0).toString());//Mostrar el id del cliente en un cuadro de texto
        txtRucProvedor.setText(TableProvedor.getValueAt(fila, 1).toString());//Mostrar el dni del cliente en un cuadro de texto
        txtNombreProvedor.setText(TableProvedor.getValueAt(fila, 2).toString());//Mostrar el nombre del cliente en un cuadro de texto
        txtTelefonoProvedor.setText(TableProvedor.getValueAt(fila, 3).toString());//Mostrar el telefono del cliente en un cuadro de texto
        txtDireccionProvedor.setText(TableProvedor.getValueAt(fila, 4).toString());//Mostrar la direccion del cliente en un cuadro de texto
        txtRazonProvedor.setText(TableProvedor.getValueAt(fila, 5).toString());//Mostrar la razon del cliente en un cuadro de texto

    }//GEN-LAST:event_TableProvedorMouseClicked
    // Sirve para mostrar lo que este en la tabla en los cuadros de texto cuando se selecionen con un clic del mouse
    private void TableClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableClienteMouseClicked

        int fila = TableCliente.rowAtPoint(evt.getPoint());//TableCliente.rowAtPoint(evt.getPoint()) Sirve oara poder obtener los datos de una tabla al seleccionarlos con el mouse y un clic
        //TableCliente.getValueAt(fila,0).toString() Sirve para obtener el valor de una fila en su respectiva pocision y pasarlo a  
        txtIdCliente.setText(TableCliente.getValueAt(fila, 0).toString());//Mostrar el id del cliente en un cuadro de texto
        txtDniCliente.setText(TableCliente.getValueAt(fila, 1).toString());//Mostrar el dni del cliente en un cuadro de texto
        txtNombreCliente.setText(TableCliente.getValueAt(fila, 2).toString());//Mostrar el nombre del cliente en un cuadro de texto
        txtTelefonoCliente.setText(TableCliente.getValueAt(fila, 3).toString());//Mostrar el telefono del cliente en un cuadro de texto
        txtDireccionCliente.setText(TableCliente.getValueAt(fila, 4).toString());//Mostrar la direccion del cliente en un cuadro de texto
        txtRazonCliente.setText(TableCliente.getValueAt(fila, 5).toString());//Mostrar la razon del cliente en un cuadro de texto

    }//GEN-LAST:event_TableClienteMouseClicked

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        // TODO add your handling code here:
        //Creamos un if para verificar que los cuadros de texto no esten vacios
        if (!"".equals(txtIdCliente.getText())) {//Si es diferente la compararcion de un espacio vacio con  caualquiera de los campos txt entonces
            // JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro") sirve para hacer preguntas al usuario 
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro");//Vamos a hacer una pregunta
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdCliente.getText());
                client.EliminarCliente(id);
                JOptionPane.showMessageDialog(null, "Cliente eliminado");//Mostrar en pantalla el mensaje
                LimpiarTable();
                ListarCliente();
                LimpiarCliente();

            }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarClienteActionPerformed
        // TODO add your handling code here:
        if ("".equals(txtIdCliente.getText())) {//Si es diferente la compararcion de un espacio vacio con un caualquiera de los campos txt entonces
            JOptionPane.showMessageDialog(null, "Selecciona una fila");//Mostrar en pantalla el mensaje
        } else {
            //Verificamos con un if si los campos estan vacios
            if (!"".equals(txtDniCliente.getText()) || !"".equals(txtNombreCliente.getText()) || !"".equals(txtTelefonoCliente.getText()) || !"".equals(txtDireccionCliente.getText()) || !"".equals(txtRazonCliente.getText())) {
                cl.setId(Integer.parseInt(txtIdCliente.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtIdCliente" convertido a entero
                cl.setDni(Integer.parseInt(txtDniCliente.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" convertido a entero
                cl.setNombre(txtNombreCliente.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" 
                cl.setTelefono(Integer.parseInt(txtTelefonoCliente.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtTelefonoCliente" convertido a entero
                cl.setDireccion(txtDireccionCliente.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDireccionCliente "
                cl.setRazon(txtRazonCliente.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtRazonCliente "
                client.ModificarCliente(cl);
                JOptionPane.showMessageDialog(null, "Cliente actualizado");//Mostrar en pantalla el mensaje
                LimpiarTable();
                LimpiarCliente();
                ListarCliente();

            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");//Mostrar en pantalla el mensaje

            }

        }

    }//GEN-LAST:event_btnEditarClienteActionPerformed

    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClienteActionPerformed
        // TODO add your handling code here:
        LimpiarCliente();
    }//GEN-LAST:event_btnNuevoClienteActionPerformed

    private void btnEliminarProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProvedorActionPerformed
        // TODO add your handling code here:
        //Creamos un if para verificar que los cuadros de texto no esten vacios
        if (!"".equals(txtIdProvedor.getText())) {//Si es diferente la compararcion de un espacio vacio con  caualquiera de los campos txt entonces
            // JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro") sirve para hacer preguntas al usuario 
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro");//Vamos a hacer una pregunta
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdProvedor.getText());
                PrDao.EliminarPovedor(id);
                JOptionPane.showMessageDialog(null, "Provedor eliminado");//Mostrar en pantalla el mensaje
                LimpiarTable();
                ListarProvedor();
                LimpiarProvedor();

            }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnEliminarProvedorActionPerformed

    private void btnEditarProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProvedorActionPerformed
        // TODO add your handling code here:
        if ("".equals(txtIdProvedor.getText())) {//Si es diferente la compararcion de un espacio vacio con un caualquiera de los campos txt entonces
            JOptionPane.showMessageDialog(null, "Selecciona una fila");//Mostrar en pantalla el mensaje
        } else {
            //Verificamos con un if si los campos estan vacios
            if (!"".equals(txtRucProvedor.getText()) || !"".equals(txtNombreProvedor.getText()) || !"".equals(txtTelefonoProvedor.getText()) || !"".equals(txtDireccionProvedor.getText()) || !"".equals(txtRazonProvedor.getText())) {
                pr.setId(Integer.parseInt(txtIdProvedor.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtIdCliente" convertido a entero
                pr.setRuc(Integer.parseInt(txtRucProvedor.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" convertido a entero
                pr.setNombre(txtNombreProvedor.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" 
                pr.setTelefono(Integer.parseInt(txtTelefonoProvedor.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtTelefonoCliente" convertido a entero
                pr.setDireccion(txtDireccionProvedor.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDireccionCliente "
                pr.setRazon(txtRazonProvedor.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtRazonCliente "
                PrDao.ModificarProvedor(pr);//Enviamos los parametros al metodo de modificar
                JOptionPane.showMessageDialog(null, "Provedor modificado");//Mostrar en pantalla el mensaje
                LimpiarTable();
                ListarProvedor();
                LimpiarProvedor();

            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");//Mostrar en pantalla el mensaje

            }

        }

    }//GEN-LAST:event_btnEditarProvedorActionPerformed

    private void btnNuevoProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProvedorActionPerformed
        // TODO add your handling code here:
        LimpiarProvedor();
    }//GEN-LAST:event_btnNuevoProvedorActionPerformed

    private void btnGuardarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductosActionPerformed
        // TODO add your handling code here:
        //Creamos un if para verificar que los cuadros de texto no esten vacios
        if (!"".equals(txtCodigoProducto.getText()) || !"".equals(txtDescripcionProducto.getText()) || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtPrecioProducto.getText()) || !"".equals(cbxProvedor.getSelectedItem())) {//Si es diferente la compararcion de un espacio vacio con un caualquiera de los campos txt entonces
            pro.setCodigo((txtCodigoProducto.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" convertido a entero
            pro.setNombre(txtDescripcionProducto.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" 
            pro.setProvedor(cbxProvedor.getSelectedItem().toString());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtTelefonoCliente" convertido a entero
            pro.setStock(Integer.parseInt(txtCantidadProducto.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDireccionCliente "
            pro.setPrecio(Double.parseDouble(txtPrecioProducto.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtRazonCliente "
            ProDao.RegistrarProducto(pro);//Llamamos  a la clase "ClienteDao" mediante el objeto "client" y utilizammos su metodo de registrar clientes con el paramettro cl
            LimpiarTable();
            ListarProducto();
            LimpiarProducto();

            JOptionPane.showMessageDialog(null, "Producto Registrado");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }

    }//GEN-LAST:event_btnGuardarProductosActionPerformed
    // Sirve para mostrar lo que este en la tabla en los cuadros de texto cuando se selecionen con un clic del mouse 
    private void TableProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProductoMouseClicked
        // TODO add your handling code here:
        int fila = TableProducto.rowAtPoint(evt.getPoint());//TableCliente.rowAtPoint(evt.getPoint()) Sirve oara poder obtener los datos de una tabla al seleccionarlos con el mouse y un clic
        //TableCliente.getValueAt(fila,0).toString() Sirve para obtener el valor de una fila en su respectiva pocision y pasarlo a  
        txtIdPro.setText(TableProducto.getValueAt(fila, 0).toString());//Mostrar el id del cliente en un cuadro de texto
        txtCodigoProducto.setText(TableProducto.getValueAt(fila, 1).toString());//Mostrar el dni del cliente en un cuadro de texto
        txtDescripcionProducto.setText(TableProducto.getValueAt(fila, 2).toString());//Mostrar el nombre del cliente en un cuadro de texto
        cbxProvedor.setSelectedItem(TableProducto.getValueAt(fila, 3).toString());//Mostrar el telefono del cliente en un cuadro de texto
        txtCantidadProducto.setText(TableProducto.getValueAt(fila, 4).toString());//Mostrar la direccion del cliente en un cuadro de texto
        txtPrecioProducto.setText(TableProducto.getValueAt(fila, 5).toString());//Mostrar la razon del cliente en un cuadro de texto

    }//GEN-LAST:event_TableProductoMouseClicked

    private void btnEliminarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductosActionPerformed
        // TODO add your handling code here:
        //Creamos un if para verificar que los cuadros de texto no esten vacios
        if (!"".equals(txtIdPro.getText())) {//Si es diferente la compararcion de un espacio vacio con  caualquiera de los campos txt entonces
            // JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro") sirve para hacer preguntas al usuario 
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro");//Vamos a hacer una pregunta
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdPro.getText());
                ProDao.EliminarProducto(id);
                JOptionPane.showMessageDialog(null, "Provedor eliminado");//Mostrar en pantalla el mensaje
                LimpiarTable();
                ListarProducto();
                LimpiarProducto();

            }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }

    }//GEN-LAST:event_btnEliminarProductosActionPerformed

    private void btnEditarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProductosActionPerformed
        // TODO add your handling code here:
        if ("".equals(txtIdPro.getText())) {//Si es diferente la compararcion de un espacio vacio con un caualquiera de los campos txt entonces
            JOptionPane.showMessageDialog(null, "Selecciona una fila");//Mostrar en pantalla el mensaje
        } else {
            //Verificamos con un if si los campos estan vacios
            if (!"".equals(txtCodigoProducto.getText()) || !"".equals(txtDescripcionProducto.getText()) || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtPrecioProducto.getText())) {
                pro.setCodigo(txtCodigoProducto.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" convertido a entero
                pro.setNombre(txtDescripcionProducto.getText());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDniCliente" 
                pro.setProvedor(cbxProvedor.getSelectedItem().toString());//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtTelefonoCliente" convertido a entero
                pro.setStock(Integer.parseInt(txtCantidadProducto.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtDireccionCliente "
                pro.setPrecio(Double.parseDouble(txtPrecioProducto.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtRazonCliente "
                pro.setId(Integer.parseInt(txtIdPro.getText()));//Mandamos a la clase Cliente mediante el objeto cl a lo obtenido en "txtIdCliente" convertido a entero
                ProDao.ModificarProducto(pro);//Enviamos los parametros al metodo de modificar
                LimpiarTable();
                ListarProducto();
                LimpiarProducto();
                JOptionPane.showMessageDialog(null, "Producto modificado");//Mostrar en pantalla el mensaje
            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");//Mostrar en pantalla el mensaje

            }

        }

    }//GEN-LAST:event_btnEditarProductosActionPerformed

    private void btnExcelProvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelProvedorActionPerformed
        // TODO add your handling code here:
        Excel.reporte();
    }//GEN-LAST:event_btnExcelProvedorActionPerformed

    private void txtCodigoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyPressed
        // TODO add your handling code here:
        //evt.getKeyCode()==KeyEvent."Tecla a presionar"
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {//Creamos un if para verificar que el usuario presiono la tecla enter y no otra tecla
            String cod = txtCodigoVenta.getText();//Obtenemos lo que esta en el cuadro de texto y lo guardamos en una variabel
            pro = ProDao.BuscarProducto(cod);//Otra forma de crear un contructor para la Clase ProDao
            //Creamos otro if para verificar que el campo no este vacio
            if (!"".equals(txtCodigoVenta.getText())) {
                //Creamos un if para saber si existe un producto o no
                if (pro.getNombre() != null) {//Si no se obtiene algo diferente de null entonces
                    txtDescripcionVenta.setText("" + pro.getNombre());//Mostramos en el cuadro de texto lo que esta en la clase producto
                    txtPrecioVenta.setText("" + pro.getPrecio());
                    txtStockDisponibleVenta.setText("" + pro.getStock());
                    //requestFocus()sirve para mandar el cursor a donde quieras
                    txtCantidadVenta.requestFocus();//Despues de mostrarlo, el cursor lo mandaremos al cuadro de texto
                } else {//Si no existe el producto
                    //Mostramos  los cuadro de texto vacios
                    LimpiarVenta();
                    txtCodigoVenta.requestFocus();// el cursor lo mandaremos al cuadro de texto

                }

            } else {//Si esta vacio el cuadro de texto "txtCodigoVenta"
                JOptionPane.showMessageDialog(null, "Ingrese el codigo del producto");//Mostrar en pantalla el mensaje
                txtCodigoVenta.requestFocus();// el cursor lo mandaremos al cuadro de texto
            }

        }
    }//GEN-LAST:event_txtCodigoVentaKeyPressed

    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {//Creamos un if para verificar que el usuario presiono la tecla enter y no otra tecla
            if (!"".equals(txtCodigoVenta.getText())) {//Creamos if par verificar que el cuadro de texto no este vacio 
                //Creamos la variables para guardar los datos
                String codigo = txtCodigoVenta.getText();
                String descripcion = txtDescripcionVenta.getText();
                int cantidad = Integer.parseInt(txtCantidadVenta.getText());
                int stock = Integer.parseInt(txtStockDisponibleVenta.getText());
                double precio = Double.parseDouble(txtPrecioVenta.getText());
                double toatl = cantidad * precio;//Creamos la variable para guardar el totoal
                //Creamos un if para comprovar que la cantidad no sea mayor al stock
                if (stock >= cantidad) {
                    item = item + 1;
                    tmp = (DefaultTableModel) tableVenta.getModel();//Creamos temporal modelo de tipo tabla para que se guarde los datos de la tabla cuando se cambie de pestaña
                    //tableVenta.getRowCount()sirve para contar cuantas filas tiene la tabla
                    for (int i = 0; i < tableVenta.getRowCount(); i++) {//Creamos un for para rrecorra toda la tabla
                        if (tableVenta.getValueAt(i, 1).equals(txtDescripcionVenta.getText())) {//Creamos un if para validar que no se repitan los productos en la tabla
                            JOptionPane.showMessageDialog(null, "El producto ya esta registrado");//Mostrar en pantalla el mensaje                  
                            return;//Mnadamos un return para que el programa continue 
                        }

                    }

                    ArrayList lista = new ArrayList(); //Declaramos un lista
                    //Agregamos a la lista todo el producto
                    lista.add(item);
                    lista.add(codigo);
                    lista.add(descripcion);
                    lista.add(cantidad);
                    lista.add(precio);
                    lista.add(toatl);
                    //Creamos un arreglo llamado objet para agregarlo a ala tabla
                    Object[] O = new Object[5];
                    O[0] = lista.get(1);
                    O[1] = lista.get(2);
                    O[2] = lista.get(3);
                    O[3] = lista.get(4);
                    O[4] = lista.get(5);
                    tmp.addRow(O);//Agregamos el objeto al modelo
                    tableVenta.setModel(tmp);//Agreagamos el modelo a la tabla
                    TotalPagar();
                    LimpiarVenta();
                    txtCodigoVenta.requestFocus();// el cursor lo mandaremos al cuadro de texto
                } else {//Si la cantidad sobrepasa el stock
                    JOptionPane.showMessageDialog(null, "Stock no disponible");//Mostrar en pantalla el mensaje
                }
            } else {//Si la cantidad esta vacia
                JOptionPane.showMessageDialog(null, "Ingrese la cantidad");//Mostrar en pantalla el mensaje
            }

        }
    }//GEN-LAST:event_txtCantidadVentaKeyPressed
    //Eliminar el  producto de la tabla de nueva venta por si el usuario se equivoco
    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        // TODO add your handling code here:
        modelo = (DefaultTableModel) tableVenta.getModel();//Creamos el objeto modelo de la tabla de nueva venta
        modelo.removeRow(tableVenta.getSelectedRow());//le quitamos la fila que el usuario selecciono   a la  tabla nueva venta para eliminarla 
        TotalPagar();//Mandamos a traer el metodo para actualizar el total a pagar
        txtCodigoVenta.requestFocus();//devolvemos el maus  al inicio 
    }//GEN-LAST:event_btnEliminarVentaActionPerformed
    //Para mostrar los datos del cliente 
    private void txtRucVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucVentaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {//Creamos un if para verificar que el usuario presiono la tecla enter y no otra tecla
            if (!"".equals(txtRucVenta.getText())) {//Creamos if par verificar que el cuadro de texto no este vacio 
                int dni = Integer.parseInt(txtRucVenta.getText());//Guardamos el dni
                cl = client.BuscarCliente(dni);//Mandamos el dni al metodo de buscar cliente
                if (cl.getNombre() != null) {//Creamos un if para validar si hay algun resultado
                    //Si hay resultado, mostrara los datos obtenidos en los cuadros de texto
                    txtNombreClienteVenta.setText("" + cl.getNombre());
                    txtDireccionClienteVenta2.setText("" + cl.getDireccion());
                    txtTelefonoClienteVenta.setText("" + cl.getTelefono());
                    txtRazonClienteVenta1.setText("" + cl.getRazon());
                } else {//Si no encontro resultados del dni del cliente
                    txtRucVenta.setText("");
                    JOptionPane.showMessageDialog(null, "El cliente no existe");//Mostrar en pantalla el mensaje

                }
            }

        }

    }//GEN-LAST:event_txtRucVentaKeyPressed

    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        // TODO add your handling code here:
        //Creamos un if para validar que la tabla no este vacia y otro para que cliente no este vacio
        if (tableVenta.getRowCount() > 0) {//Para condicionar el numero de filas
            if (!"".equals(txtNombreClienteVenta.getText())) {//Compararamos si esta vacio el cuadro de texto del nombre del cliente
                RegistrarVenta();
                RegistrarDetalle();
                ActualizarStock();
                pdf();
                LimpiarTableVenta();
                LimpiarClienteVenta();
            } else {
                JOptionPane.showMessageDialog(null,"Debes busar el nombre de un cliente");//Mostrar en pantalla un mensaje             
            }

        }else{
            JOptionPane.showMessageDialog(null,"No hay productos en la venta ");//Mostrar en pantalla un mensaje con el error de MySQL
             
        }

    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    private void txtCodigoVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyTyped
        // TODO add your handling code here:
        event.numberKeyPress(evt);//metodo para validar solo numeros
    }//GEN-LAST:event_txtCodigoVentaKeyTyped

    private void txtDescripcionVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionVentaKeyTyped
        // TODO add your handling code here:
        event.textKeyPress(evt);//metodo para validar solo letras
    }//GEN-LAST:event_txtDescripcionVentaKeyTyped

    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyTyped
        // TODO add your handling code here:
         event.numberKeyPress(evt);//metodo para validar solo numeros
    }//GEN-LAST:event_txtCantidadVentaKeyTyped

    private void txtPrecioProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProductoKeyTyped
        // TODO add your handling code here:
        event.numberDecimalKeyPress(evt, txtPrecioProducto);//metodo para validar solo numeros decimales
    }//GEN-LAST:event_txtPrecioProductoKeyTyped

    private void btnActualizarConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarConfigActionPerformed
        // TODO add your handling code here:
        //Verificamos con un if si los campos estan vacios
            if (!"".equals(txtRucConfig.getText()) || !"".equals(txtNombreConfig.getText()) || !"".equals(txtTelefonoConfig.getText()) || !"".equals(txtDireccionConfig.getText()) || !"".equals(txtRazonConfig.getText())) {
                conf.setId(Integer.parseInt(txtIdConfig.getText()));//Mandamos a la clase Conf mediante el objeto conf a lo obtenido en "txtIdConfig" convertido a entero
                conf.setRuc(Integer.parseInt(txtRucConfig.getText()));//Mandamos a la clase Conf mediante el objeto conf a lo obtenido en "txtRucConfig" convertido a entero
                conf.setNombre(txtNombreConfig.getText());//Mandamos a la clase Conf mediante el objeto conf a lo obtenido en "txtNombreConfig" 
                conf.setTelefono(Integer.parseInt(txtTelefonoConfig.getText()));//Mandamos a la clase Conf mediante el objeto conf a lo obtenido en "txtTelefonoConfig" convertido a entero
                conf.setDireccion(txtDireccionConfig.getText());//Mandamos a la clase Conf mediante el objeto conf a lo obtenido en "txtDireccionConfig "
                conf.setRazon(txtRazonConfig.getText());//Mandamos a la clase Conf mediante el objeto conf a lo obtenido en "txtRazonConfig "
                ProDao.ModificarDatos(conf);//Manadamos el objeto "conf"al otro objeto "ProDao" con el metodo de modificar los datos de la empresa conf
                JOptionPane.showMessageDialog(null, "Datos de la empresa actualizado");//Mostrar en pantalla el mensaje
                ListarConfig();

            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");//Mostrar en pantalla el mensaje

            }
    }//GEN-LAST:event_btnActualizarConfigActionPerformed

    // Sirve para mostrar lo que este en la tabla en los cuadros de texto cuando se selecionen con un clic del mouse 
    private void TableVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasMouseClicked
        // TODO add your handling code here:
        ////"nombre de la tabla".rowAtPoint(evt.getPoint())
        int fila = TableVentas.rowAtPoint(evt.getPoint());//TableCliente.rowAtPoint(evt.getPoint()) Sirve oara poder obtener el numero de filas de una tabla
        //"nombre de la tabla".getValueAt("nombre variable con la cantidad de filas","siempre incia en 0").to"tipo de variable a pasar"() Sirve para obtener el valor de una fila en su respectiva pocision con la columna que indiquemos 
        txtIdVenta.setText(TableVentas.getValueAt(fila, 0).toString());//Mostrar el id de la venta en un cuadro de texto
        
    }//GEN-LAST:event_TableVentasMouseClicked

    private void btnPdfVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfVentasActionPerformed
        // TODO add your handling code here:
        //NOTA: SI TENEMOS PROBLEMAS AL NO ENCONTRAR LAS PALABRAS QUE VAMOS A UTILIZAR, ENTONCES SELECCIONAMOS LA PALBRA, Y NOS VAMOS DONDE SE IMPORTAN LAS LIBRERIAS, Y LA ELIMINAMOS. POSTERIORMENTE REGRESEAMOS E IMPORTAMOS LA LIBRERIA CORRECTA
        try {//Intentar hacer lo siguiente  a crear la ruta de salida
            int id = Integer.parseInt(txtIdVenta.getText());//Creamos una varibale para guardar el id de la venta
            // File "nombre del objeto tipo archivo" = new File("url del archivo junto con su nombre ");
            File file = new File("src/pdf/venta" + id + ".pdf");//Creamos un objeto para guardar la ruta del archivo pdf que se va a crear de nombre "venta.pdf"
            //Desktop.getDesktop().open("nombre del objeto tipo archivo donde esta el URL");
            Desktop.getDesktop().open(file);//Llamamos al metodo para que se abra el pdf cada que se genere una venta
        } catch (IOException ex) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara de tipo documentacion y IOException)
            System.out.println(ex.toString());;//Mostrar en pantalla un mensaje con el error del pdf

        }
    }//GEN-LAST:event_btnPdfVentasActionPerformed

    private void btnGraficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficarActionPerformed
        // TODO add your handling code here:
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(MyDate.getDate());
        Grafico.Graficar(fechaReporte); //llamamos la clase grafico con su metodo graficar y mandamos como parametro la variable FechaActual
    }//GEN-LAST:event_btnGraficarActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        Registro reg = new Registro();//mandamos a llamar la clase Registro
        reg.setVisible(true);//hacemos visible el formulario
    }//GEN-LAST:event_btnRegistrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonClientes;
    private javax.swing.JButton BotonConfiguracion;
    private javax.swing.JButton BotonNuevaVenta;
    private javax.swing.JButton BotonProductos;
    private javax.swing.JButton BotonProvedor;
    private javax.swing.JButton BotonVentas;
    private javax.swing.JLabel LabelTotal;
    private javax.swing.JLabel LabelVendedor;
    private com.toedter.calendar.JDateChooser MyDate;
    private javax.swing.JTable TableCliente;
    private javax.swing.JTable TableProducto;
    private javax.swing.JTable TableProvedor;
    private javax.swing.JTable TableVentas;
    private javax.swing.JButton btnActualizarConfig;
    private javax.swing.JButton btnEditarCliente;
    private javax.swing.JButton btnEditarProductos;
    private javax.swing.JButton btnEditarProvedor;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProductos;
    private javax.swing.JButton btnEliminarProvedor;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JButton btnExcelProvedor;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGraficar;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnGuardarProductos;
    private javax.swing.JButton btnGuardarProvedor;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnNuevoProductos;
    private javax.swing.JButton btnNuevoProvedor;
    private javax.swing.JButton btnPdfVentas;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cbxProvedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tableVenta;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCodigoVenta;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDescripcionVenta;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtDireccionClienteVenta2;
    private javax.swing.JTextField txtDireccionConfig;
    private javax.swing.JTextField txtDireccionProvedor;
    private javax.swing.JTextField txtDniCliente;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdConfig;
    private javax.swing.JTextField txtIdPro;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdProvedor;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtIdVenta1;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreConfig;
    private javax.swing.JTextField txtNombreProvedor;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtRazonCliente;
    private javax.swing.JTextField txtRazonClienteVenta1;
    private javax.swing.JTextField txtRazonConfig;
    private javax.swing.JTextField txtRazonProvedor;
    private javax.swing.JTextField txtRucConfig;
    private javax.swing.JTextField txtRucProvedor;
    private javax.swing.JTextField txtRucVenta;
    private javax.swing.JTextField txtStockDisponibleVenta;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTelefonoClienteVenta;
    private javax.swing.JTextField txtTelefonoConfig;
    private javax.swing.JTextField txtTelefonoProvedor;
    // End of variables declaration//GEN-END:variables
    //Creamos un metodo privado para limpiar los cuadros de texto de clientes
    private void LimpiarCliente() {
        txtIdCliente.setText("");
        txtDniCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
        txtDireccionCliente.setText("");
        txtRazonCliente.setText("");

    }
    //Metodo para limpiar los cuadros de texto del boton provedor
    private void LimpiarProvedor() {
        txtIdProvedor.setText("");
        txtRucProvedor.setText("");
        txtNombreProvedor.setText("");
        txtTelefonoProvedor.setText("");
        txtDireccionProvedor.setText("");
        txtRazonProvedor.setText("");

    }
    //Metodo para limpiar los cuadros de texto del boton producto
    private void LimpiarProducto() {
        txtIdPro.setText("");
        txtCodigoProducto.setText("");
        cbxProvedor.setSelectedItem(null);
        txtDescripcionProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");

    }

    //Metodo para actualizar el total a pagar cada que se agregue un producto a la tabla venta  
    private void TotalPagar() {
        Totalpagar = 0.000;//Verificamos nuevamente que la variable este en 0
        int numfila = tableVenta.getRowCount();//Obtenemos el numero de filas de la tablaVentas       
        for (int i = 0; i < numfila; i++) {//Creamos un for para recorrer las filas 
            double cal = Double.parseDouble(String.valueOf(tableVenta.getModel().getValueAt(i, 4)));//guardamos en una variable, el valor obtenido de la tablaVenta en la fila i, columna 4 y lo pasamos a String y despues a double
            Totalpagar = Totalpagar + cal;//Ir sumando lo que esta en la fila(i,4)    
        }
        //"nombre del label".setText(String.format("%,2f", "nombre de variable a mostrar"));
        LabelTotal.setText(String.format("%.2f", Totalpagar));//para mostrar el total a pagar en el label en formato de dos decimales
    }

    //Metodo para limpar el   
    private void LimpiarVenta() {
        txtDescripcionVenta.setText("");//Mostramos en el cuadro de texto lo que esta en la clase producto
        txtPrecioVenta.setText("");
        txtStockDisponibleVenta.setText("");
        txtCodigoVenta.setText("");// el cursor lo mandaremos al cuadro de texto
        txtCantidadVenta.setText("");
        txtIdProducto.setText("");

    }

    //Creamos un metodo para registrar la venta
    private void RegistrarVenta() {
        String cliente = txtNombreClienteVenta.getText();//Guardamos en la valriable el valor obtenido en el cuadro de texto
        String vendedor = LabelVendedor.getText();//Guardamos en la valriable el valor obtenido en el label 
        double monto = Totalpagar;//Guardamos en una variable el totla a pagar
        v.setCliente(cliente);//Mandamos la variable a la clase cliente
        v.setVendedor(vendedor);//Mandamos la variable a la clase cliente
        v.setTotal(monto);//Mandamos la variable a la clase cliente
        v.setFecha(FechaActual);//Mandamos la variable de una variable con una fecha
        Vdao.RegistrarVenta(v);//Mandamos v a Vdao para iniciar el registro

    }

    //Creamos el metodo de registrar el detalle, primero instanciamos la clase Detalle y luego usamos DetalleDao
    private void RegistrarDetalle() {  //Para crear el metodo, necesitamos los valores de toda la tablaVenta
        int id = Vdao.IdVenta();//Consultamos la id mas alta en la tabla "Ventas" de la BD "sistemaventa" y la guardamos en una variable
        //"nombre de la tabla".getRowCount() sirve para contar cuantas filas tiene una tabla
        for (int i = 0; i < tableVenta.getRowCount(); i++) {//Creamos un for para ir recorriendo la tabla e ir capturando los datos
            String cod = tableVenta.getValueAt(i, 0).toString();//Guardamos en una variable lo que halla capturado en la tabla en la pocicion i,codigo
            int cantidad = Integer.parseInt(tableVenta.getValueAt(i, 2).toString());//Guardamos en una variable lo que halla capturado en la tabla en la pocicion i,cantidad
            double precio = Double.parseDouble(tableVenta.getValueAt(i, 3).toString());//Guardamos en una variable lo que halla capturado en la tabla en la pocicion i,precio            
            Dv.setCodigo_Producto(cod);//Mandamos lo obtenido a la clase detalle
            Dv.setCantidad(cantidad);//Mandamos lo obtenido a la clase detalle
            Dv.setPrecio(precio);//Mandamos lo obtenido a la clase detalle
            Dv.setId_venta(id);//Mandamos lo obtenido a la clase detalle
            Vdao.RegistrarDetalle(Dv);//llamamos el objeto de VentaDao donde tenemos el metodo de registrar detalle y mandamos el objeto Dv(Destalleventa)

        }
    }

    //Creamos el metodo para actualizar el Stock
    private void ActualizarStock() {
        //Creamos un for para que valla obtieniendo la cantidad y los codigos de los productos en la tabla venta y los valla actualizando
        for (int i = 0; i < tableVenta.getRowCount(); i++) {//Creamos un for para ir recorriendo la tabla e ir capturando los datos
            String cod = tableVenta.getValueAt(i, 0).toString();//Guardamos en una variable lo que halla capturado en la tabla en la pocicion i,codigo
            int cantidad = Integer.parseInt(tableVenta.getValueAt(i, 2).toString());//Guardamos en una variable lo que halla capturado en la tabla en la pocicion i,cantidad
            pro = ProDao.BuscarProducto(cod);//Lamamos al metodo de buscar codigo que esta en el objeto pro
            //Realizamos la opreacion para actualizar el stock
            int StockActual = pro.getStock() - cantidad;//Creamos una variable para guardar el stock actualizado
            Vdao.ActualizarStock(StockActual, cod);//Mandamos a llamar el metodo de actualizar stock de la clase VentasDao
        }
    }

    //Creamos un metodo para limpar la tablaVenta
    public void LimpiarTableVenta() {
        tmp = (DefaultTableModel) tableVenta.getModel();//Declaramos tmp como tabla
        int fila = tableVenta.getRowCount();//Obtenemos el nuemro de filas de la tablaVenta
        for (int i = 0; i < fila; i++) {//tamaño de la tabla del paquete modelo
            tmp.removeRow(0);//Este comando sirve para eliminar filas en las tablas del tmp
        }
    }

    //Creamos un metodo para limpiar los cuadros de texto de los datos del cliente para el boton nueva venta
    public void LimpiarClienteVenta() {
        txtRucVenta.setText("");
        txtNombreClienteVenta.setText("");
        txtDireccionClienteVenta2.setText("");
        txtTelefonoClienteVenta.setText("");
        txtRazonClienteVenta1.setText("");
    }

    //Creamos un metodo para generar los reportes en pdf, utilizaremos la libreria (itextpdf5.5.1.jar)
    public void pdf() {
        //NOTA: SI TENEMOS PROBLEMAS AL NO ENCONTRAR LAS PALABRAS QUE VAMOS A UTILIZAR, ENTONCES SELECCIONAMOS LA PALBRA, Y NOS VAMOS DONDE SE IMPORTAN LAS LIBRERIAS, Y LA ELIMINAMOS. POSTERIORMENTE REGRESEAMOS E IMPORTAMOS LA LIBRERIA CORRECTA
        try {//Intentar hacer lo siguiente  a crear la ruta de salida
            int id = Vdao.IdVenta();//Creamos una varibale para guardar el id de la venta
            //FileOutputStream "nombre del archivo";
            FileOutputStream archivo;// Creamos un nuevo archivo
            // File "nombre del objeto tipo archivo" = new File("url del archivo junto con su nombre ");
            File file = new File("src/pdf/venta" + id + ".pdf");//Creamos un objeto para guardar la ruta del archivo pdf que se va a crear de nombre "venta.pdf"
            archivo = new FileOutputStream(file);//Utilizamos archivo y le mandamos la rtura 
            //Document "nombre del documento" = Document();
            Document doc = new Document();//Creamos un documento llamado doc
            PdfWriter.getInstance(doc, archivo);
            doc.open();//abrimos el documento
            //A partir de aqui podemos realizar lo que queramos en el pdf
            //Image "nombre objeto tipo imagen" =Image.getInstance("url de la imagen a tomar");
            Image img = Image.getInstance("src/Imagenes/logo_pdf.png");//Insertamos una imagen en el pdf
            Paragraph fecha = new Paragraph();//Creamos un objeto "fecha" de tipo parrafo
            //Font "nombre objeto tipo font"= new Font(Font.FontFamily."tipo de letra","tamaño letra", Font."tipo letra", BaseColor."color de la base");
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);//Definimos el tipo de fuente a utilizar (tipo letra)
            fecha.add(Chunk.NEWLINE);//Sirve para agregar una nueva linea en el pdf
            //Obtenemos la fecha actual del sistema
            Date date = new Date(); //Llamamos el metodo para obtener la fecha
            //"nombre objeto tipo parrafo".add("Fecha: "+ new SimpleDateFormat("Formato  fecha").format("nombre varibale con la fecha")+"\n\n");
            fecha.add("Factura:" + id + "\n" + "Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(date) + "\n\n");//agregamos datos en una linea del pdf como la fecha, la fecha en su respectivo formato y tambien el numero de factura
            //Crearemos nuestra tabla
            //PdfPTable "nombre Objeto Tipo TablaPDF" = new PdfPTable("numero de columnas");
            PdfPTable Encabezado = new PdfPTable(4);//creamos el objeto "Encabezado" de tipo una tabla en el pdf de 4 columnas
            //"nombre Objeto Tipo TablaPDF".setWidthPercentage("tamaño del encabezado");
            Encabezado.setWidthPercentage(100);//Especificamos el tamaño del encabezado
            Encabezado.getDefaultCell().setBorder(0);//Quitamos el borde de la tabla es decir a "0"
            //Crearemos el tamaño de cada una de las celdas
            float[] ComlumnasEncabezado = new float[]{20f, 30f, 70f, 40f};//Creamos un arreglo para almacenar el tamaño de las celdas.NOTA:con f indico float
            //"nombre Objeto Tipo TablaPDF".setWidths("nombre agreglo con tamaño de celdas");
            Encabezado.setWidths(ComlumnasEncabezado);//pasamos los tamaños al encabezado
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);//Especificamos la pocision
            //vamos a empezar a escribir en cada una de las celdas
            Encabezado.addCell(img);//agregamos a la primer celda la imagen que anteriormente guardamos 1ER COLUMNA
            //Agregamos los datos de la empresa
            //creamos  variables para gusrdar los datos que se obtienen de la empresa en config
            String ruc = txtRucConfig.getText();
            String nombre = txtNombreConfig.getText();
            String telefono = txtTelefonoConfig.getText();
            String direccion = txtDireccionConfig.getText();
            String razon = txtRazonConfig.getText();
            Encabezado.addCell("");//para mantener centrados los datos de la empresa creamos una celda vacia 2DA COLUMNA
            Encabezado.addCell("Ruc: " + ruc + "\nNombre: " + nombre + "\nTelefono: " + telefono + "\nDireccion: " + direccion + "\nRazon: " + razon);//Agregamos los datos de la empresa  3ERA COLUMNA
            Encabezado.addCell(fecha);//agregamos la fecha  4TA COLUMNA
            //"nombre del documento".add("nombre Objeto Tipo TablaPDF");
            doc.add(Encabezado);//Agregamos las celdas al documento
            Paragraph cli = new Paragraph();//Creamos un objeto "cli" de tipo parrafo
            cli.add(Chunk.NEWLINE);//Agregamos una nueva linea a "cli"
            cli.add("Datos de los clientes\n\n");//Agregamos el titulo del la linea
            doc.add(cli);//Agregamos el objeto "cli" de tipo parrafo al documento 
            //Creamos una tabla para obtener los datos del cliente
            PdfPTable Tablacli = new PdfPTable(4);//creamos el objeto "Tablacli" de tipo una tabla en el pdf de 4 columnas
            //"nombre Objeto Tipo TablaPDF".setWidthPercentage("tamaño del encabezado");
            Tablacli.setWidthPercentage(100);//Especificamos el tamaño del encabezado
            Tablacli.getDefaultCell().setBorder(0);//Quitamos el borde de la tabla es decir a "0"
            //Crearemos el tamaño de cada una de las celdas
            float[] Comlumnascli = new float[]{20f, 50f, 30f, 40f};//Creamos un arreglo para almacenar el tamaño de las celdas.NOTA:con f indico float
            //"nombre Objeto Tipo TablaPDF".setWidths("nombre agreglo con tamaño de celdas");
            Tablacli.setWidths(Comlumnascli);//pasamos los tamaños al Tablacli
            Tablacli.setHorizontalAlignment(Element.ALIGN_LEFT);//Especificamos la pocision
            //Vamos a agregar los titulos a  celdas para la Tablacli
            //PdfPCell "Nombre objeto tipo celda" = new PdfPCell(new Phrase("lo que muestre la celda"));//
            PdfPCell cl1 = new PdfPCell(new Phrase("Dni/Ruc", negrita));//Mostramos en la celda "cl1" el dni
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre", negrita));//Mostramos en la celda "cll" el dni
            PdfPCell cl3 = new PdfPCell(new Phrase("Telefono", negrita));//Mostramos en la celda "cll" el dni
            PdfPCell cl4 = new PdfPCell(new Phrase("Direccion", negrita));//Mostramos en la celda "cll" el dni
            //Quitamos los bordes a cada una de las celdas
            //"Nombre objeto tipo celda".setBorder("tamaño del borde");
            cl1.setBorder(0);
            cl2.setBorder(0);
            cl3.setBorder(0);
            cl4.setBorder(0);
            //Agregamos todas las celdas a la tabla
            Tablacli.addCell(cl1);//Agregamos la celda 1 a Tablacli
            Tablacli.addCell(cl2);//Agregamos la celda 2 a Tablacli
            Tablacli.addCell(cl3);//Agregamos la celda 3 a Tablacli
            Tablacli.addCell(cl4);//Agregamos la celda 4 a Tablacli
            //Agregamos los valores a las celdas
            Tablacli.addCell(txtRucVenta.getText());//Agregamos lo que obtenga en el cuadro de texto
            Tablacli.addCell(txtNombreClienteVenta.getText());//Agregamos lo que obtenga en el cuadro de texto
            Tablacli.addCell(txtTelefonoClienteVenta.getText());//Agregamos lo que obtenga en el cuadro de texto
            Tablacli.addCell(txtDireccionClienteVenta2.getText());//Agregamos lo que obtenga en el cuadro de texto
            doc.add(Tablacli);//Agregamos la tabla al documento
            //Ahora agreagaremos los productos al pdf
            //Creamos una tabla para obtener los datos de los productos
            PdfPTable Tablapro = new PdfPTable(4);//creamos el objeto "Tablapro" de tipo una tabla en el pdf de 4 columnas
            //"nombre Objeto Tipo TablaPDF".setWidthPercentage("tamaño del encabezado");
            Tablapro.setWidthPercentage(100);//Especificamos el tamaño del encabezado
            Tablapro.getDefaultCell().setBorder(0);//Quitamos el borde de la tabla es decir a "0"
            //Crearemos el tamaño de cada una de las celdas
            float[] Comlumnaspro = new float[]{10f, 50f, 15f, 20f};//Creamos un arreglo para almacenar el tamaño de las celdas.NOTA:con f indico float
            //"nombre Objeto Tipo TablaPDF".setWidths("nombre agreglo con tamaño de celdas");
            Tablapro.setWidths(Comlumnaspro);//pasamos los tamaños al Tablacli
            Tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);//Especificamos la pocision
            //Vamos a agregar los titulos a  celdas para la Tablapro
            //PdfPCell "Nombre objeto tipo celda" = new PdfPCell(new Phrase("lo que muestre la celda"));//
            PdfPCell pro1 = new PdfPCell(new Phrase("Cantidad", negrita));//Mostramos en la celda "pro1" el Cantidad
            PdfPCell pro2 = new PdfPCell(new Phrase("Descripcion", negrita));//Mostramos en la celda "pro2" el Descripcion
            PdfPCell pro3 = new PdfPCell(new Phrase("Precio", negrita));//Mostramos en la celda "pro3" el Precio
            PdfPCell pro4 = new PdfPCell(new Phrase("Precio Total", negrita));//Mostramos en la celda "pro4" el Precio Total
            //Quitamos los bordes a cada una de las celdas
            //"Nombre objeto tipo celda".setBorder("tamaño del borde");
            pro1.setBorder(0);
            pro2.setBorder(0);
            pro3.setBorder(0);
            pro4.setBorder(0);
            pro1.setBackgroundColor(BaseColor.DARK_GRAY);//Agregamos color de forndo a la celda pro1
            pro2.setBackgroundColor(BaseColor.DARK_GRAY);//Agregamos color de forndo a la celda pro2
            pro3.setBackgroundColor(BaseColor.DARK_GRAY);//Agregamos color de forndo a la celda pro3
            pro4.setBackgroundColor(BaseColor.DARK_GRAY);//Agregamos color de forndo a la celda pro4
            //Agregamos todas las celdas a la tabla
            Tablapro.addCell(pro1);//Agregamos la celda 1 a Tablapro
            Tablapro.addCell(pro2);//Agregamos la celda 2 a Tablapro
            Tablapro.addCell(pro3);//Agregamos la celda 3 a Tablapro
            Tablapro.addCell(pro4);//Agregamos la celda 4 a Tablapro
            //Creamos un for para ir recorreiendo la tabla e ir obteniendo los valores.NOTA: TODA FILA DE LA TABLA INICIA EN LA POCICION 0 Y LA COLUMNA EN 1
            for (int i = 0; i < tableVenta.getRowCount(); i++) {
                String producto = tableVenta.getValueAt(i, 1).toString(); //Creamos una variable para guardar lo que obtenga de la fila i y columna Cantidad pasandolo a string
                String cantidad = tableVenta.getValueAt(i, 2).toString(); //Creamos una variable para guardar lo que obtenga de la fila i y columna Cantidad pasandolo a string
                String precio = tableVenta.getValueAt(i, 3).toString(); //Creamos una variable para guardar lo que obtenga de la fila i y columna Cantidad pasandolo a string
                String total = tableVenta.getValueAt(i, 4).toString(); //Creamos una variable para guardar lo que obtenga de la fila i y columna Cantidad pasandolo a string
                //Agregamos los valores a las celdas del pdf
                Tablapro.addCell(cantidad);//Agregamos lo que obtenga 
                Tablapro.addCell(producto);//Agregamos lo que obtenga 
                Tablapro.addCell(precio);//Agregamos lo que obtenga 
                Tablapro.addCell(total);//Agregamos lo que obtenga 
            }
            doc.add(Tablapro);//Agregamos la tabla al documento
            Paragraph info = new Paragraph();//Creamos un objeto "info" de tipo parrafo
            info.add(Chunk.NEWLINE);//agregamos el parrafo en una nueva linea
            info.add("Total a pagar: " + Totalpagar);//Agregamos un titulo en el objeto "info" de tipo parrafo
            //"Nombre objeto tipo parrafo".setAlignment(Element."posicion que se desa");
            info.setAlignment(Element.ALIGN_RIGHT);//posicionamos hacia la derecha el objeto "info" de tipo parrafo
            doc.add(info);//agregamos el objeto "info" de tipo parrafo al documento 
            //Creamos otro parrafo para la firma
            Paragraph firma = new Paragraph();//Creamos un objeto "firma" de tipo parrafo
            firma.add(Chunk.NEWLINE);//agregamos el parrafo en una nueva linea
            firma.add("Cancelacion y firma\n\n");//Agregamos un titulo en el objeto "info" de tipo parrafo
            firma.add("________________");//Creamos guiones para que el cliente firme
            //"Nombre objeto tipo parrafo".setAlignment(Element."posicion que se desa");
            firma.setAlignment(Element.ALIGN_CENTER);//posicionamos hacia la derecha el objeto "info" de tipo parrafo
            doc.add(firma);//agregamos el objeto "info" de tipo parrafo al documento 
            //Creamos otro parrafo para darle un mesaje al cliente
            Paragraph mensaje = new Paragraph();//Creamos un objeto "mensaje" de tipo parrafo
            mensaje.add(Chunk.NEWLINE);//agregamos el parrafo en una nueva linea
            mensaje.add("Gracias por su preferencia");//Agregamos un titulo en el objeto "info" de tipo parrafo        
            //"Nombre objeto tipo parrafo".setAlignment(Element."posicion que se desa");
            mensaje.setAlignment(Element.ALIGN_CENTER);//posicionamos hacia la derecha el objeto "info" de tipo parrafo
            doc.add(mensaje);//agregamos el objeto "info" de tipo parrafo al documento 
            doc.close();//para cerrar el documento 
            archivo.close();//para cerrar el archivo
            //Desktop.getDesktop().open("nombre del objeto tipo archivo donde esta el URL");
            Desktop.getDesktop().open(file);//Llamamos al metodo para que se abra el pdf cada que se genere una venta
        } catch (DocumentException | IOException e) {//Capturar posibles errores del la ejecucion anterior (try)(errores que arrojara de tipo documentacion y IOException)
            System.out.println(e.toString());;//Mostrar en pantalla un mensaje con el error del pdf

        }
    }

}
