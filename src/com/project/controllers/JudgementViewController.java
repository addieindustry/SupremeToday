package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.project.GetMyIP;
import com.project.JavaHelper;
import com.project.WebEventDispatcher;
import com.project.helper.Queries;
import com.project.helper.ResultViewHelper;
import com.project.helper.ServiceHelper;
import com.project.interfaces.SearchFromWebViewEventHandler;
import com.project.interfaces.SearchInSearchFromWebViewEventHandler;
import com.project.model.JudgementResultModel;
import com.project.utility.SearchUtility;
import com.project.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.project.helper.ServiceHelper.getUserDetails;

public class JudgementViewController implements Initializable {

    @FXML
    private WebView webViewDocView;

    @FXML
    private Button btnGoogleTranslator, btnBookMark, btnHighlightPre, btnHighlightNext, btnPreviousDoc, btnNextDoc, btnHeadNote, btnFullCase, btnAnalysis, btnCitator, btnJudicial, btnZoomPlus, btnZoomMinus, btnPDF, btnMail, btnPrint, btnTruePrint;

    @FXML
    private TextField textFieldHighlight;
    private WebEngine engine;
    private ObservableList<JudgementResultModel> data;
    private String titleText = "";
    private String docId = "";
    private String docType = "";

    private String search_query = "", filter_query = "", sortBy = "", facet_fields = "", fields = "title, Type, DocType, caseId, id, judge, citation_store, judgementHeader_store,acts_store,impNotes_store,summary_store,content_store,whitelist,advocates,result,decisionDate", hl_fields = "content_store,judgementHeader_store";
    private String href_query = "";
    private String open_tab_pre = "FullCase";
    private int start_from = 0;
//    private int TotalResultCount = 0;
//    private static int MAX_COUNT = 50;
//    int tempValue = 0;

    public String queryJudgementSearch = "";
    private static boolean showTableView = true;
    ContextMenu contextMenu;
    private HBox toolBar;
    private String judgementId = "";
    private boolean isJudgementORHeadnote = true;/*default is Judgement loading*/

    private String htmlHeader = "";
    private String copyHeader = "";
    String judgementHTML = "";
    String googleTranslatorScript = "<script>function googleTranslateElementInit(){new google.translate.TranslateElement({pageLanguage:'en'},'google_translate_element');}</script> <script src=\"http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>";
    String googleTranslatorDiv = "<div id=\"google_translate_element\"></div>";
//    String googleTranslatorDiv = "";
    String judgementCSS = "";
    String javaScript = "<script>function scrollTo(o){document.getElementById(o).scrollIntoView()}</script>";

    ResultViewHelper resultViewHelper = new ResultViewHelper();

//    String javaScript = "<script language='JavaScript'> function keyup(){if (window.getSelection) {if (window.getSelection().empty) {window.getSelection().empty();} else if (window.getSelection().removeAllRanges) {window.getSelection().removeAllRanges();}} else if (document.selection) {document.selection.empty();}}</script>";
//    String javaScript = "<script language='JavaScript'> function keyup(){if (window.getSelection) {if (window.getSelection().empty) {window.getSelection().empty();} else if (window.getSelection().removeAllRanges) {window.getSelection().removeAllRanges();}} else if (document.selection) {document.selection.empty();}}</script>";

//    private String sortParameter = Queries.SORT_JDATE + Queries.STRING + Queries.DESC_CAPITAL;
//    private boolean insertIntoHistory = true;

//    MenuItem itemWebView = null;

//    private boolean COURT_SORT = true;
//    private boolean JUDGEMENT_SORT = true;
//    private boolean BENCH_SORT = true;

    //    private static final List<ActSearchFromJudgementHTMLListener> ActSearchFromJudgementInterfaceList = Lists.newArrayList();
    private ObservableList<String> listActsForHTMLHighlight = FXCollections.observableArrayList();

    public JudgementViewController() {

    }

    private static final java.util.List<SearchFromWebViewEventHandler> listeners = Lists.newArrayList();

    public void setSearchFromWebViewKeyword(SearchFromWebViewEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }

    private static final java.util.List<SearchInSearchFromWebViewEventHandler> listenerslistenersSearchInSearchFromWebViewEventHandler = Lists.newArrayList();

    public void setSearchInSearchFromWebViewKeyword(SearchInSearchFromWebViewEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listenerslistenersSearchInSearchFromWebViewEventHandler.add(listener);
    }

    private void focusTextInBrowser(WebEngine engine, String text) {
        if (engine.executeScript("window.find('" + text + "',0,0,0,0,0,1);").equals(false)){
            new Utils().showDialogAlert(text + " keyword finding reached to end of judgement!");
        }
//        engine.executeScript("window.find('" + text + "',0,0,0,0,0,1);");
    }

    private int highLightCount = 1;

