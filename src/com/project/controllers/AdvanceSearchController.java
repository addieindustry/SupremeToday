package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.interfaces.*;
import com.project.model.CourtModel;
import com.project.model.HistoryModel;
import com.project.utility.SearchUtility;
import com.project.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AdvanceSearchController implements Initializable {

    @FXML
    private AutoTextAutoCompleteTextField autoFillTextFreeText, autoFillTextJudge, autoFillTextActRef, autoFillTextActSectionRef;

    @FXML
    private Label labelFreeText,labSearch,labelPublisher,labelYear,labelVolume,labelPage;

    @FXML
    private SplitPane split_pane;

    @FXML
    private Button btnSearch, btnReset, btnAddAct, btnJudgeAdd, btnHistory;

    @FXML
    private ComboBox<String> comboboxPublisher, comboboxYear, comboboxVolume, comboboxPage;

    @FXML
    private DatePicker dateFrom, dateTo;

    @FXML
    private CheckBox cbHeadnote;

//    @FXML
//    private Label labelJudgeName;

    @FXML
    ListView<String> ActListView, JudgeListView;

    @FXML
    private TextField textFieldAppRes, textFieldwordWithin, textFieldCaseNo, textFieldCaseYear, textFieldAdvocates;

    @FXML
    private AnchorPane result_view_pane;

    @FXML
    private AnchorPane root;

    @FXML
    private StackPane advance_search_pane;

    @FXML
    private Hyperlink hpHelp;

    private static final List<ClickEventHandler> listeners = Lists.newArrayList();

    public void addListener(ClickEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }

    private static final List<ResetClickEventHandler> listenersReset = Lists.newArrayList();

    public void addListener(ResetClickEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listenersReset.add(listener);
    }

    /*Converting a Date Format*/
    private final String pattern = "dd-MM-yyyy";
    private ObservableList<String> listActs = FXCollections.observableArrayList();
    private HistoryModel historyModel = new HistoryModel("", "", "", "", "Advance");
    private double split_pan_length = 0.4;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public AdvanceSearchController() {

    }

    static class XCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("X");
        String lastItem;

        public XCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> getListView().getItems().remove(getItem()));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item!=null ? item : "<null>");
                setGraphic(hbox);
            }
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        split_pane.setResizableWithParent(root, false);
        split_pane.setResizableWithParent(result_view_pane, false);
        split_pane.setDividerPositions(split_pan_length);

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            textFieldwordWithin.setVisible(Boolean.FALSE);
            cbHeadnote.setVisible(Boolean.FALSE);
            btnHistory.setStyle("-fx-background-color: steelblue");
            btnAddAct.setStyle("-fx-background-color: steelblue");
            btnJudgeAdd.setStyle("-fx-background-color: steelblue");
            btnSearch.setStyle("-fx-background-color: steelblue");
            btnReset.setStyle("-fx-background-color: steelblue");
        }

        autoFillTextFreeText.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.50));
        textFieldwordWithin.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.20));

        autoFillTextActRef.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.50));
        autoFillTextActSectionRef.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.20));

        textFieldAppRes.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.50));
        autoFillTextJudge.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.50));

        comboboxPublisher.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));
        comboboxYear.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));
        comboboxVolume.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));
        comboboxPage.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));
        labelPublisher.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        labelYear.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        labelVolume.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        labelPage.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        labelFreeText.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));

        dateFrom.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));
        dateTo.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.15));
        btnSearch.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        btnReset.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        btnAddAct.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));
        btnHistory.prefWidthProperty().bind(advance_search_pane.widthProperty().multiply(0.10));

        btnSearch.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        btnReset.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        btnAddAct.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        btnHistory.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.05));
        autoFillTextFreeText.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        textFieldwordWithin.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        autoFillTextActRef.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        autoFillTextActSectionRef.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));

        textFieldCaseNo.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        textFieldCaseYear.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        textFieldAdvocates.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));

        textFieldAppRes.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        autoFillTextJudge.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        comboboxPublisher.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        comboboxYear.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        comboboxVolume.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        comboboxPage.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        dateFrom.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));
        dateTo.prefHeightProperty().bind(advance_search_pane.heightProperty().multiply(0.10));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/result_view.fxml"));
        AdvanceSearchResultViewController controller = new AdvanceSearchResultViewController();
        loader.setController(controller);

        try {
            result_view_pane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_view_pane.setVisible(false);

        btnAddAct.setOnAction(event -> {
            addToActListView();
        });

        btnHistory.setOnAction(event ->{
            showHistoryKeywordDialogWindow();
        });

        new TabViewController().clickResetOnAdvanceSearchListener(new ResetClickEventOnAdvanceSearchListener() {

            @Override
            public void clickResetListener() {
                for (ClickEventHandler listner_ : listeners) {
                    clearAllControls();
                    for (ResetClickEventHandler listner : listenersReset) {
                        listner.handleResetClick();
                    }
//                    split_pane.setResizableWithParent(root, false);
//                    split_pane.setResizableWithParent(result_view_pane, false);
                    split_pane.setDividerPositions(split_pan_length);
                }
            }
        });

        new HistoryKeywordController().applyHistoryKeywordInterface(new HistorySearchKeywordListener() {
            @Override
            public void openHistoryResultListener(String historyKeyword) {
                if (Queries.GLOBAL_OR_ADVANCE.equals("ADVANCE")){
                    autoFillTextFreeText.setText(historyKeyword);
                }
            }
        });

        new AdvanceSearchResultViewController().clickFullCollapseListener(new AdvanceSearchFullCollapseListener() {
            @Override
            public void clickFullCollapseListener() {
                split_pane.setDividerPositions(split_pan_length);
//                if (split_pane.getDividers().get(0).positionProperty().lessThan(0.01).getValue()){
//                    split_pane.setDividerPositions(0.35);
//                }else{
//                    split_pane.setDividerPositions(0.00);
//                }
            }
        });

        JudgeListView.setOrientation(Orientation.HORIZONTAL);
        JudgeListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new XCell();
            }
        });

        ActListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new XCell();
            }
        });

        btnJudgeAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String judgeName = autoFillTextJudge.getText().toString().trim();
                if (!judgeName.isEmpty()) {
                    if (!JudgeListView.getItems().contains(judgeName)) {
                        JudgeListView.getItems().add(judgeName);
                    }
                }
            }
        });

