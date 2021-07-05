
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.google.common.collect.Multimap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.project.controllers.JudgementViewController;
import com.project.model.*;
import com.project.utility.SearchUtility;
import com.project.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author addie
 */

//class MyClass {
//    private Integer id;
//    private String name;
//    private Boolean valid;
//    //getters & setters
//}
//
//public class MyComparator implements Comparator<MyClass> {
//    @Override
//    public int compare(MyClass o1, MyClass o2) {
//        return o1.getId().compareTo(o2.getId());
//    }
//}

public class ResultViewHelper {
    public boolean showJudgementDialogWindow(int start_form, String search_query, String sort_by, String hl_fields, Multimap<String, String> filterBy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_judgement_view.fxml"));
            JudgementViewController controller = new JudgementViewController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Judgement View");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
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
            dialogStage.setHeight(screenBounds.getHeight());
            dialogStage.setWidth(screenBounds.getWidth());
//            dialogStage.setHeight(screenBounds.getHeight() * 98 / 100);
//            dialogStage.setWidth(screenBounds.getWidth() * 98 / 100);

            /*disabled maximaize and minimize except close use*/
//            dialogStage.initModality(Modality.APPLICATION_MODAL);
//            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setResizable(true);

//             Set the person into the controller
//            JudgementViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            String filter_query = "";
            for(String key : filterBy.keys()){
                String sub_filter_query="";
                for(String entry : filterBy.get(key)){
                    sub_filter_query += ""+ key + ":\"" + entry + "\" ";
                }
                filter_query += "+(" + sub_filter_query.trim() + ") ";
            }
            filter_query = filter_query.trim();
            controller.initData(search_query, filter_query, sort_by, hl_fields, start_form - 1);
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

    public int loadTableData(int startRow, int endRow, ObservableList<JudgementResultModel> data, WebView webViewDocView, TreeView TreeViewFacet, Label LabelTotalRecords, String search_query, String sort_by, Multimap<String, String> filterBy, String hl_fields, int totalResultCount) {
        data.clear();
        try {
            String filter_query = "";
            for(String key : filterBy.keys()){
                String sub_filter_query="";
                for(String entry : filterBy.get(key)){
                    sub_filter_query += ""+ key + ":\"" + entry + "\" ";
                }
                filter_query += "+(" + sub_filter_query.trim() + ") ";
            }
            filter_query = filter_query.trim();

//            for(Map.Entry<String, String> entry : filterBy.entries()){
//                filter_query += ""+ entry.getKey() + ":\"" + entry.getValue() + "\" ";
////                filter_query += "+"+ entry.getKey() + ":\"" + entry.getValue() + "\" ";
//
////                filter_query += "+"+ entry.getKey() + ":\"" + entry.getValue() + ";\" ";
////                filter_query += "+"+ entry.getKey() + ":" + entry.getValue() + " ";
//            }


            String facet_fields = "DocType,courttitle,finalyear,acts,judge,bench,result";
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                facet_fields = "DocType,courttitle,finalyear";
            }

            String fields = "title,decisionDate,Type,DocType,courttitle,cited,bench,caseId,impNotes_store,summary,summary_store,summary_new_store,acts_store,whitelist,overruled,isoverruled";

//            String fields = "title,decisionDate,Type,DocType,courttitle,cited,bench,caseId,impNotes_store,summary_store,acts_store,whitelist";
//            String hl_fields = "";
//            String hl_fields = "content_store";
            SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
            JsonObject jo = new JsonObject();
            try {
                jo = searchUtility.search(search_query, startRow - 1, endRow - (startRow - 1), sort_by, facet_fields, fields, hl_fields, filter_query.trim(), search_query);
            } catch (Exception ex) {
                Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            }

            JsonArray jData = jo.getAsJsonArray("docs");
            totalResultCount = Integer.parseInt(jo.get("numFound").toString().trim());
            LabelTotalRecords.setText("Total " + totalResultCount + " records found");

            JsonObject objFacets = jo.getAsJsonObject("facets");
            String[] facetArray = {"DocType","courttitle","finalyear","acts","judge","bench","result"};
            TreeViewFacet.setCellFactory(CheckBoxTreeCell.forTreeView());
            TreeViewFacet.setShowRoot(false);

            TreeItem<String> dummyRoot = new TreeItem<>();
            for (String facet : facetArray)
            {
                JsonArray jFacet = objFacets.getAsJsonArray(facet);

                if (jFacet!=null){
//                    TreeItem<String> rootItem = new TreeItem<>(facet);
                    CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(facet);
//                    rootItem.setIndependent(true);

                    if (facet.equals("courttitle")){
                        ArrayList<String> _tempTreeItems = new ArrayList<String>();
                        for (JsonElement ele : jFacet) {
                            JsonObject obj = (JsonObject) ele;
                            _tempTreeItems.add(obj.get("label").getAsString() + " (" + obj.get("value").getAsString() + ")");
                        }
                        Collections.sort(_tempTreeItems);
                        for (String _str : _tempTreeItems) {
                            CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(_str);
                            try{
                                if (filterBy.containsKey(facet) && filterBy.containsValue(_str.substring(0, _str.indexOf("(")).trim())){
                                    checkBoxTreeItem.setSelected(true);
                                }
                            } catch (Exception e) {}
                            rootItem.getChildren().add(checkBoxTreeItem);
                        }
                    } else if (facet.equals("acts")){
                        ArrayList<String> _tempTreeItems = new ArrayList<String>();
                        for (JsonElement ele : jFacet) {
                            JsonObject obj = (JsonObject) ele;
                            _tempTreeItems.add(obj.get("label").getAsString() + " (" + obj.get("value").getAsString() + ")");
                        }
                        Collections.sort(_tempTreeItems);
                        for (String _str : _tempTreeItems) {
                            CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(_str);
                            try{
                                if (filterBy.containsKey(facet) && filterBy.containsValue(_str.substring(0, _str.indexOf("(")).trim())){
                                    checkBoxTreeItem.setSelected(true);
                                }
                            } catch (Exception e) {}
                            rootItem.getChildren().add(checkBoxTreeItem);
                        }
                    } else if (facet.equals("judge")){
                        ArrayList<String> _tempTreeItems = new ArrayList<String>();
                        for (JsonElement ele : jFacet) {
                            JsonObject obj = (JsonObject) ele;
                            _tempTreeItems.add(obj.get("label").getAsString() + " (" + obj.get("value").getAsString() + ")");
                        }
                        Collections.sort(_tempTreeItems);
                        for (String _str : _tempTreeItems) {
                            CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(_str);
                            try{
                                if (filterBy.containsKey(facet) && filterBy.containsValue(_str.substring(0, _str.indexOf("(")).trim())){
                                    checkBoxTreeItem.setSelected(true);
                                }
                            } catch (Exception e) {}
                            rootItem.getChildren().add(checkBoxTreeItem);
                        }
                    } else if (facet.equals("finalyear")){
                        ArrayList<String> _tempTreeItems = new ArrayList<String>();
                        for (JsonElement ele : jFacet) {
                            JsonObject obj = (JsonObject) ele;
                            _tempTreeItems.add(obj.get("label").getAsString() + " (" + obj.get("value").getAsString() + ")");
                        }
                        Collections.sort(_tempTreeItems, Collections.reverseOrder());
                        for (String _str : _tempTreeItems) {
                            CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(_str);
                            try{
                                if (filterBy.containsKey(facet) && filterBy.containsValue(_str.substring(0, _str.indexOf("(")).trim())){
                                    checkBoxTreeItem.setSelected(true);
                                }
                            } catch (Exception e) {}
                            rootItem.getChildren().add(checkBoxTreeItem);
                        }
                    }else{
                        for (JsonElement ele : jFacet) {
                            JsonObject obj = (JsonObject) ele;

                            CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(obj.get("label").getAsString() + " (" + obj.get("value").getAsString() + ")");
                            if (filterBy.containsKey(facet) && filterBy.containsValue(obj.get("label").getAsString()))
                            {
                                checkBoxTreeItem.setSelected(true);
                            }
                            rootItem.getChildren().add(checkBoxTreeItem);
                        }
                    }


//                    for (JsonElement ele : jFacet) {
//                        JsonObject obj = (JsonObject) ele;
//
//                        CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>(obj.get("label").getAsString() + " (" + obj.get("value").getAsString() + ")");
//                        if (filterBy.containsKey(facet) && filterBy.containsValue(obj.get("label").getAsString()))
//                        {
//                            checkBoxTreeItem.setSelected(true);
//                        }
//                        rootItem.getChildren().add(checkBoxTreeItem);
//                    }

                    dummyRoot.getChildren().add(rootItem);
                }
            }
            TreeViewFacet.setRoot(dummyRoot);

            TreeViewFacet.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

                @Override
                public void changed(ObservableValue observable, Object oldValue,
                                    Object newValue) {

                    TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                    if (selectedItem.isExpanded()){
                        selectedItem.setExpanded(false);
                    }else{
                        selectedItem.setExpanded(true);
                    }
//                    System.out.println("Selected Text : " + selectedItem.getValue());
                    // do what ever you want
                }
            });

