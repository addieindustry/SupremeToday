package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.JavaHelper;
import com.project.WebEventDispatcher;
import com.project.interfaces.SearchFromWebViewEventHandler;
import com.project.interfaces.SearchInSearchFromWebViewEventHandler;
import com.project.model.JudgementResultModel;
import com.project.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupremePadController implements Initializable {

    @FXML
    private WebView webViewDocView;

    @FXML
    private Button btnCopy, btnSave, btnOpen;

//    @FXML
//    private Button btnBookMark, btnHighlightPre, btnHighlightNext, btnPreviousDoc, btnNextDoc, btnHeadNote, btnFullCase, btnAnalysis, btnCitator, btnReferred, btnZoomPlus, btnZoomMinus, btnPDF, btnMail, btnPrint;

    @FXML
//    private TextField textFieldHighlight;
    private WebEngine engine;
    private ObservableList<JudgementResultModel> data;
//    private String titleText = "";
//    private String docId = "";
//    private String docType = "";
//
//    private String search_query = "", filter_query = "", sortBy = "", facet_fields = "", fields = "title, Type, DocType, caseId, id, judge, citation_store, judgementHeader_store,acts_store,impNotes_store,summary_store,content_store,whitelist,advocates,result,decisionDate", hl_fields = "content_store,judgementHeader_store";
//    private String href_query = "";
//    private int start_from = 0;
//    private int TotalResultCount = 0;
//    private static int MAX_COUNT = 50;
//    int tempValue = 0;

//    public String queryJudgementSearch = "";
//    private static boolean showTableView = true;
//    ContextMenu contextMenu;
//    private HBox toolBar;
//    private String judgementId = "";
//    private boolean isJudgementORHeadnote = true;/*default is Judgement loading*/
//
//    private String htmlHeader = "";
    private String copyHeader = "";
//    String judgementHTML = "";
//    String judgementCSS = "";
//    String javaScript = "";

//    ResultViewHelper resultViewHelper = new ResultViewHelper();

//    String javaScript = "<script language='JavaScript'> function keyup(){if (window.getSelection) {if (window.getSelection().empty) {window.getSelection().empty();} else if (window.getSelection().removeAllRanges) {window.getSelection().removeAllRanges();}} else if (document.selection) {document.selection.empty();}}</script>";
//    String javaScript = "<script language='JavaScript'> function keyup(){if (window.getSelection) {if (window.getSelection().empty) {window.getSelection().empty();} else if (window.getSelection().removeAllRanges) {window.getSelection().removeAllRanges();}} else if (document.selection) {document.selection.empty();}}</script>";

//    private String sortParameter = Queries.SORT_JDATE + Queries.STRING + Queries.DESC_CAPITAL;
//    private boolean insertIntoHistory = true;

//    MenuItem itemWebView = null;

//    private boolean COURT_SORT = true;
//    private boolean JUDGEMENT_SORT = true;
//    private boolean BENCH_SORT = true;

    //    private static final List<ActSearchFromJudgementHTMLListener> ActSearchFromJudgementInterfaceList = Lists.newArrayList();
//    private ObservableList<String> listActsForHTMLHighlight = FXCollections.observableArrayList();
//
//    public SupremePadController() {
//    }
//
    private static final java.util.List<SearchFromWebViewEventHandler> listeners = Lists.newArrayList();

    public void setSearchFromWebViewKeyword(SearchFromWebViewEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }
//
    private static final java.util.List<SearchInSearchFromWebViewEventHandler> listenerslistenersSearchInSearchFromWebViewEventHandler = Lists.newArrayList();

    public void setSearchInSearchFromWebViewKeyword(SearchInSearchFromWebViewEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listenerslistenersSearchInSearchFromWebViewEventHandler.add(listener);
    }
//
//    private void focusTextInBrowser(WebEngine engine, String text) {
//        if (engine.executeScript("window.find('" + text + "',0,0,0,0,0,1);").equals(false)){
//            new Utils().showDialogAlert(text + " keyword finding reached to end of judgement!");
//        }
////        engine.executeScript("window.find('" + text + "',0,0,0,0,0,1);");
//    }
//
//    private int highLightCount = 1;
//
//    private boolean highLightFocus(int highLightCount) {
//        Object response = engine.executeScript("function myFalse() {return false;};function myTrue() {return true;};element=document.getElementById(\"hl" + highLightCount + "\");alignWithTop=true;if(element){element.scrollIntoView(alignWithTop);myTrue();} else {myFalse();};");
//        return Boolean.parseBoolean(response.toString());
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = webViewDocView.getEngine();
        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);
        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);
        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);

        webViewDocView.setContextMenuEnabled(false);
        WebEventDispatcher webEventDispatcher = new WebEventDispatcher(webViewDocView.getEventDispatcher());

        /*Webview click event*/
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    webViewDocView.setEventDispatcher(webEventDispatcher);
                    // note next classes are from org.w3c.dom domain
                    EventListener listener = new EventListener() {
                        public void handleEvent(Event ev) {
//                            String href = ((org.w3c.dom.Element) ev.getTarget()).getAttribute("href");
//                            if (!href.isEmpty()){
//                                if (href.startsWith("act:")) {
//                                    showCentralActsDialogWindow(href);
//                                }else if (href.startsWith("overruled:")) {
//                                    System.out.println(href);
//                                    loadOverruledHTML(href.replace("overruled:", ""));
////                                    showCentralActsDialogWindow(href);
//                                } else {
//                                    String search_query="caseId:" + href;
//                                    String sort_by="";
//                                    String hl_fields="false";
//                                    Multimap<String, String> filterBy = ArrayListMultimap.create();
//                                }
//                            }
                        }
                    };

