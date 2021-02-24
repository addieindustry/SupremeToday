package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.interfaces.JudgementSearchFromAct;
import com.project.model.TitleIdListModel;
import com.project.utils.PTableColumn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WordPhrasesController implements Initializable {

    @FXML
    private Button btnZoomIn, btnZoomOut;
    @FXML
    private Hyperlink hpJudgementResults;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TableView tableTitleList;
    @FXML
    private PTableColumn columnId, columnTitle;
    @FXML
    private WebView webViewContent;
    private String currentWordPhraseTitle= "";
    private String currentWordPhraseId= "";

//    private ObservableList<DictionaryTitleModel> data;
    private ObservableList<TitleIdListModel> tableData = FXCollections.observableArrayList();

    /*INTERFACE FOR judgement search*/
    private static final List<JudgementSearchFromAct> JudgementSearchInterfaceList = Lists.newArrayList();

    public void setJudgementSearchFromAct(JudgementSearchFromAct judgementSearchListener) {
        Preconditions.checkNotNull(judgementSearchListener);
        JudgementSearchInterfaceList.add(judgementSearchListener);
    }

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public WordPhrasesController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TitleIdListModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TitleIdListModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().idProperty();
            }
        });

        columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TitleIdListModel, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TitleIdListModel, String> t) {
                // t.getValue() returns the Test instance for a particular TableView row
                return t.getValue().titleProperty();
                // or
            }
        });
//        columnId.setCellValueFactory(new PropertyValueFactory("id"));
//        columnTitle.setCellValueFactory(new PropertyValueFactory("title"));

        /*ZOOM IN BUTTON*/
        btnZoomIn.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2));

//            webViewContent.setZoom(webViewContent.getZoom() / 1.1);
            String content = ServiceHelper.getWordPhrasesContentById(currentWordPhraseId);
            content  = content.replace("\n", "<br>");
            webViewContent.getEngine().loadContent(content, "text/html");
        });

        /*ZOOM OUT BUTTON*/
        btnZoomOut.setOnAction(event -> {
//            webViewContent.setZoom(webViewContent.getZoom() * 1.1);

            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2));

            String content = ServiceHelper.getWordPhrasesContentById(currentWordPhraseId);
            content  = content.replace("\n", "<br>");
            webViewContent.getEngine().loadContent(content, "text/html");
        });


        /*Hyperlink click events*/
        hpJudgementResults.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (JudgementSearchFromAct listner : JudgementSearchInterfaceList) {
                    listner.openAdvanceSearchView(currentWordPhraseTitle, "");
                }
            }
        });

        loadData();

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<TitleIdListModel> filteredData = new FilteredList<>(tableData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        textFieldTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(searchType -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return searchType.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<TitleIdListModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableTitleList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableTitleList.setItems(sortedData);

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

        //Add change listener
        tableTitleList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tableTitleList.getSelectionModel().getSelectedItem() != null) {
                TitleIdListModel hm = (TitleIdListModel) newValue;
                currentWordPhraseTitle=hm.getTitle();
                currentWordPhraseId=hm.getId();
                String content = ServiceHelper.getWordPhrasesContentById(currentWordPhraseId);
                content  = content.replace("\n", "<br>");
                webViewContent.getEngine().loadContent(content, "text/html");
            }
        });
    }

    private void loadData() {
        tableData = ServiceHelper.getAllWordPhrases();

        tableTitleList.setItems(null);
        if (tableData.size() > 0) {
            tableTitleList.setItems(tableData);
        }
    }
}
