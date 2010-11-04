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
        return file.isDirectory() || file.getAbsolutePath().endsWith(".jar");
    }

    @Override
    public String getDescription() {
        return "Jar files (*.jar)";
    }
}
