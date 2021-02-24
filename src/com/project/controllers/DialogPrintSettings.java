package com.project.controllers;

import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.helper.SqliteHelper;
import com.project.model.HistoryModel;
import com.project.model.PrintSettingModel;
import com.project.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Addie on 6/13/2017.
 */
public class DialogPrintSettings implements Initializable {

    @FXML
    private Button btnOk, btnCancel;

    @FXML
    private TextField textFieldMarginTop, textFieldMarginBottom, textFieldMarginRight, textFieldMarginLeft;

    @FXML
    private RadioButton rbNativeOption, rbPDFOption;

    @FXML
    private ComboBox<String> comboboxPageType, comboboxDisplayFontSize, comboboxPrintFontSize;

    @FXML
    private CheckBox checkboxPrintwithLogo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboboxPageType.setEditable(true);
        ObservableList<String> options = FXCollections.observableArrayList("A4","Legal","Letter");
        comboboxPageType.setItems(options);

        comboboxDisplayFontSize.setEditable(true);
        comboboxPrintFontSize.setEditable(true);
//        options = FXCollections.observableArrayList("8","9","10","11","12","13","14","15","16","17","18","19","20");
        options = FXCollections.observableArrayList("8","10","14","18","20","24", "28", "30", "32", "34", "36", "40");
        comboboxDisplayFontSize.setItems(options);
        comboboxPrintFontSize.setItems(options);

        btnOk.setDefaultButton(true);
        btnOk.setOnAction(event -> {
            if ((!comboboxPageType.getValue().isEmpty()) && (!comboboxPrintFontSize.getValue().isEmpty()) && (!comboboxDisplayFontSize.getValue().isEmpty()))
            {
                Queries.PRINT_SETTING_MODEL.setPageType(comboboxPageType.getValue());
                Queries.PRINT_SETTING_MODEL.setPrintFontSize(comboboxPrintFontSize.getValue());
                Queries.PRINT_SETTING_MODEL.setDisplayFontSize(comboboxDisplayFontSize.getValue());
                Queries.PRINT_SETTING_MODEL.setPrintLogo(checkboxPrintwithLogo.isSelected());
                Queries.PRINT_SETTING_MODEL.setUseNativeBrowser(rbNativeOption.isSelected());

                Queries.PRINT_SETTING_MODEL.setMarginTop(Float.valueOf(textFieldMarginTop.getText()));
                Queries.PRINT_SETTING_MODEL.setMarginBottom(Float.valueOf(textFieldMarginBottom.getText()));
                Queries.PRINT_SETTING_MODEL.setMarginRight(Float.valueOf(textFieldMarginRight.getText()));
                Queries.PRINT_SETTING_MODEL.setMarginLeft(Float.valueOf(textFieldMarginLeft.getText()));

                Queries.PRINT_SETTING_MODEL.setResultFontSize(Queries.PRINT_SETTING_MODEL.getResultFontSize());

                if (ServiceHelper.setPrintSetting(Queries.PRINT_SETTING_MODEL))
                {
                    new Utils().showDialogAlert("Print setting updated successfully!");
                }else{
                    new Utils().showDialogAlert("Print setting not updated!");
                }
            }else{
                new Utils().showDialogAlert("Please select the value!");
            }
        });

        btnCancel.setOnAction(event -> {
            handleCancel();
        });
        fillData();
    }

    public void fillData() {
        ServiceHelper.getPrintSetting();
        comboboxPageType.getSelectionModel().select(Queries.PRINT_SETTING_MODEL.getPageType());
        comboboxPrintFontSize.getSelectionModel().select(Queries.PRINT_SETTING_MODEL.getPrintFontSize());
        comboboxDisplayFontSize.getSelectionModel().select(Queries.PRINT_SETTING_MODEL.getDisplayFontSize());
        checkboxPrintwithLogo.setSelected(Queries.PRINT_SETTING_MODEL.getPrintLogo());

        textFieldMarginTop.setText(String.valueOf(Queries.PRINT_SETTING_MODEL.getMarginTop()));
        textFieldMarginBottom.setText(String.valueOf(Queries.PRINT_SETTING_MODEL.getMarginBottom()));
        textFieldMarginRight.setText(String.valueOf(Queries.PRINT_SETTING_MODEL.getMarginRight()));
        textFieldMarginLeft.setText(String.valueOf(Queries.PRINT_SETTING_MODEL.getMarginLeft()));

        if (Queries.PRINT_SETTING_MODEL.getUseNativeBrowser()){
            rbNativeOption.setSelected(true);
            rbPDFOption.setSelected(false);
        }else{
            rbNativeOption.setSelected(false);
            rbPDFOption.setSelected(true);
        }
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

//    public void initData(String docId, String Title, String docType) {
//        doc_id = docId;
//        title = Title;
//        doc_type = docType;
//    }

}