//        ActListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent click) {
//                if (click.getClickCount() == 2) {
//                    ActListView.getItems().remove(ActListView.getSelectionModel().getSelectedIndex());
//                }
//            }
//        });
//
//        JudgeListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent click) {
//                if (click.getClickCount() == 2) {
//                    JudgeListView.getItems().remove(JudgeListView.getSelectionModel().getSelectedIndex());
//                }
//            }
//        });

        new CentralActController().setJudgementSearchFromAct(new JudgementSearchFromAct() {
            @Override
            public void openAdvanceSearchView(String actTitle, String sectionTitle) {

                if (actTitle.contains(",") && Character.isDigit(actTitle.charAt(actTitle.length()-1)))
                {
                    actTitle = actTitle.substring(0, actTitle.lastIndexOf(","));
                }

                Queries.ACT_RUEL_REG=actTitle;
                Queries.AUTO_COMPLETE_TEXTFIELD="sectionref";
                autoFillTextActRef.setText(actTitle);
                autoFillTextActSectionRef.setText(sectionTitle);

                for (ClickEventHandler listner : listeners) {
                    String searchQuery = createQuery();
                    if (!searchQuery.isEmpty())
                    {
                        listner.handleSeachClick(createQuery());
                        result_view_pane.setVisible(true);
                    }
                    else
                    {new Utils().showDialogAlert("search form is blank!");}
                }
            }
        });

        new HistoryController().setHistorySearchInterface(new HistorySearchListener() {
            @Override
            public void openHistoryResultListener(HistoryModel historyModel) {
                for (ClickEventHandler listner : listeners) {
                    String searchQuery = historyModel.getQuery();
                    if (!searchQuery.isEmpty())
                    {
                        listner.handleSeachClick(searchQuery);
                        result_view_pane.setVisible(true);
                    }
                    else
                    {new Utils().showDialogAlert("search form is blank!");}
                }
            }
        });

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/result_view.fxml"));
//        try {
//            Pane newLoadedPane =  (Pane) loader.load();
//            result_view_pane.getChildren().add(newLoadedPane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // A browser.
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent
                (
                        "<h3>Hints:</h3> Type S. for Sections<br>Type O.? R.? for Order & Rules<br>Type Art. for Article<br>Type O. for Order<br>Type R. for Rule<br>Type Reg. for Regulation<br>Type Sch. for Schedule"
                );

        Tooltip tooltip = new Tooltip();
        tooltip.setPrefSize(200, 250);
        tooltip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        // Set tooltip content
        tooltip.setGraphic(webView);

        // Install tooltip for button (same as button.setTooltip)
        Tooltip.install(hpHelp, tooltip);

        comboboxPublisher.setEditable(true);
        comboboxYear.setEditable(true);
        comboboxVolume.setEditable(true);
        comboboxPage.setEditable(true);

        comboboxPublisher.setItems(ServiceHelper.getAllPublishers());
        FxUtil.autoCompleteComboBox(comboboxPublisher, FxUtil.AutoCompleteMode.STARTS_WITH);
        FxUtil.getComboBoxValue(comboboxPublisher);

        comboboxPublisher.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    comboboxYear.setItems(ServiceHelper.getAllYearByPublisher(newValue));
                    FxUtil.autoCompleteComboBox(comboboxYear, FxUtil.AutoCompleteMode.STARTS_WITH);
                    FxUtil.getComboBoxValue(comboboxYear);
                }
                else{
                    comboboxYear.getItems().clear();
                    comboboxVolume.getItems().clear();
                    comboboxPage.getItems().clear();
                }
            }
        });

        comboboxYear.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    if (!comboboxPublisher.getSelectionModel().getSelectedItem().isEmpty())
                    {
                        comboboxVolume.setItems(ServiceHelper.getAllVolumeByPublisherYear(comboboxPublisher.getValue().toString(), newValue));
                        FxUtil.autoCompleteComboBox(comboboxVolume, FxUtil.AutoCompleteMode.STARTS_WITH);
                        FxUtil.getComboBoxValue(comboboxVolume);
                    }
                }
                else
                {
                    comboboxVolume.getItems().clear();
                    comboboxPage.getItems().clear();
                }
            }
        });

        comboboxVolume.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    if ((!comboboxPublisher.getSelectionModel().getSelectedItem().isEmpty()) && (!comboboxYear.getSelectionModel().getSelectedItem().isEmpty()))
                    {
                        comboboxPage.setItems(ServiceHelper.getAllPageByPublisherYearVolume(comboboxPublisher.getValue().toString(), comboboxYear.getValue().toString(), newValue));
                        FxUtil.autoCompleteComboBox(comboboxPage, FxUtil.AutoCompleteMode.STARTS_WITH);
                        FxUtil.getComboBoxValue(comboboxPage);
                    }
                }
                else{
                    comboboxPage.getItems().clear();
                }
            }
        });