    private boolean highLightFocus(int highLightCount) {
        Object response = engine.executeScript("function myFalse() {return false;};function myTrue() {return true;};element=document.getElementById(\"hl" + highLightCount + "\");alignWithTop=true;if(element){element.scrollIntoView(alignWithTop);myTrue();} else {myFalse();};");
        return Boolean.parseBoolean(response.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        engine = webViewDocView.getEngine();
//        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);
//        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);
//        webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            btnGoogleTranslator.setStyle("-fx-background-color: steelblue");
//            btnBookMark.setStyle("-fx-background-color: steelblue");
//            btnHighlightPre.setStyle("-fx-background-color: steelblue");
//            btnHighlightNext.setStyle("-fx-background-color: steelblue");
//            btnPreviousDoc.setStyle("-fx-background-color: steelblue");
//            btnNextDoc.setStyle("-fx-background-color: steelblue");
            btnHeadNote.setStyle("-fx-background-color: steelblue");
            btnFullCase.setStyle("-fx-background-color: steelblue");
            btnAnalysis.setStyle("-fx-background-color: steelblue");
            btnCitator.setStyle("-fx-background-color: steelblue");
            btnJudicial.setStyle("-fx-background-color: steelblue");
//            btnZoomPlus.setStyle("-fx-background-color: steelblue");
//            btnZoomMinus.setStyle("-fx-background-color: steelblue");
//            btnPDF.setStyle("-fx-background-color: steelblue");
//            btnMail.setStyle("-fx-background-color: steelblue");
//            btnPrint.setStyle("-fx-background-color: steelblue");
            btnTruePrint.setStyle("-fx-background-color: steelblue");
        }

        String base64 = "";
        try {
            base64 = new String(Files.readAllBytes(Paths.get(Queries.LOGO_BASE64_FILE_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        htmlHeader = "<div style=\"width: 100%;text-align: left\" ><img src='" + base64 + "'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + ServiceHelper.getLicText() + "</div></br>";

        textFieldHighlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (engine.getDocument() != null) {
                    focusTextInBrowser(engine, textFieldHighlight.getText());
                }
            }
        });

        btnGoogleTranslator.setOnAction(event -> {
            String _html = (String) engine.executeScript("document.documentElement.outerHTML");
            try {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(Paths.get(Queries.RESOURCE_PATH, "temp.html").toString()), "utf-8"))) {
                    writer.write(_html);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            File f = new File(Paths.get(Queries.RESOURCE_PATH, "temp.html").toString());
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(f.toURI().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
//            showBookmarkAddDialogWindow();
        });

        btnTruePrint.setOnAction(event -> {
            HashMap<String, String> userDetails = (HashMap<String, String>) getUserDetails();
            String subscriberId = userDetails.get("sub_id");
            String saveTempFilePath = Paths.get(Queries.TEMP_DIRECTORY_PATH, "temp.pdf").toString();
//            System.out.println(saveTempFilePath);
            final int BUFFER_SIZE = 4096;

            try {
                String truePrintURL = Queries.TRUE_PRINT_URL + judgementId + "?sub=" + subscriberId + "&ip=" + GetMyIP.Get();
//                System.out.println(truePrintURL);
                URL myurl = new URL(truePrintURL);
                HttpURLConnection con = (HttpURLConnection) myurl.openConnection();

                con.setDoOutput(true);
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Java client");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // opens input stream from the HTTP connection
                InputStream inputStream = con.getInputStream();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveTempFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//            con.disconnect();
            }
            JavaHelper.viewpdffile(saveTempFilePath);
        });

        btnBookMark.setOnAction(event -> {
            showBookmarkAddDialogWindow();
        });

        btnPDF.setOnAction(event -> {
            DirectoryChooser directoryChooser
                    = new DirectoryChooser();
            final File selectedDirectory
                    = directoryChooser.showDialog(new Stage(StageStyle.DECORATED));
            if (selectedDirectory != null) {
                if (JavaHelper.createPdf(judgementHTML, selectedDirectory.getAbsolutePath() + File.separator + titleText.replaceAll("/", "_").replaceAll("&", "_").replaceAll(":", "_") + ".pdf")){
                    new Utils().showDialogAlert("PDF saved successfully!");
                }
            }
//            JavaHelper.saveAsPdf(judgementHTML, titleText.replaceAll("/", "_").replaceAll("&", "_").replaceAll(":", "_") + ".pdf");
        });

        btnPrint.setOnAction(event -> {
            try {
                JavaHelper.print(judgementHTML);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        btnMail.setOnAction(event -> {
            showEmailDialogWindow();
//            JavaHelper.saveAsPdf(judgementHTML+".pdf", titleText);
        });

        btnPreviousDoc.setOnAction(event -> {
            if (btnPreviousDoc.getTooltip().getText().equals("Go to MainCase")) {
                btnPreviousDoc.setTooltip(new Tooltip("Navigate to Previous"));
                btnNextDoc.setVisible(true);
                loadHTML("FullCase");
            } else {
                if (start_from != 0) {
                    start_from -= 1;
                    loadHTML("FullCase");
                }
            }
        });

        btnNextDoc.setOnAction(event -> {
            start_from += 1;
            loadHTML("FullCase");
        });

        btnHighlightPre.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (highLightCount != 1) {
                    highLightFocus(highLightCount--);
                }
            }
        });

        btnHighlightNext.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (highLightFocus(highLightCount)) {
                    highLightCount++;
                }
            }
        });

        /*ZOOM IN BUTTON*/
        btnZoomPlus.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) + 2));
            loadHTML(open_tab_pre);
//            webViewDocView.setZoom(webViewDocView.getZoom() * 1.1);
        });

        /*ZOOM OUT BUTTON*/
        btnZoomMinus.setOnAction(event -> {
            ServiceHelper.updateDisplayFontInPrintSetting( Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2);
            Queries.PRINT_SETTING_MODEL.setDisplayFontSize(String.valueOf(Integer.parseInt(Queries.PRINT_SETTING_MODEL.getDisplayFontSize()) - 2));
            loadHTML(open_tab_pre);
//            webViewDocView.setZoom(webViewDocView.getZoom() / 1.1);
        });

        btnHeadNote.setOnAction(event -> {
            loadHTML("HeadNote");
        });

        btnFullCase.setOnAction(event -> {
            if (!btnFullCase.getText().equals("FullAct")) {
                loadHTML("FullCase");
            } else {
                loadHTML("FullAct");
            }
        });

        btnAnalysis.setOnAction(event -> {
            loadHTML("Analysis");
        });

        btnCitator.setOnAction(event -> {
            loadHTML("Citator");
        });

//        btnReferred.setOnAction(event -> {
//            loadHTML("Referred");
//        });

        btnJudicial.setOnAction(event -> {
            loadHTML("Judicial");
        });

        webViewDocView.setContextMenuEnabled(false);
        WebEventDispatcher webEventDispatcher = new WebEventDispatcher(webViewDocView.getEventDispatcher());
