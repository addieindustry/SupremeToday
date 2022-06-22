package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
//import com.project.WebViewFitContent;
import com.project.helper.Queries;
import com.project.helper.ResultViewHelper;
import com.project.helper.ServiceHelper;
import com.project.interfaces.AdvanceSearchFullCollapseListener;
import com.project.interfaces.ClickEventHandler;
import com.project.interfaces.HistorySearchKeywordListener;
import com.project.interfaces.ResetClickEventHandler;
import com.project.model.JudgementResultModel;
import com.project.utility.SearchUtility;
import com.project.utils.FxUtil;
import com.project.utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.w3c.dom.events.Event;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvanceSearchResultViewController implements Initializable {

    @FXML
    private WebView webViewDocView;

    @FXML
    private Button btnFirst, btnLast, btnPrevious, btnNext, btnFilter, btnFilterReset, btnZoomPlus, btnZoomMinus, btnFullCollapse, btnFullCollapseRefresh;

    @FXML
    private TreeView TreeViewFacet;

    @FXML
    private ComboBox comboboxSortBy;

    @FXML
    private Label LabelTotalRecords;

    @FXML
    private SplitPane spMain;

    @FXML
    private AnchorPane filterbyanchor, resultviewanchor;

//    @FXML
//    private TableView TableViewResults;
//
//    @FXML
//    private TableColumn columnBookmark, columnhtml, columnId, columnHN;

    private ObservableList<JudgementResultModel> data;

//    private WebEngine engine;
    //Result label
    int resultFrom = Queries.RESULT_START_FROM;
    int resultTo = Queries.RESULT_TOTAL;
    int tempValue = 0;
    private int totalResultCount = 0;
    private static int MAX_COUNT = Queries.RESULT_TOTAL;
    String search_query="*:*";
    String sort_by="";
    String hl_fields = Queries.LUCENEFIELD_CONTENT_STORE + "," + Queries.LUCENEFIELD_SUMMARY_STORE;
//    private boolean is_search_loaded = false;
//    String filter_query="";

    //    String search_query="DocType:Judgements";
//    HashMap<String, String> filterBy = new HashMap<>();
    Multimap<String, String> filterBy = ArrayListMultimap.create();

    ResultViewHelper resultViewHelper = new ResultViewHelper();

    private static final List<ClickEventHandler> listeners = Lists.newArrayList();

    public void addListener(ClickEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }


    private static final List<AdvanceSearchFullCollapseListener> fullCollapseListeners = Lists.newArrayList();

    public void clickFullCollapseListener(AdvanceSearchFullCollapseListener listener) {
        Preconditions.checkNotNull(listener);
        fullCollapseListeners.add(listener);
    }

//    String filterBy="";

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public AdvanceSearchResultViewController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spMain.setResizableWithParent(filterbyanchor, false);
        spMain.setResizableWithParent(resultviewanchor, false);
//        spMain.setDividerPositions(0.20);
        spMain.setDividerPositions(Queries.PRINT_SETTING_MODEL.getFilterSplitter());

        // Listen to the position property
        spMain.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            if (totalResultCount > 0){
                ServiceHelper.updateFilterSplitterInPrintSetting( Float.parseFloat(String.valueOf(newVal.doubleValue())));
                Queries.PRINT_SETTING_MODEL.setFilterSplitter(Float.parseFloat(String.valueOf(newVal.doubleValue())));
            }
        });


        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            btnFirst.setStyle("-fx-background-color: steelblue");
            btnLast.setStyle("-fx-background-color: steelblue");
            btnPrevious.setStyle("-fx-background-color: steelblue");
            btnNext.setStyle("-fx-background-color: steelblue");
            btnFilter.setStyle("-fx-background-color: steelblue");
            btnFilterReset.setStyle("-fx-background-color: steelblue");
            btnZoomPlus.setStyle("-fx-background-color: steelblue");
            btnZoomMinus.setStyle("-fx-background-color: steelblue");
            btnFullCollapse.setStyle("-fx-background-color: steelblue");
            btnFullCollapseRefresh.setStyle("-fx-background-color: steelblue");
        }