            JudgementResultModel jrm = null;
            int srNo = startRow;

            URL JQUERY_UI_CSS_URL = new File(Queries.JQUERY_UI_CSS_PATH).toURI().toURL();
            String styleTag = "<link rel=\"stylesheet prefetch\" href=\""+JQUERY_UI_CSS_URL+"\"><style>.page-wrap,.tooltip{overflow:auto;background:#fff}body{line-height:1.5;font-size:" + Queries.PRINT_SETTING_MODEL.getResultFontSize() + "px;}.page-wrap{padding:2%;position:fixed;height:90%;width:95%}a{position:relative;display:inline-block}.tooltip{display:none;width:600px;height:120px;position:absolute;padding:12px;color:#000;border:2px solid #000}a:link{font-size:18px;color:#8b0000;text-decoration:none;font-weight:700}a:hover{color:#6484d1;text-decoration:underline}::-webkit-scrollbar{width:15px}::-webkit-scrollbar-track{-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,.3);-webkit-border-radius:10px;border-radius:10px}::-webkit-scrollbar-thumb{-webkit-border-radius:10px;border-radius:10px;-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,.5)}::-webkit-scrollbar-thumb:window-inactive{background:rgba(192,192,192,.4)}</style>"+
                    "<script>window.console=window.console||function(e){},document.location.search.match(/type=embed/gi)&&window.parent.postMessage(\"resize\",\"*\");</script>";
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                styleTag = "<link rel=\"stylesheet prefetch\" href=\""+JQUERY_UI_CSS_URL+"\"><style>.page-wrap,.tooltip{overflow:auto;background:#fff}body{line-height:1.5;font-size:" + Queries.PRINT_SETTING_MODEL.getResultFontSize() + "px;}.page-wrap{padding:2%;position:fixed;height:90%;width:95%}a{position:relative;display:inline-block}.tooltip{display:none;width:600px;height:120px;position:absolute;padding:12px;color:#000;border:2px solid #000}a:link{font-size:18px;color:steelblue;text-decoration:none;font-weight:700}a:hover{color:#6484d1;text-decoration:underline}::-webkit-scrollbar{width:15px}::-webkit-scrollbar-track{-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,.3);-webkit-border-radius:10px;border-radius:10px}::-webkit-scrollbar-thumb{-webkit-border-radius:10px;border-radius:10px;-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,.5)}::-webkit-scrollbar-thumb:window-inactive{background:rgba(192,192,192,.4)}</style>"+
                        "<script>window.console=window.console||function(e){},document.location.search.match(/type=embed/gi)&&window.parent.postMessage(\"resize\",\"*\");</script>";
            }
            StringBuilder stb = new StringBuilder("");
            StringBuilder stbTooltip = new StringBuilder("");

            for (JsonElement ele : jData) {
                JsonObject obj = (JsonObject) ele;
                String summary="";
                String imp_notes = "";
                String whiteList = "";

                if (obj.get("DocType").getAsString().equals("Judgements")){
                    boolean _isOverruled=false;
                    if (ServiceHelper.getIsOverruled(obj.get("caseId").getAsString())){
                        _isOverruled=true;
//                        stb.append(" | <span style=\"color:red ; font-weight: bold\">Overruled</span>");
                    };

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    SimpleDateFormat sm = new SimpleDateFormat("dd MMM yyyy");
                    String jDate = sm.format(format.parse(obj.get("decisionDate").getAsString()));

                    if (obj.get("impNotes_store").getAsString().length()>3){
                        if (Queries.IS_SUPREME_TODAY_APP == Boolean.TRUE){
                            imp_notes = obj.get("impNotes_store").getAsString();
                        }
                    }

                    if (imp_notes.length()>3){
                        stb.append("<div>("+srNo+") <a style=\"font-size:" + Queries.PRINT_SETTING_MODEL.getResultFontSize() + "px;\" href='"+srNo+"' data-tooltip=\""+srNo+"i\">"+ obj.get("title").getAsString() + " , " + jDate +"</a></div>");
//                        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
//                            stb.append("<div>("+srNo+") <a style=\"font-size:" + Queries.PRINT_SETTING_MODEL.getResultFontSize() + "px;\" href='"+srNo+"' data-tooltip=\""+srNo+"i\" style=\"background-color:steelblue;color:white;\">"+ obj.get("title").getAsString() + " , " + jDate +"</a></div>");
//                        }else{
//                        }
                        stbTooltip.append("<div class=\"tooltip\" id=\"open\" data-tooltip=\""+srNo+"i\">"+imp_notes+"</div>");
                    }else{
                        stb.append("<div>("+srNo+") <a style=\"font-size:" + Queries.PRINT_SETTING_MODEL.getResultFontSize() + "px;\" href='"+srNo+"'>"+ obj.get("title").getAsString() + " , " + jDate +"</a></div>");
//                        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
//                            stb.append("<div>("+srNo+") <a style=\"font-size:" + Queries.PRINT_SETTING_MODEL.getResultFontSize() + "px;\" href='"+srNo+"' style=\"background-color:steelblue;color:white;\">"+ obj.get("title").getAsString() + " , " + jDate +"</a></div>");
//                        }else{
//                        }
                    }


//                    if (imp_notes.length()>3)
//                    {
//                        stb.append("<div class=\"html-tooltip-demo\" data-tooltip data-options='{\"direction\":\"up\", \"content\": \"" + imp_notes + "\"}'>("+srNo+") <a href='"+srNo+"'>"+ obj.get("title").getAsString() + " , " + jDate +"</a></div>");
//                    }else{
//                        stb.append("<div>("+srNo+") <a href='"+srNo+"'>"+ obj.get("title").getAsString() + " , " + jDate +"</a></div>");
//                    }


//                    stb.append("("+srNo+") <a href='"+srNo+"' class=\"tooltip\">" + obj.get("title").getAsString() + " , " + jDate +"<span class=\"tooltip-text\">"+ imp_notes+"</span></a><br/>");
//                    stb.append("("+srNo+") <a href='"+srNo+"' class=\"tooltip tooltip-scroll\">" + obj.get("title").getAsString() + " , " + jDate +"<div class=\"wrapper\"><span class=\"tooltip-text\">"+ imp_notes+"</span></div></a><br/>");
//                    stb.append("("+srNo+") <a href='"+srNo+"' id = \"tooltip-1\" title='"+imp_notes+"'>" + obj.get("title").getAsString() + " , " + jDate +"</a><br/>");
//                    stb.append("("+srNo+") <a href='"+srNo+"' class=\"tooltip\" data-tool='"+imp_notes+"'>" + obj.get("title").getAsString() + " , " + jDate +"</a><br/>");
//                    stb.append("("+srNo+") <div class=\"tooltip\"><a href='"+srNo+"'>" + obj.get("title").getAsString() + " , " + jDate +"</a><span class=\"tooltiptext\">"+ imp_notes+"</span></div><br/>");


                    if (obj.get("whitelist").getAsString().length()!=0){
                        whiteList = obj.get("whitelist").getAsString();
                    }
                    if (obj.has("hl")){
                        if (_isOverruled){
                            if (obj.get("hl").getAsString().length()!=0){
                                stb.append("<div style=\"color:red ;text-align:justify\">"+obj.get("hl").getAsString()+"</div><br/>");
                            }
                        }else{
                            if (obj.get("hl").getAsString().length()!=0){
                                stb.append("<div style=\"text-align:justify\">"+obj.get("hl").getAsString()+"</div><br/>");
                            }
                        }
                    }else{
                        if (obj.has("acts_store")){
                            if (_isOverruled){
                                if (obj.get("acts_store").getAsString().length()!=0){
                                    stb.append("<div style=\"color:red ;text-align:justify ; color:blue\">"+obj.get("acts_store").getAsString()+"</div>");
                                }
                            }else{
                                if (obj.get("acts_store").getAsString().length()!=0){
                                    stb.append("<div style=\"text-align:justify ; color:blue\">"+obj.get("acts_store").getAsString()+"</div>");
                                }
                            }
                        }
                    }

                    stb.append("Judgements > Court: " +obj.get("courttitle").getAsString());
                    int _citedIn = ServiceHelper.getCitatorCountByCaseId(obj.get("caseId").getAsString());
                    if (_citedIn>0){
                        stb.append(" | <span style=\"color:blue ; font-weight: bold\">Cited: " +_citedIn + "</span>");
//                        stb.append(" | Cited: " +obj.get("cited").getAsString());
                    }

//                    if (obj.get("cited").getAsString().length()!=0){
//                        stb.append(" | <span style=\"color:blue ; font-weight: bold\">Cited: " +obj.get("cited").getAsString() + "</span>");
////                        stb.append(" | Cited: " +obj.get("cited").getAsString());
//                    }

                    if (obj.get("bench").getAsString().length()!=0){
                        stb.append(" | Bench: " +obj.get("bench").getAsString());
                    }
                    if (_isOverruled){
                        stb.append(" | <span style=\"color:red ; font-weight: bold\">Overruled</span>");
                    };

//                    if (obj.get("summary_store").getAsString().length()!=0){
//                        summary = obj.get("summary_store").getAsString();
//                        if (summary.contains("<Body>")){
//                            summary = summary.substring(summary.indexOf("<Body>") + 6).trim();
//                        }
//                        if (summary.contains("</body>")){
//                            summary = summary.substring(0, summary.indexOf("</body>")).trim();
//                        }
////                        summary = summary.replace(System.lineSeparator(), "").replace("/n", "").trim();
//                        summary = summary.replace("</p>", "").replace("<p align=\"justify\">", "");
////                        summary = summary.replaceAll("<[^>]*>", "");
//                        summary = summary.replace("\t", "");
//
//                        if (summary.length()>3)
//                        {
//                            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
//                                summary = "";
//                                if (obj.get("summary_new_store").getAsString().length()!=0){
//                                    summary = obj.get("summary_new_store").getAsString();
//                                }
//                                stb.append("<a href=\"#\" data-tooltip=\""+srNo+"h\" style=\"float:right;background-color:steelblue;color:white;padding: 2px 5px 2px 5px;border-style: solid;border-width: 2px;font-weight:bold\">HN</a>");
//                                stbTooltip.append("<div class=\"tooltip\" id=\"open\" data-tooltip=\""+srNo+"h\">"+summary+"</div>");
//                            }else{
//                                stb.append("<a href=\"#\" data-tooltip=\""+srNo+"h\" style=\"float:right;background-color:darkred;color:white;padding: 2px 5px 2px 5px;border-style: solid;border-width: 2px;font-weight:bold\">HN</a>");
//                                stbTooltip.append("<div class=\"tooltip\" id=\"open\" data-tooltip=\""+srNo+"h\">"+summary+"</div>");
//                            }
//                        }
//
////                        if (summary.length()>3)
////                        {
////                            stb.append("<div class=\"html-tooltip-demo\" data-tooltip data-options='{\"direction\":\"up\", \"content\": \"" + summary + "\"}'><span style=\"float:right;background-color:darkred;color:white;padding: 2px 5px 2px 5px;border-style: solid;border-width: 2px;font-weight:bold;font-size:14px;\">HN</span></div>");
////                        }
////                        stb.append("<a class=\"tooltip tooltip-scroll\"><div class=\"wrapper\"><span class=\"tooltip-text\">" + summary + "</span></div></a>");
//
////                        stb.append("<a class=\"tooltip tooltip-scroll\">HN<div class=\"wrapper\"><span class=\"tooltip-text\">" + summary + "</span></div></a>");
////                        stb.append("<a title='"+obj.get("summary_store").getAsString()+"'>HN</a>");
////                        stb.append("<a class=\"tooltip\" data-tool='"+obj.get("summary_store").getAsString()+"'>HN</a>");
//                    }

                    if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                        if (obj.get("summary_new_store").getAsString().length()!=0){
                            summary = obj.get("summary_new_store").getAsString();
                            if (summary.contains("<Body>")){
                                summary = summary.substring(summary.indexOf("<Body>") + 6).trim();
                            }
                            if (summary.contains("</body>")){
                                summary = summary.substring(0, summary.indexOf("</body>")).trim();
                            }
                            summary = summary.replace("</p>", "").replace("<p align=\"justify\">", "");
                            summary = summary.replace("\t", "");

                            if (summary.length()>3)
                            {
                                stb.append("<a href=\"#\" data-tooltip=\""+srNo+"h\" style=\"float:right;background-color:steelblue;color:white;padding: 2px 5px 2px 5px;border-style: solid;border-width: 2px;font-weight:bold\">HN</a>");
                                stbTooltip.append("<div class=\"tooltip\" id=\"open\" data-tooltip=\""+srNo+"h\">"+summary+"</div>");
                            }
                        }
                    }else{
                        if (obj.get("summary_store").getAsString().length()!=0){
                            summary = obj.get("summary_store").getAsString();
                            if (summary.contains("<Body>")){
                                summary = summary.substring(summary.indexOf("<Body>") + 6).trim();
                            }
                            if (summary.contains("</body>")){
                                summary = summary.substring(0, summary.indexOf("</body>")).trim();
                            }
                            summary = summary.replace("</p>", "").replace("<p align=\"justify\">", "");
                            summary = summary.replace("\t", "");

                            if (summary.length()>3)
                            {
                                stb.append("<a href=\"#\" data-tooltip=\""+srNo+"h\" style=\"float:right;background-color:darkred;color:white;padding: 2px 5px 2px 5px;border-style: solid;border-width: 2px;font-weight:bold\">HN</a>");
                                stbTooltip.append("<div class=\"tooltip\" id=\"open\" data-tooltip=\""+srNo+"h\">"+summary+"</div>");
                            }
                        }
                    }
                }
                else if (obj.get("DocType").getAsString().equals("WordPhrase")){
                    if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                        stb.append("("+srNo+") <a href='"+srNo+"' id = \"tooltip-2\" title='"+obj.get("title").getAsString()+"' style=\"float:right;background-color:steelblue;color:white;\">" + obj.get("title").getAsString() +"</a>");
                    }else{
                        stb.append("("+srNo+") <a href='"+srNo+"' id = \"tooltip-2\" title='"+obj.get("title").getAsString()+"'>" + obj.get("title").getAsString() +"</a>");
                    }


