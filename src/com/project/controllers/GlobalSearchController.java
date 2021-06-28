package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.interfaces.*;
import com.project.model.HistoryModel;
import com.project.utility.SearchUtility;
import com.project.utils.AutoTextAutoCompleteTextField;
import com.project.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GlobalSearchController implements Initializable {

    @FXML
    private AutoTextAutoCompleteTextField autoFillTextAllWords, autoFillTextAnyWords, autoFillTextNoneWords;

    @FXML
    private TextField textFieldwordWithin;

    @FXML
    private SplitPane split_pane;

    @FXML
    private Label labSearch, lbSearchInLabel;

    @FXML
    private RadioButton rbJudgementText, rbHeadnote;

    @FXML
    private Button btnSearch, btnReset, btnHistory;

    @FXML
    private AnchorPane result_view_pane;

    @FXML
    private AnchorPane root;

    @FXML
    private StackPane global_search_pane;

    private HistoryModel historyModel = new HistoryModel("", "", "", "", "Global");
    private double split_pan_length = 0.2;

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

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public GlobalSearchController() {

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
            autoFillTextAnyWords.setVisible(Boolean.FALSE);
            autoFillTextNoneWords.setVisible(Boolean.FALSE);
            rbJudgementText.setVisible(Boolean.FALSE);
            rbHeadnote.setVisible(Boolean.FALSE);
            lbSearchInLabel.setVisible(Boolean.FALSE);
            btnHistory.setStyle("-fx-background-color: steelblue");
            btnSearch.setStyle("-fx-background-color: steelblue");
            btnReset.setStyle("-fx-background-color: steelblue");
        }


//        System.out.println(global_search_pane.heightProperty().doubleValue());
//        split_pane.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                double height = split_pane.getHeight();
//                double dividerPositionFromTop = 100;
//                split_pane.setDividerPosition(0, dividerPositionFromTop/height);
//            }
//        });

//        split_pane.setDividerPosition(0, global_search_pane.heightProperty().doubleValue());
//        split_pane.setDividerPosition(1, root.heightProperty().multiply(0.75).doubleValue());

        autoFillTextAllWords.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.50));
        textFieldwordWithin.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.20));
        autoFillTextAnyWords.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.20));
        autoFillTextNoneWords.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.20));

        btnSearch.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.10));
        btnReset.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.10));
        btnHistory.prefWidthProperty().bind(global_search_pane.widthProperty().multiply(0.10));

        btnSearch.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));
        btnReset.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));
        btnHistory.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));
        autoFillTextAllWords.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));
        textFieldwordWithin.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));
        autoFillTextAnyWords.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));
        autoFillTextNoneWords.prefHeightProperty().bind(global_search_pane.heightProperty().multiply(0.10));



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/result_view.fxml"));
        GlobalSearchResultViewController controller = new GlobalSearchResultViewController();
        loader.setController(controller);

        try {
            result_view_pane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_view_pane.setVisible(false);

        autoFillTextAllWords.setOnAction(e -> {
            LoadSearchResults();
        });
        textFieldwordWithin.setOnAction(e -> {
            LoadSearchResults();
        });
        autoFillTextNoneWords.setOnAction(e -> {
            LoadSearchResults();
        });

        // force the field to be numeric only
        textFieldwordWithin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldwordWithin.setText(newValue.replaceAll("[^\\d]", ""));
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
//                btnSearch.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });

        new TabViewController().clickResetOnGlobalSearchListener(new ResetClickEventOnGlobalSearchListener() {
            @Override
            public void clickResetListener() {
                for (ClickEventHandler listner_ : listeners) {
                    clearAllControls();
                    for (ResetClickEventHandler listner : listenersReset) {
                        listner.handleResetClick();
                    }
                }
            }
        });

        new HistoryKeywordController().applyHistoryKeywordInterface(new HistorySearchKeywordListener() {
            @Override
            public void openHistoryResultListener(String historyKeyword) {
                if (Queries.GLOBAL_OR_ADVANCE.equals("GLOBAL")){
                    autoFillTextAllWords.setText(historyKeyword);
                }
            }
        });

        new WordPhrasesController().setJudgementSearchFromAct(new JudgementSearchFromAct() {
            @Override
            public void openAdvanceSearchView(String fullText, String sectionTitle) {
                autoFillTextAllWords.setText(fullText);
//                autoFillTextActRef.setText(actTitle);
//                autoFillTextActSectionRef.setText(sectionTitle);

                for (ClickEventHandler listner : listeners) {
//                btnSearch.getScene().setCursor(Cursor.WAIT);
//                btnSearch.setCursor(Cursor.WAIT);
                    String searchQuery = createQuery();
                    if (!searchQuery.isEmpty())
                    {
                        listner.handleSeachClick(createQuery());
                        result_view_pane.setVisible(true);
                    }
                    else
                    {new Utils().showDialogAlert("search form is blank!");}
//                btnSearch.getScene().setCursor(Cursor.DEFAULT);
                }

            }
        });

        new JudgementViewController().setSearchFromWebViewKeyword(new SearchFromWebViewEventHandler() {
            @Override
            public void handleSearchKeyword(String searchKeywords) {
                if (Queries.GLOBAL_OR_ADVANCE.equals("GLOBAL") || Queries.GLOBAL_OR_ADVANCE.equals("YEAR")) {
                    autoFillTextAllWords.setText(searchKeywords);
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
                if (Queries.GLOBAL_OR_ADVANCE.equals("GLOBAL")){
                    String temp = autoFillTextAllWords.getText();
                    if (temp.trim().length()>0){
                        autoFillTextAllWords.setText(temp.trim() + " " + searchKeywords);
                    }else{
                        autoFillTextAllWords.setText(searchKeywords);
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


//        btnSearch.setOnMouseEntered(new EventHandler() {
//            @Override
//            public void handle(Event event) {
//                btnSearch.setCursor(Cursor.HAND);
//            }
//        });
//
//        btnSearch.setOnMouseExited(new EventHandler() {
//            @Override
//            public void handle(Event event) {
//                btnSearch.setCursor(Cursor.HAND);
//            }
//        });

        //Search button click event
        btnSearch.setOnAction(event -> {
            LoadSearchResults();
        });

        btnHistory.setOnAction(event ->{
            showHistoryKeywordDialogWindow();
        });

        //Reset button click event
        btnReset.setOnAction(event ->{
            clearAllControls();
            for (ResetClickEventHandler listner : listenersReset) {
                listner.handleResetClick();
            }
        });

        autoFillTextAllWords.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="fulltext";
            }
        });
        autoFillTextAnyWords.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="fulltext";
            }
        });
        autoFillTextNoneWords.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Queries.AUTO_COMPLETE_TEXTFIELD="fulltext";
            }
        });

        new GlobalSearchResultViewController().clickFullCollapseListener(new GlobalSearchFullCollapseListener() {
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


//        autoFillTextJudge.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
////                getCount();
//            }
//        });
//        autoFillTextJudge.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
//                if (!newPropertyValue) {
//                    /*Textfield out focus*/
////                    getCount();
//                }
//            }
//        });

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
                System.out.println("CLICKED");
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

    private String createQuery() {
//        String content_or_headnote_field = "";
//        if (rbJudgementText.isSelected())
//        {content_or_headnote_field = "";}
//        else{content_or_headnote_field = "+" + Queries.LUCENEFIELD_SUMMARY + ":";}

        String allWords = autoFillTextAllWords.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String anyWords = autoFillTextAnyWords.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String noneWords = autoFillTextNoneWords.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();
        String wordWithin = textFieldwordWithin.getText().replace("/", " ").replace("(", " ").replace(")", " ").replace("'", " ").replace(":", " ").trim();

        String query = "";
        String query_title = "";

        if (!allWords.isEmpty()) {
            if (!wordWithin.isEmpty()) {
                query = query + "(\"" + allWords + "\"~" + wordWithin + ") ";
                query_title += allWords + "~" + wordWithin;
            }else if (allWords.indexOf(8220)>-1 || allWords.indexOf(8221)>-1 || allWords.indexOf(34)>-1 || allWords.contains("~")){
                query = query + "(" + allWords + ") ";
                query_title += allWords;
            }else{
                query = query + "((\""+allWords+"\"~10^25000) OR (\""+allWords+"\"~25^25000) OR (\""+allWords+"\"~100^12000) OR +("+allWords.replace(" ", " AND ") +"))";
                query_title += allWords.replace(" ", " AND ");
            }
            historyModel.setKeyword(allWords);
        }

//        if (!allWords.isEmpty()) {
//            if (!allWords.contains("~")){
//                if (!wordWithin.isEmpty()) {
//                    query = query + content_or_headnote_field + "(\"" + allWords + "\"~" + wordWithin + ") ";
//                    query_title += content_or_headnote_field + " : " + allWords + "~" + wordWithin;
//                }else if (allWords.indexOf(8220)>-1 || allWords.indexOf(8221)>-1 || allWords.indexOf(34)>-1){
//                    query = query + content_or_headnote_field + "(" + allWords + ") ";
//                    query_title += content_or_headnote_field + " : " + allWords;
//                }else{
//                    query = query + content_or_headnote_field + "((\""+allWords+"\"~10^25000) OR (\""+allWords+"\"~25^25000) OR (\""+allWords+"\"~100^12000) OR +("+allWords.replace(" ", " AND ") +"))";
//                    query_title += content_or_headnote_field + " : " + allWords.replace(" ", " AND ");
//                }
//            }else{
//                allWords = allWords.replace("\"", "");
//                allWords = allWords.replace(" ~", "~").replace("~ ", "~");
//                allWords = allWords.replaceAll("(~[0-9]+ )", "$1 AND \"");
//                allWords = "\"" + allWords.replace("~", "\"~");
//
//                query = query + content_or_headnote_field + "(" + allWords + ") ";
//                query_title += content_or_headnote_field + " : " + allWords;
//            }
//            historyModel.setKeyword(allWords);
//        }

        if (!anyWords.isEmpty()) {
            query = query + "(" + anyWords.replace(" ", " OR ") + ") ";
            query_title += anyWords.replace(" ", " OR ");
        }

        if (!noneWords.isEmpty()) {
            query = query + "-" + "(" + noneWords + ") ";
//            query = query + "-" + content_or_headnote_field + ":(" + noneWords + ") ";
            query_title += " Not in " + noneWords;
        }

        if (rbJudgementText.isSelected()){
            query = SearchUtility.getMultiSearchQuery(query);}
        else{
            query = SearchUtility.getSingleFieldSearchQuery(query, Queries.LUCENEFIELD_SUMMARY);
        }
        historyModel.setQuery(query);
        historyModel.setTitle(query_title);
        return query.trim();
    }

    private void clearAllControls() {
        /*Text Field clear code*/
        result_view_pane.setVisible(false);
        textFieldwordWithin.clear();
        autoFillTextAllWords.clear();
        autoFillTextAnyWords.clear();
        autoFillTextNoneWords.clear();
        rbJudgementText.setSelected(true);
//        rbHeadnote.setSelected(false);
    }

    private void showWaitCursor(Node node){
        labSearch.setText("Please wait, Loading...");
        node.getScene().getRoot().setCursor(Cursor.WAIT);
        node.setCursor(Cursor.WAIT);
    }

    private void showDefaultCursor(Node node){
        labSearch.setText("");
        node.getScene().getRoot().setCursor(Cursor.DEFAULT);
        node.setCursor(Cursor.DEFAULT);
    }

    private void LoadSearchResults(){
        showWaitCursor(btnSearch);
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
                            showDefaultCursor(btnSearch);
                        });
                    }
                },
                500
        );


//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                for (ClickEventHandler listner : listeners) {
//                    String searchQuery = createQuery();
//                    if (!searchQuery.isEmpty())
//                    {
//                        ServiceHelper.setHistory(historyModel);
//                        listner.handleSeachClick(searchQuery);
//                        result_view_pane.setVisible(true);
//                    }else{new Utils().showDialogAlert("search form is blank!");}
//                }
//                showDefaultCursor(btnSearch);
//            }
//        });
    }

}
