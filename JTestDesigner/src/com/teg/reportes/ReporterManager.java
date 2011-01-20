/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teg.reportes;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer.CConvertException;

/**
 *
 * @author maya
 */
public class ReporterManager {

    private static CYaHPConverter converter = new CYaHPConverter();

    public ReporterManager() {
    }

    public void htmlToPdfConverter(ArrayList<String> rutasHTML, String pdfOutfile, String folderLocation) {

        FileOutputStream out = null;
        String contenido = "";
        for (String file : rutasHTML) {
            try {
                File miHtml = new File(file);
                contenido = contenido + "<br><br>"
                        + FileUtils.readFileToString(miHtml);
            } catch (IOException ex) {
                Logger.getLogger(ReporterManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            String fontPath = null;
            File fout = new File(pdfOutfile + System.getProperty("file.separator"));
            out = new FileOutputStream(fout);
            Map properties = new HashMap();
            List headerFooterList = new ArrayList();

            headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter("<table width=\"100%\"><tbody><tr><td align=\"left\">" + "Generated with YaHPConverter.</td><td align=\"right\">Page <pagenumber>/<" + "pagecount></td></tr></tbody></table>", IHtmlToPdfTransformer.CHeaderFooter.HEADER));
            headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter("<table width=\"100%\"><tbody><tr><td align=\"left\">" + "Generated with YaHPConverter.</td><td align=\"right\">Page <pagenumber>/<" + "pagecount></td></tr></tbody></table>", IHtmlToPdfTransformer.CHeaderFooter.FOOTER));

            properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS,
                    IHtmlToPdfTransformer.FLYINGSAUCER_PDF_RENDERER);

            if (fontPath != null) {
                properties.put(IHtmlToPdfTransformer.FOP_TTF_FONT_PATH, fontPath);
            }

            converter.convertToPdf(contenido,
                    IHtmlToPdfTransformer.A4P, headerFooterList,
                    "file://" + folderLocation + "/", out, properties);
            //System.out.println("mihtml \n" + contenido);

            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ReporterManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CConvertException ex) {
            Logger.getLogger(ReporterManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ReporterManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
