package com.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ErrorController {
    @FXML
    private TextArea errorMessage;

    public void setErrorText(String text) {
        errorMessage.setEditable(false);
        errorMessage.setText(text);
    }

    @FXML
    private void close() {
        errorMessage.getScene().getWindow().hide();
    }
}