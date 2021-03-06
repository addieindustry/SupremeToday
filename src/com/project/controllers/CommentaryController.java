package com.project.controllers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.project.JavaHelper;
import com.project.WebEventDispatcher;
import com.project.helper.Queries;
import com.project.helper.ResultViewHelper;
import com.project.helper.ServiceHelper;
import com.project.model.TitleIdListModel;
import com.project.utils.FxUtil;
import com.project.utils.PTableColumn;
import com.project.utils.Utils;
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
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.w3c.dom.events.Event;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CommentaryController implements Initializable {

    @FXML
    private Button btnFullAct, btnZoomIn, btnZoomOut, btnMail, btnPrint;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TableView tableTitleList;
    @FXML
    private PTableColumn columnId, columnTitle;
    @FXML
    private WebView webViewContent;
    @FXML
    private ComboBox comboboxType, comboboxCommentaryType;

    @FXML
    private AnchorPane result_view_pane;

    @FXML
    private AnchorPane root;

    @FXML
    private SplitPane split_pane;

    private double split_pan_length = 0.3;

    private ObservableList<TitleIdListModel> tableData = FXCollections.observableArrayList();
    private String currentCentralActId= "";
    private String currentCentralActSectionId= "";
//    private String javaScript = "";
//    private String javaScript = "<script language='JavaScript'> function keyup(){if (window.getSelection) {if (window.getSelection().empty) {window.getSelection().empty();} else if (window.getSelection().removeAllRanges) {window.getSelection().removeAllRanges();}} else if (document.selection) {document.selection.empty();}}</script>";
    private String htmlContent = "";

    ResultViewHelper resultViewHelper = new ResultViewHelper();

//    private ObservableList<DictionaryTitleModel> data;
//    private ObservableList<String> entries = FXCollections.observableArrayList();

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public CommentaryController() {

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

        webViewContent.setContextMenuEnabled(false);

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

        comboboxCommentaryType.setItems(FXCollections.observableArrayList("All", "Exhaustive Commentary", "Comments & Case Law"));
        comboboxCommentaryType.getSelectionModel().select(0);

        comboboxCommentaryType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (oldValue != null) {
//                    System.out.println(oldValue);
                }
                if (comboboxCommentaryType.getSelectionModel().isSelected(0)){
                    loadData();
                }else if (comboboxCommentaryType.getSelectionModel().isSelected(1)){
                    tableData = ServiceHelper.getAllCommentaryListByFilter(true);

                    tableTitleList.setItems(null);
                    if (tableData.size() > 0) {
                        tableTitleList.setItems(tableData);
                    }
                }else if (comboboxCommentaryType.getSelectionModel().isSelected(2)){
                    tableData = ServiceHelper.getAllCommentaryListByFilter(false);

                    tableTitleList.setItems(null);
                    if (tableData.size() > 0) {
                        tableTitleList.setItems(tableData);
                    }
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
                currentCentralActId = hm.getId();
                comboboxType.getItems().clear();
                comboboxType.setItems(ServiceHelper.getCommentaryTypeById(currentCentralActId));
                FxUtil.autoCompleteComboBox(comboboxType, FxUtil.AutoCompleteMode.CONTAINING);
                FxUtil.getComboBoxValue(comboboxType);
                comboboxType.getSelectionModel().select(0);
//                String content = ServiceHelper.getDictionaryContentById(hm.getId());
//                webViewContent.getEngine().loadContent(content, "text/html");
            }
        });

        comboboxType.setEditable(true);
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
                if (oldValue != null) {
//                    System.out.println(oldValue);
                }
                if (newValue != null) {
                    currentCentralActSectionId = newValue;
                    htmlContent = ServiceHelper.getCommentaryContentById(currentCentralActId, currentCentralActSectionId);

                    htmlContent = tuneHTMLContent(htmlContent);
                    webViewContent.getEngine().loadContent(htmlContent, "text/html");
//                    webViewContent.getEngine().loadContent("<html><head><meta charset=\"UTF-8\">" + javaScript + "</head><body>" + htmlContent + "</body></html>", "text/html");
                }
            }
        });
        //Search button click event
        btnFullAct.setOnAction(event -> {
            btnFullAct.setCursor(Cursor.WAIT);
            currentCentralActSectionId = "";
            htmlContent = ServiceHelper.getCommentaryContentById(currentCentralActId, currentCentralActSectionId);
            webViewContent.getEngine().loadContent(htmlContent, "text/html");
//            webViewContent.getEngine().loadContent("<html><head><meta charset=\"UTF-8\">" + javaScript + "</head><body>" + htmlContent + "</body></html>", "text/html");
            btnFullAct.setCursor(Cursor.DEFAULT);
        });

        /*ZOOM IN BUTTON*/
        btnZoomIn.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2));

            htmlContent = ServiceHelper.getCommentaryContentById(currentCentralActId, currentCentralActSectionId);
            webViewContent.getEngine().loadContent(htmlContent, "text/html");

