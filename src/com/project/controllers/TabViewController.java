package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.OSValidator;
import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.interfaces.*;
import com.project.model.HistoryModel;
import com.project.model.LiveUpdateModel;
import com.project.utils.Utils;
import com.project.viewerdemo.ViewerDemo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class TabViewController implements Initializable {//, ClickEventHandler {//,JudgementSearchController.redirectToResultPage{

    @FXML
    TabPane tabpan, advance_search;

    @FXML
    private Hyperlink hpDictionary, hpBookmarks, hpHistory, hpFeedBack, hpHelpManual, hpSupremeCourtofIndia, hpCauseList, hpAbout, hpPrintSetting, hpLiveUpdate;

    /*initialize topical dialog interface*/
    public static TopicalDialogLitener topicalDialogLitener;

    public void setTopicalDialogLitener(TopicalDialogLitener topicalDialogLitener) {
        TabViewController.topicalDialogLitener = topicalDialogLitener;
    }

    /*initialize history dialog interface*/
    public static HistoryDialogLitener dialogListener;

    public void setHistoryDialogListener(HistoryDialogLitener dialogListener) {
        TabViewController.dialogListener = dialogListener;
    }

    /*initialize bookmark dialog interface*/
    public static BookmarkDialogLitener bookmarkDialogLitener;

    public void setBookmarkDialogListener(BookmarkDialogLitener bookmarkDialogLitener) {
        TabViewController.bookmarkDialogLitener = bookmarkDialogLitener;
    }

    public static SettingDialogLitener settingDialogLitener;

    public void setSettingDialogListener(SettingDialogLitener settingDialogLitener) {
        TabViewController.settingDialogLitener = settingDialogLitener;
    }

    private static final List<ResetClickEventOnGlobalSearchListener> globalSearchResetListeners = Lists.newArrayList();

    public void clickResetOnGlobalSearchListener(ResetClickEventOnGlobalSearchListener listener) {
        Preconditions.checkNotNull(listener);
        globalSearchResetListeners.add(listener);
    }

    private static final List<ResetClickEventOnAdvanceSearchListener> advanceSearchListeners = Lists.newArrayList();

    public void clickResetOnAdvanceSearchListener(ResetClickEventOnAdvanceSearchListener listener) {
        Preconditions.checkNotNull(listener);
        advanceSearchListeners.add(listener);
    }

    public static Number PRE_TAB_NO = 0;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceHelper.alterPrintSetting();
        ServiceHelper.getPrintSetting();

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            hpDictionary.setVisible(Boolean.FALSE);
            hpCauseList.setVisible(Boolean.FALSE);
            hpDictionary.setStyle("-fx-text-fill: steelblue");
            hpBookmarks.setStyle("-fx-text-fill: steelblue");
            hpHistory.setStyle("-fx-text-fill: steelblue");
//            hpFeedBack.setStyle("-fx-text-fill: steelblue");
            hpFeedBack.setVisible(Boolean.FALSE);
            hpHelpManual.setVisible(Boolean.FALSE);
            hpSupremeCourtofIndia.setVisible(Boolean.FALSE);
            hpCauseList.setStyle("-fx-text-fill: steelblue");
            hpAbout.setVisible(Boolean.FALSE);
            hpPrintSetting.setStyle("-fx-text-fill: steelblue");
            hpLiveUpdate.setStyle("-fx-text-fill: steelblue");
        }



//        if (ServiceHelper.isCommentaryHide()!=1)
//        {
//            tabpan.getTabs().remove(3);
//        }

//        Stage stage = (Stage) borderpan.getScene().getWindow();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/home_screen.fxml"));
            HomeSearchController controller = new HomeSearchController();
            loader.setController(controller);
            Tab tab = new Tab("HOME");
            tab.setContent(loader.load());
            tabpan.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/year_wise.fxml"));
//            YearWiseController controller = new YearWiseController();
            loader.setController(new YearWiseController());
            Tab tab;
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                tab = new Tab("LATEST CASE");
            }else{
                tab = new Tab("YEAR WISE");
            }
            tab.setContent(loader.load());
            tabpan.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/central_act.fxml"));
                CentralActController controller = new CentralActController();
                loader.setController(controller);
                Tab tab = new Tab("BARE ACTS");
                tab.setContent(loader.load());
                tabpan.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
            if (ServiceHelper.isCommentaryHide()==1)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/commentary.fxml"));
                    CommentaryController controller = new CommentaryController();
                    loader.setController(controller);
                    Tab tab = new Tab("COMMENTARY");
                    tab.setContent(loader.load());
                    tabpan.getTabs().add(tab);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/word_phrases.fxml"));
                WordPhrasesController controller = new WordPhrasesController();
                loader.setController(controller);
                Tab tab = new Tab("WORD PHRASES");
                tab.setContent(loader.load());
                tabpan.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/overruled.fxml"));
