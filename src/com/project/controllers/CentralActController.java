package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.JavaHelper;
import com.project.WebEventDispatcher;
import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.interfaces.JudgementSearchFromAct;
import com.project.interfaces.SearchFromWebViewEventHandler;
import com.project.interfaces.SearchInSearchFromWebViewEventHandler;
import com.project.model.TitleIdListModel;
import com.project.utils.FxUtil;
import com.project.utils.PTableColumn;
import com.project.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CentralActController implements Initializable {

    @FXML
    private Hyperlink hpJudgementResults;

    @FXML
    private Button btnFullAct, btnZoomIn, btnZoomOut, btnMail, btnPrint, btnIndexView;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TableView tableTitleList;

    @FXML
    private PTableColumn columnId, columnTitle;

    @FXML
    private WebView webViewContent;
    private WebEngine engine;

    @FXML
    private AnchorPane result_view_pane;

    @FXML
    private AnchorPane root;

    @FXML
    private SplitPane split_pane;

    @FXML
    private ComboBox comboboxType;

    private ObservableList<TitleIdListModel> tableData = FXCollections.observableArrayList();
    private String currentCentralActId = "";
    private String currentCentralActTitle = "";
    private String currentCentralActSectionTitle = "";
    private boolean _is_index_view = false;

    //    private String javaScript = "<script language='JavaScript'> function keyup(){if (window.getSelection) {if (window.getSelection().empty) {window.getSelection().empty();} else if (window.getSelection().removeAllRanges) {window.getSelection().removeAllRanges();}} else if (document.selection) {document.selection.empty();}}</script>";
    private String javaScript = "";
    private String htmlContent = "";
    private double split_pan_length = 0.3;

    /*INTERFACE FOR judgement search*/
    private static final List<JudgementSearchFromAct> JudgementSearchInterfaceList = Lists.newArrayList();

    public void setJudgementSearchFromAct(JudgementSearchFromAct judgementSearchListener) {
        Preconditions.checkNotNull(judgementSearchListener);
        JudgementSearchInterfaceList.add(judgementSearchListener);
    }
//    private ObservableList<DictionaryTitleModel> data;
//    private ObservableList<String> entries = FXCollections.observableArrayList();

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public CentralActController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        engine = webViewContent.getEngine();

        split_pane.setResizableWithParent(root, false);
        split_pane.setResizableWithParent(result_view_pane, false);
        split_pane.setDividerPositions(split_pan_length);

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            btnFullAct.setStyle("-fx-background-color: steelblue");
            btnZoomIn.setStyle("-fx-background-color: steelblue");
            btnZoomOut.setStyle("-fx-background-color: steelblue");
            btnMail.setStyle("-fx-background-color: steelblue");
            btnPrint.setStyle("-fx-background-color: steelblue");
            btnIndexView.setStyle("-fx-background-color: steelblue");
        }


//        webViewContent.setContextMenuEnabled(false);

//        root.prefWidthProperty().bind(split_pane.widthProperty().multiply(0.70));


//        webViewContent.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
//        engine.setJavaScriptEnabled(true);

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

        loadData();

           /*Hyperlink click events*/
        hpJudgementResults.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Queries.HIDE_AUTOSUGGET = true;
                for (JudgementSearchFromAct listner : JudgementSearchInterfaceList) {
                    listner.openAdvanceSearchView(currentCentralActTitle, currentCentralActSectionTitle);
                }
            }
        });

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
                Pane header = (Pane) tableTitleList.lookup("TableHeaderRow");
                if (header != null && header.isVisible()) {
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
                currentCentralActId = hm.getId();
                currentCentralActTitle = hm.getTitle();
                comboboxType.getItems().clear();
                comboboxType.setItems(ServiceHelper.getCentralActTypeById(currentCentralActId));
                FxUtil.autoCompleteComboBox(comboboxType, FxUtil.AutoCompleteMode.CONTAINING);
                FxUtil.getComboBoxValue(comboboxType);

                htmlContent = ServiceHelper.getCentralActIndexById(currentCentralActId);
                engine.loadContent(htmlContent, "text/html");
//                webViewContent.getEngine().loadContent(htmlContent, "text/html");
                _is_index_view = true;
            }
        });

        comboboxType.setEditable(true);
        /*Combobox on focus expand code*/
        for (final ComboBox<String> choiceBox : Arrays.asList(comboboxType)) {
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

        comboboxType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    currentCentralActSectionTitle = newValue;
                    htmlContent = ServiceHelper.getCentralActContentById(currentCentralActId, newValue);
                    engine.loadContent("<html><head><meta charset=\"UTF-8\">" + javaScript + "</head><body>" + htmlContent + "</body></html>", "text/html");
                    _is_index_view = false;
                }
            }
        });

        //Search button click event
        btnFullAct.setOnAction(event -> {
            btnFullAct.setCursor(Cursor.WAIT);
            currentCentralActSectionTitle = "";
            htmlContent = ServiceHelper.getCentralActContentById(currentCentralActId, "");
            engine.loadContent("<html><head><meta charset=\"UTF-8\">" + javaScript + "</head><body>" + htmlContent + "</body></html>", "text/html");
            _is_index_view = false;
            btnFullAct.setCursor(Cursor.DEFAULT);
        });

        //Search button click event
        btnIndexView.setOnAction(event -> {
            btnIndexView.setCursor(Cursor.WAIT);
            currentCentralActSectionTitle = "";
            htmlContent = ServiceHelper.getCentralActIndexById(currentCentralActId);
            engine.loadContent(htmlContent, "text/html");
            _is_index_view = true;
            btnIndexView.setCursor(Cursor.DEFAULT);
        });

        /*ZOOM IN BUTTON*/
        btnZoomIn.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2));

            if (_is_index_view){
                htmlContent = ServiceHelper.getCentralActIndexById(currentCentralActId);
                engine.loadContent(htmlContent, "text/html");
            }else{
                htmlContent = ServiceHelper.getCentralActContentById(currentCentralActId, currentCentralActSectionTitle);
                engine.loadContent(htmlContent, "text/html");
            }