//        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);
//        engine = webViewDocView.getEngine();

        /*ZOOM IN BUTTON*/
        btnZoomPlus.setOnAction(event -> {
//            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2);
//            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2));

            ServiceHelper.updateResultFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getResultFontSize()) + 2);
            Queries.PRINT_SETTING_MODEL.setResultFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getResultFontSize()) + 2));
            totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
        });

        /*ZOOM OUT BUTTON*/
        btnZoomMinus.setOnAction(event -> {
//            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2);
//            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2));

            ServiceHelper.updateResultFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getResultFontSize()) - 2);
            Queries.PRINT_SETTING_MODEL.setResultFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getResultFontSize()) - 2));
            totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
        });


        /*Webview click event*/
        webViewDocView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {

                    // note next classes are from org.w3c.dom domain
                    org.w3c.dom.events.EventListener listener = new org.w3c.dom.events.EventListener() {
                        public void handleEvent(Event ev) {
                            String href = ((org.w3c.dom.Element) ev.getTarget()).getAttribute("href");
                            if (!href.isEmpty())
                            {
                                resultViewHelper.showJudgementDialogWindow(Integer.parseInt(href), search_query, sort_by, hl_fields, filterBy);
                            }
                        }
                    };
                    org.w3c.dom.Document doc = webViewDocView.getEngine().getDocument();
                    org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        org.w3c.dom.Node node = nodeList.item(i);
                        org.w3c.dom.events.EventTarget eventTarget = (org.w3c.dom.events.EventTarget) node;
                        eventTarget.addEventListener("click", listener, false);
                    }
                }
            }
        });

        /*on click of Judgement Search "Search button" this method is called*/
        new AdvanceSearchController().addListener(new ClickEventHandler() {
            @Override
            public void handleSeachClick(String SearchQuery) {
                LabelTotalRecords.setText("Please wait, Loading...!");
                filterBy.clear();
                filterBy.put("DocType", "Judgements");
                search_query=SearchQuery;
                btnFullCollapse.setVisible(true);
                btnFullCollapseRefresh.setVisible(true);
                totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
//                spMain.setDividerPositions(Queries.PRINT_SETTING_MODEL.getFilterSplitter());
//                loadTableData(resultFrom, resultTo, data, TableViewResults, columnBookmark, columnhtml, columnId, columnHN, TreeViewFacet, comboboxSortBy, LabelTotalRecords, search_query, sort_by, filterBy, TotalResultCount);
//                comboboxSortBy.requestFocus();
//                comboboxSortBy.hide();
            }
        });

        /*on click of "Reset button" this method is called*/
        new AdvanceSearchController().addListener(new ResetClickEventHandler() {
            @Override
            public void handleResetClick() {
                clearAll();
            }
        });

        loadAllEvents();
    }

    private void loadAllEvents() {
        clearAll();

        btnFirst.setOnAction(event -> {
            resultFrom = Queries.RESULT_START_FROM;
            resultTo = Queries.RESULT_TOTAL;
            totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
        });

        btnLast.setOnAction(event -> {
            if ((MAX_COUNT) <= totalResultCount) {
                resultFrom = (totalResultCount / MAX_COUNT) * MAX_COUNT;
                resultTo = totalResultCount;
                totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
            } else {
                resultFrom = Queries.RESULT_START_FROM;
                resultTo = Queries.RESULT_TOTAL;
                totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
            }
        });

        btnPrevious.setOnAction(event -> {
            if (tempValue != 0) {
                resultFrom = resultFrom - MAX_COUNT;
                resultTo = resultTo - tempValue;
                tempValue = 0;
                totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
            } else {
                if (resultFrom > 1) {
                    resultFrom = resultFrom - MAX_COUNT;
                    if (totalResultCount ==resultTo){
                        resultTo = resultFrom + MAX_COUNT;
                    }else{
                        resultTo = resultTo - MAX_COUNT;
                    }
                    if (resultFrom==0){resultFrom = 1;}
                    totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
                }
            }
        });

        btnNext.setOnAction(event -> {
            if ((resultTo + MAX_COUNT) <= totalResultCount) {
                resultFrom = resultTo + 1;
                resultTo = resultTo + MAX_COUNT;
                totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
            } else if ((resultTo + 1) < totalResultCount) {
                resultFrom = resultTo + 1;
                tempValue = (totalResultCount - resultTo);
                resultTo = totalResultCount;
                totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
            }
        });

        btnFullCollapse.setOnAction(event -> {
            for (AdvanceSearchFullCollapseListener listner : fullCollapseListeners) {
                listner.clickFullCollapseListener(false);
                btnFullCollapse.setVisible(false);
            }
        });

        btnFullCollapseRefresh.setOnAction(event -> {
            for (AdvanceSearchFullCollapseListener listner : fullCollapseListeners) {
                listner.clickFullCollapseListener(true);
                btnFullCollapseRefresh.setVisible(false);
            }
        });

        btnFilter.setOnAction(event -> {
            filterBy.clear();
            //get a new list of children of the root
            ObservableList parentObjects = TreeViewFacet.getRoot().getChildren();
            //loop to get the selected items.
            for (int i = 0; i < parentObjects.size(); i++) {
                CheckBoxTreeItem<String> parentItem = (CheckBoxTreeItem<String>) parentObjects.get(i);
                ObservableList childObjects = parentItem.getChildren();

                for (int i2 = 0; i2 < childObjects.size(); i2++) {
                    CheckBoxTreeItem<String> childItem = (CheckBoxTreeItem<String>) childObjects.get(i2);
                    if (childItem.isSelected())
                    {
                        filterBy.put(parentItem.getValue(), childItem.getValue().substring(0, childItem.getValue().indexOf("(")).trim());
//                        filterBy += "+"+parentItem.getValue() + ":" + childItem.getValue().substring(0, childItem.getValue().indexOf("(")).trim()+ " ";
                    }
                }
            }
//            filterBy = filterBy.trim();
//            if (filterBy.endsWith("AND"))
//            {
//                filterBy = filterBy.substring(0, filterBy.length() - 3).trim();
//            }
            totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
        });

        btnFilterReset.setOnAction(event -> {
            filterBy.clear();
            totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
        });

        comboboxSortBy.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    if (comboboxSortBy.getValue()!=null)
                    {
                        if (comboboxSortBy.getValue().toString().equals("Most Cited"))
                        {sort_by = "cited_sort INT DESC";}
                        else if (comboboxSortBy.getValue().toString().equals("Latest"))
                        {sort_by = "decisionDate_sort STRING DESC";}
                        else if (comboboxSortBy.getValue().toString().equals("Party Name (A-Z)"))
                        {sort_by = "stitle_sort STRING ASC";}
                        else if (comboboxSortBy.getValue().toString().equals("Party Name (Z-A)"))
                        {sort_by = "stitle_sort STRING DESC";}
                        else if (comboboxSortBy.getValue().toString().equals("Bench"))
                        {sort_by = "benchcounter_sort INT DESC";}
                        else if (comboboxSortBy.getValue().toString().equals("Relevent"))
                        {sort_by = "";}
                        else if (comboboxSortBy.getValue().toString().equals("Overruled"))
                        {sort_by = "whitelist_sort INT DESC";}
                    }else
                    {
                        sort_by = "";
                    }
                    totalResultCount = resultViewHelper.loadTableData(resultFrom, resultTo, data, webViewDocView, TreeViewFacet, LabelTotalRecords, search_query, sort_by, filterBy, hl_fields, totalResultCount);
                }
            }
        });

        /*Combobox on focus expand code*/
        for (final ComboBox<String> choiceBox : Arrays.asList(comboboxSortBy)) {
            final ChangeListener<? super Boolean> showHideBox = (__, ___, isFocused) ->
            {
                if (isFocused.booleanValue()) {
                    choiceBox.show();
                } else {
                    choiceBox.hide();
                }
            };
            choiceBox.focusedProperty().addListener(showHideBox);
            choiceBox.addEventFilter(MouseEvent.MOUSE_RELEASED, release ->
            {
//                release.consume();
                //choiceBox.requestFocus();
            });
        }

        /*FILTER COMBOBOX VALUE*/
        FxUtil.autoCompleteComboBox(comboboxSortBy, FxUtil.AutoCompleteMode.STARTS_WITH);
        FxUtil.getComboBoxValue(comboboxSortBy);