//        comboboxPage.valueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                System.out.println(newValue);
//                if (newValue != null) {
//                    pageno = newValue;
//                }
//            }
//        });

        /*Combobox on focus expand code*/
        for (final ComboBox<String> choiceBox : Arrays.asList(comboboxPublisher, comboboxYear, comboboxVolume, comboboxVolume, comboboxPage)) {
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

        new JudgementViewController().setSearchFromWebViewKeyword(new SearchFromWebViewEventHandler() {
            @Override
            public void handleSearchKeyword(String searchKeywords) {
                    if (Queries.GLOBAL_OR_ADVANCE.equals("ADVANCE")) {
                    autoFillTextFreeText.setText(searchKeywords);
                    for (ClickEventHandler listner : listeners) {
                        String searchQuery = createQuery();
                        if (!searchQuery.isEmpty()) {
                            listner.handleSeachClick(createQuery());
                            result_view_pane.setVisible(true);
                        } else {
                            new Utils().showDialogAlert("search form is blank!");
                        }
                    }
                }
            }
        });

        new JudgementViewController().setSearchInSearchFromWebViewKeyword(new SearchInSearchFromWebViewEventHandler() {
            @Override
            public void handleSearchKeyword(String searchKeywords) {
                if (Queries.GLOBAL_OR_ADVANCE.equals("ADVANCE")){
                    String temp = autoFillTextFreeText.getText();
                    if (temp.trim().length()>0){
                        autoFillTextFreeText.setText(temp.trim() + " " + searchKeywords);
                    }else{
                        autoFillTextFreeText.setText(searchKeywords);
                    }
                    for (ClickEventHandler listner : listeners) {
                        String searchQuery = createQuery();
                        if (!searchQuery.isEmpty())
                        {
                            listner.handleSeachClick(createQuery());
                            result_view_pane.setVisible(true);
                        }
                        else
                        {new Utils().showDialogAlert("search form is blank!");}
                    }
                }
            }
        });


//        /*FILTER COMBOBOX VALUE*/
//        FxUtil.autoCompleteComboBox(comboboxPublisher, FxUtil.AutoCompleteMode.STARTS_WITH);
//        FxUtil.getComboBoxValue(comboboxPublisher);
//
//        FxUtil.autoCompleteComboBox(comboboxYear, FxUtil.AutoCompleteMode.STARTS_WITH);
//        FxUtil.getComboBoxValue(comboboxYear);
//
//        FxUtil.autoCompleteComboBox(comboboxVolume, FxUtil.AutoCompleteMode.STARTS_WITH);
//        FxUtil.getComboBoxValue(comboboxVolume);
//
//        FxUtil.autoCompleteComboBox(comboboxPage, FxUtil.AutoCompleteMode.STARTS_WITH);
//        FxUtil.getComboBoxValue(comboboxPage);

//        new AutoCompleteComboBoxListener<>(comboboxPublisher);
//        new AutoCompleteComboBoxListener<>(comboboxYear);
//        new AutoCompleteComboBoxListener<>(comboboxVolume);
//        new AutoCompleteComboBoxListener<>(comboboxPage);

        //Search button click event
        btnSearch.setOnAction(event -> {
            labSearch.setText("Please wait, Loading...");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                            // your code here
                                for (ClickEventHandler listner : listeners) {
                                    String searchQuery = createQuery();
                                    if (!searchQuery.isEmpty())
                                    {
                                        ServiceHelper.setHistory(historyModel);
                                        listner.handleSeachClick(searchQuery);
                                        result_view_pane.setVisible(true);
                                        split_pane.setDividerPositions(0.00);
                                    }else{new Utils().showDialogAlert("search form is blank!");}
                                }
                                labSearch.setText("");
//                                if (split_pane.getDividers().get(0).positionProperty().lessThan(0.01).getValue()){
//                                    split_pane.setDividerPositions(0.35);
//                                }else{
//                                    split_pane.setDividerPositions(0.00);
//                                }
                            });
                        }
                    },
                    500
            );
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    for (ClickEventHandler listner : listeners) {
//                        String searchQuery = createQuery();
//                        if (!searchQuery.isEmpty())
//                        {
//                            ServiceHelper.setHistory(historyModel);
//                            listner.handleSeachClick(searchQuery);
//                            result_view_pane.setVisible(true);
//                        }
//                        else
//                        {new Utils().showDialogAlert("search form is blank!");}
//                    }
//                    labSearch.setText("");
//                }
//            });
        });

        //Reset button click event
        btnReset.setOnAction(event ->{
            clearAllControls();
            for (ResetClickEventHandler listner : listenersReset) {
                listner.handleResetClick();
            }
        });

        /*Converting a Date Format*/
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        /*Converting a Date Format*/
        dateFrom.setConverter(converter);
        dateFrom.setPromptText(pattern.toLowerCase());
        dateTo.setConverter(converter);
        dateTo.setPromptText(pattern.toLowerCase());

        // DATE CHANGE EVENT
        dateFrom.setOnAction(e -> {
        });

        dateTo.setOnAction(event -> {
//            getCount();
        });