//            webViewContent.setZoom(webViewContent.getZoom() / 1.1);
        });

        /*ZOOM OUT BUTTON*/
        btnZoomOut.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2));

            if (_is_index_view){
                htmlContent = ServiceHelper.getCentralActIndexById(currentCentralActId);
                engine.loadContent(htmlContent, "text/html");
            }else{
                htmlContent = ServiceHelper.getCentralActContentById(currentCentralActId, currentCentralActSectionTitle);
                engine.loadContent(htmlContent, "text/html");
            }

//            webViewContent.setZoom(webViewContent.getZoom() * 1.1);
        });

        btnMail.setOnAction(event -> {
            showEmailDialogWindow();
//            JavaHelper.saveAsPdf(judgementHTML+".pdf", titleText);
        });

        btnPrint.setOnAction(event -> {
            try {
                JavaHelper.print(htmlContent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        webViewContent.setContextMenuEnabled(false);
        WebEventDispatcher webEventDispatcher = new WebEventDispatcher(webViewContent.getEventDispatcher());

        /*Webview click event*/
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    webViewContent.setEventDispatcher(webEventDispatcher);
                    if (Queries.IS_COPY_DISABLE){
                        webViewContent.setEventDispatcher(webEventDispatcher);
                    }
                    // note next classes are from org.w3c.dom domain
                    EventListener listener = new EventListener() {
                        public void handleEvent(Event ev) {
                            String href = ((org.w3c.dom.Element) ev.getTarget()).getAttribute("href");
                            if (!href.isEmpty()) {
                                String act,section="";
                                if (href.contains("~")){
                                    act = href.substring(0, href.indexOf("~")).replace("act:", "").trim();
                                    section = href.substring(href.indexOf("~") + 1).trim();
//                                    engine.executeScript("alert('" + section + "');");
//                                    comboboxType.getSelectionModel().select(section);
                                    showCentralActsDialogWindow(href);

                                }else{
                                    act = href.replace("act:", "").trim();
                                }
                            }
                        }
                    };

                    Platform.runLater(() -> {
                        Document doc = engine.getDocument();
                        org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            org.w3c.dom.events.EventTarget eventTarget = (org.w3c.dom.events.EventTarget) nodeList.item(i);
                            eventTarget.addEventListener("click", listener, false);
                        }
                    });
                }
            }
        });

        webViewContent.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
//                        if (e.getButton() == MouseButton.SECONDARY)
//                        {
                        final ContextMenu contextMenuWebView = new ContextMenu();

                        String selectionStr = (String) engine.executeScript("window.getSelection().toString()");

                        if (selectionStr != null && !selectionStr.isEmpty()) {
                            MenuItem itemWebViewCopy = new MenuItem("Copy");
                            contextMenuWebView.getItems().addAll(itemWebViewCopy);

                            itemWebViewCopy.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    StringSelection stringSelection;
                                    if (selectionStr.length() > 3000) {
                                        stringSelection = new StringSelection(selectionStr.substring(0, 3000));
                                    } else {
                                        stringSelection = new StringSelection(selectionStr);
                                    }
                                    java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                    clpbrd.setContents(stringSelection, null);
                                }
                            });
                        }
//                            contextMenuWebView.show(webViewContent, e.getScreenX(), e.getScreenY());
//                        }
                        if (e.getButton() == MouseButton.SECONDARY) {
                            contextMenuWebView.show(webViewContent, e.getScreenX(), e.getScreenY());
                        }
                    }
                }
        );
    }

    public boolean showCentralActsDialogWindow(String href) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_central_act.fxml"));
            DialogCentralActController controller = new DialogCentralActController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Central Act Dialog");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.setResizable(false);

//             Set the person into the controller
//            DialogBookmarkAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            controller.initData(href);
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

    private boolean showEmailDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_send_email.fxml"));
            DialogSendEMailController controller = new DialogSendEMailController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Judgement View");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

//             Set the person into the controller
//            DialogSendEMailController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setHTML(htmlContent);

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

//            if (!controller.isOkClicked()) {
//
//            }

//            return controller.isOkClicked();
            return true;
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return true;
        }
    }

    private void loadData() {
        tableData = ServiceHelper.getAllCentralActList();

        tableTitleList.setItems(null);
        if (tableData.size() > 0) {
            tableTitleList.setItems(tableData);
        }
    }
}