//                    stb.append("("+srNo+") " + obj.get("title").getAsString());
                    if (obj.has("hl"))
                    {
                        if (obj.get("hl").getAsString().length()!=0){
                            stb.append("<br/><div>"+obj.get("hl").getAsString()+"</div>");
                        }
                    }
                    stb.append("<br/>DocType > WordPhrase");
                }
                else if (obj.get("DocType").getAsString().equals("BareActs")){
                    stb.append("("+srNo+") <a href='"+srNo+"' title='"+obj.get("Type").getAsString() + " of " + obj.get("title").getAsString()+"'>" + obj.get("Type").getAsString() + " of " + obj.get("title").getAsString() +"</a>");

//                    stb.append("("+srNo+") " + obj.get("Type").getAsString() + " of " + obj.get("title").getAsString());
                    if (obj.has("hl"))
                    {
                        if (obj.get("hl").getAsString().length()!=0){
                            stb.append("<br/><div>"+obj.get("hl").getAsString()+"</div>");
                        }
                    }
                    stb.append("<br/>DocType > BareActs");
                }
                else if (obj.get("DocType").getAsString().equals("Commentary")){
                    stb.append("("+srNo+") <a href='"+srNo+"' title='"+obj.get("Type").getAsString() + " of " + obj.get("title").getAsString()+"'>" + obj.get("title").getAsString() +"</a>");
                    stb.append("<span style=\"text-align:justify ; color:blue\"><br/><strong>" + obj.get("Type").getAsString() + " : " + obj.get("summary").getAsString()+"</strong></span>");
//                    stb.append("("+srNo+") <a href='"+srNo+"' title='"+obj.get("title").getAsString()+"'>" + obj.get("title").getAsString() +"</a>");
//                    stb.append("("+srNo+") " + obj.get("title").getAsString());
                    if (obj.has("hl"))
                    {
                        if (obj.get("hl").getAsString().length()!=0){
                            stb.append("<br/><div>"+obj.get("hl").getAsString()+"</div>");
                        }
                    }
                    stb.append("<br/>DocType > Commentary");
                }
                stb.append("<br/><hr><br/>");