//                    Platform.runLater(() -> {
//                        Document doc = engine.getDocument();
//                        org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");
//                        for (int i = 0; i < nodeList.getLength(); i++) {
////                            org.w3c.dom.Node node = nodeList.item(i);
//                            org.w3c.dom.events.EventTarget eventTarget = (org.w3c.dom.events.EventTarget) nodeList.item(i);
//                            eventTarget.addEventListener("click", listener, false);
//                        }
//                    });
                }
            }
        });

        btnCopy.setOnAction(event -> {
            String _html = (String) webViewDocView.getEngine().executeScript("document.documentElement.outerHTML");
            _html = _html.replaceAll("\\<[^>]*>","");
            StringSelection stringSelection = new StringSelection(_html);
            java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
        });

        btnSave.setOnAction(event -> {
            DirectoryChooser directoryChooser
                    = new DirectoryChooser();
            final File selectedDirectory
                    = directoryChooser.showDialog(new Stage(StageStyle.DECORATED));
            String fileName = "SupremePad.html";
            if (selectedDirectory != null) {
                OutputStream file = null;
                try {
                    String _outputFilePath = selectedDirectory.getAbsolutePath() + File.separator + fileName;
                    File f = new File(selectedDirectory.getAbsolutePath() + File.separator + fileName);
                    if (f.exists()) {
                        f.delete();
                    }
                    file = new FileOutputStream(new File(selectedDirectory.getAbsolutePath() + File.separator + fileName));

                    String _html = (String) webViewDocView.getEngine().executeScript("document.documentElement.outerHTML");
                    try (PrintStream out = new PrintStream(new FileOutputStream(_outputFilePath))) {
                        out.print(_html);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    new Utils().showDialogAlert("SupremePad file save successfully!");
                } catch (Exception ex) {
                    Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        file.close();
                    } catch (IOException ex) {
                        Logger.getLogger(JavaHelper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        btnOpen.setOnAction(event -> {
            String _tempPath = new java.io.File("").getAbsolutePath() + "\\" + "supremepad.html";
            String _html = (String) webViewDocView.getEngine().executeScript("document.documentElement.outerHTML");
            try (PrintStream out = new PrintStream(new FileOutputStream(_tempPath))) {
                out.print(_html);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            File htmlFile = new File(_tempPath);
            try {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        final ContextMenu contextMenuWebView = new ContextMenu();
//        MenuItem itemWebViewTop = new MenuItem("Top");
//        MenuItem itemWebViewSearchWithin = new MenuItem("Search Within");
//        MenuItem itemWebViewSearch = new MenuItem("Search");
//        MenuItem itemWebViewCopy = new MenuItem("Copy");
//        MenuItem itemWebViewSupremePad = new MenuItem("Supreme Pad");
        MenuItem itemWebViewPrint = new MenuItem("Print");
        contextMenuWebView.getItems().addAll(itemWebViewPrint);

        webViewDocView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        if (e.getButton() == MouseButton.SECONDARY) {
                            /*Set Webview context Menu at run time*/
//                            contextMenuWebView.getItems().add(itemWebViewTop);
//                            contextMenuWebView.getItems().addAll(itemWebViewSearchWithin, itemWebViewSearch, itemWebViewCopy, itemWebViewPrint);

                            String selectionStr = "";
                            try {
                                selectionStr = (String) engine.executeScript("window.getSelection().toString()");
                            } catch (Exception ex) {
//                                ex.printStackTrace();
                            }

//                            String finalSelectionStr = selectionStr;
//                            itemWebViewSearch.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    for (SearchFromWebViewEventHandler listner : listeners) {
//                                        listner.handleSearchKeyword(finalSelectionStr);
//                                    }
//                                    Platform.runLater(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            handleCancel();
//                                        }
//                                    });
//                                    //                                    isOkClicked();
//                                }
//                            });
//
//                            String finalSelectionStr1 = selectionStr;
//                            itemWebViewSearchWithin.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    for (SearchInSearchFromWebViewEventHandler listner : listenerslistenersSearchInSearchFromWebViewEventHandler) {
//                                        listner.handleSearchKeyword(finalSelectionStr1);
//                                    }
//                                    Platform.runLater(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            handleCancel();
//                                        }
//                                    });
//                                }
//                            });
//
//                            String finalSelectionStr2 = selectionStr;
//                            itemWebViewCopy.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    if (finalSelectionStr2.length() > 5000) {
//                                        StringSelection stringSelection = new StringSelection(copyHeader + "\r\n\n" + finalSelectionStr2.substring(0, 5000));
//                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
//                                        clpbrd.setContents(stringSelection, null);
//                                    } else {
//                                        StringSelection stringSelection = new StringSelection(copyHeader + "\r\n\n" + finalSelectionStr2);
//                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
//                                        clpbrd.setContents(stringSelection, null);
//                                    }
//                                }
//                            });
//
//                            String finalSelectionStr4 = selectionStr;
//                            itemWebViewSupremePad.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    String _temp = "";
//                                    if (finalSelectionStr4.length() > 5000) {
//                                        _temp = finalSelectionStr4.substring(0, 5000);
//                                    }
//                                }
//                            });

                            String finalSelectionStr3 = selectionStr;
                            itemWebViewPrint.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        JavaHelper.print(finalSelectionStr3);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

//                            itemWebViewTop.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    try {
//                                        engine.executeScript("window.scrollTo(0,1);window.scrollTo(0,0);");
//                                    } catch (Exception ex) {
//                                    }
//                                }
//                            });

                            contextMenuWebView.show(webViewDocView, e.getScreenX(), e.getScreenY());
                        }else{
                            contextMenuWebView.hide();
                        }
                    }
                }
        );

    }

    private void loadHTML(String _temp) {
        engine.loadContent(_temp, "text/html");
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

    public void initData(String _temp) {
        loadHTML(_temp);
    }
}