//        btnNext.setOnAction(event -> navigateToNext());
//        btnPrevious.setOnAction(event -> navigateToPrevious());

        data = FXCollections.observableArrayList();
        data.clear();

//        /*TABLE VIEW CLICK EVENT*/
//        TableViewResults.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            //Check whether item is selected and set value of selected item to Label
//            if (TableViewResults.getSelectionModel().getSelectedItem() != null) {
//                JudgementResultModel jrm = (JudgementResultModel) newValue;
//                     /*SET TITLE TEXT*/
//                System.out.println(jrm.getJudgementid());
////                titleText = jrm.getAppellantsRespondants();
////
////                judgementId = jrm.getJudgementid();
////                insertIntoHistory = true;
////                loadJudgementHtml(judgementId, ConstantsClass.HL_QUERY);
////
////                    /*reset highlight word counter*/
////                highLightCount = 2;
//            }
//        });

//            /*Table row double click event */
//        TableViewResults.setRowFactory(tv -> {
//            TableRow<JudgementResultModel> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                System.out.println("clicked here " + event.getClickCount());
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    System.out.println(TableViewResults.getSelectionModel().getSelectedItem());
//                    if (TableViewResults.getSelectionModel().getSelectedItem() != null) {
//                        JudgementResultModel jrm = (JudgementResultModel) TableViewResults.getSelectionModel().getSelectedItem();
//
//                        resultViewHelper.showJudgementDialogWindow(Integer.parseInt(jrm.getSrNo()), search_query, sort_by, hl_fields, filterBy);
//                    }
//                }
//            });
//            return row;
//        });

