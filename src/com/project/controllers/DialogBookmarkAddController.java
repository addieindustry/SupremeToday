package com.project.controllers;

import com.project.helper.ServiceHelper;
import com.project.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Addie on 6/13/2017.
 */
public class DialogBookmarkAddController implements Initializable {

    @FXML
    private Button btnOk, btnCancel, btnAddCategory;

    @FXML
    private ComboBox<String> comboboxCategory;

    @FXML
    private TextField textFieldNewCategory, textFieldDescription;

    private String doc_id, title, doc_type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboboxCategory.setEditable(true);
        comboboxCategory.setItems(ServiceHelper.getAllBookmarkCategory());

        btnOk.setDefaultButton(true);
        btnOk.setOnAction(event -> {
            if ((!comboboxCategory.getValue().isEmpty()) && (!textFieldDescription.getText().isEmpty()))
            {
                String msg = ServiceHelper.setBookmark(null, doc_id, title, doc_type, comboboxCategory.getValue().toString(), textFieldDescription.getText().toString());
                new Utils().showDialogAlert(msg);
            }else{
                new Utils().showDialogAlert("Please fill the value!");
            }

//            note = textFieldNote.getText().trim();
//            dialogStage.close();
        });

        btnCancel.setOnAction(event -> {
            handleCancel();
        });

        btnAddCategory.setOnAction(event -> {
            if (!textFieldNewCategory.getText().isEmpty())
            {
                comboboxCategory.getItems().add(textFieldNewCategory.getText());
                textFieldNewCategory.setText("");
            }else{
                new Utils().showDialogAlert("Please fill new Category!");
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

    public void initData(String docId, String Title, String docType) {
        doc_id = docId;
        title = Title;
        doc_type = docType;
    }

}