//                OverRuledController controller = new OverRuledController();
//                loader.setController(controller);
//                Tab tab = new Tab("OVERRULED");
//                tab.setContent(loader.load());
//                tabpan.getTabs().add(tab);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/overruled.fxml"));
            OverRuledController controller = new OverRuledController();
            loader.setController(controller);
            Tab tab = new Tab("OVERRULED");
            tab.setContent(loader.load());
            tabpan.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/global_search.fxml"));
            GlobalSearchController controller = new GlobalSearchController();
            loader.setController(controller);
            Tab tab = new Tab("GLOBAL SEARCH");
            tab.setContent(loader.load());
            tabpan.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/advance_search.fxml"));
            AdvanceSearchController controller = new AdvanceSearchController();
            loader.setController(controller);
            Tab tab = new Tab("ADVANCE SEARCH");
            tab.setContent(loader.load());
            tabpan.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/central_act.fxml"));
                CentralActController controller = new CentralActController();
                loader.setController(controller);
                Tab tab = new Tab("BARE ACTS");
                tab.setContent(loader.load());
                tabpan.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

           /*Hyperlink click events*/
        hpDictionary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showDictionaryDialogWindow();
            }
        });

        hpBookmarks.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showBookmarkDialogWindow();
            }
        });

        hpHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showHistoryDialogWindow();
            }
        });

        hpFeedBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showFeedbackDialogWindow();
            }
        });

        hpHelpManual.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ViewerDemo.openFile(Queries.HELP_MANUAL_PATH);
//                showDialogWindow("/com/project/ui/dialog_about_us.fxml", Queries.TITLE_MAIN_WINDOW);
            }
        });

        hpSupremeCourtofIndia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(Desktop.isDesktopSupported())
                {
                    try {
                        Desktop.getDesktop().browse(new URI("http://www.sci.gov.in/"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        hpCauseList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(Desktop.isDesktopSupported())
                {
                    try {
                        Desktop.getDesktop().browse(new URI("http://www.sci.nic.in/newcl.htm"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        hpAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showDialogWindow("/com/project/ui/dialog_about_us.fxml", Queries.TITLE_ABOUT_US_WINDOW);
            }
        });

//        hpJharkhandPoliceManual.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                showDialogWindow("/com/project/ui/dialog_about_us.fxml", Queries.TITLE_ABOUT_US_WINDOW);
//            }
//        });

        hpPrintSetting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showPrintSettingDialogWindow();
            }
        });

        hpLiveUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showLiveUpdateDialogWindow();
//                showPrintSettingDialogWindow();
            }
        });

        //set tab selection first time
        SingleSelectionModel<Tab> selectionModel = tabpan.getSelectionModel();
//        if (selectionModel.getSelectedIndex() == 0) {
        selectionModel.select(0);
//        }