//        TableViewResults.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                // Get the table header
//                Pane header = (Pane)TableViewResults.lookup("TableHeaderRow");
//                if(header!=null && header.isVisible()) {
//                    header.setMaxHeight(0);
//                    header.setMinHeight(0);
//                    header.setPrefHeight(0);
//                    header.setVisible(false);
//                    header.setManaged(false);
//                }
//            }
//        });

//        // Table cell coloring
//        columnHN.setCellFactory(new Callback<TableColumn<JudgementResultModel, String>, TableCell<JudgementResultModel, String>>() {
//            @Override
//            public TableCell<JudgementResultModel, String> call(TableColumn<JudgementResultModel, String> param) {
//                return new TableCell<JudgementResultModel, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (!isEmpty()) {
//                            if (!item.isEmpty())
//                            {
//                                WebView webView = new WebView();
//                                WebEngine webEngine = webView.getEngine();
//                                webEngine.loadContent(item.replace("\n", "<br/>"));
//                                Tooltip tooltip = new Tooltip();
//                                tooltip.setPrefSize(700, 250);
////                                tooltip.setWrapText(true);
//                                tooltip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//                                // Set tooltip content
//                                tooltip.setGraphic(webView);
//                                tooltip.setHideOnEscape(true);
//                                tooltip.setAutoHide(true);
////                            setText(item.replaceAll("\\<[^>]*>", ""));
//                                VBox vbox = new VBox();
//                                Label lbl = new Label("HN");
////                            lbl.setAlignment(Pos.CENTER_LEFT);
//                                lbl.setStyle("-fx-font-weight:bold;-fx-text-fill: #FFFFFF;-fx-background-color: #8B0000;");
//
//                                vbox.setAlignment(Pos.CENTER_LEFT);
//                                vbox.getChildren().add(lbl);
//                                setGraphic(vbox);
////                                updateTooltipBehavior(tooltip, 1000, 10000, 50000, true);
//                                hackTooltipStartTiming(tooltip);
////                                setTooltip(tooltip);
//                                lbl.setTooltip(tooltip);
//                                setStyle("-fx-padding: 5 5 5 5;");
//                            }
//                        } else {
//                            setGraphic(null);
//                        }
//                    }
//                };
//            }
//        });
    }

//    public static void hackTooltipStartTiming(Tooltip tooltip) {
//        try {
//            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
//            fieldBehavior.setAccessible(true);
//            Object objBehavior = fieldBehavior.get(tooltip);
//
//            Field fieldTimer = objBehavior.getClass().getDeclaredField("leftTimer");
//            fieldTimer.setAccessible(true);
//            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
//
//            objTimer.getKeyFrames().clear();
//            objTimer.getKeyFrames().add(new KeyFrame(new Duration(10000)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void clearAll() {
        sort_by = "";
//        TableViewResults.getItems().clear();
        TreeViewFacet.setRoot(null);
        LabelTotalRecords.setText("");
        comboboxSortBy.getSelectionModel().clearSelection();
        comboboxSortBy.setValue(null);
//        comboboxSortBy.setDisable(true);
//        btnFirst.setDisable(true);
//        btnLast.setDisable(true);
//        btnNext.setDisable(true);
//        btnPrevious.setDisable(true);
//        btnFilter.setDisable(true);
//        btnFilterReset.setDisable(true);
        search_query="*:*";
    }
//    private void enableControls() {
//        comboboxSortBy.setDisable(false);
//        btnFilter.setDisable(false);
//        btnFilterReset.setDisable(false);
//        btnNext.setDisable(false);
//        btnPrevious.setDisable(false);
//        btnFirst.setDisable(false);
//        btnLast.setDisable(false);
//    }
}
