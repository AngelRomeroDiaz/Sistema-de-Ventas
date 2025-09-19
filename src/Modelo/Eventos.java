package Modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author: Angel Gabriel Mendez Diaz Fecha de creacion: 6 de enero del 2023
 * Fecha de modificacion: Nombre del programa:Clase que servira realizar las
 * validaciones de los textField del sistema
 *
 */
public class Eventos {
    //Creamos un metodo para validar solo caracteres
    public void textKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();// declaramos una variable y le asignamos un evento
        if ((car < 'a' || car > 'z') && (car < 'A' || car > 'Z') && (car != (char) KeyEvent.VK_BACK_SPACE) && (car != (char) KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }
    //Creamos un metodo solo para validar numeros
    public void numberKeyPress(KeyEvent evt) {
// declaramos una variable y le asignamos un evento
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }
   //Creamos un metodo solo para validar numeros decimales
    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
// declaramos una variable y le asignamos un evento
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        } else if ((car < '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }
}
