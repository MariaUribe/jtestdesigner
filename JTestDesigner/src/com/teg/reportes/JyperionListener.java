/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teg.reportes;

import com.lowagie.text.BadElementException;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Header;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.net.URL;

/**
 *
 * @author maya
 */
public class JyperionListener implements ITestListener {

    /**
     * Document
     */
    private Document document = null;
    /**
     * PdfPTables
     */
    PdfPTable successTable = null, failTable = null, skippedTable = null;
    /**
     * throwableMap
     */
    private HashMap<Integer, Throwable> throwableMap = null;
    /**
     * nbExceptions
     */
    private int nbExceptions = 0;
    /**
     * ruta del reporte PDF
     */
    private String rutaPDF;

    /**
     * JyperionListener
     */
    public JyperionListener() {
        log("JyperionListener()");

        this.document = new Document();
        this.throwableMap = new HashMap<Integer, Throwable>();
    }

    /**
     * @see com.beust.testng.ITestListener#onTestSuccess(com.beust.testng.ITestResult)
     */
    public void onTestSuccess(ITestResult result) {
        log("onTestSuccess(" + result + ")");

        if (successTable == null) {
            this.successTable = new PdfPTable(new float[]{.3f, .3f, .1f, .3f});
            Paragraph p = new Paragraph("PRUEBAS PASADAS", new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD));
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell(p);
            cell.setColspan(4);
            cell.setBackgroundColor(Color.GREEN);
            this.successTable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Clase"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.successTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Método"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.successTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Tiempo (ms)"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.successTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Excepción"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.successTable.addCell(cell);
        }

