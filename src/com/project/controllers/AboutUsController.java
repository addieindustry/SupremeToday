package com.project.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsController implements Initializable {

    @FXML
    private Hyperlink hyperlinkURL;
//    private Label labelTitle, labelContactUs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        labelTitle.setText(ConstantsClass.TITLE_MAIN_WINDOW + ConstantsClass.COPY_RIGHT);
//        labelContactUs.setText(ConstantsClass.ABOUT_US_NOTE);
//        labelNote.setText(ConstantsClass.NOTE);
        hyperlinkURL.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(Desktop.isDesktopSupported())
                {
                    try {
                        Desktop.getDesktop().browse(new URI("http://www.supreme-today.com/"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private Stage dialogStage;
    private boolean okClicked = false;

    public boolean isOkClicked() {
        return okClicked;
    }

    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