//        //Setting a particular date value by using the setValue method
//        dateFrom.setValue(LocalDate.of(1950, 1, 1));
//        //Setting the current date
//        dateTo.setValue(LocalDate.now());
//
//        dateFrom.setValue(null);
//        autoFillTextJudge.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
////                getCount();
//            }
//        });

        autoFillTextJudge.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="judge";
            }
        });

        autoFillTextActRef.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="actref";
                Queries.ACT_RUEL_REG = autoFillTextActRef.getText().trim();
            }
        });

        autoFillTextActSectionRef.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="sectionref";
            }
        });

        autoFillTextFreeText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="fulltext";
            }
        });

    }

    private void addToActListView() {

        String actReferred = autoFillTextActRef.getText();
        String actSectionReferedAct = autoFillTextActSectionRef.getText();

        /*Clear Act Referred on click of ADD button*/
        autoFillTextActSectionRef.clear();

        if (!actReferred.isEmpty() || !actSectionReferedAct.isEmpty()) {
            String listStr = actReferred + " " + actSectionReferedAct;
//            String listStrForHTMLHighlight = actReferred + " | " + actSectionReferedAct;
            if (!listActs.contains(listStr)) {
                listActs.add(listStr);
            }
        }
        ActListView.setItems(listActs);
    }