//                jrm = new JudgementResultModel(Integer.toString(srNo), whiteList, summary, obj.get("caseId").getAsString(), stb.toString());
//                //isoverruled (font color red)
//                data.add(jrm);
                srNo++;
            }

            URL JQUERY_MIN_JS_URL = new File(Queries.JQUERY_MIN_JS_PATH).toURI().toURL();
            URL JQUERY_UI_MIN_JS_URL = new File(Queries.JQUERY_UI_MIN_JS_PATH).toURI().toURL();
            String JavaScript = "<script src=\""+JQUERY_MIN_JS_URL+"\"></script><script src=\""+JQUERY_UI_MIN_JS_URL+"\"></script><script>$(\"a[data-tooltip]\").on(\"mouseenter\",function(){var t=$(this).data(\"tooltip\");$(\".tooltip[data-tooltip=\"+t+\"]\").position({my:\"left bottom\",at:\"left top\",of:$(this),collision:\"flip\",within:\".page-wrap\",showOn:\"focus\"}).show()}).on(\"mouseleave\",function(){var t=$(this).data(\"tooltip\");$(\".tooltip[data-tooltip=\"+t+\"]\").stop().fadeOut(function(){$(this).removeAttr(\"style\")})}),$(\".tooltip\").on(\"mouseenter\",function(){var t=$(this).data(\"tooltip\");$(\".tooltip[data-tooltip=\"+t+\"]\").stop()}).on(\"mouseleave\",function(){var t=$(this).data(\"tooltip\");$(\".tooltip[data-tooltip=\"+t+\"]\").stop().fadeOut(function(){$(this).removeAttr(\"style\")})});</script>";