//        new HistoryKeywordController().applyHistoryKeywordInterface(new HistorySearchKeywordListener() {
//            @Override
//            public void openHistoryResultListener(String historyKeyword) {
//
//            }
//        });

        new CentralActController().setJudgementSearchFromAct(new JudgementSearchFromAct() {
            @Override
            public void openAdvanceSearchView(String actTitle, String sectionTitle) {
                if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                    tabpan.getSelectionModel().select(3);
                }else{
                    if (ServiceHelper.isCommentaryHide()!=1)
                    {
                        tabpan.getSelectionModel().select(6);
                    }else{
                        tabpan.getSelectionModel().select(7);
                    }
                }
            }
        });

        new WordPhrasesController().setJudgementSearchFromAct(new JudgementSearchFromAct() {
            @Override
            public void openAdvanceSearchView(String actTitle, String sectionTitle) {
                if (ServiceHelper.isCommentaryHide()!=1)
                {
                    tabpan.getSelectionModel().select(5);
                }else{
                    tabpan.getSelectionModel().select(6);
                }
            }
        });

        tabpan.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                    if (ServiceHelper.isCommentaryHide()!=1) {
                        if (newValue.equals(3)) {
                            Queries.GLOBAL_OR_ADVANCE = "GLOBAL";
                        }else if (newValue.equals(4)){
                            Queries.GLOBAL_OR_ADVANCE="ADVANCE";
                        }else{
                            Queries.GLOBAL_OR_ADVANCE="YEAR";
                        }
                    }else{
                        if (newValue.equals(4)){
                            Queries.GLOBAL_OR_ADVANCE="GLOBAL";
                        }else if (newValue.equals(5)){
                            Queries.GLOBAL_OR_ADVANCE="ADVANCE";
                        }else{
                            Queries.GLOBAL_OR_ADVANCE="YEAR";
                        }
                    }
                }else{
                    if (ServiceHelper.isCommentaryHide()!=1) {
                        if (newValue.equals(5)) {
                            Queries.GLOBAL_OR_ADVANCE = "GLOBAL";
                        }else if (newValue.equals(6)){
                            Queries.GLOBAL_OR_ADVANCE="ADVANCE";
                        }else{
                            Queries.GLOBAL_OR_ADVANCE="YEAR";
                        }
                    }else{
                        if (newValue.equals(6)){
                            Queries.GLOBAL_OR_ADVANCE="GLOBAL";
                        }else if (newValue.equals(7)){
                            Queries.GLOBAL_OR_ADVANCE="ADVANCE";
                        }else{
                            Queries.GLOBAL_OR_ADVANCE="YEAR";
                        }
                    }
                }

//                if (ServiceHelper.isCommentaryHide()!=1) {
//                    if (newValue.equals(5)) {
//                        Queries.GLOBAL_OR_ADVANCE = "GLOBAL";
//                    }else if (newValue.equals(6)){
//                        Queries.GLOBAL_OR_ADVANCE="ADVANCE";
//                    }else{
//                        Queries.GLOBAL_OR_ADVANCE="YEAR";
//                    }
//                }else{
//                    if (newValue.equals(6)){
//                        Queries.GLOBAL_OR_ADVANCE="GLOBAL";
//                    }else if (newValue.equals(7)){
//                        Queries.GLOBAL_OR_ADVANCE="ADVANCE";
//                    }else{
//                        Queries.GLOBAL_OR_ADVANCE="YEAR";
//                    }
//                }

//                PRE_TAB_NO = oldValue;
//                ConstantsClass.isSearchInSearch = false;

                // do something...
//                System.out.println("changed" + newValue);
//                ConstantsClass.SELECTED_TAB_INDEX =(Integer)newValue;
//                Parent root = (Parent) newValue.getContent();

//                //open history dialog
//                if (newValue.intValue() == 6) {
//                    tabpan.getSelectionModel().select(oldValue.intValue());
//                    dialogListener.showDialog();
//                }
//
//                //Open bookmark dialog
//                if (newValue.intValue() == 7) {
//                    tabpan.getSelectionModel().select(oldValue.intValue());
//                    bookmarkDialogLitener.showBookmarkDialog();
//                }
//
//                //Open Topical dialog
//                if (newValue.intValue() == 5) {
//                    tabpan.getSelectionModel().select(oldValue.intValue());
//                    topicalDialogLitener.showTopicalDialog();
//                }
//
//                //Open Topical dialog
//                if (newValue.intValue() == 8) {
//                    tabpan.getSelectionModel().select(oldValue.intValue());
//                    settingDialogLitener.showSettingDialog();
//                }
            }
        });

        new JudgementViewController().setSearchFromWebViewKeyword(new SearchFromWebViewEventHandler() {
            @Override
            public void handleSearchKeyword(String searchKeywords) {
                if (ServiceHelper.isCommentaryHide()!=1) {
//                    if (tabpan.getSelectionModel().getSelectedIndex()==5){
//                        Queries.GLOBAL_OR_ADVANCE="GLOBAL";
//                    }else{
//                        Queries.GLOBAL_OR_ADVANCE="ADVANCE";
//                    }
                    if (Queries.GLOBAL_OR_ADVANCE.equals("YEAR")){
                        tabpan.getSelectionModel().select(5);
                    }
                }else{
//                    if (tabpan.getSelectionModel().getSelectedIndex()==6){
//                        Queries.GLOBAL_OR_ADVANCE="GLOBAL";
//                    }else{
//                        Queries.GLOBAL_OR_ADVANCE="ADVANCE";
//                    }
                    if (Queries.GLOBAL_OR_ADVANCE.equals("YEAR")){
                        tabpan.getSelectionModel().select(6);
                    }
                }
            }
        });
