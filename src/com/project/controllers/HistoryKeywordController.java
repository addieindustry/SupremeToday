package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.ServiceHelper;
import com.project.interfaces.HistorySearchKeywordListener;
import com.project.interfaces.HistorySearchListener;
import com.project.model.HistoryModel;
import com.project.model.JudgementResultModel;
import com.project.model.LiveUpdateModel;
import com.project.model.TitleIdListModel;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryKeywordController implements Initializable {

    @FXML
    TableView tableTitleList;

    @FXML
    private TableColumn columnKeywords;

    private ObservableList<TitleIdListModel> tableData = FXCollections.observableArrayList();

//    for (HistorySearchListener listner : searchListenerList) {

    private static final List<HistorySearchKeywordListener> listeners = Lists.newArrayList();

    public void applyHistoryKeywordInterface(HistorySearchKeywordListener listener) {
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

        tableTitleList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // Get the table header
                Pane header = (Pane)tableTitleList.lookup("TableHeaderRow");
                if(header!=null && header.isVisible()) {
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                    header.setManaged(false);
                }
            }
        });

        columnKeywords.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TitleIdListModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TitleIdListModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().titleProperty();
            }
        });
//        columnKeywords.setCellValueFactory(new PropertyValueFactory("title"));

        /*Table row double click event */
        tableTitleList.setRowFactory(tv -> {
            TableRow<JudgementResultModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    if (tableTitleList.getSelectionModel().getSelectedItem() != null) {
                        TitleIdListModel keyword = (TitleIdListModel) tableTitleList.getSelectionModel().getSelectedItem();

                /*CALL LISTERNER TO OPEN SEARCH RESULT*/
                        for (HistorySearchKeywordListener listner : listeners) {
                            listner.openHistoryResultListener(keyword.getTitle());
                        }
                        handleCancel();
                    }
                }
            });
            return row;
        });

//        //Add change listener
//        tableTitleList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            //Check whether item is selected and set value of selected item to Label
//            if (tableTitleList.getSelectionModel().getSelectedItem() != null) {
//                TitleIdListModel keyword = (TitleIdListModel) newValue;
//
//                /*CALL LISTERNER TO OPEN SEARCH RESULT*/
//                for (HistorySearchKeywordListener listner : listeners) {
//                    listner.openHistoryResultListener(keyword.getTitle());
//                }
//                handleCancel();
//            }
//        });

        loadHistory();
    }

    private void loadHistory() {
        //get table data from history table
        tableData = ServiceHelper.getHistoryListByKeywordOnly();
        tableTitleList.setItems(null);
        if (tableData.size() > 0) {
            tableTitleList.setItems(tableData);
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
