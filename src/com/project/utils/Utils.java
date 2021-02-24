package com.project.utils;

import com.project.Main;
import com.project.controllers.ErrorController;
import com.project.helper.Queries;
import com.project.interfaces.ConfirmationInterface;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

//import org.jdic.web.BrComponent;

public class Utils {

    public void showDialogAlert(String message) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Button button = new Button("OK");
        Text txt = new Text(message);

        // VBox
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 10, 50));
        vb.setSpacing(10);
        vb.getChildren().add(txt);
        vb.getChildren().add(button);
        vb.setAlignment(Pos.CENTER);

//        Scene scene = new Scene(VBoxBuilder.create().children(new Text(message), button).alignment(Pos.CENTER).padding(new Insets(5)).build());
//        Scene scene = new Scene(VBoxBuilder.create().children(txt, button).alignment(Pos.CENTER).padding(new Insets(10, 20, 20, 10)).build());
        Scene scene = new Scene(vb);
//        scene.getIcons().add(new Image(getClass().getResourceAsStream("../resources/logo.png")));
        dialogStage.setScene(scene);

        /*disabled maximaize and minimize except close use*/
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);

        button.setOnAction(event -> dialogStage.close());

            /*Close window on Escap key press*/
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evt) {
                if (evt.getCode().equals(KeyCode.ESCAPE)) {
                    dialogStage.close();
                }
            }
        });


        // Get the Stage.
        // Add a custom icon.
        dialogStage.getIcons().add(new Image(this.getClass().getResource("/com/project/resources/logo.png").toString()));
        dialogStage.show();
    }


//    public void confirmationDialog(String message, ConfirmationInterface confirmationInterface) {
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation");
////        alert.setHeaderText("Look, a Confirmation Dialog");
//        alert.setContentText(message);
//
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
////        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
//
//        // Add a custom icon.
////        stage.getIcons().add(new Image(this.getClass().getResource("../resources/logo.png").toString()));
//
//        alert.initModality(Modality.WINDOW_MODAL);
//
//         /*disabled maximaize and minimize except close use*/
////        alert.initModality(Modality.APPLICATION_MODAL);
//
////        alert.initStyle(StageStyle.UTILITY);
//        alert.setResizable(false);
//
//        /*Close window on Escap key press*/
//        stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent evt) {
//                if (evt.getCode().equals(KeyCode.ESCAPE)) {
//                    alert.close();
//                }
//            }
//        });
//
//        Optional<ButtonType> result = alert.showAndWait();
//
//        if (result.get() == ButtonType.OK) {
//            // ... user chose OK
//
//            confirmationInterface.yesHeandale();
//        } else {
//            // ... user chose CANCEL or closed the dialog
//            confirmationInterface.NoHeandale();
//        }
//    }

    public void PrintWebView(WebView webView) {
//        Node node = new Circle(100, 200, 200);
//        PrinterJob job = PrinterJob.createPrinterJob();
//        if (job != null){
//            job.showPrintDialog(null);
//            boolean success = job.printPage(node);
//            if (success) {
//                job.endJob();
//            }
//        }



//        WebView webView = new WebView();
//        webView.getEngine().loadContent(html, "text/html");


//        if ((ConstantsClass.JOURNAL_TRUE_PRINT_PDF.length() > 0) && (webView.getEngine().getDocument().toString().contains("Search for, ") == false))
//        {
//            try {
//                Desktop.getDesktop().open(new File(ConstantsClass.JOURNAL_TRUE_PRINT_PDF));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else
//        {

            final WebEngine webEngine = webView.getEngine();
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            PrinterJob job = PrinterJob.createPrinterJob(printer);

            if (job != null) {
                Object obj = job.getJobSettings();
//                job.getJobSettings().setPageLayout(pageLayout);
//                job.getJobSettings().setPageRanges(job.getJobSettings().getPageRanges());
//                job.getJobSettings().setPageRanges(new PageRange(1, Integer.MAX_VALUE));
                job.showPageSetupDialog(null);
                job.showPrintDialog(null);
                webEngine.print(job);
                job.endJob();
            }
//        }
    }

//    public JFrame frame;
//    public BrComponent brC;
//    public WebBrowser webBrowser = new WebBrowser();

//    public void PrintWebView(String htmlText) {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int screenHeight = screenSize.height;
//        int screenWidth = screenSize.width;
//
//        try{
//            PrintWriter writer = new PrintWriter(Paths.get(ConstantsClass.DB_PATH, "temp.html").toString(), "UTF-8");
//            writer.print(htmlText);
//            writer.close();
//        } catch (IOException e) {
//            // do something
//        }
////        WebBrowser webBrowser = new WebBrowser();
////        try {
////            webBrowser.setURL(Paths.get(ConstantsClass.DB_PATH, "temp.html").toUri().toURL());
////        } catch (MalformedURLException e) {
////            e.printStackTrace();
////        }
////        webBrowser.requestFocus();
//
////        if (frame == null){
//        JFrame frame= new JFrame("SupremeToday - Popup Window");
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            Container contentPane = frame.getContentPane();
////            contentPane.add(webBrowser, BorderLayout.CENTER);
//            frame.setSize(screenWidth,screenHeight);
////        }
//        frame.repaint();
//        frame.setVisible(true);
////        frame.hide();
//    }

    public void PrintWebView(URL url) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;


//        WebBrowser webBrowser = new WebBrowser();
//        webBrowser.setURL(url);
//        webBrowser.repaint();
//        webBrowser.revalidate();

//        if (frame != null){
//            frame.dispose();
//        }
//        if (frame == null){
        JFrame frame = new JFrame("SupremeToday - Popup Window");
//        frame.getContentPane().removeAll();
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Container contentPane = frame.getContentPane();
//            contentPane.add(webBrowser, BorderLayout.CENTER);
//        frame.pack();
            frame.setSize(screenWidth,screenHeight);
//        }
        frame.repaint();
        frame.setVisible(true);
//        frame.hide();
//        server.stop(10);
    }

  /*  public static void PrintWebView(final Node node) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
//        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
//        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
//        node.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        job.getJobSettings().setPageLayout(pageLayout);
        if (job != null) {
            job.showPrintDialog(null);
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }
    }*/

    public String currentDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void showErrorDialog(Exception e) {
      /*  StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));*/

        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

        String error = e.toString() + " at " + className + "." + methodName + "():" + lineNumber;

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/project/ui/Error.fxml"));
        ErrorController controller = new ErrorController();
        loader.setController(controller);
        try {
            Parent root = loader.load();
            controller.setErrorText(error);
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.show();
            /*Close window on Escap key press*/
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent evt) {
                    if (evt.getCode().equals(KeyCode.ESCAPE)) {
                        dialog.close();
                    }
                }
            });

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            writeToTextFile(error);
        }

    }

    /*Write to text file*/
    public static void writeToTextFile(String error) {

        String currentDate = new Utils().currentDate();
        String path = Queries.ERROR_LOG_FILE_PATH;

        /*StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));*/

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(path, true));
            bw.newLine();
            bw.newLine();
            bw.write(currentDate);
            bw.newLine();
            bw.write(error);
//            bw.newLine();

        } catch (IOException er) {
            er.printStackTrace();
        } finally {

            try {
                if (bw != null)
                    bw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
