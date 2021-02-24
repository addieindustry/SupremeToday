package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.ServiceHelper;
import com.project.interfaces.HistorySearchListener;
import com.project.model.BookmarkModel;
import com.project.model.HistoryModel;
import com.project.model.JudgementResultModel;
import com.project.utils.PTableColumn;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    Button btnClose, btnDelete;

    @FXML
    TableView tableHistory;

    @FXML
    private PTableColumn columnSearchText, columnHistoryDate, columnSearchType;

    ObservableList<HistoryModel> tableData = FXCollections.observableArrayList();


//    for (HistorySearchListener listner : searchListenerList) {

    private static final List<HistorySearchListener> listeners = Lists.newArrayList();

    public void setHistorySearchInterface(HistorySearchListener listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }

    /*INIT HISTORY SEARCH INTERFACE*/
//    private static final List<HistorySearchListener> searchListenerList = Lists.newArrayList();

//    public void setHistorySearchInterface(HistorySearchListener searchListener) {
////        Preconditions.checkNotNull(searchListener);
////        searchListenerList.add(searchListener);
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnSearchText.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HistoryModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HistoryModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().keywordProperty();
            }
        });
        columnHistoryDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HistoryModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HistoryModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().created_dateProperty();
            }
        });
        columnSearchType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HistoryModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HistoryModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().search_typeProperty();
            }
        });

//        columnSearchText.setCellValueFactory(new PropertyValueFactory("keyword"));
//        columnHistoryDate.setCellValueFactory(new PropertyValueFactory("created_date"));
//        columnSearchType.setCellValueFactory(new PropertyValueFactory("search_type"));

        /*Table row double click event */
        tableHistory.setRowFactory(tv -> {
            TableRow<JudgementResultModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    if (tableHistory.getSelectionModel().getSelectedItem() != null) {
                        HistoryModel hm = (HistoryModel) tableHistory.getSelectionModel().getSelectedItem();
                /*CALL LISTERNER TO OPEN SEARCH RESULT*/
                        for (HistorySearchListener listner : listeners) {
                            if (hm.getsearch_type().equals("Advance"))
                            {
                                listner.openHistoryResultListener(hm);
                            }else{
                                listner.openHistoryResultListener(hm);
                            }

//                    if (type.equals("Document")) {
//                        listner.openHistoryResultListener(hm.getFileType());
//                    } else {
//                        listner.openHistoryResultListener(hm.getTitle());
//                    }
                        }
                        handleCancel();
                    }
                }
            });
            return row;
        });

        //Add change listener
//        tableHistory.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            //Check whether item is selected and set value of selected item to Label
//            if (tableHistory.getSelectionModel().getSelectedItem() != null) {
//                HistoryModel hm = (HistoryModel) newValue;
//                /*CALL LISTERNER TO OPEN SEARCH RESULT*/
//                for (HistorySearchListener listner : listeners) {
//                    if (hm.getsearch_type().equals("Advance"))
//                    {
//                        listner.openHistoryResultListener(hm);
//                    }else{
//                        listner.openHistoryResultListener(hm);
//                    }
//
////                    if (type.equals("Document")) {
////                        listner.openHistoryResultListener(hm.getFileType());
////                    } else {
////                        listner.openHistoryResultListener(hm.getTitle());
////                    }
//                }
//                handleCancel();
//            }
//        });

//        btnDelete.setOnAction(event -> {
//            new Utils().confirmationDialog(ConstantsClass.MESSAGE_DELETE_ALL_HISTORY, confirmationInterface);
//        });

        /*Button Close*/
        btnClose.setOnAction(event -> {
            handleCancel();
        });
        loadHistory();
    }

    private void loadHistory() {
        //get table data from history table
        tableData = ServiceHelper.getHistoryList();
        tableHistory.setItems(null);
        if (tableData.size() > 0) {
            tableHistory.setItems(tableData);
        }
    }

    private Stage dialogStage;

    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void handleCancel() {
        dialogStage.close();
    }
}