//
//        new JudgementViewController().setSearchInSearchFromWebViewKeyword(new SearchInSearchFromWebViewEventHandler() {
//            @Override
//            public void handleSearchKeyword(String searchKeywords) {
//                if (ServiceHelper.isCommentaryHide()!=1) {
//                    if (tabpan.getSelectionModel().getSelectedIndex()==5){
//                        Queries.GLOBAL_OR_ADVANCE="GLOBAL";
//                    }else{
//                        Queries.GLOBAL_OR_ADVANCE="ADVANCE";
//                    }
////                    tabpan.getSelectionModel().select(5);
//                }else{
//                    if (tabpan.getSelectionModel().getSelectedIndex()==6){
//                        Queries.GLOBAL_OR_ADVANCE="GLOBAL";
//                    }else{
//                        Queries.GLOBAL_OR_ADVANCE="ADVANCE";
//                    }
////                    tabpan.getSelectionModel().select(6);
//                }
////                if (ServiceHelper.isCommentaryHide()!=1)
////                {tabpan.getSelectionModel().select(5);}else{tabpan.getSelectionModel().select(6);}
//            }
//        });

//        /*Search from home to Judgement OR Act*/
//        new HomeSearchController().setHomeSearchListener(new HomeSearchListener() {
//            @Override
//            public void openHomeSearchResult(String qry, boolean isJudgement) {
//                if (isJudgement) {
//                    tabpan.getSelectionModel().select(2);
//                } else {
//                    tabpan.getSelectionModel().select(3);
//                }
//            }
//        });

//        new JudgementSearchController().addListener(new ClickEventHandler() {
//            @Override
//            public void handleSeachClick(String SearchQuery, ObservableList<String> listActsForHTMLHighlight) {
//                tabpan.getSelectionModel().select(2);
//            }
//        });

//        new ActResultViewController().setJudgementSearchFromAct(new JudgementSearchFromAct() {
//            @Override
//            public void openActResultView(String qry, String judgementId, String href, boolean isViewJ) {
//                tabpan.getSelectionModel().select(2);
//            }
//        });

//        new JudgementResultViewController().setActSearchFromJudgementInterface(new ActSearchFromJudgementHTMLListener() {
//            @Override
//            public void openActResultViewListener(String qry) {
//                tabpan.getSelectionModel().select(3);
//            }
//        });

//        tabpan.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
//
//
////                PRE_TAB_NO = oldValue;
////                ConstantsClass.isSearchInSearch = false;
//
//                // do something...
////                System.out.println("changed" + newValue);
////                ConstantsClass.SELECTED_TAB_INDEX =(Integer)newValue;
////                Parent root = (Parent) newValue.getContent();
//
////                //open history dialog
////                if (newValue.intValue() == 6) {
////                    tabpan.getSelectionModel().select(oldValue.intValue());
////                    dialogListener.showDialog();
////                }
////
////                //Open bookmark dialog
////                if (newValue.intValue() == 7) {
////                    tabpan.getSelectionModel().select(oldValue.intValue());
////                    bookmarkDialogLitener.showBookmarkDialog();
////                }
////
////                //Open Topical dialog
////                if (newValue.intValue() == 5) {
////                    tabpan.getSelectionModel().select(oldValue.intValue());
////                    topicalDialogLitener.showTopicalDialog();
////                }
////
////                //Open Topical dialog
////                if (newValue.intValue() == 8) {
////                    tabpan.getSelectionModel().select(oldValue.intValue());
////                    settingDialogLitener.showSettingDialog();
////                }
//            }
//        });

//        new JudgementResultViewController().setGoBackInterface(new GoBackLitener() {
//            @Override
//            public void goBackClickLitener() {
//                tabpan.getSelectionModel().select(Integer.parseInt(PRE_TAB_NO + ""));
//            }
//        });

      /*OPEN judgement result view, called from history table click event*/
        new HistoryController().setHistorySearchInterface(new HistorySearchListener() {
            @Override
            public void openHistoryResultListener(HistoryModel historyModel){
                if (historyModel.getsearch_type().equals("Advance"))
                {
                    if (ServiceHelper.isCommentaryHide()!=1)
                    {
                        tabpan.getSelectionModel().select(6);
                    }else{
                        tabpan.getSelectionModel().select(7);
                    }
                }else{
                    if (ServiceHelper.isCommentaryHide()!=1)
                    {
                        tabpan.getSelectionModel().select(5);
                    }else{
                        tabpan.getSelectionModel().select(6);
                    }
                }
            }
        });

        new BookmarkController().setBookmarkSearchInterface(new BookmarkSearchListener() {
            @Override
            public void openBookmarkResultListener(String judgementId) {
                if (ServiceHelper.isCommentaryHide()!=1)
                {
                    tabpan.getSelectionModel().select(1);
                }else{
                    tabpan.getSelectionModel().select(2);
                }
            }
        });