//    List<String> listJudgeNames= new ArrayList<>();

//    private void setJudgeName(String judgeName) {
//        if (!listJudgeNames.contains(judgeName)) {
//            listJudgeNames.add(judgeName);
//
//            String str = "";
//            for (String name : listJudgeNames) {
//                if (!str.isEmpty()) {
//                    str = str + ",";
//                }
//                str = str + name;
//            }
//            labelJudgeName.setText(str);
//
//            /*call to load records count*/
////            getCount();
//        }
//    }

    /*dd/MM/yyyy TO yyyyMMDD*/
    private String convertDate(String date) {
//        System.out.println(date);
        return date.split("-")[0] + date.split("-")[1] + date.split("-")[2];
    }

    private String createQuery() {
        String freeText = autoFillTextFreeText.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String wordWithin = textFieldwordWithin.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String judge =autoFillTextJudge.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String multipleJudge = "";
//        String multipleJudge = labelJudgeName.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ");
        String actRef =autoFillTextActRef.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String actSectionRef =autoFillTextActSectionRef.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String appRes = textFieldAppRes.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").replace(".", ". ").replace("  ", " ").trim();

        String caseNo = textFieldCaseNo.getText();
        String caseYear = textFieldCaseYear.getText();
        String advocates = textFieldAdvocates.getText();

        String fromDate = "";
        String toDate = "";
        if ((dateFrom.getValue()!=null) && (dateTo.getValue()!=null))
        {
            fromDate = dateFrom.getValue().getYear() + "-" + String.format("%02d", dateFrom.getValue().getMonthValue()) + "-" + dateFrom.getValue().getDayOfMonth();
            toDate = dateTo.getValue().getYear() + "-" + String.format("%02d", dateTo.getValue().getMonthValue()) + "-" + dateTo.getValue().getDayOfMonth();
        }
//        if (dateFrom.getValue()!=null)
//        {fromDate = convertDate(dateFrom.getValue().toString());}
//        if (dateFrom.getValue()!=null)
//        {toDate = convertDate(dateTo.getValue().toString());}

        String query = "";
        String query_title = "";

        String content_query = "";
        String content_or_headnote_field = "";
        if (!freeText.isEmpty()) {
            if (!wordWithin.isEmpty()) {
                content_query = content_query + "(\"" + freeText + "\"~" + wordWithin + ") ";
                query_title += freeText + "~" + wordWithin;
            }else if (freeText.indexOf(8220)>-1 || freeText.indexOf(8221)>-1 || freeText.indexOf(34)>-1 || freeText.contains("~")){
                content_query = content_query + "(" + freeText + ") ";
                query_title += freeText;
            }else{
                content_query = content_query + "+((\""+freeText+"\"~10^25000) OR (\""+freeText+"\"~25^25000) OR (\""+freeText+"\"~100^12000) OR +("+freeText.replace(" ", " AND ") +"))";
                query_title += freeText.replace(" ", " AND ");
            }
            historyModel.setKeyword(freeText);
        }
//        if (!freeText.isEmpty()){
//            if (!freeText.contains("~")){
//                if (!wordWithin.isEmpty()){
//                    freeText = freeText.replace("\"", "");
//                    content_query = "(\"" + freeText + "\"~" + wordWithin + ") ";
//                    query_title += "Free Text : \"" + freeText + "\"~" + wordWithin;
//                }else if (freeText.indexOf(8220)>-1 || freeText.indexOf(8221)>-1 || freeText.indexOf(34)>-1){
//                    content_query = "(" + freeText + ") ";
//                    query_title += "Free Text : " + freeText;
//                }else{
//                    content_query = "((\""+freeText+"\"~10^25000) OR (\""+freeText+"\"~25^25000) OR (\""+freeText+"\"~100^12000) OR +("+freeText.replace(" ", " AND ") +"))";
//                    query_title += "Free Text : " + freeText.replace(" ", " AND ");
//                }
//            }else{
//                freeText = freeText.replace("\"", "");
//                freeText = freeText.replace(" ~", "~").replace("~ ", "~");
//                freeText = freeText.replaceAll("(~[0-9]+ )", "$1 AND \"");
//                freeText = "\"" + freeText.replace("~", "\"~");
//
//                content_query = "(" + freeText + ") ";
//                query_title += "Free Text : " + freeText;
//            }
//            historyModel.setKeyword(freeText);
//        }


        if (!content_query.isEmpty()){
            if (cbHeadnote.isSelected()) {
                query = query + Queries.OPEN_BRACKET + SearchUtility.getSingleFieldSearchQuery(content_query, Queries.LUCENEFIELD_SUMMARY)+ Queries.CLOSE_BRACKET;
            }else{
                query = query + Queries.OPEN_BRACKET + SearchUtility.getMultiSearchQuery(content_query) + Queries.CLOSE_BRACKET;
            }
        }

        String pubCitation = "";
        if (comboboxPublisher.getSelectionModel().getSelectedItem() != null) {
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                pubCitation = pubCitation + comboboxPublisher.getValue().toString().replace("ICLF", "Supreme") + " ";
            }else{
                pubCitation = pubCitation + comboboxPublisher.getValue().toString() + " ";
            }
        }
        if (comboboxYear.getSelectionModel().getSelectedItem() != null) {
            pubCitation = pubCitation + comboboxYear.getValue().toString() + " ";
        }
        if (comboboxVolume.getSelectionModel().getSelectedItem() != null) {
            pubCitation = pubCitation + comboboxVolume.getValue().toString() + " ";
        }
        if (comboboxPage.getSelectionModel().getSelectedItem() != null) {
            pubCitation = pubCitation + comboboxPage.getValue().toString() + " ";
        }

        pubCitation = pubCitation.trim();
        if (!pubCitation.isEmpty()) {
            if (!query.isEmpty())
                query = query + Queries.AND;
            query = query + " +" + Queries.LUCENEFIELD_CITATION + ":\"" + pubCitation + "\" ";
            query_title += " Citation : " + pubCitation;
        }

        if (!judge.isEmpty()) {
            if (!multipleJudge.contains(judge))
            {
                if (!query.isEmpty())
                    query = query + Queries.AND;

                query_title += " Judge : " + judge;
                query = query + "+(" + Queries.LUCENEFIELD_JUDGE + ":\"" + judge + "\") ";
            }
        }

        if (!caseNo.isEmpty() && !caseYear.isEmpty()) {
            query_title += " Case No / Year : " + caseNo;
            query = query + "+" + Queries.LUCENEFIELD_JUDGEMENTHEADER + ":\"" + caseNo + " " + caseYear + "\"~5 ";
        }else if (!caseNo.isEmpty()) {
            query_title += " Case No : " + caseNo;
            query = query + "+" + Queries.LUCENEFIELD_JUDGEMENTHEADER + ":\"" + caseNo + "\" ";
        }else if (!caseYear.isEmpty()) {
            query_title += " Case Year : " + caseYear;
            query = query + "+" + Queries.LUCENEFIELD_JUDGEMENTHEADER + ":\"" + caseYear + "\" ";
        }


