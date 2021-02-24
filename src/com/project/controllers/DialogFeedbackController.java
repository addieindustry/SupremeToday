package com.project.controllers;

import com.project.helper.ServiceHelper;
import com.project.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogFeedbackController implements Initializable {

    @FXML
    Button btnSend, btnCancel;

    @FXML
    private TextField textFieldSubject;

    @FXML
    private TextArea textFieldDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textFieldSubject.requestFocus();

        btnSend.setOnAction(event -> {
            //judgementHTML to be send in Email
            String subject = textFieldSubject.getText().trim();
            String content = textFieldDescription.getText().trim();

            if (!(subject.isEmpty()) && !(content.isEmpty()))
            {
                new Utils().showDialogAlert(ServiceHelper.sendFeedback("todaysupreme@gmail.com", "Testing UserName", subject, content));
            }
        });

        /*Button cancel*/
        btnCancel.setOnAction(event -> {
            handleCancel();
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
