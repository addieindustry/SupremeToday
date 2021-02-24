package com.project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogBookmarkNoteController implements Initializable {

    @FXML
    Button btnOk;

    @FXML
    private TextField textFieldNote;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnOk.setDefaultButton(true);
        btnOk.setOnAction(event -> {
            note = textFieldNote.getText().trim();
            dialogStage.close();
        });
    }

    private Stage dialogStage;
    private String note;

    public String isOkClicked() {
        return note;
    }

    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
