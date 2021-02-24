/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;

import com.github.junrar.exception.RarException;
import com.project.helper.KeyHelper;
import com.project.helper.Queries;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author addie
 */
public class CDSelection {

    static String packageCode = "";
    static String packageName = "";
    static File selectedFile = null;
    static File extractFile = null;

    public static GridPane getForm(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("RAR", "*.rar"));
        Button openButton = new Button("Select File");
        Button copyButton = new Button("Start Copy");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Please Select file for extract the data.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

//        TextField agentCodeTextField = new TextField();
//        grid.add(agentCodeTextField, 1, 1);
        Label fileNameLabel = new Label("");
        grid.add(fileNameLabel, 0, 1);
        grid.add(openButton, 1, 1);
        copyButton.setDisable(true);
        grid.add(copyButton, 2, 1);

//        ProgressIndicator pin = new ProgressIndicator();
//        pin.setProgress(-1.0f);
//        pin.setVisible(false);
//        grid.add(pin, 3, 1);
        Button activateBtn = new Button("Extract Files");
        
        activateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String tempPath = new java.io.File("").getAbsolutePath() + "\\tmp";

//                    File archive = new File("F:\\iconflux\\projects\\supreme_desktop\\Builds\\Data\\zip\\Index.part2.rar");
                    File destination = new File(Queries.DATA_PATH);
                    if (!destination.exists()) {
                        destination.mkdirs();
                    }
                    boolean ret = new ExtractArchive().extractArchive(extractFile, destination);
                    if (ret) {
                        new File(tempPath).delete();
                        Dialogs.create().owner(primaryStage).title("Information Dialog").masthead("Succefully extracted!!").message(null).showInformation();
                    } else {
                        Dialogs.create().owner(primaryStage).title("Information Dialog").masthead("Something goes wriong!").message(null).showInformation();

                    }
                } catch (RarException ex) {
                    Logger.getLogger(CDSelection.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CDSelection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    selectedFile = file;
                    fileNameLabel.setText(file.getName());
//                        openFile(file);
                    copyButton.setDisable(false);
                }
            }
        });

        copyButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        try {
                            openButton.setDisable(true);
                            copyButton.setDisable(true);
                            
                            File destination = new File(Queries.DATA_PATH);
                            if (!destination.exists()) {
                                destination.mkdirs();
                            }
                            boolean ret = new ExtractArchive().extractArchive(selectedFile, destination);
                            if (ret) {

                                Dialogs.create().owner(primaryStage).title("Information Dialog").masthead("Succefully extracted!!").message(null).showInformation();
                            } else {
                                Dialogs.create().owner(primaryStage).title("Information Dialog").masthead("Something goes wriong!").message(null).showInformation();

                            }
                        
//                        try {
//                            String tempPath = new java.io.File("").getAbsolutePath() + "\\tmp";
//                            File f = new File(tempPath);
//                            if (!f.exists()) {
//                                f.mkdirs();
//                            }
//                            File destFile = new File(tempPath + "\\" + selectedFile.getName());
//                            copyFileUsingStream(selectedFile, destFile);
//                            if(extractFile==null){
//                            extractFile = selectedFile;
//                            }
//                            selectedFile = null;
//                            fileNameLabel.setText("");
//                            Dialogs.create().owner(primaryStage).title("Information Dialog").masthead("File copy successfully!").message(null).showInformation();
//                        } catch (Exception ex) {
//                            copyButton.setDisable(false);
//                            Logger.getLogger(CDSelection.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                            openButton.setDisable(false);
                        } catch (RarException ex) {
                            Logger.getLogger(CDSelection.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(CDSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

//        HBox hbBtn = new HBox(11);
//        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
//        hbBtn.getChildren().add(activateBtn);
//        grid.add(hbBtn, 1, 11);
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 12);
        return grid;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