//        if (!caseNo.isEmpty()) {
//            query_title += " Case No : " + caseNo;
//            query = query + "+" + Queries.LUCENEFIELD_JUDGEMENTHEADER + ":\"" + caseNo + "\" ";
//        }
//
//        if (!caseYear.isEmpty()) {
//            query_title += " Case Year : " + caseYear;
//            query = query + "+" + Queries.LUCENEFIELD_JUDGEMENTHEADER + ":\"" + caseYear + "\" ";
//        }

        if (!advocates.isEmpty()) {
            query_title += " Advocates : " + advocates;
            query = query + "+" + Queries.LUCENEFIELD_ADVOCATES + ":\"" + advocates + "\" ";
        }

        if (!multipleJudge.isEmpty()) {
            query_title += "judge:" + multipleJudge + " ";
            if (multipleJudge.contains(","))
            {
                for (String singleJudge : multipleJudge.split(",")) {
                    if (!query.isEmpty())
                        query = query + Queries.AND;

//                    query = query + ConstantsClass.OPEN_BRACKET + ConstantsClass.OPEN_BRACKET + ConstantsClass.JUDGE + ConstantsClass.COLON + "\"" + singleJudge + "\"" + ConstantsClass.CLOSE_BRACKET + ConstantsClass.OR + ConstantsClass.OPEN_BRACKET + ConstantsClass.JUDGE + ConstantsClass.COLON + "\"" + singleJudge + "\"" + "~0.7" + ConstantsClass.CLOSE_BRACKET + ConstantsClass.CLOSE_BRACKET;
                    query += Queries.OPEN_BRACKET + Queries.LUCENEFIELD_JUDGE + Queries.COLON + "\"" + singleJudge + "\"" + Queries.CLOSE_BRACKET;
                }
            }
            else
            {
                if (!query.isEmpty())
                    query = query + Queries.AND;

//                query = query + ConstantsClass.OPEN_BRACKET + ConstantsClass.OPEN_BRACKET + ConstantsClass.JUDGE + ConstantsClass.COLON + "\"" + judge + "\"" + ConstantsClass.CLOSE_BRACKET + ConstantsClass.OR + ConstantsClass.OPEN_BRACKET + ConstantsClass.JUDGE + ConstantsClass.COLON + "\"" + judge + "\"" + "~0.7" + ConstantsClass.CLOSE_BRACKET + ConstantsClass.CLOSE_BRACKET;
                query += Queries.OPEN_BRACKET + Queries.LUCENEFIELD_JUDGE + Queries.COLON + "\"" + judge + "\"" + Queries.CLOSE_BRACKET;
            }
        }

        if (!appRes.isEmpty()) {
            if (!query.isEmpty())
                query = query + Queries.AND;

            query = query + " +("+ Queries.LUCENEFIELD_TITLE +":\"" + appRes + "\"~15) ";
            query_title += " Party Name : " + appRes;
        }

        /*Act section list view query*/
        if (!listActs.isEmpty()) {
            if (!query.isEmpty())
                query = query + Queries.AND;

            String qry = "";
            for (String str : listActs) {
                if (!qry.isEmpty())
                    qry = qry + Queries.AND;

//                qry = qry + Queries.OPEN_BRACKET + "+" + Queries.LUCENEFIELD_ACTS + Queries.COLON + "\"" + str + "\"" + Queries.CLOSE_BRACKET;
                qry = qry + "+" + Queries.LUCENEFIELD_ACTS + Queries.COLON + "\"" + str.trim() + "\"";
            }
            if (!qry.isEmpty()) {
                query += qry;
                query_title += qry + Queries.SEPERATOR;
            }

//            if (!qry.isEmpty()) {
//                query += Queries.OPEN_BRACKET + qry + Queries.CLOSE_BRACKET;
//                query_title += Queries.OPEN_BRACKET + qry + Queries.CLOSE_BRACKET + Queries.SEPERATOR;
//            }
        }

        if ((!actRef.isEmpty()) && (!actSectionRef.isEmpty())) {
            query = query + " +" + Queries.LUCENEFIELD_ACTS + ":\"" + actRef.trim() + " " + actSectionRef.trim() + "\" ";
            query_title += " Act/Section refer : " + actRef.trim() + " " + actSectionRef.trim();
        }
        else if (!actRef.isEmpty()){
            query = query + " +" + Queries.LUCENEFIELD_ACTS + ":\"" + actRef.trim() + "\" ";
            query_title += " Act/Section refer : " + actRef.trim();
        }

        /*FORM TO DATE*/
        //(jdate:[19500101 TO 20160419]))

        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            if (!query.isEmpty())
                query = query + Queries.AND;

            query = query + Queries.OPEN_BRACKET + Queries.LUCENEFIELD_DECISIONDATE + Queries.COLON + "[" + fromDate + Queries.TO + toDate + "]" + Queries.CLOSE_BRACKET;
        }
        System.out.println(query);
        historyModel.setQuery(query);
        historyModel.setTitle(query_title);
        return query.trim();
    }

    private boolean showHistoryKeywordDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_history_keyword.fxml"));
            HistoryKeywordController controller = new HistoryKeywordController();
            loader.setController(controller);

            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Searched Keywords");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
