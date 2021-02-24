/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.project.helper.CommanHelper;
import com.project.helper.Queries;
import com.project.helper.ResponseMaster;
import com.project.helper.ServiceHelper;
import com.project.model.JSONBookmarkModel;
import com.project.utility.OSValidator;
import com.project.utils.Utils;
import com.project.viewerdemo.ViewerDemo;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.jdic.browser.WebBrowser;

public class JavaHelper {

    public JavaHelper() {

    }

    public static boolean createPdf(String data, String filePath) {
        System.out.println(filePath);
        URL kruti_font = null;
        try {
            kruti_font = new File(Queries.KRUTI_FONT_PATH).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (Queries.PDF_COUNT < Queries.PDF_LIMIT) {
            Queries.PDF_COUNT = Queries.PDF_COUNT + 1;
            CommanHelper.setCurrentValue(Queries.INDEX_PATH + "/_segment001", "D", Queries.PDF_COUNT);

            try {
                Date today = new Date();
                SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd yyyy");
                String judgementCSS = "<style>@font-face {font-family: 'KrutiDev';src: url('"+kruti_font+"') format('truetype');-fs-pdf-font-embed: embed;-fs-pdf-font-encoding: Identity-H;}body {font-size:"+Queries.PRINT_SETTING_MODEL.getPrintFontSize()+"px;}@page{@top-center{content:\"" + dt1.format(today) + "\"}}@page{@top-right{content:\"Page \"counter(page) \" of \" counter(pages)}}</style>";
                data = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"+judgementCSS+"</head><body><br/><div style=\"width: 100%;text-align: center\" ><img src=\""+Queries.LOGO_PATH_BY_APPLICATION+"\" /> </div>"+ ServiceHelper.getLicText() +"<br/>" + data + "</body></html>";
                data = data.replaceAll("font-family: Kruti Dev 012", "font-family: KrutiDev");
                data = data.replaceAll("font-family:Kruti Dev 010;", "font-family: KrutiDev");

                File f = new File(filePath);
                if (f.exists()) {
                    f.delete();
                }

                ConverterProperties props = new ConverterProperties();
                props.setTagWorkerFactory(new ITextProperties.CustomTagWorkerFactory());

                PdfDocument temp = new PdfDocument(new PdfWriter(filePath));
                if (Queries.PRINT_SETTING_MODEL.getPageType().equals("A4")){
                    temp.setDefaultPageSize(PageSize.A4);
                }else if (Queries.PRINT_SETTING_MODEL.getPageType().equals("Legal")) {
                    temp.setDefaultPageSize(PageSize.LEGAL);
                }else if (Queries.PRINT_SETTING_MODEL.getPageType().equals("Letter")){
                    temp.setDefaultPageSize(PageSize.LETTER);
                }
                HtmlConverter.convertToPdf(data, temp, props);
                return true;
            } catch (Exception ex) {
                Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            new Utils().showDialogAlert("PDF count exceeded!");
        }
        return false;
    }

//    public static void openPdf(String url) {
//        String basePath = new java.io.File("").getAbsolutePath();
//        url = basePath + "/web/jpm/" + url;
//        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
//        try {
//            File f = new File(url);
////            URI uri = new URI(url);
//            if (f.exists()) {
//                desktop.open(f);
//            } else {
//                JOptionPane.showMessageDialog(null, "File Not Exist!");
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public void exportBookmark() {
        DirectoryChooser directoryChooser
                = new DirectoryChooser();
        final File selectedDirectory
                = directoryChooser.showDialog(new Stage(StageStyle.DECORATED));
        ResponseMaster data = new ServiceHelper().getAllBookmarksInJSON();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(selectedDirectory + "\\supreme_backup.txt"), "utf-8"))) {
            writer.write(new Gson().toJson(data.getData()));
            new Utils().showDialogAlert("Bookmarks exported successfully!");
        } catch (Exception e) {

        }
    }

    public void importBookmark() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            final File selectedFile = fileChooser.showOpenDialog(new Stage(StageStyle.DECORATED));
            String sCurrentLine;
            Gson g = new Gson();
            FileReader reader = new FileReader(selectedFile);

            Type listType = new TypeToken<ArrayList<JSONBookmarkModel>>() {
            }.getType();
            ArrayList<JSONBookmarkModel> data = g.fromJson(reader, listType);

            for (int i = 0; i < data.size(); i++) {
                JSONBookmarkModel bookmark = data.get(i);
                new ServiceHelper().setBookmark(null, bookmark.getDoc_id(), bookmark.getTitle(), bookmark.getDoc_type(), bookmark.getCategory(), bookmark.getNote());
            }
            new Utils().showDialogAlert("Bookmarks imported successfully!");