//            String sss = "<html><head><meta charset=\"UTF-8\">"+styleTag+"</head><body translate=\"no\"><div class=\"page-wrap\">" + stb.toString() +"</div>" + stbTooltip.toString() + JavaScript +"</body></html>";
//            try(  PrintWriter out = new PrintWriter(Paths.get(Queries.CURRENT_PATH, "temp2.html").toString() )  ){
//                out.println( sss );
//            }

//            webViewDocView.getEngine().load("file:///F:/ST/auto-adjust/auto-adjust.html");

            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                webViewDocView.getEngine().loadContent("<html><head><meta charset=\"UTF-8\">"+styleTag+"</head><body translate=\"no\" style=\"font-family:georgia,garamond,serif;font-size:16px;\"><div class=\"page-wrap\">" + stb.toString() +"</div>" + stbTooltip.toString() + JavaScript +"</body></html>", "text/html");
            }else{
                webViewDocView.getEngine().loadContent("<html><head><meta charset=\"UTF-8\">"+styleTag+"</head><body translate=\"no\"><div class=\"page-wrap\">" + stb.toString() +"</div>" + stbTooltip.toString() + JavaScript +"</body></html>", "text/html");
            }

//            TableViewResults.getItems().clear();
//            TableViewResults.getItems().addAll(data);
        } catch (Exception ex) {
//            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            new Utils().showErrorDialog(ex);
        }
        return totalResultCount;
    }
}