//            HistoryKeywordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
//            controller.setPerson(person);

               /*Close window on Escap key press*/
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent evt) {
                    if (evt.getCode().equals(KeyCode.ESCAPE)) {
                        dialogStage.close();
                    }
                }
            });

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (!controller.isOkClicked()) {

            }

            return controller.isOkClicked();
//            return true;
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return true;
        }
    }

    private void clearAllControls() {
        result_view_pane.setVisible(false);
        /*Text Field clear code*/
        autoFillTextFreeText.clear();
        autoFillTextJudge.clear();
        autoFillTextActRef.clear();
        autoFillTextActSectionRef.clear();
        textFieldAppRes.clear();
        textFieldwordWithin.clear();
        textFieldCaseNo.clear();
        textFieldCaseYear.clear();
        textFieldAdvocates.clear();
        cbHeadnote.setSelected(false);

        /*Combobox Reset code*/
        comboboxPublisher.getSelectionModel().clearSelection();
        comboboxPublisher.setValue(null);

        comboboxPublisher.setItems(ServiceHelper.getAllPublishers());
        FxUtil.autoCompleteComboBox(comboboxPublisher, FxUtil.AutoCompleteMode.STARTS_WITH);
        FxUtil.getComboBoxValue(comboboxPublisher);

//        comboboxPublisher.getSelectionModel().selectFirst();
        comboboxYear.getSelectionModel().clearSelection();
        comboboxVolume.getSelectionModel().clearSelection();
        comboboxPage.getSelectionModel().clearSelection();

        dateFrom.setValue(null);
        dateTo.setValue(null);

        ActListView.getItems().clear();
        JudgeListView.getItems().clear();

//        //Reset the date From & To
//        //Setting a particular date value by using the setValue method
//        dateFrom.setValue(LocalDate.of(1950, 1, 1));
//        //Setting the current date
//        dateTo.setValue(LocalDate.now());
    }

    private void showWaitCursor(Node node){
        node.getScene().getRoot().setCursor(Cursor.WAIT);
        node.setCursor(Cursor.WAIT);
    }

    private void showDefaultCursor(Node node){
        node.getScene().getRoot().setCursor(Cursor.DEFAULT);
        node.setCursor(Cursor.DEFAULT);
    }
}
