/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teg.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author maya
 */
public class SwingDialog {

    public SwingDialog() {
    }

    public void errorDialog(String mensaje, JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void exitoDialog(String mensaje, JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                mensaje,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public int advertenciaDialog(String mensaje, JFrame frame) {
        int n = JOptionPane.showConfirmDialog(
                frame,
                mensaje,
                "Advertencia",
                JOptionPane.YES_NO_OPTION);

        return n;
    }

    public String modificarNombreVarDialog(JFrame frame) {

        String response = JOptionPane.showInputDialog(null,
                "Ingrese el nuevo nombre de la variable",
                "Nombre",
                JOptionPane.QUESTION_MESSAGE);
        return response;

    }
}
