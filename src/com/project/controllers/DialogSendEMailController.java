package com.project.controllers;

import com.project.helper.ServiceHelper;
import com.project.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogSendEMailController implements Initializable {

    @FXML
    Button btnSend, btnCancel;

    @FXML
    private TextField textFieldEmailID;

    private String judgementHTML = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textFieldEmailID.requestFocus();

        /*Button cancel*/
        btnCancel.setOnAction(event -> {
            handleCancel();
        });

        btnSend.setOnAction(event -> {
            //judgementHTML to be send in Email
            String toEmailId = textFieldEmailID.getText().trim();
            new Utils().showDialogAlert(ServiceHelper.sendEmail(toEmailId, "todaysupreme@gmail.com", "Supreme Today Judgement", judgementHTML));
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


    public void setHTML(String HTML) {
        this.judgementHTML = HTML;
    }
}