//        webViewDocView.getEngine().setJavaScriptEnabled(true);
//        webViewDocView.getSettings().setJavaScriptEnabled(true);

        /*Webview click event*/
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    if (Queries.IS_COPY_DISABLE){
                        webViewDocView.setEventDispatcher(webEventDispatcher);
                    }
                    // note next classes are from org.w3c.dom domain
                    EventListener listener = new EventListener() {
                        public void handleEvent(Event ev) {
                            String href = ((org.w3c.dom.Element) ev.getTarget()).getAttribute("href");
                            if (!href.isEmpty()){
                                if (href.startsWith("act:")) {
                                    showCentralActsDialogWindow(href);
                                }else if (href.startsWith("overruled:")) {
                                    loadOverruledHTML(href.replace("overruled:", ""));
//                                    showCentralActsDialogWindow(href);
                                }else if (href.startsWith("#")) {
                                    ev.preventDefault();
                                    ev.stopPropagation();
                                    engine.executeScript("scrollTo('" + href.replace("#", "") + "');");
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

                    Platform.runLater(() -> {
                        Document doc = engine.getDocument();
                        org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");
                        for (int i = 0; i < nodeList.getLength(); i++) {
//                            org.w3c.dom.Node node = nodeList.item(i);
                            org.w3c.dom.events.EventTarget eventTarget = (org.w3c.dom.events.EventTarget) nodeList.item(i);
                            eventTarget.addEventListener("click", listener, false);
//                            if (!eventTarget.toString().startsWith("#")){
//                                eventTarget.addEventListener("click", listener, false);
//                            }
                        }
                    });
                }
            }
        });

        final ContextMenu contextMenuWebView = new ContextMenu();
        MenuItem itemWebViewTop = new MenuItem("Top");
        MenuItem itemWebViewSearchWithin = new MenuItem("Search Within");
        MenuItem itemWebViewSearch = new MenuItem("Search");
        MenuItem itemWebViewCopy = new MenuItem("Copy");
        MenuItem itemWebViewSupremePad = new MenuItem("Supreme Pad");
        MenuItem itemWebViewPrint = new MenuItem("Print");
        contextMenuWebView.getItems().addAll(itemWebViewTop, itemWebViewSearchWithin, itemWebViewSearch, itemWebViewCopy, itemWebViewSupremePad, itemWebViewPrint);

        webViewDocView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        if (e.getButton() == MouseButton.SECONDARY) {
                            /*Set Webview context Menu at run time*/
//                            contextMenuWebView.getItems().add(itemWebViewTop);
//                            contextMenuWebView.getItems().addAll(itemWebViewSearchWithin, itemWebViewSearch, itemWebViewCopy, itemWebViewPrint);

                            String selectionStr = "";
                            try {
                                selectionStr = (String) engine.executeScript("document.getSelection().toString()");
//                                selectionStr = (String) engine.executeScript("window.getSelection().toString()");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            String finalSelectionStr = selectionStr;
                            itemWebViewSearch.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    for (SearchFromWebViewEventHandler listner : listeners) {
                                        listner.handleSearchKeyword(finalSelectionStr);
                                    }
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            handleCancel();
                                        }
                                    });
                                    //                                    isOkClicked();
                                }
                            });

                            String finalSelectionStr1 = selectionStr;
                            itemWebViewSearchWithin.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    for (SearchInSearchFromWebViewEventHandler listner : listenerslistenersSearchInSearchFromWebViewEventHandler) {
                                        listner.handleSearchKeyword(finalSelectionStr1);
                                    }
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            handleCancel();
                                        }
                                    });
                                }
                            });

                            String finalSelectionStr2 = selectionStr;
                            itemWebViewCopy.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    if (finalSelectionStr2.length() > 5000) {
                                        StringSelection stringSelection = new StringSelection(copyHeader + "\r\n\n" + finalSelectionStr2.substring(0, 5000));
                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                        clpbrd.setContents(stringSelection, null);
                                    } else {
                                        StringSelection stringSelection = new StringSelection(copyHeader + "\r\n\n" + finalSelectionStr2);
                                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                        clpbrd.setContents(stringSelection, null);
                                    }
                                }
                            });

                            String finalSelectionStr4 = selectionStr;
                            itemWebViewSupremePad.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    String _temp = "";
                                    if (finalSelectionStr4.length() > 5000) {
                                        _temp = "<div style=\"color:darkblue;font-weight: bold;\">" + copyHeader.replace("\r\n", "</br>") + "</div></br><div style=\"text-align:justify\">" + finalSelectionStr4.substring(0, 5000) + "</div>";
                                    }else{
                                        _temp = "<div style=\"color:darkblue;font-weight: bold;\">" + copyHeader.replace("\r\n", "</br>") + "</div></br><div style=\"text-align:justify\">" + finalSelectionStr4 + "</div>";
                                    }
                                    _temp  = _temp.replace("\n\n", "</br></br>");
                                    showSupremePadWindow(_temp);
                                }
                            });

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

                            itemWebViewTop.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        engine.executeScript("window.scrollTo(0,1);window.scrollTo(0,0);");
                                    } catch (Exception ex) {
                                    }
                                }
                            });
                            contextMenuWebView.show(webViewDocView, e.getScreenX(), e.getScreenY());
                        }else{
                            contextMenuWebView.hide();
                        }
                    }
                }
        );
    }

    public boolean showSupremePadWindow(String _temp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_supreme_pad.fxml"));
            SupremePadController controller = new SupremePadController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Supreme Pad");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    dialogStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - dialogStage.getWidth() / 2); //dialog.getWidth() = NaN
//                    dialogStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - dialogStage.getHeight() / 2); //dialog.getHeight() = NaN
//                }
//            });
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//            dialogStage.setHeight(screenBounds.getHeight());
//            dialogStage.setWidth(screenBounds.getWidth());
            dialogStage.setResizable(true);

            controller.setDialogStage(dialogStage);
            controller.initData(_temp);

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
//            dialogStage.showAndWait();
            dialogStage.show();

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

    private void loadOverruledHTML(String caseId) {
        String caseOverruledBy = ServiceHelper.getOverruledByCaseId(caseId);
        if (!caseOverruledBy.isEmpty()) {
            StringBuilder stb = new StringBuilder();
            stb.append("<span style=\"background-color:blue;color:white\"><strong>Overruled By :</strong></span><br/><br/>" + caseOverruledBy + "<br/><br/>");
            engine.loadContent("<html><head><meta charset=\"UTF-8\">" + javaScript + judgementCSS + "</head><body onkeydown=keyup()>" + htmlHeader + stb.toString() + "</body></html>", "text/html");
        }
    }

    private void loadHTML(String displayOnly) {
        open_tab_pre = displayOnly;
        URL kruti_font = null;
        try {
            kruti_font = new File(Queries.KRUTI_FONT_PATH).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        judgementCSS = "<style>@font-face {font-family: 'Kruti Dev';src: url('" + kruti_font + "') format('truetype')}.KrutiDev_hindi_text {font-family: Kruti Dev !important;font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px !important;}body {font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px;}table, th, td {border: 1px solid black;}</style>";
        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            judgementCSS = "<style>@font-face {font-family: 'Kruti Dev';src: url('" + kruti_font + "') format('truetype')}.KrutiDev_hindi_text {font-family: Kruti Dev !important;font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px !important;}body {font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px;font-family:georgia,garamond,serif;}table, th, td {border: 1px solid black;}</style>";
        }

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            if (!href_query.isEmpty()) {
                jo = searchUtility.search(href_query, 0, 1, sortBy, facet_fields, fields, hl_fields, "", "");
            } else {
                jo = searchUtility.search(search_query, start_from, 1, sortBy, facet_fields, fields, hl_fields, filter_query, search_query);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonArray jData = jo.getAsJsonArray("docs");
        for (JsonElement ele : jData) {
            JsonObject obj = (JsonObject) ele;
            docId = obj.get("caseId").getAsString();
            docType = obj.get("DocType").getAsString();
            boolean isWhiteListExist = false;
            boolean isGreenListExist = false;
            StringBuilder stb = new StringBuilder("");
            if (obj.get("DocType").getAsString().equals("Judgements")) {
                btnHeadNote.setVisible(true);
                btnFullCase.setVisible(true);
                btnFullCase.setText("FullCase");
                btnAnalysis.setVisible(true);
                btnCitator.setVisible(true);
                btnJudicial.setVisible(true);
                String startIndex = "#hPreTag#";
                String endIndex = "#hPostTag#";
                Integer hlCount = 1;

                if (ServiceHelper.isCitatorAvailableByCaseId(obj.get("caseId").getAsString())){
                    btnCitator.setStyle("-fx-background-color: #4169e1;");
                }else{
                    if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                        btnCitator.setStyle("-fx-background-color: steelblue");
                    }else{
                        btnCitator.setStyle("-fx-background-color: darkred;");
                    }
                }

                if (ServiceHelper.isJudicialCitatorAvailableByCaseId(obj.get("caseId").getAsString())){
                    btnJudicial.setStyle("-fx-background-color: #4169e1;");
                }else{
                    if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                        btnJudicial.setStyle("-fx-background-color: steelblue");
                    }else{
                        btnJudicial.setStyle("-fx-background-color: darkred;");
                    }
                }

                if (displayOnly.equals("FullCase")) {
                    if (ServiceHelper.getIsOverruled(obj.get("caseId").getAsString())){
                        isWhiteListExist = true;
                    };

                    if (ServiceHelper.getIsDistinguished(obj.get("caseId").getAsString())){
                        isGreenListExist = true;
                    };

//                    if (!obj.get("whitelist").getAsString().isEmpty()) {
//                        if (!obj.get("whitelist").getAsString().equals("0")) {
//                            isWhiteListExist = true;
//                        }
//                    }

                    if (!href_query.isEmpty()) {
                        btnPreviousDoc.setTooltip(new Tooltip("Go to MainCase"));
                        btnNextDoc.setVisible(false);
                    }
                    if (!obj.get("citation_store").getAsString().isEmpty()) {
                        String _citation = obj.get("citation_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "");

                        String _mainCitation = GetCrossCitationFromCitations(_citation,  true);
                        String _crossCitation = GetCrossCitationFromCitations(_citation, false);

                        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                            _mainCitation = _mainCitation.replace("Supreme", "ICLF");
                        }

                        stb.append("<div style=\"color:blue\"><center><strong>" + _mainCitation + "</strong></center></div>");
                        stb.append("<div><center><strong>" + _crossCitation + "</strong></center></div>");
//                        stb.append("<center><strong>" + _citation + "</strong></center>" + "<br/>");

                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        SimpleDateFormat sm = new SimpleDateFormat("dd MMM yyyy");
                        String jDate = null;
                        try {
                            jDate = sm.format(format.parse(obj.get("decisionDate").getAsString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        copyHeader = obj.get("title").getAsString() + ", " + jDate  + "\r\n" + _citation.trim() + "\r\n";
                    }

                    if (!obj.get("judgementHeader_store").getAsString().isEmpty()) {
                        String html = obj.get("judgementHeader_store").getAsString().replace("<#hPreTag#", "<").replace("</#hPreTag#", "</").replace("#hPostTag#>", ">");
                        while (html.contains(startIndex) && html.contains(endIndex)) {
                            String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                            html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                            hlCount += 1;
                        }
//                        html = html.replace("<BR>Versus<BR>", "Versus");
//                        html = html.replace("<BR>Decided on", "Decided on");
                        html = html.replace("<p align=\"justify\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>", "");
                        if (isWhiteListExist) {
                            stb.append("<div style=\"color:"+Queries.OVERRULED_BACKGROUND_COLOR+";\">" + html + "</div>");
                        }else if (isGreenListExist) {
                            stb.append("<div style=\"color:"+Queries.DISTINGUISHED_BACKGROUND_COLOR+";\">" + html + "</div>");
                        }else{
                            stb.append(html);
                        }

//                        stb.append(obj.get("judgementHeader_store").getAsString().replaceAll("#hPreTag#", "<STRONG>").replaceAll("#hPostTag#", "</STRONG>") + "<br/>");
                    }

                    if (!obj.get("summary_store").getAsString().isEmpty()) {
                        if (obj.get("summary_store").getAsString().trim().length() > 3) {
                            String html = obj.get("summary_store").getAsString().replace("<#hPreTag#", "<").replace("</#hPreTag#", "</").replace("#hPostTag#>", ">");
                            while (html.contains(startIndex) && html.contains(endIndex)) {
                                String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                                html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                                hlCount += 1;
                            }
                            html = html.replace("<p align=\"justify\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>", "");
                            stb.append("<p align=\"justify\"><strong>" + html + "</strong></p>");
                        }
                    }

                    String casesReferred = ServiceHelper.getCaseReferedByCaseId(obj.get("caseId").getAsString());
                    if (!casesReferred.isEmpty()) {
                        if (casesReferred.trim().length() > 3) {
                            stb.append("<span style=\"background-color:blue;color:white\"><strong>Cases referred:</strong></span><br/><br/>" + casesReferred + "<br/><br/>");
                        }
                    }

                    if (!obj.get("acts_store").getAsString().isEmpty()) {
                        stb.append("<span style=\"color:blue\"><strong>" + obj.get("acts_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</strong></span><br/><br/>");
                    }

                    if (!obj.get("impNotes_store").getAsString().isEmpty()) {
                        if (obj.get("impNotes_store").getAsString().trim().length() > 3) {
                            stb.append("<span style=\"color:red\">" + obj.get("impNotes_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</span><br/><br/>");
                        }
                    }

                    if (!obj.get("advocates").getAsString().isEmpty()) {
                        if (obj.get("advocates").getAsString().trim().length() > 3) {
//                            System.out.println(obj.get("advocates").getAsString());
                            String _advocate = obj.get("advocates").getAsString();
                            if (_advocate.contains("\r\n")){
                                _advocate = _advocate.replace("\r\n", "<br/>");
                            }else{
                                _advocate = _advocate.replace("Advocates Appeared :", "Advocates Appeared :<br/>");
                                _advocate = _advocate.replace(";", "<br/>");
                            }
                            stb.append("<span>" + _advocate.replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</span><br/><br/>");
                        }
                    }

                    if (!obj.get("content_store").getAsString().isEmpty()) {
                        String html = obj.get("content_store").getAsString().replace("<#hPreTag#", "<").replace("</#hPreTag#", "</").replace("#hPostTag#>", ">");
                        while (html.contains(startIndex) && html.contains(endIndex)) {
                            String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                            html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                            hlCount += 1;
                        }

                        stb.append(html + "<br/>");
                    }
                }
                else if (displayOnly.equals("HeadNote")) {
                    if (!obj.get("citation_store").getAsString().isEmpty()) {
                        stb.append("<center><strong>" + obj.get("citation_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</strong></center>" + "<br/>");
                    }

                    if (!obj.get("judgementHeader_store").getAsString().isEmpty()) {
                        stb.append(obj.get("judgementHeader_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "<br/>");
                    }

                    if (!obj.get("summary_store").getAsString().isEmpty()) {
                        if (obj.get("summary_store").getAsString().trim().length() > 3) {
                            String html = obj.get("summary_store").getAsString();
                            while (html.contains(startIndex) && html.contains(endIndex)) {
                                String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                                html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                                hlCount += 1;
                            }
                            stb.append("<strong>" + html + "</strong><br/>");
                        }
                    }

//                    if (!obj.get("summary_store").getAsString().isEmpty())
//                    {stb.append(obj.get("summary_store").getAsString() + "<br/>");}

                    if (!obj.get("acts_store").getAsString().isEmpty()) {
                        stb.append("<span style=\"color:blue\"><strong>" + obj.get("acts_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</strong></span><br/><br/>");
                    }

                    if (!obj.get("impNotes_store").getAsString().isEmpty()) {
                        stb.append(obj.get("impNotes_store").getAsString() + "<br/>");
                    }
                }
                else if (displayOnly.equals("Analysis")) {
//                    if (!obj.get("judgementHeader_store").getAsString().isEmpty()) {
//                        stb.append(obj.get("judgementHeader_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "<br/>");
//                    }
                    stb.append("<table style=\"text-align:left;\"><col width=\"20%\"><col width=\"80%\">");
                    if (!obj.get("title").getAsString().isEmpty()) {
                        stb.append("<tr><th style=\"font-weight: bold;background-color: #4286f4;\">Case Name</th><th style=\"font-weight: normal;\">"+obj.get("title").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "")+"</th></tr>");
                    }
                    if (!obj.get("judge").getAsString().isEmpty()) {
                        stb.append("<tr><th style=\"font-weight: bold;background-color: #5041f4;color:#ffffff;\">Judge Name</th><th style=\"font-weight: normal;\">"+obj.get("judge").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "")+"</th></tr>");
                    }
                    if (!obj.get("advocates").getAsString().isEmpty()) {
                        String _advocate = obj.get("advocates").getAsString();
                        if (_advocate.contains("\r\n")){
                            _advocate = _advocate.replace("\r\n", "<br/>");
                        }else{
                            _advocate = _advocate.replace("Advocates Appeared :", "Advocates Appeared :<br/>");
                            _advocate = _advocate.replace(";", "<br/>");
                        }

                        stb.append("<tr><th style=\"font-weight: bold;background-color: #5041f4;color:#ffffff;\">Advocates appeared</th><th style=\"font-weight: normal;\">" + _advocate.replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</th></tr>");
                    }
                    if (!obj.get("result").getAsString().isEmpty()) {
                        stb.append("<tr><th style=\"font-weight: bold;background-color: #5041f4;color:#ffffff;\">Results</th><th style=\"font-weight: normal;\">"+obj.get("result").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "")+"</th></tr>");
                    }
                    if (!obj.get("citation_store").getAsString().isEmpty()) {
                        stb.append("<tr><th style=\"font-weight: bold;background-color: #5041f4;color:#ffffff;\">Citations</th><th style=\"font-weight: normal;\">"+obj.get("citation_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "")+"</th></tr>");
                    }
                    if (!obj.get("acts_store").getAsString().isEmpty()) {
                        stb.append("<tr><th style=\"font-weight: bold;background-color: #5041f4;color:#ffffff;\">Acts & Sections</th><th style=\"font-weight: normal;\">"+obj.get("acts_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "")+"</th></tr>");
                    }
                    if (!obj.get("impNotes_store").getAsString().isEmpty()) {
                        stb.append("<tr><th style=\"font-weight: bold;background-color: #5041f4;color:#ffffff;\">Important Point</th><th style=\"font-weight: normal;\">"+obj.get("impNotes_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "")+"</th></tr>");
                    }
                    stb.append("</table>");
//                    if (!obj.get("citation_store").getAsString().isEmpty()) {
//                        stb.append("<center><strong>" + obj.get("citation_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</strong></center>" + "<br/>");
//                    }
//
//                    if (!obj.get("judgementHeader_store").getAsString().isEmpty()) {
//                        stb.append(obj.get("judgementHeader_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "<br/>");
//                    }
//
//                    if (!obj.get("advocates").getAsString().isEmpty()) {
//                        stb.append(obj.get("advocates").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "<br/><br/>");
//                    }
//
//                    if (!obj.get("acts_store").getAsString().isEmpty()) {
//                        stb.append("<span style=\"color:blue\"><strong>" + obj.get("acts_store").getAsString().replaceAll("#hPreTag#", "").replaceAll("#hPostTag#", "") + "</strong></span><br/><br/>");
//                    }
//
//                    if (!obj.get("result").getAsString().isEmpty()) {
//                        stb.append(obj.get("result").getAsString() + "<br/><br/>");
//                    }
                }
                else if (displayOnly.equals("Citator")) {
                    String caseCitator = ServiceHelper.getCitatorByCaseId(obj.get("caseId").getAsString());
                    if (!caseCitator.isEmpty()) {
                        int _citedIn = ServiceHelper.getCitatorCountByCaseId(obj.get("caseId").getAsString());
                        stb.append("<span style=\"background-color:blue;color:white\"><strong>Case Citator, <i>Total Count :</i>" + _citedIn + "</strong></span><br/><br/>" + caseCitator + "<br/><br/>");
                    }
                }
                else if (displayOnly.equals("Referred")) {
                    String casesReferred = ServiceHelper.getCaseReferedByCaseId(obj.get("caseId").getAsString());
                    if (!casesReferred.isEmpty()) {
                        stb.append("<span style=\"background-color:blue;color:white\"><strong>Cases referred:</strong></span><br/><br/>" + casesReferred + "<br/><br/>");
                    }
                }
                else if (displayOnly.equals("Judicial")) {
                    String casesReferred = ServiceHelper.getJudicialCitatorByCaseId(obj.get("caseId").getAsString());
                    if (!casesReferred.isEmpty()) {
                        stb.append("<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>.accordion{background-color: #eee; color: #444; cursor: pointer; padding: 18px; width: 90%; border: none; text-align: left; outline: none; font-size: 15px; transition: 0.4s;}.active, .accordion:hover{background-color: #ccc;}.panel{padding: 0 18px; display: none; background-color: white; overflow: hidden;}</style></head><body>");
                        stb.append("<center><span style=\"background-color:blue;color:white\"><strong>Judicial Analysis:</strong></span><br/><br/>" + casesReferred + "</center>");
                        stb.append("<script>var acc=document.getElementsByClassName(\"accordion\");var i;for (i=0; i < acc.length; i++){acc[i].addEventListener(\"click\", function(){this.classList.toggle(\"active\"); var panel=this.nextElementSibling; if (panel.style.display===\"block\"){panel.style.display=\"none\";}else{panel.style.display=\"block\";}});}</script></body></html>");
//                        stb.append("<span style=\"background-color:blue;color:white\"><strong>Judicial Analysis:</strong></span><br/><br/>" + casesReferred + "<br/><br/>");
                    }


//                    String casesReferred = ServiceHelper.getJudicialByCaseId(obj.get("caseId").getAsString());
//                    if (!casesReferred.isEmpty()) {
//                        stb.append("<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>.accordion{background-color: #eee; color: #444; cursor: pointer; padding: 18px; width: 90%; border: none; text-align: left; outline: none; font-size: 15px; transition: 0.4s;}.active, .accordion:hover{background-color: #ccc;}.panel{padding: 0 18px; display: none; background-color: white; overflow: hidden;}</style></head><body>");
//                        stb.append("<center><span style=\"background-color:blue;color:white\"><strong>Judicial Analysis:</strong></span><br/><br/>" + casesReferred + "</center>");
//                        stb.append("<script>var acc=document.getElementsByClassName(\"accordion\");var i;for (i=0; i < acc.length; i++){acc[i].addEventListener(\"click\", function(){this.classList.toggle(\"active\"); var panel=this.nextElementSibling; if (panel.style.display===\"block\"){panel.style.display=\"none\";}else{panel.style.display=\"block\";}});}</script></body></html>");
////                        stb.append("<span style=\"background-color:blue;color:white\"><strong>Judicial Analysis:</strong></span><br/><br/>" + casesReferred + "<br/><br/>");
//                    }
                }
            }
            else if (obj.get("DocType").getAsString().equals("WordPhrase")) {
                btnHeadNote.setVisible(false);
                btnFullCase.setVisible(false);
                btnAnalysis.setVisible(false);
                btnCitator.setVisible(false);
                btnJudicial.setVisible(false);

                if (!obj.get("title").getAsString().isEmpty()) {
                    stb.append("<center><strong>" + obj.get("title").getAsString() + "</strong></center>" + "<br/>");
                }

                if (!obj.get("content_store").getAsString().isEmpty()) {
                    String startIndex = "#hPreTag#";
                    String endIndex = "#hPostTag#";
                    Integer hlCount = 1;
                    String html = obj.get("content_store").getAsString();

                    while (html.contains(startIndex) && html.contains(endIndex)) {
                        String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                        html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                        hlCount += 1;
                    }

                    stb.append(html + "<br/>");
                }
            }
            else if (obj.get("DocType").getAsString().equals("BareActs")) {

                btnHeadNote.setVisible(false);
                btnFullCase.setVisible(true);
                btnFullCase.setText("FullAct");
                btnAnalysis.setVisible(false);
                btnCitator.setVisible(false);
                btnJudicial.setVisible(false);

                if (displayOnly.equals("FullAct")) {
                    btnFullCase.setCursor(Cursor.WAIT);
                    stb.append(ServiceHelper.getCentralActContentById(obj.get("id").getAsString(), ""));
//                    webViewContent.getEngine().loadContent(htmlContent, "text/html");
                    btnFullCase.setCursor(Cursor.DEFAULT);
                } else {
                    if ((!obj.get("Type").getAsString().isEmpty()) && (!obj.get("title").getAsString().isEmpty())) {
                        stb.append("<center><strong>" + obj.get("Type").getAsString() + " of " + obj.get("title").getAsString() + "</strong></center>" + "<br/>");
                    } else if (!obj.get("title").getAsString().isEmpty()) {
                        stb.append("<center><strong>" + obj.get("title").getAsString() + "</strong></center>" + "<br/>");
                    }

                    if (!obj.get("content_store").getAsString().isEmpty()) {
                        String startIndex = "#hPreTag#";
                        String endIndex = "#hPostTag#";
                        Integer hlCount = 1;
                        String html = obj.get("content_store").getAsString();

                        while (html.contains(startIndex) && html.contains(endIndex)) {
                            String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                            html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                            hlCount += 1;
                        }

                        stb.append(html + "<br/>");
                    }
                }
            }
            else if (obj.get("DocType").getAsString().equals("Commentary")) {
                btnHeadNote.setVisible(false);
                btnFullCase.setVisible(true);
                btnFullCase.setText("FullAct");
                btnAnalysis.setVisible(false);
                btnCitator.setVisible(false);
                btnJudicial.setVisible(false);

                if ((!obj.get("Type").getAsString().isEmpty()) && (!obj.get("title").getAsString().isEmpty())) {
                    stb.append("<center><strong>" + obj.get("Type").getAsString() + " of " + obj.get("title").getAsString() + "</strong></center>" + "<br/>");
                } else if (!obj.get("title").getAsString().isEmpty()) {
                    stb.append("<center><strong>" + obj.get("title").getAsString() + "</strong></center>" + "<br/>");
                }
                if (!obj.get("content_store").getAsString().isEmpty()) {
                    String startIndex = "#hPreTag#";
                    String endIndex = "#hPostTag#";
                    Integer hlCount = 1;
                    String html = obj.get("content_store").getAsString();

                    while (html.contains(startIndex) && html.contains(endIndex)) {
                        String hlTag = html.substring(html.indexOf(startIndex), html.indexOf(endIndex, html.indexOf(startIndex) + startIndex.length()) + endIndex.length());
                        html = html.replaceFirst(hlTag, "<span id=\"hl" + hlCount + "\" style=\"background-color:lightblue;\">" + hlTag.replace(startIndex, "").replace(endIndex, "") + "</span>");
                        hlCount += 1;
                    }
                    stb.append(html + "<br/>");
                }
            }

            if (!obj.get("title").getAsString().isEmpty()) {
                titleText = obj.get("title").getAsString();
                titleText = titleText.replace(",", " ").replace(";", " ").replace(".", " ").replace(" ", "_");
            } else {
                titleText = "SupremeToday";
            }

            judgementHTML = stb.toString();

//            String temp2="<html><head><meta charset=\"UTF-8\">" + javaScript + judgementCSS + "</head><body onkeydown=keyup()>" + htmlHeader + judgementHTML + "</body></html>";
//            try {
//                judgementHTML = new String(Files.readAllBytes(Paths.get("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp.html")));
//
//
////                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
////                        new FileOutputStream("D:\\Addie\\SupremeToday\\Sample\\64250.htm"), "utf-8"))) {
////                    writer.write(temp2);
////                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
//                    (webView, message, lineNumber, sourceId)-> System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message)
//            );
//
////            engine.load("https://translate.google.co.in/");
////            engine.loadContent(judgementHTML, "text/html");
//            System.out.println("engine.isJavaScriptEnabled()");
//            System.out.println(engine.isJavaScriptEnabled());

            judgementHTML = judgementHTML.replace("<font face=\"Kruti Dev 011\">", "<em class=\"KrutiDev_hindi_text\" style=\"font-family:Kruti Dev 010;\">");
            judgementHTML = judgementHTML.replace("</font>", "</em>");
            judgementHTML = judgementHTML.replace("<a name=\"", "<a id=\"");

//            if (googleTranslatorScript.contains("<script src=\"http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>")){
//                try {
//                    String javaScript = new String(Files.readAllBytes(Paths.get(Queries.GOOGLE_TRANSLATOR_SCRIPT)));
//                    System.out.println(javaScript);
//                    googleTranslatorScript = googleTranslatorScript.replace("<script src=\"http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>", "<script>" + javaScript + "</script>");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            String _html = "";
            if (isWhiteListExist) {
                String overruledTitle = "<div align=\"center\">This Judgement has been Overruled, <a href='overruled:"+docId+"'>Click Here to View</a></div>";
                _html = "<html><head><meta charset=\"UTF-8\">" + googleTranslatorScript + javaScript + judgementCSS + "</head><body style=\"padding-right:20;\" onkeydown=keyup()>" + htmlHeader + googleTranslatorDiv + overruledTitle + judgementHTML + "</body></html>";

//                engine.loadContent(_html, "text/html");
//                engine.loadContent("<html><head><meta charset=\"UTF-8\">" + javaScript + judgementCSS + "</head><body onkeydown=keyup()><div style=\"background-color:" + Queries.OVERRULED_BACKGROUND_COLOR + "\">" + htmlHeader + overruledTitle + judgementHTML + "</body></html>", "text/html");
            } else {
                _html = "<html><head><meta charset=\"UTF-8\">" + googleTranslatorScript + javaScript + judgementCSS + "</head><body style=\"padding-right:20;\" onkeydown=keyup()>" + htmlHeader + googleTranslatorDiv + judgementHTML + "</body></html>";
            }


//            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//            new FileOutputStream(Queries.GOOGLE_TRANSLATOR_HTML), "utf-8"))) {
//                writer.write(_html);
////                File f = new File(Queries.GOOGLE_TRANSLATOR_HTML);
////                URL url = this.getClass().getResource(Queries.GOOGLE_TRANSLATOR_HTML);
//                System.out.println(f.toURI().toString());
//                engine.load(f.toURI().toString());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

                engine.loadContent(_html, "text/html");
//                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//                        new FileOutputStream("D:\\64250.htm"), "utf-8"))) {
//                    writer.write(_html);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            judgementId = docId;
            if (checkIfTruePrintAvailable(docId)){
//                System.out.println("btnTruePrint.disableProperty().setValue(false);");
                btnTruePrint.disableProperty().setValue(false);
            }else{
//                System.out.println("btnTruePrint.disableProperty().setValue(true);");
                btnTruePrint.disableProperty().setValue(true);
            }

        }
        highLightCount = 1;
        href_query = "";
    }

    private boolean checkIfTruePrintAvailable(String id){
        String url = Queries.IS_TRUE_PRINT_AVAIL_API + id;

        URL myurl = null;
        try {
            myurl = new URL(url);

            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            StringBuilder content_sb = null;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content_sb = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content_sb.append(line);
                    content_sb.append(System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (Boolean.parseBoolean(content_sb.toString().trim()) == true){
                return true;
            }
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean fireClick(){
//        System.out.println("fireClick");
        engine.executeScript("location.href='#Introduction1';");
        engine.executeScript("location.hash='#Introduction1';");
        engine.executeScript("scrollTo('Introduction1');");
        return true;
    }

    private String GetCrossCitationFromCitations(String _citations, boolean IsMainCitationOnly) {
        List<String> _citationList = new ArrayList<String>(Arrays.asList(_citations.split(";")));

        StringBuilder _stbCitation = new StringBuilder();
        for(int i=0;i<_citationList.size();i++){
            String _citation = _citationList.get(i);
            if (IsMainCitationOnly){
                if (_citation.contains(" Supreme(")){
                    _stbCitation.append(_citation);
                }
            }else{
                if (!_citation.contains(" Supreme(")){
                    _stbCitation.append(_citation + "; ");
                }
            }
        }

//        StringBuilder _stbCitation = new StringBuilder();
//        for(int i=0;i<_citationList.size();i++){
//            String _citation = _citationList.get(i);
//            if (IsMainCitationOnly){
//                if (_citation.contains(" STO(")){
//                    _stbCitation.append(_citation);
//                }
//            }else{
//                if (!_citation.contains(" STO(")){
//                    _stbCitation.append(_citation + "; ");
//                }
//            }
//        }

        if (_stbCitation.toString().endsWith("; ")){
            _stbCitation.delete(_stbCitation.toString().length() - 2, _stbCitation.toString().length());
        }
        return _stbCitation.toString().trim();
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

            if (judgementHTML.contains("Kruti")){
                judgementHTML = judgementHTML.replace("<font face=\"Kruti Dev 011\">", "<font face=\"K11\">").
                        replace("<font class=\"krutiFontClass\">", "<font face=\"K11\">").replace("<em class=\"KrutiDev_hindi_text\" style=\"font-family:Kruti Dev 010;\">", "<font face=\"K11\">");
            }

            controller.setHTML(htmlHeader + judgementHTML);

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
            return true;
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return true;
        }
    }

    public boolean showBookmarkAddDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_bookmark_add.fxml"));
            DialogBookmarkAddController controller = new DialogBookmarkAddController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add to bookmark");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

//             Set the person into the controller
//            DialogBookmarkAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            controller.initData(docId, titleText, docType);
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

    public void initData(String searchQuery, String filterQuery, String SortBy, String hlFields, int startFrom) {
        search_query = searchQuery;
        filter_query = filterQuery;
        sortBy = SortBy;
        start_from = startFrom;
        hl_fields = hlFields;
        loadHTML("FullCase");
    }
}