//            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "File Not found!");
        } catch (Exception ex) {
            Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
            new Utils().showDialogAlert("Something goes wrong!");
        }
    }

    public String d = "";

    public static boolean print(String data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (OSValidator.isMac() || OSValidator.isUnix() || (OSValidator.isWindows() && !Queries.PRINT_SETTING_MODEL.getUseNativeBrowser())) {
            try {
                String filePath = Paths.get(System.getProperty("java.io.tmpdir"), "print.pdf").toString();
                if (createPdf(data, filePath)) {
                    ViewerDemo.openFile(filePath);
                } else {
                    new Utils().showDialogAlert("Something goes wrong!");
//                    JOptionPane.showMessageDialog(null, "Something goes wrong!");
                }
                return true;
            } catch (Exception ex) {
                Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            data = data.replaceAll("<html>", "");
            data = data.replaceAll("</html>", "");
            data = data.replaceAll("<HTML>", "");
            data = data.replaceAll("</HTML>", "");
            data = data.replaceAll("<Html>", "");
            data = data.replaceAll("</Html>", "");
            data = data.replaceAll("<body>", "");
            data = data.replaceAll("</body>", "");
            data = data.replaceAll("<BODY>", "");
            data = data.replaceAll("</BODY>", "");
            data = data.replaceAll("<Body>", "");
            data = data.replaceAll("</Body>", "");
            data = data.replaceAll("‘", "'").replaceAll("’", "'").replaceAll("—", "-").replaceAll("–", "-").replaceAll("“", "\"").replaceAll("”", "\"");
            data = data.replaceAll("<body class=\"ContentFont\" style=\"margin:0px 20px;\">", "");

            try {
//                WinRegistry.createKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\JavaSoft\\Prefs");
//                WinRegistry.createKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Wow6432Node\\JavaSoft\\Prefs");

                WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Internet Explorer\\PageSetup\\", "header", "&b&d");
                WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Internet Explorer\\PageSetup\\", "footer", "&w&bPage &p of &P");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String licText = new ServiceHelper().getLicText();
            String script = "<script type=\"text/javascript\">function printpr() {var WebBrowser = '<OBJECT ID=\"WebBrowser1\" WIDTH=0 HEIGHT=0 CLASSID=\"CLSID:8856F961-340A-11D0-A96B-00C04FD705A2\"></OBJECT>';document.body.insertAdjacentHTML('beforeEnd', WebBrowser); WebBrowser1.ExecWB(7, 1);WebBrowser1.outerHTML = \"\";}</script>";
            String str = "<script>\t\tfunction printpr()\t\t{\tvar OLECMDID = 7;\t\tvar PROMPT = 1;\tvar WebBrowser = '<OBJECT ID=\"WebBrowser1\" WIDTH=0 HEIGHT=0 CLASSID=\"CLSID:8856F961-340A-11D0-A96B-00C04FD705A2\"></OBJECT>';\t\tdocument.body.insertAdjacentHTML('beforeEnd', WebBrowser);\t\tWebBrowser1.ExecWB(OLECMDID, PROMPT);\t\tWebBrowser1.outerHTML = \"\";\t\t}\t\t</script>";
            script = str;
            URL kruti_font = null;
            try {
                kruti_font = new File(Queries.KRUTI_FONT_PATH).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String judgementCSS = "<style>@font-face {font-family: 'Kruti Dev';src: url('" + kruti_font + "') format('truetype')}.KrutiDev_hindi_text {font-family: Kruti Dev !important;font-size:" + Queries.PRINT_SETTING_MODEL.getPrintFontSize() + "px !important;}body {font-size:" + Queries.PRINT_SETTING_MODEL.getPrintFontSize() + "px;}</style>";
            if (Queries.PRINT_SETTING_MODEL.getPrintLogo() == false) {
                data = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" + script + judgementCSS + "</head><body onload=\"printpr();\">" + data + "</body></html>";
            } else {
                data = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" + script + judgementCSS + "</head><body onload=\"printpr();\"><br/><div style=\"width: 100%;text-align: center\" ><img src=\"" + Queries.LOGO_PATH_BY_APPLICATION + "\" /> </div><br/>" + licText + data + "</body></html>";
            }

            try {
                File file = new File(Queries.TEMP_FILE_PATH);
                FileWriter fileWriter = new FileWriter(file, false);
                fileWriter.write(data);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;

            WebBrowser webBrowser = new WebBrowser();
            webBrowser.setSize(screenWidth, screenHeight);

            JFrame frame = new JFrame("Supreme Today Print Priview");
            Container contentPane = frame.getContentPane();
            contentPane.add(webBrowser, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
            frame.toFront();
            URL url = null;
            try {
                url = Paths.get(Queries.TEMP_FILE_PATH).toUri().toURL();
            } catch (MalformedURLException e) {
                Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, e);
//                System.out.println(e);
            }
            webBrowser.setURL(url);

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                // your code here
                                frame.hide();
//                                frame.dispose();
                            });
                        }
                    },
                    2000
            );
        }
        return false;
    }

    public boolean print1(String data) {

//        System.out.println(data);
//data = getData();
//        if (Queries.PRINT_COUNT == 0) {
//            int printCount = CommanHelper.getCurrentValue(Queries.INDEX_PATH + "/_segment001", "P");
//
//            //Queries.PRINT_COUNT = CommanHelper.getCurrentValue(Queries.INDEX_PATH + "/_segment001","P");
//            Queries.PRINT_COUNT = printCount + 1;
//        }
//        System.out.println(data);
//        JOptionPane.showMessageDialog(null, Queries.PRINT_COUNT+"");
//        System.out.println("PRINT_COUNT::" + Queries.PRINT_COUNT);
        if (Queries.PRINT_COUNT < Queries.PRINT_LIMIT) {
            Queries.PRINT_COUNT = Queries.PRINT_COUNT + 1;
            CommanHelper.setCurrentValue(Queries.INDEX_PATH + "/_segment001", "P", Queries.PRINT_COUNT);
//        System.out.println("Print....");
            data = data.replaceAll("<html>", "");
            data = data.replaceAll("</html>", "");
            data = data.replaceAll("<HTML>", "");
            data = data.replaceAll("</HTML>", "");
            data = data.replaceAll("<Html>", "");
            data = data.replaceAll("</Html>", "");
            data = data.replaceAll("<body>", "");
            data = data.replaceAll("</body>", "");
            data = data.replaceAll("<BODY>", "");
            data = data.replaceAll("</BODY>", "");
            data = data.replaceAll("<Body>", "");
            d = data.replaceAll("</Body>", "");

            HashMap<String, String> userDetails = new ServiceHelper().getUserDetails();
            d = new ServiceHelper().getLicText() + d;
//            System.out.println(d);
//data = data.replaceAll(" – ", "-");
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new PrintPreview(d).start();

                }
            });
            return true;
        } else {
//            System.out.println("Print count exceeded!");
            JOptionPane.showMessageDialog(null, "Print count exceeded!");
            return false;
        }
    }

    public static void viewpdffile(String url) {
        ViewerDemo.openFile(url);
    }

}
