/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.reportes;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maya
 */
public class ReporterManager {

    public ReporterManager() {
    }

    public void HtmlToPDF(ArrayList<String> rutasHTML, String rutaPDF) {

        try {
            Document document = new Document();
            StyleSheet style = new StyleSheet();
            style.loadTagStyle("body", "leading", "16,0");

            PdfWriter.getInstance(document, new FileOutputStream("/Users/maya/HelloWorld.pdf"));

            document.open();
            document.add(new Paragraph(" "));

            for (String ruta : rutasHTML) {
                ArrayList p = HTMLWorker.parseToList(new FileReader(ruta), style);
                for (int k = 0; k < p.size(); ++k) {
                    document.add((Element) p.get(k));
                    document.add(new Paragraph(" "));
                }

                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (IOException ex) {
            Logger.getLogger(ReporterManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ReporterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
