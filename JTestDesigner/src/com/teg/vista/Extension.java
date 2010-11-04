/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teg.vista;

import java.io.File;

/**
 *
 * @author maya
 */
public class Extension extends javax.swing.filechooser.FileFilter {

    /**
     * Metodo accept
     * @param file
     * @return
     */
    @Override
    public boolean accept(File file) {
        // Allow only directories, or files with ".txt" extension
        return file.isDirectory() || file.getAbsolutePath().endsWith(".jar");
    }

    @Override
    public String getDescription() {
        // This description will be displayed in the dialog,
        // hard-coded = ugly, should be done via I18N
        return "Jar files (*.jar)";
    }
}