        PdfPCell cell = new PdfPCell(new Paragraph(result.getTestClass().toString()));
        this.successTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(result.getMethod().toString()));
        this.successTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
        this.successTable.addCell(cell);

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
            this.nbExceptions++;
            Paragraph excep = new Paragraph(
                    new Chunk(throwable.toString(),
                    new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.UNDERLINE)).setLocalGoto("" + throwable.hashCode()));
            cell = new PdfPCell(excep);
            this.successTable.addCell(cell);
        } else {
            this.successTable.addCell(new PdfPCell(new Paragraph("")));
        }
    }

    /**
     * @see com.beust.testng.ITestListener#onTestFailure(com.beust.testng.ITestResult)
     */
    public void onTestFailure(ITestResult result) {
        log("onTestFailure(" + result + ")");

        if (this.failTable == null) {
            this.failTable = new PdfPTable(new float[]{.3f, .3f, .1f, .3f});
            this.failTable.setTotalWidth(20f);
            Paragraph p = new Paragraph("PRUEBAS FALLIDAS", new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD));
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell(p);
            cell.setColspan(4);
            cell.setBackgroundColor(Color.RED);
            this.failTable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Clase"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.failTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Método"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.failTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Tiempo (ms)"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.failTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Excepción"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.failTable.addCell(cell);
        }

        PdfPCell cell = new PdfPCell(new Paragraph(result.getTestClass().toString()));
        this.failTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(result.getMethod().toString()));
        this.failTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
        this.failTable.addCell(cell);

        String exception = result.getThrowable() == null ? "" : result.getThrowable().toString();
        cell = new PdfPCell(new Paragraph(exception));
        this.failTable.addCell(cell);

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
            this.nbExceptions++;
            Paragraph excep = new Paragraph(
                    new Chunk(throwable.toString(),
                    new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.UNDERLINE)).setLocalGoto("" + throwable.hashCode()));
            cell = new PdfPCell(excep);
            this.failTable.addCell(cell);
        } else {
            this.failTable.addCell(new PdfPCell(new Paragraph("")));
        }
    }

    /**
     * @see com.beust.testng.ITestListener#onTestSkipped(com.beust.testng.ITestResult)
     */
    public void onTestSkipped(ITestResult result) {
        log("onTestSkipped(" + result + ")");

        if (this.skippedTable == null) {
            this.skippedTable = new PdfPTable(new float[]{.3f, .3f, .1f, .3f});
            this.skippedTable.setTotalWidth(20f);
            Paragraph p = new Paragraph("PRUEBAS SALTADAS", new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD));
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell(p);
            cell.setColspan(4);
            cell.setBackgroundColor(Color.YELLOW);
            this.skippedTable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Clase"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.skippedTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Método"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.skippedTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Tiempo (ms)"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.skippedTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Excepción"));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            this.skippedTable.addCell(cell);
        }

        PdfPCell cell = new PdfPCell(new Paragraph(result.getTestClass().toString()));
        this.skippedTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(result.getMethod().toString()));
        this.skippedTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
        this.skippedTable.addCell(cell);

        String exception = result.getThrowable() == null ? "" : result.getThrowable().toString();
        cell = new PdfPCell(new Paragraph(exception));
        this.skippedTable.addCell(cell);

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
            this.nbExceptions++;
            Paragraph excep = new Paragraph(
                    new Chunk(throwable.toString(),
                    new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.UNDERLINE)).setLocalGoto("" + throwable.hashCode()));
            cell = new PdfPCell(excep);
            this.skippedTable.addCell(cell);
        } else {
            this.skippedTable.addCell(new PdfPCell(new Paragraph("")));
        }
    }

    /**
     * @see com.beust.testng.ITestListener#onStart(com.beust.testng.ITestContext)
     */
    public void onStart(ITestContext context) {

        log("onStart(" + "context" + ")");
        try {
            File file = new File(context.getOutputDirectory());
            rutaPDF = file.getParent() + System.getProperty("file.separator") + "pdf"
                    + System.getProperty("file.separator") + context.getName() + ".pdf";

            PdfWriter.getInstance(this.document,
                    new FileOutputStream(rutaPDF));
            this.setRutaPDF(rutaPDF);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.document.open();
        document.newPage();

        Paragraph p = new Paragraph("RESULTADOS",
                FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 255)));
        p.setAlignment(Element.ALIGN_CENTER);

        try {
            this.document.add(p);
            Paragraph fecha = new Paragraph("(" + new Date().toString() + ")");
            fecha.setAlignment(Element.ALIGN_CENTER);
            this.document.add(fecha);

            //com.lowagie.text.Image image = com.lowagie.text.Image.getInstance("/Users/maya/Desktop/java.jpg");
            //document.add(image);
        } catch (BadElementException ex) {
            Logger.getLogger(JyperionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException e1) {
            System.out.println(e1.getMessage());
        }
    }

    /**
     * @see com.beust.testng.ITestListener#onFinish(com.beust.testng.ITestContext)
     */
    public void onFinish(ITestContext context) {
        log("onFinish(" + context + ")");

        try {
            if (this.successTable != null) {
                log("Added success table");
                this.successTable.setSpacingBefore(15f);
                this.document.add(this.successTable);
                this.successTable.setSpacingAfter(15f);
            }

            if (this.skippedTable != null) {
                log("Added skipped table");
                this.skippedTable.setSpacingBefore(15f);
                this.document.add(this.skippedTable);
                this.skippedTable.setSpacingAfter(15f);
            }

            if (this.failTable != null) {
                log("Added fail table");
                this.failTable.setSpacingBefore(15f);
                this.document.add(this.failTable);
                this.failTable.setSpacingAfter(15f);
            }

        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }

        if ((this.failTable != null) || (this.skippedTable != null)) {
            Paragraph p = new Paragraph("SUMARIO DE EXCEPCIONES",
                    FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
            p.setAlignment(Element.ALIGN_CENTER);

            try {
                this.document.add(new Paragraph(" "));
                this.document.add(p);
            } catch (DocumentException e1) {
                System.out.println(e1.getMessage());
            }
        }

        Set<Integer> keys = this.throwableMap.keySet();

        assert keys.size() == this.nbExceptions;

        for (Integer key : keys) {
            Throwable throwable = this.throwableMap.get(key);

            Chunk chunk = new Chunk(throwable.toString(),
                    FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0)));
            chunk.setLocalDestination("" + key);
            Paragraph throwTitlePara = new Paragraph(chunk);
            try {
                this.document.add(throwTitlePara);
            } catch (DocumentException e3) {
                System.out.println(e3.getMessage());
            }

            StackTraceElement[] elems = throwable.getStackTrace();
            //String exception = "";
            for (StackTraceElement ste : elems) {
                Paragraph throwParagraph = new Paragraph(ste.toString());
                try {
                    this.document.add(throwParagraph);
                } catch (DocumentException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        this.document.close();
        this.showPDF();
    }

    public void showPDF() {
        String ruta = this.getRutaPDF();

        try {
            File path = new File(ruta);
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * log
     * @param o
     */
    public static void log(Object o) {
        //System.out.println("[JyperionListener] " + o);
    }

    public void onTestStart(ITestResult itr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult itr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the rutaPDF
     */
    public String getRutaPDF() {
        return rutaPDF;
    }

    /**
     * @param rutaPDF the rutaPDF to set
     */
    public void setRutaPDF(String rutaPDF) {
        this.rutaPDF = rutaPDF;
    }
}
