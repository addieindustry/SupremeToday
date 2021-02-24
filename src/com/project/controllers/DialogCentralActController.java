package com.project.controllers;

import com.project.helper.ServiceHelper;
import com.project.model.TitleIdListModel;
import com.project.utils.PTableColumn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogCentralActController implements Initializable {

    @FXML
    private WebView webViewContent;

//    private String href;

//    private Stage dialogStage;
//    private String okClicked = "";

//    private ObservableList<DictionaryTitleModel> data;
    private ObservableList<TitleIdListModel> tableData = FXCollections.observableArrayList();
//    private ObservableList<String> entries = FXCollections.observableArrayList();

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public DialogCentralActController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        columnId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TitleIdListModel, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<TitleIdListModel, String> p) {
//                // p.getValue() returns the Person instance for a particular TableView row
//                return p.getValue().idProperty();
//            }
//        });
//
//        columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TitleIdListModel, String>,
//                ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<TitleIdListModel, String> t) {
//                // t.getValue() returns the Test instance for a particular TableView row
//                return t.getValue().titleProperty();
//                // or
//            }
//        });
//        columnId.setCellValueFactory(new PropertyValueFactory("id"));
//        columnTitle.setCellValueFactory(new PropertyValueFactory("title"));

//        loadData();

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
//        FilteredList<TitleIdListModel> filteredData = new FilteredList<>(tableData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
//        textFieldTitle.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(searchType -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Compare first name and last name of every person with filter text.
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                return searchType.getTitle().toLowerCase().contains(lowerCaseFilter);
//            });
//        });
//
//        // 3. Wrap the FilteredList in a SortedList.
//        SortedList<TitleIdListModel> sortedData = new SortedList<>(filteredData);
//
//        // 4. Bind the SortedList comparator to the TableView comparator.
//        sortedData.comparatorProperty().bind(tableTitleList.comparatorProperty());
//
//        // 5. Add sorted (and filtered) data to the table.
//        tableTitleList.setItems(sortedData);
//
//        tableTitleList.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                // Get the table header
//                Pane header = (Pane)tableTitleList.lookup("TableHeaderRow");
//                if(header!=null && header.isVisible()) {
//                    header.setMaxHeight(0);
//                    header.setMinHeight(0);
//                    header.setPrefHeight(0);
//                    header.setVisible(false);
//                    header.setManaged(false);
//                }
//            }
//        });
//
//        //Add change listener
//        tableTitleList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            //Check whether item is selected and set value of selected item to Label
//            if (tableTitleList.getSelectionModel().getSelectedItem() != null) {
//                TitleIdListModel hm = (TitleIdListModel) newValue;
//                String content = ServiceHelper.getDictionaryContentById(hm.getId());
//                webViewContent.getEngine().loadContent(content, "text/html");
//            }
//        });
    }

//    private void loadData() {
//        tableData = ServiceHelper.getAllDictionaryTitle();
//
//        tableTitleList.setItems(null);
//        if (tableData.size() > 0) {
//            tableTitleList.setItems(tableData);
//        }
//    }

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

    public void initData(String href) {
        String act,section="";
        if (href.contains("~")){
            act = href.substring(0, href.indexOf("~")).replace("act:", "").trim();
            section = href.substring(href.indexOf("~") + 1).trim();
        }else{
            act = href.replace("act:", "").trim();
        }
        String htmlContent = ServiceHelper.getCentralActContentById(act, section);
        webViewContent.getEngine().loadContent(htmlContent, "text/html");
//        doc_id = docId;
//        title = Title;
//        doc_type = docType;
    }
}