//
//        Task longTask = new Task<Void>() {
//            @Override public Void call() {
//                String url = Queries.LICENSE_EXPIRE_DATE_API;
//
//                try {
//                    URL myurl = new URL(url);
//                    HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
//
//                    con.setDoOutput(true);
//                    con.setRequestMethod("GET");
//                    con.setRequestProperty("User-Agent", "Java client");
//                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
////                    try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
////                        wr.write(postDataBytes);
////                    }
//
//                    StringBuilder content_sb;
//
//                    try (BufferedReader in = new BufferedReader(
//                            new InputStreamReader(con.getInputStream()))) {
//
//                        String line;
//                        content_sb = new StringBuilder();
//
//                        while ((line = in.readLine()) != null) {
//                            content_sb.append(line);
//                            content_sb.append(System.lineSeparator());
//                        }
//                    }
//
//                    System.out.println(content_sb.toString());
//                    updateMessage(content_sb.toString());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//
////            con.disconnect();
//                }
//
//                updateMessage("Updation completed");
//                return null;
//            }
//        };
//
////        primaryStage.titleProperty().bind(longTask.messageProperty());
//        new Thread(longTask).start();




//        new DialogTopicalController().setTopicalInterface(new TopicalInterfaceListener() {
//            @Override
//            public void openByTopicalListener(String str) {
//                tabpan.getSelectionModel().select(2);
//            }
//        });

//        new JudgementResultViewController().setSearchInSearchInterface(new SearchInSearchLitener() {
//            @Override
//            public void searchInSearchClickLitener(String oldQuery, String oldLabelQuery) {
//                tabpan.getSelectionModel().select(1);
//                ConstantsClass.isSearchInSearch = true;
//            }
//        });
    }

    private boolean showBookmarkDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_bookmark.fxml"));
            BookmarkController controller = new BookmarkController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Queries.TITLE_BOOKMARK_WINDOW);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
//            BookmarkController controller = loader.getController();
            controller.setDialogStage(dialogStage);

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

    public boolean showHistoryDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_history.fxml"));
            HistoryController controller = new HistoryController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Queries.TITLE_HISTORY_WINDOW);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
//            HistoryController controller = loader.getController();
            controller.setDialogStage(dialogStage);

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

    public boolean showFeedbackDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_feedback.fxml"));
            DialogFeedbackController controller = new DialogFeedbackController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Queries.APPLICATION_NAME);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
//            DialogFeedbackController controller = loader.getController();
            controller.setDialogStage(dialogStage);

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

    public boolean showPrintSettingDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_print_settings.fxml"));
            DialogPrintSettings controller = new DialogPrintSettings();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Print Setting");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
//            DialogPrintSettings controller = loader.getController();
            controller.setDialogStage(dialogStage);

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

    public boolean showLiveUpdateDialogWindow() {
        if (OSValidator.isWindows()){
            try {
                Runtime.getRuntime().exec("cmd /c start " + Queries.AUTO_UPDATE_EXE_FILE_SINGLEFILEPATH);
                Runtime.getRuntime().exec("cmd /c start " + Queries.AUTO_UPDATE_EXE_FILE_SINGLEFILEPATH);
//                Runtime.getRuntime().exec(Queries.AUTO_UPDATE_EXE_FILE);
//                Runtime.getRuntime().exec(Queries.AUTO_UPDATE_EXE_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_live_update.fxml"));
            DialogLiveUpdateController controller = new DialogLiveUpdateController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Queries.APPLICATION_NAME + " - Live Update");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
            controller.setDialogStage(dialogStage);

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
    }

    public boolean showDictionaryDialogWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_dictionary.fxml"));
            DialogDictionaryController controller = new DialogDictionaryController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Queries.TITLE_DICTIONARY);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
            controller.setDialogStage(dialogStage);

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
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return true;
        }
    }

    public boolean showDialogWindow(String fxmlPath, String dialogTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(dialogTitle);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            // Set the person into the controller
//            HistoryController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
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

//            if (!controller.isOkClicked()) {
//
//            }
//
//            return controller.isOkClicked();
            return true;
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return true;
        }
    }
}