//            webViewContent.setZoom(webViewContent.getZoom() / 1.1);
        });

        /*ZOOM OUT BUTTON*/
        btnZoomOut.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2));

            htmlContent = ServiceHelper.getCommentaryContentById(currentCentralActId, currentCentralActSectionId);
            webViewContent.getEngine().loadContent(htmlContent, "text/html");

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

        WebEventDispatcher webEventDispatcher = new WebEventDispatcher(webViewContent.getEventDispatcher());

        webViewContent.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    webViewContent.setEventDispatcher(webEventDispatcher);
//                    if (Queries.IS_COPY_DISABLE){
//                        webViewContent.setEventDispatcher(webEventDispatcher);
//                    }

                    org.w3c.dom.events.EventListener listener = new org.w3c.dom.events.EventListener() {
                        public void handleEvent(Event ev) {
                            String href = ((org.w3c.dom.Element) ev.getTarget()).getAttribute("href");
                            if (!href.isEmpty()){
                                if (href.startsWith("act:")) {

                                } else {
                                    String search_query="caseId:" + href;
                                    String sort_by="";
                                    String hl_fields="false";
                                    Multimap<String, String> filterBy = ArrayListMultimap.create();
                                    resultViewHelper.showJudgementDialogWindow(1, search_query, sort_by, hl_fields, filterBy);
//                                    resultViewHelper.showJudgementDialogWindow(Integer.parseInt(href), search_query, sort_by, hl_fields, filterBy);
                                }
                            }
                        }
                    };
                    org.w3c.dom.Document doc = webViewContent.getEngine().getDocument();
                    org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        org.w3c.dom.Node node = nodeList.item(i);
                        org.w3c.dom.events.EventTarget eventTarget = (org.w3c.dom.events.EventTarget) node;
                        eventTarget.addEventListener("click", listener, false);
                    }
//                    webViewContent.setEventDispatcher(webEventDispatcher);
                }
            }
        });

        webViewContent.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        /*Set Webview context Menu at run time*/
                        final ContextMenu contextMenuWebView = new ContextMenu();

                        String selectionStr = (String) webViewContent.getEngine().executeScript("window.getSelection().toString()");

                        if (selectionStr != null && !selectionStr.isEmpty()) {
                            MenuItem itemWebViewCopy = new MenuItem("Copy");
                            contextMenuWebView.getItems().addAll(itemWebViewCopy);

                            itemWebViewCopy.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    if (selectionStr.length()>3000) {
                                        StringSelection stringSelection = new StringSelection(selectionStr.substring(0, 3000));
                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                        clpbrd.setContents(stringSelection, null);
                                    }else{
                                        StringSelection stringSelection = new StringSelection(selectionStr);
                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                        clpbrd.setContents(stringSelection, null);
                                    }
//                                    if (selection.length()<=3000) {
//                                        StringSelection stringSelection = new StringSelection(selection);
//                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
//                                        clpbrd.setContents(stringSelection, null);
//                                    }
                                }
                            });
                        }
                        if (e.getButton() == MouseButton.SECONDARY) {
                            contextMenuWebView.show(webViewContent, e.getScreenX(), e.getScreenY());
                        }
                    }
                }
        );
    }

    private String tuneHTMLContent(String inputHtml){
        try {
            inputHtml = inputHtml.replaceAll("font-size\\s?:\\s?\\d+(pt|px|em);", "");
            inputHtml = inputHtml.replaceAll("line-height\\s?:\\s?\\d+(pt|px|em);", "");
            inputHtml = inputHtml.replaceAll("text-indent\\s?:\\s?\\d+(pt|px|em);", "");
//            inputHtml = inputHtml.replaceAll("style\\s?='\\s+'", "");
            inputHtml = inputHtml.replace("<style>body {}</style>", "");
            inputHtml = inputHtml.replaceAll("style=", "");

            String _start_index = "<style>";
            String _end_index = "</style>";

            if (inputHtml.contains(_start_index) && inputHtml.contains(_end_index)){
                String temp = inputHtml.substring(inputHtml.indexOf(_start_index), inputHtml.indexOf(_end_index) + _end_index.length());
                inputHtml = inputHtml.replace(temp, "");
            }

            return inputHtml;
        } catch (Exception e) {
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return inputHtml;
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
        tableData = ServiceHelper.getAllCommentaryList();

        tableTitleList.setItems(null);
        if (tableData.size() > 0) {
            tableTitleList.setItems(tableData);
        }
    }
}
