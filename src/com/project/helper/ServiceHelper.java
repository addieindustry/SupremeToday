/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.project.extrautility.PropertyMaster;
import com.project.model.*;
import com.project.utility.SearchUtility;
import com.project.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author addie
 */
public class ServiceHelper {

    public static ObservableList<CourtModel> getAllCourts() {
        ObservableList<CourtModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            ResultSet rs = sqliteHelper.select(Queries.GET_ALL_COURTS);

            while (rs.next()) {
                list.add(new CourtModel(rs.getString("courtId"), rs.getString("courtName")));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getYearListByCourtId(String courtId) {
        String query = "courtId:" + courtId, sortBy = "", facet_fields = "finalyear", fields = "", hl_fields = "", filter_query = "";
        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 200, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            JsonObject facets = jo.getAsJsonObject("facets");
            JsonArray jData = facets.getAsJsonArray("finalyear");
            for (JsonElement ele : jData) {
                JsonObject obj = (JsonObject) ele;
                list.add(obj.get("label").getAsString());
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(list);
        Collections.reverse(list);
        return list;
    }

    public static ObservableList<String> getAllPublishers() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            ResultSet rs = sqliteHelper.select(Queries.GET_ALL_PUBLISHERS_FROM_CITATION);

            while (rs.next()) {
                if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                    list.add(rs.getString("publisher").replace("Supreme", "ICLF"));
                }else{
                    list.add(rs.getString("publisher"));
                }
//                list.add(rs.getString("publisher"));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getAllYearByPublisher(String publisher) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                publisher = publisher.replace("ICLF", "Supreme");
            }
            String q = String.format(Queries.GET_YEAR_FROM_CITATION_BY_PUBLISHERS, publisher);
            ResultSet rs = sqliteHelper.select(q);

            while (rs.next()) {
                list.add(rs.getString("year"));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getAllVolumeByPublisherYear(String publisher, String year) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                publisher = publisher.replace("ICLF", "Supreme");
            }
            String q = String.format(Queries.GET_VOLUME_FROM_CITATION_BY_PUBLISHERS_YEAR, publisher, year);
            ResultSet rs = sqliteHelper.select(q);

            while (rs.next()) {
                list.add(rs.getString("volume"));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getAllPageByPublisherYearVolume(String publisher, String year, String volume) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                publisher = publisher.replace("ICLF", "Supreme");
            }
            String q = String.format(Queries.GET_PAGE_FROM_CITATION_BY_PUBLISHERS_YEAR_VOLUME, publisher, year, volume);
            ResultSet rs = sqliteHelper.select(q);

            while (rs.next()) {
                list.add(rs.getString("pageno"));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel> getAllDictionaryTitle() {
        ObservableList<TitleIdListModel> list = FXCollections.observableArrayList();
        String query = "(DocType:Dictionary)", sortBy = "",facet_fields = "",
                fields = "caseId,title,Hindi", hl_fields = "", filter_query = "";
        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    list.add(new TitleIdListModel(obj.get("caseId").getAsString(), obj.get("title").getAsString()));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel> getAllCentralActList() {
        ObservableList<TitleIdListModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            ResultSet rs = sqliteHelper.select(Queries.GET_CENTRAL_ACT_LIST);

            while (rs.next()) {
                list.add(new TitleIdListModel(rs.getString("ID"), rs.getString("Acts1")));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel> getAllCommentaryList() {
        ObservableList<TitleIdListModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            ResultSet rs = sqliteHelper.select(Queries.GET_COMMENTARY_LIST);
            while (rs.next()) {
                list.add(new TitleIdListModel(rs.getString("cmtId"), rs.getString("cmtType")));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel> getAllCommentaryListByFilter(boolean isExhaustive) {
        ObservableList<TitleIdListModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            ResultSet rs;
            if (isExhaustive)
            {
                rs = sqliteHelper.select(String.format(Queries.GET_COMMENTARY_LIST_WITH_EXHAUSTIVE, "%Exhaustive%"));
            }else{
                rs = sqliteHelper.select(String.format(Queries.GET_COMMENTARY_LIST_WITHOUT_EXHAUSTIVE, "%Exhaustive%"));
            }
            while (rs.next()) {
                list.add(new TitleIdListModel(rs.getString("cmtId"), rs.getString("cmtType")));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel> getAllWordPhrases() {
        ObservableList<TitleIdListModel> list = FXCollections.observableArrayList();
        String query = "(DocType:WordPhrase)", sortBy = "",facet_fields = "",
                fields = "caseId,title", hl_fields = "", filter_query = "";
        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    list.add(new TitleIdListModel(obj.get("caseId").getAsString(), obj.get("title").getAsString()));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static String getDictionaryContentById(String text) {
        String query = "(DocType:Dictionary)", sortBy = "",facet_fields = "",
                fields = "title,Hindi", hl_fields = "", filter_query = "caseId:\"" + text + "\"";

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 1, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    URL kruti_font =new File(Queries.KRUTI_FONT_PATH).toURI().toURL();
                    return "<html><head><meta charset=\"UTF-8\"><style>@font-face {font-family: 'Kruti Dev';src: url('"+kruti_font+"') format('truetype')}.KrutiDev_hindi_text {font-family: Kruti Dev !important;font-size: 18px !important;}</style></head>\n<body>\n<span class=\"KrutiDev_hindi_text\">" + obj.get("Hindi").getAsString() + "</span></body>\n</html>\n";
//                    return "<html><head><meta charset=\"UTF-8\"></head>\n<body>\n<font face=\"Kruti Dev 011\">" + obj.get("Hindi").getAsString() + "</font></body>\n</html>\n";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Found!";
    }

    public static ObservableList<String> getCentralActTypeById(String text) {
        String query = "(DocType:BareActs)", sortBy = "caseId_sort STRING ASC",facet_fields = "",
                fields = "id,Type", hl_fields = "", filter_query = "id:\"" + text + "\"";
        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    list.add(obj.get("Type").getAsString());
                    System.out.println(obj.get("Type").getAsString());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getCommentaryTypeById(String text) {
//        Type_sort STRING ASC
        String query = "(DocType:Commentary)", sortBy = "",facet_fields = "",
                fields = "id,Type", hl_fields = "", filter_query = "id:\"" + text + "\"";
        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    list.add(obj.get("Type").getAsString());
//                    System.out.println(obj.get("caseId").getAsString());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static String getCentralActContentById(String id, String Type) {
        String googleTranslatorScript = "<script>function googleTranslateElementInit(){new google.translate.TranslateElement({pageLanguage:'en'},'google_translate_element');}</script> <script src=\"http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>";
        String googleTranslatorDiv = "<div id=\"google_translate_element\"></div>";

        String query = "DocType:BareActs", sortBy = "",facet_fields = "",
                fields = "title,Type,summary,content_store", hl_fields = "", filter_query = "id:\"" + id + "\" AND Type:\"" + Type + "\"";

        if (Type.length() == 0){
            filter_query = "id:\"" + id + "\"";
        }

        System.out.println(filter_query);

        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                StringBuilder stb = new StringBuilder("");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;

                    stb.append("<center><font color=\"blue\">" + obj.get("title").getAsString() + "</font></center><br>");
                    stb.append("<center><font color=\"red\">" + obj.get("Type").getAsString() + " " +  obj.get("summary").getAsString() + "</font></center><br>");

                    String content = obj.get("content_store").getAsString();
                    content = content.replaceAll("font-size\\s?:\\s?\\d+(pt|px|em);", "");
                    stb.append(content);

                }
                return "<html><head><meta charset=\"UTF-8\">" + googleTranslatorScript + "<style>body {font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px;}</style></head>" + googleTranslatorDiv + "<body>" + stb.toString() + "</body></html>";
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Found!";
    }

    public static String getCentralActIndexById(String id) {
        String googleTranslatorScript = "<script>function googleTranslateElementInit(){new google.translate.TranslateElement({pageLanguage:'en'},'google_translate_element');}</script> <script src=\"http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>";
        String googleTranslatorDiv = "<div id=\"google_translate_element\"></div>";

        String query = "DocType:BareActs", sortBy = "caseId_sort STRING ASC",facet_fields = "",
                fields = "title,Type,summary,content_store", hl_fields = "", filter_query = "id:\"" + id + "\"";

        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");

                String _title = "";
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    _title = obj.get("title").getAsString();
                    break;
                }
//                StringBuilder stb = new StringBuilder("<center><font color=\"blue\">" + obj.get("title").getAsString() + "</font></center><br><ul style=\"line-height: 2;\">");

                StringBuilder stb = new StringBuilder("<ul style=\"line-height: 2;\">");
                if (_title.length()>0){
                    stb = new StringBuilder("<center><font color=\"red\">" + _title + "</font></center><br><ul style=\"line-height: 2;\">");
                }

                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;

                    stb.append("<l><a style=\"text-decoration:none\" href=\"act:"+id+"~"+obj.get("Type").getAsString()+"\">" + obj.get("Type").getAsString() + " " +  obj.get("summary").getAsString() + "</a></l><br>");
                }
                stb.append("</ul>");
//                return stb.toString();
                return "<html><head><meta charset=\"UTF-8\">" + googleTranslatorScript + "<style>body {font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px;}</style></head>\n" + googleTranslatorDiv + "<body>\n" + stb.toString() + "</body>\n</html>\n";
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Found!";
    }



    public static String  getWordPhrasesContentById(String text) {
        String query = "(DocType:WordPhrase)", sortBy = "",facet_fields = "",
                fields = "title,content_store", hl_fields = "", filter_query = "caseId:\"" + text + "\"";

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 1, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    return "<html><head><meta charset=\"UTF-8\"><style>body {font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px;}</style></head><body>" + obj.get("content_store").getAsString() + "</body></html>";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Found!";
    }


    public static String getCommentaryContentById(String id, String Type) {
        String query = "(DocType:Commentary)", sortBy = "",facet_fields = "",
                fields = "title,Type,summary,content_store", hl_fields = "", filter_query = "id:\"" + id + "\" AND Type:\"" + Type + "\"";
        if (Type == null){
            return "";
        }
        if (Type.length() == 0)
        {
            filter_query = "id:\"" + id + "\"";
        }

        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                StringBuilder stb = new StringBuilder("");
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;

                    stb.append("<center><font color=\"blue\">" + obj.get("title").getAsString() + "</font></center><br>");
                    stb.append("<center><font color=\"red\">" + obj.get("Type").getAsString() + " " +  obj.get("summary").getAsString() + "</font></center><br>");
                    stb.append(obj.get("content_store").getAsString());
//                    stb.append(obj.get("content_store").getAsString().replace("\n", "<br>"));
                }
//                return stb.toString();
                return "<html><head><meta charset=\"UTF-8\"><style>body {font-size:" + Queries.PRINT_SETTING_MODEL.getDisplayFontSize() + "px;}</style></head><body>" + stb.toString() + "</body></html>";
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Found!";
    }

    public static ObservableList<BookmarkModel> getAllBookmarks() {
        ObservableList<BookmarkModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_ALL_BOOKMARK);
            ResultSet rs = sqliteHelper.select(q);
            while (rs.next()) {
                list.add(new BookmarkModel(rs.getString("_id"), rs.getString("doc_id"), rs.getString("title"), rs.getString("doc_type"), rs.getString("category"), rs.getString("note")));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static String setBookmark(String _id, String doc_id, String title, String doc_type, String category, String note) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.INSERT_BOOKMARK, doc_id, title, doc_type, category, note);
            if (_id != null && !_id.isEmpty()) {
                q = String.format(Queries.UPDATE_BOOKMARK, doc_id, title, doc_type, category, note, _id);
            }
            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return "Bookmark Insert/Update Succesfully!";
            } else {
                sqliteHelper.close();
                return "Sql Error!";
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    public static String removeBookmark(String _id) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = "";
            if (_id != null && !_id.isEmpty()) {
                q = String.format(Queries.DELETE_BOOKMARK, _id);
            }
            boolean ret = sqliteHelper.delete(q);
            sqliteHelper.close();
            return "Bookmark Deleted Succesfully!";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    public static ObservableList<String> getAllBookmarkCategory() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, false);
            sqliteHelper.open();
            String q = Queries.GET_BOOKMARK_CATEGORIES;
            ResultSet rs = sqliteHelper.select(q);

            while (rs.next()) {
                list.add(rs.getString("category"));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ResponseMaster getBookmarkByCaseId(String caseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            HashMap<String, Object> data = new HashMap<String, Object>();
            String q = String.format(Queries.GET_BOOKMARK_BY_CASEID, caseId);
            ResultSet rs = sqliteHelper.select(q);
            HashMap<String, String> bookmark = new HashMap<>();
            if (rs.next()) {
                bookmark.put("_id", rs.getString("_id"));
                bookmark.put("doc_id", rs.getString("doc_id"));
                bookmark.put("title", rs.getString("title"));
                bookmark.put("doc_type", rs.getString("doc_type"));
                bookmark.put("category", rs.getString("category"));
                bookmark.put("note", rs.getString("note"));
            } else {
                bookmark.put("_id", "");
                bookmark.put("doc_id", "");
                bookmark.put("title", "");
                bookmark.put("doc_type", "");
                bookmark.put("category", "");
                bookmark.put("note", "");
            }
            rs = sqliteHelper.select(Queries.GET_BOOKMARK_CATEGORIES);
            List<HashMap<String, String>> categories = new ArrayList<HashMap<String, String>>();
//            List<String> categories = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> cat = new HashMap<>();
                cat.put("category", rs.getString("category"));
                categories.add(cat);
            }
            data.put("categories", categories);
            data.put("bookmark", bookmark);
            rs.close();
            sqliteHelper.close();
            return new ResponseMaster(data, 200);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex, 101);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex, 101);
        }
    }

    public ResponseMaster getAllBookmarksInJSON() {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_ALL_BOOKMARK);
            ResultSet rs = sqliteHelper.select(q);
            List<HashMap<String, String>> bookmarks = new ArrayList<HashMap<String, String>>();
            while (rs.next()) {
                HashMap<String, String> bookmark = new HashMap<>();
                bookmark.put("_id", rs.getString("_id"));
                bookmark.put("doc_id", rs.getString("doc_id"));
                bookmark.put("title", rs.getString("title"));
                bookmark.put("doc_type", rs.getString("doc_type"));
                bookmark.put("category", rs.getString("category"));
                bookmark.put("note", rs.getString("note"));
                bookmarks.add(bookmark);
            }

            rs.close();
            sqliteHelper.close();
            return new ResponseMaster(bookmarks, 200);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex, 101);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex, 101);
        }
    }

    public static void getPrintSetting() {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_PRINT_SETTING);
            ResultSet rs = sqliteHelper.select(q);
            while (rs.next()) {
                Queries.PRINT_SETTING_MODEL = new PrintSettingModel(rs.getString("_id"), rs.getString("page_type"), rs.getString("display_font_size"), rs.getString("print_font_size"), rs.getString("result_font_size"), rs.getFloat("margin_top"),  rs.getFloat("margin_bottom"), rs.getFloat("margin_right"), rs.getFloat("margin_left"),  Boolean.parseBoolean(rs.getString("print_logo")), Boolean.parseBoolean(rs.getString("use_native_browser")), rs.getFloat("filter_splitter"), rs.getFloat("global_result_splitter"), rs.getFloat("advance_result_splitter"));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void alterPrintSetting() {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            //sqliteHelper.insert(Queries.ALTER_PRINT_SETTING);
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN margin_top FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            //sqliteHelper.insert(Queries.ALTER_PRINT_SETTING);
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN margin_bottom FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN margin_right FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN margin_left FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN result_font_size INTEGER");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN filter_splitter FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN global_result_splitter FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert("ALTER TABLE print_setting ADD COLUMN advance_result_splitter FLOAT");
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {} catch (SQLException ex) {}

    }

    public static boolean setPrintSetting(PrintSettingModel printModel) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            sqliteHelper.insert(Queries.DELETE_PRINT_SETTING);
//            String q = String.format(Queries.INSERT_PRINT_SETTING, printModel.getPageType(), printModel.getPrintFontSize(), printModel.getDisplayFontSize(), printModel.getMarginTop().toString(), printModel.getMarginBottom().toString(), printModel.getMarginRight().toString(), printModel.getMarginLeft().toString(), printModel.getPrintLogo(), printModel.getUseNativeBrowser());
            String q = String.format(Queries.INSERT_PRINT_SETTING, printModel.getPageType(), printModel.getPrintFontSize(), printModel.getDisplayFontSize(), printModel.getMarginTop().toString(), printModel.getMarginBottom().toString(), printModel.getMarginRight().toString(), printModel.getMarginLeft().toString(), printModel.getPrintLogo(), printModel.getUseNativeBrowser(), printModel.getResultFontSize(), printModel.getFilterSplitter(), printModel.getGlobalResultSplitter(), printModel.getAdvancedResultSplitter());
            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return true;
            } else {
                sqliteHelper.close();
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateDisplayFontInPrintSetting(int _dispalyFont) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.UPDATE_DISPLAY_FONT_IN_PRINT_SETTING, _dispalyFont);
            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return true;
            } else {
                sqliteHelper.close();
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateResultFontInPrintSetting(int _resultFont) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.UPDATE_RESULT_FONT_IN_PRINT_SETTING, _resultFont);
            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return true;
            } else {
                sqliteHelper.close();
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateFilterSplitterInPrintSetting(float _filterSplitter) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.UPDATE_FILTER_SPLITTER_IN_PRINT_SETTING, _filterSplitter);
            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return true;
            } else {
                sqliteHelper.close();
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static ObservableList<HistoryModel> getHistoryList() {
        ObservableList<HistoryModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_HISTORY_LIST);
            ResultSet rs = sqliteHelper.select(q);
            while (rs.next()) {
                list.add(new HistoryModel(rs.getString("_id"), rs.getString("title"), rs.getString("query"), rs.getString("keyword"), rs.getString("search_type"), rs.getString("created_date")));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel> getHistoryListByKeywordOnly() {
        ObservableList<TitleIdListModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, false);
            sqliteHelper.open();
            String q = Queries.GET_HISTORY_KEYWORD_LIST;
            ResultSet rs = sqliteHelper.select(q);

            while (rs.next()) {
                list.add(new TitleIdListModel("1", rs.getString("keyword")));
            }
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static boolean setHistory(HistoryModel historyModel) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.INSERT_HISTORY, historyModel.getTitle(), historyModel.getQuery(), historyModel.getKeyword(), historyModel.getsearch_type());
//            String q = String.format(Queries.INSERT_HISTORY, title, query, keyword, search_type);
            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return true;
            } else {
                sqliteHelper.close();
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static String sendFeedback(String email, String username, String subject, String content) {
        try {
            SendMailHelper.sendMail(content, username, subject, email, true);
            return "Email sent successfully!";
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    public static ObservableList<OverruledModel> getAllOverruled() {
        ObservableList<OverruledModel> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_ALL_OVERRULED);
            ResultSet rs = sqliteHelper.select(q);
            Integer i = 0;
            while (rs.next()) {
                i += 1;
                DecimalFormat decimalFormat = new DecimalFormat("00000000000");
                list.add(new OverruledModel(i.toString(), rs.getString("courtId"), decimalFormat.format(Long.parseLong(rs.getString("white"))), decimalFormat.format(Long.parseLong(rs.getString("red"))), rs.getString("whiteText"), rs.getString("redText"), rs.getString("overruled")));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static boolean getIsOverruled(String text) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_IS_OVERRULED, text);
            ResultSet rs = sqliteHelper.select(q);
            int _totalRows = 0;
            while (rs.next()){
                _totalRows = rs.getInt(1);
            }
            rs.close();
            sqliteHelper.close();
            if (_totalRows>0){
                return true;
            }else{
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean getIsDistinguished(String text) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_IS_DISTINGUISHED, text);
            ResultSet rs = sqliteHelper.select(q);
            int _totalRows = 0;
            while (rs.next()){
                _totalRows = rs.getInt(1);
            }
            rs.close();
            sqliteHelper.close();
            if (_totalRows>0){
                return true;
            }else{
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static ObservableList<String>  getAllJudges(String text) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_ALL_JUDGES, text);
            ResultSet rs = sqliteHelper.select(q);
            while (rs.next()) {
                list.add(rs.getString("judgeName"));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<TitleIdListModel>  getAllShortFullActsRefTitle() {
        ObservableList<TitleIdListModel> short_list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
            ResultSet rs1 = sqliteHelper.select(Queries.GET_SHORT_DATA);

            while (rs1.next()) {
                short_list.add(new TitleIdListModel(rs1.getString("short_title").toLowerCase(), rs1.getString("full_title")));
            }
            rs1.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return short_list;
    }

    public static ObservableList<String>  getAllActsRefTitle(String text, ObservableList<TitleIdListModel> short_list) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
            String q = String.format(Queries.GET_ACT_REF_TITLE, text + "%", "%" + text + "%");

            System.out.println(q);
            String subtext = text.replace("%", "").toLowerCase();
            for (TitleIdListModel short_item : short_list) {
                if (short_item.getId().equals(subtext))
                {
                    list.add(short_item.getTitle().toString());
                }
            }
            ResultSet rs = sqliteHelper.select(q);
            while (rs.next()) {
                list.add(rs.getString("actTitle"));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String>  getAllActsRefSectionByTitle(String text) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
//            String q = String.format(Queries.GET_ACT_REF_SECTION_BY_TITLE, text);
            String q = String.format(Queries.GET_ACT_REF_SECTION_BY_TITLE, Queries.ACT_RUEL_REG, text);
            ResultSet rs = sqliteHelper.select(q);
            while (rs.next()) {
                list.add(rs.getString("actType"));
            }
            rs.close();
            sqliteHelper.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String>  getAutoCompleteList(String text) {
        String indexPath = Queries.INDEX_PATH;
        if (com.project.utility.OSValidator.isWindows()) {
            indexPath = indexPath + "\\auto";
        } else {
            indexPath = indexPath + "/auto";
        }
        String subText = "";
        text = text.trim();
        if (text.contains(" ")) {
            subText = text.substring(0, text.lastIndexOf(" "));
            text = text.substring(text.lastIndexOf(" ") + 1);
        }else if (text.startsWith("\"")){
            subText ="\"";
        }
        String query = "term:" + text.replace("\"", "") + " OR term:"+text.replace("\"", "")+"* OR term:"+text.replace("\"", "")+"~0.7", sortBy = "term_freq_sort INT DESC", facet_fields = "", fields = "all", hl_fields = "", filter_query = "";
//        String query = "term:" + text, sortBy = "", facet_fields = "", fields = "term,term_sort,term_freq,term_freq_sort", hl_fields = "", filter_query = "";
        ObservableList<String> list = FXCollections.observableArrayList();

        SearchUtility searchUtility = new SearchUtility(indexPath);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 20, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
//                System.out.println("COUNT:" + jo.get(Queries.LUCENE_NUMFOUND));
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    if (subText.length() > 0) {
                        if (subText != "\""){list.add(subText + " " + obj.get("term").getAsString());
                        }else{list.add(subText + obj.get("term").getAsString());}
                    } else {
                        list.add(obj.get("term").getAsString());
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ResponseMaster search(String queryString, int start, int rows, String sortBy, String facet_fields, String fields, String hl_fields, String filter_query, String searchType) {
        String indexPath = Queries.INDEX_PATH;
        if (searchType.equals("3")) {
            if (com.project.utility.OSValidator.isWindows()) {
                indexPath = indexPath + "\\auto";
            } else {
                indexPath = indexPath + "/auto";
            }
        }
        SearchUtility searchUtility = new SearchUtility(indexPath);

        JsonObject jo = new JsonObject();
        int code = 200;
        try {
            jo = searchUtility.search(queryString, start, rows, sortBy, facet_fields, fields, hl_fields, filter_query, "");

        } catch (Exception ex) {
            code = 500;
            jo.addProperty("message", ex.toString());
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseMaster(jo, code);
    }

    public static String getCaseReferedByCaseId(String CaseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.GET_CASES_REFERED_BY_CASEID, CaseId);
            ResultSet rs = sqliteHelper.select(q);
            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            StringBuilder stb = new StringBuilder("");
            while (rs.next()) {
                if (!rs.getString("targetCaseId").isEmpty()){
                    if (rs.getString("caseReferred").length()>3){
                        stb.append("<a href=" + rs.getString("targetCaseId") + ">" + rs.getString("caseReferred") + "</a><br/>");
                    }
                }else{
                    if (rs.getString("caseReferred").length()>3){
                        stb.append(rs.getString("caseReferred") + "<br/>");
                    }
                }
            }
            sqliteHelper.close();
            return stb.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public static HashMap<String, String> getJudicialAnalysisById(String id) {
        String query = "(DocType:Judgements)", sortBy = "",facet_fields = "",
                fields = "caseId,title,courttitle,decisionDate,acts_store", hl_fields = "", filter_query = id;

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();

        HashMap<String, String> _caserefer_id = new HashMap<String, String>();
        try {
            jo = searchUtility.search(query, 0, 5000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");

                StringBuilder stb = new StringBuilder("");

                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    SimpleDateFormat sm = new SimpleDateFormat("dd MMM yyyy");
                    String jDate = sm.format(format.parse(obj.get("decisionDate").getAsString()));

                    if (!obj.get("caseId").getAsString().isEmpty()){
                        if (obj.get("title").getAsString().length()>3){
                            _caserefer_id.put(obj.get("caseId").getAsString(), "<p><a style=\"color:#8b0000;text-decoration:none;font-weight:bold;\" href=" + obj.get("caseId").getAsString() + ">" + obj.get("title").getAsString() + "</a>, <span style=\"color:darkred\">"+ jDate + "</span> - <span style=\"color:darkolivegreen\">" + obj.get("courttitle").getAsString() + "</span><br/><span style=\"text-align:justify ; color:blue\">" + obj.get("acts_store").getAsString() + "</span></p>");
                        }
                    }else{
                        if (obj.get("title").getAsString().length()>3){
                            _caserefer_id.put(obj.get("caseId").getAsString(), "<p>" + obj.get("title").getAsString() + ", <span style=\"color:darkred\">"+ jDate + "</span> - <span style=\"color:darkolivegreen\">" + obj.get("courttitle").getAsString() + "</span><br/><span style=\"text-align:justify ; color:blue\">" + obj.get("acts_store").getAsString() + "</span></p>");
                        }
                    }
                }
                return _caserefer_id;
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return _caserefer_id;
    }

    public static String getJudicialCitatorByCaseId(String caseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.GET_JUDICIAL_CITATOR_BY_CASEID, caseId);
            ResultSet rs = sqliteHelper.select(q);

            StringBuilder stb = new StringBuilder("");
            StringBuilder stbTemp = new StringBuilder("");

            String _refBy = "";
            int _refByCounter = 0;

            StringBuilder _caseIds = new StringBuilder("");
            HashMap<String, String> _caserefer_id = new HashMap<String, String>();
            while (rs.next()) {
                _caseIds.append("caseId:\"" + rs.getString("caseId") + "\" OR ");
            }

            if (_caseIds.toString().endsWith(" OR ")){
                _caseIds.delete(_caseIds.toString().length()-4, _caseIds.toString().length()).trimToSize();
            }

            _caserefer_id = getJudicialAnalysisById(_caseIds.toString());

            rs = sqliteHelper.select(q);

            while (rs.next()) {
                String _refByTemp = rs.getString("referredBy");
                String _caseId = rs.getString("caseId");

                if(_refByTemp != null) {
                    if(_refByTemp.length() == 0){
                        _refByTemp = "Referred";
                    }
                }

                if(_refBy != null) {
                    if (_refBy.length() == 0){
                        _refBy = _refByTemp;
                    }
                }

                if(_refBy != null) {
                    if (!_refBy.equalsIgnoreCase(_refByTemp)){
                        stb.append("<button class=\"accordion\">" + _refBy + " (" + _refByCounter + ")</button>");
                        stb.append("<div class=\"panel\">" + stbTemp.toString() + "</div>");

                        stbTemp = new StringBuilder("");
                        _refBy = _refByTemp;
                        _refByCounter = 0;
                    }
                }
                if(_refBy != null) {
                    try {
                        stbTemp.append(_caserefer_id.get(_caseId).toString());
                    } catch (Exception ex) {}
                }
                _refByCounter += 1;
            }
            if (stbTemp.length()>0){
                stb.append("<button class=\"accordion\">" + _refBy + " (" + _refByCounter + ")</button>");
                stb.append("<div class=\"panel\">" + stbTemp.toString() + "</div>");
            }
            sqliteHelper.close();
            return stb.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public static String getOverruledByCaseId(String CaseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.GET_OVERRULED_BY_CASEID, CaseId);
            ResultSet rs = sqliteHelper.select(q);
            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

            StringBuilder _caseIds = new StringBuilder("");
            while (rs.next()) {
                DecimalFormat decimalFormat = new DecimalFormat("00000000000");
                _caseIds.append("caseId:\"" + decimalFormat.format(Long.parseLong(rs.getString("white"))) + "\" OR ");
//                _caseIds.append("caseId:\"" + decimalFormat.format(Integer.parseInt(rs.getString("white"))) + "\" OR ");
            }

            if (_caseIds.toString().endsWith(" OR ")){
                _caseIds.delete(_caseIds.toString().length()-4, _caseIds.toString().length()).trimToSize();
            }

            sqliteHelper.close();

            if (_caseIds.toString().length()>0){
                return getJudgementHeaderById(_caseIds.toString());
            }
            return "";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public static String getJudgementHeaderById(String id) {
        String query = id, sortBy = "decisionDate_sort STRING DESC",facet_fields = "",
                fields = "caseId,title,courttitle,decisionDate,acts_store", hl_fields = "", filter_query = "(DocType:Judgements)";

        SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
        JsonObject jo = new JsonObject();
        try {
            jo = searchUtility.search(query, 0, 65000, sortBy, facet_fields, fields, hl_fields, filter_query, "");
            if (jo != null) {
                JsonArray jData = jo.getAsJsonArray("docs");
                StringBuilder stb = new StringBuilder("");
                Integer _tempCounter = 0;
                for (JsonElement ele : jData) {
                    JsonObject obj = (JsonObject) ele;
                    _tempCounter+=1;

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    SimpleDateFormat sm = new SimpleDateFormat("dd MMM yyyy");
                    String jDate = sm.format(format.parse(obj.get("decisionDate").getAsString()));

                    stb.append("(" + _tempCounter + ") <a style=\"color:#8b0000;text-decoration:none;font-weight:bold;\" href=" + obj.get("caseId").getAsString() + ">" + obj.get("title").getAsString() + "</a>, <span style=\"color:darkred\">"+ jDate + "</span> - <span style=\"color:darkolivegreen\">" + obj.get("courttitle").getAsString() + "</span><br/><span style=\"text-align:justify ; color:blue\">" + obj.get("acts_store").getAsString() + "</span><br/><br/>");
                }
                return stb.toString();
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Found!";
    }

    public static String getCitatorByCaseId(String caseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.GET_CITATOR_BY_CASEID, caseId);
            ResultSet rs = sqliteHelper.select(q);
            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

            StringBuilder _caseIds = new StringBuilder("");
            while (rs.next()) {
                _caseIds.append("caseId:\"" + rs.getString("caseId") + "\" OR ");
            }

            if (_caseIds.toString().endsWith(" OR ")){
                _caseIds.delete(_caseIds.toString().length()-4, _caseIds.toString().length()).trimToSize();
            }

            sqliteHelper.close();

            if (_caseIds.toString().length()>0){
                return getJudgementHeaderById(_caseIds.toString().trim());
            }
//            return stb.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return "";
    }

    public static int getCitatorCountByCaseId(String caseId) {
        int _totalRows = 0;
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.IS_CITATOR_AVAILABLE_BY_CASEID, caseId);
            ResultSet rs = sqliteHelper.select(q);

            while (rs.next()){
                _totalRows = rs.getInt(1);
            }
            rs.close();
            sqliteHelper.close();
            return _totalRows;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return _totalRows;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return _totalRows;
        }
    }

    public static boolean isCitatorAvailableByCaseId(String caseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.IS_CITATOR_AVAILABLE_BY_CASEID, caseId);
            ResultSet rs = sqliteHelper.select(q);
            int _totalRows = 0;
            while (rs.next()){
                _totalRows = rs.getInt(1);
            }
            rs.close();
            sqliteHelper.close();
            if (_totalRows>0){
                return true;
            }else{
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean isJudicialCitatorAvailableByCaseId(String caseId) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
            sqliteHelper.open();
            String q = String.format(Queries.IS_JUDICIAL_CITATOR_AVAILABLE_BY_CASEID, caseId);
            ResultSet rs = sqliteHelper.select(q);
            int _totalRows = 0;
            while (rs.next()){
                _totalRows = rs.getInt(1);
            }
            rs.close();
            sqliteHelper.close();
            if (_totalRows>0){
                return true;
            }else{
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static String sendEmail(String email, String username, String subject, String content) {
        String url = Queries.EMAIL_SERVER_API;
//        String urlParameters = "email="+email+"&title="+subject+"&content="+content;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("email", email);
        params.put("title", subject);
        params.put("content", content);
//        System.out.println("content");
//        System.out.println(content);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            postData.append('=');
            try {
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        byte[] postDataBytes = null;
        try {
            postDataBytes = postData.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {

            URL myurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postDataBytes);
            }

            StringBuilder content_sb;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content_sb = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content_sb.append(line);
                    content_sb.append(System.lineSeparator());
                }
            }

            if (content_sb.toString().contains("true")){
                return "Email sent successfully!";
            }else{
                return "Email failed to send!";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return "Email failed to send!";
    }

    public static String getLicText() {
        HashMap<String, String> userDetails = new ServiceHelper().getUserDetails();
        String licText = "<span style=\"color: red;font-weight: bold;font-size: 0.875em;\">This Product is Licensed to " + userDetails.get("user_name") + ", " + userDetails.get("organization_name") + ", " + userDetails.get("city") + "</span>";
        return licText;
    }

    public static HashMap<String, String> getUserDetails() {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            sqliteHelper.open();
            String q = Queries.GET_USER_DETAILS;
            ResultSet rs = sqliteHelper.select(q);

            HashMap<String, String> userDetails = new HashMap<>();
            if (rs.next()) {
                userDetails.put("agent_code", rs.getString("agent_code"));
                userDetails.put("user_name", rs.getString("user_name"));
                userDetails.put("organization_name", rs.getString("organization_name"));
                userDetails.put("client_number", rs.getString("client_number"));
                userDetails.put("client_email", rs.getString("client_email"));
                userDetails.put("city", rs.getString("city"));
                userDetails.put("lic_key", rs.getString("lic_key"));
                userDetails.put("client_package", rs.getString("client_package"));
                userDetails.put("sub_id", rs.getString("sub_id"));
            }
            sqliteHelper.close();
            return userDetails;
//            return new ResponseMaster(userDetails, 200);
        } catch (Exception ex) {
            return null;
        }
    }

    public static ResponseMaster getCurrentVersion() {
        try {

            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();
//            HashMap<String, Object> data = new HashMap<String, Object>();
            String q = Queries.GET_VERSION_MASTER;
            ResultSet rs = sqliteHelper.select(q);
            HashMap<String, String> versionDetails = new HashMap<>();
            if (rs.next()) {
                versionDetails.put("versionId", rs.getString("versionId"));
                versionDetails.put("isCommentry", rs.getString("isCommentry"));
                versionDetails.put("isJharkhandPoliceManual", rs.getString("isJharkhandPoliceManual"));
            }
            sqliteHelper.close();
            return new ResponseMaster(versionDetails, 200);
        } catch (Exception ex) {
//            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex, 101);
        }

    }

    public static int isCommentaryHide() {
        HashMap<String, String> versionDetails = (HashMap<String, String>) getCurrentVersion().getData();
        return Integer.parseInt(versionDetails.get("isCommentry"));
//        return 0;
//        return Boolean.parseBoolean(versionDetails.get("isCommentry"));
    }

    private final String USER_AGENT = "Mozilla/5.0";

    public static ResponseMaster checkUpdate() {
        try {
            HashMap<String, String> userDetails = (HashMap<String, String>) getUserDetails();
            HashMap<String, String> versionDetails = (HashMap<String, String>) getCurrentVersion().getData();
            String getUpdateQuery = "http://www.supreme-today.com:8080/api/get_desktop_updates?version=" + versionDetails.get("versionId") + "&sub_id=" + userDetails.get("sub_id") + "&lic_key=" + userDetails.get("lic_key");
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                getUpdateQuery = getUpdateQuery.replace("www.supreme-today.com", "www.indiancaselawfinder.com");
            }
            String ret = HttpClientHelper.sendGET(getUpdateQuery);

            JsonObject j = (JsonObject) new JsonParser().parse(ret);

            int code = j.get("code").getAsInt();
            if (code == 200) {
                List<String> courtList = new ArrayList();
                JsonArray jsonArray = j.get("data").getAsJsonArray();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject obj = jsonArray.get(i).getAsJsonObject();
                    JsonArray courtArray = obj.get("courts").getAsJsonArray();
                    for (int k = 0; k < courtArray.size(); k++) {
                        String courtString = courtArray.get(k).getAsString();
                        if (!courtList.contains(courtString)) {
                            courtList.add(courtString);
                        }
                    }
                }

                SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
                sqliteHelper.open();
                String q = String.format(Queries.GET_COURTS_BY_IDS, String.join(",", courtList));
                List<String> courtNameList = new ArrayList<>();
                ResultSet rs = sqliteHelper.select(q);
                while (rs.next()) {
                    courtNameList.add(rs.getString("courtName"));
                }
                sqliteHelper.close();

                JsonObject jObject = new JsonObject();
                jObject.addProperty("courts", String.join(",", courtNameList));
                jObject.add("versions", j.get("data"));
                return new ResponseMaster(jObject, 200);
            } else {
                if (code == 203) {
                    SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
                    sqliteHelper.open();
                    String q = "";

                    q = String.format(Queries.DELETE_USER);
                    boolean ret1 = sqliteHelper.delete(q);

                }
                return new ResponseMaster(j.get("data"), code);
            }

        } catch (UnknownHostException ex) {
//            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex.toString(), 101);

        } catch (Exception ex) {
//            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseMaster(ex.toString(), 102);
        }
    }

    public static ResponseMaster getUpdate(String version, String court) {
        try {
            HashMap<String, String> userDetails = getUserDetails();
            String url = "http://www.supreme-today.com:8080/api/update_desktop?version=" + version + "&court=" + court + "&sub_id=" + userDetails.get("sub_id") + "&lic_key=" + userDetails.get("lic_key");
            String fileName = court;
            String indexPath = Queries.INDEX_PATH;//args[0];
            String path = Queries.INDEX_PATH + "\\updates";

            Type type = new TypeToken<PropertyMaster>(){}.getType();
            PropertyMaster pm = new Gson().fromJson(Queries.PROPERTIES,type);
            PropertyMaster.TableMaster propData = null;
            for (PropertyMaster.TableMaster table : pm.getSchema()) {
                if (table.getTableName().equals("cases_all")) {
                    propData = table;
                    System.out.println("break");
                    break;
                }
            }

            PropertyMaster.TableMaster actPropData = null;
            for (PropertyMaster.TableMaster table : pm.getSchema()) {
                if (table.getTableName().equals("BareActs")) {
                    actPropData = table;
                    System.out.println("break");
                    break;
                }
            }
            UpdateHelper.update(url, path, fileName, indexPath, propData, actPropData);
            return new ResponseMaster("Sucess", 200);
        } catch (Exception e) {
            new Utils().showErrorDialog(e);
            return new ResponseMaster(e.toString(), 102);
        }
    }

    public static ResponseMaster versionUpdate(String version) {
        try {
            SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper.open();

            String q = String.format(Queries.UPDATE_VERSION, version);

            int rs = sqliteHelper.insert(q);
            if (rs > 0) {
                sqliteHelper.close();
                return new ResponseMaster("Update Succesfully!", 200);
            } else {
                sqliteHelper.close();
                return new ResponseMaster("Sql Error!", 201);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            new Utils().showErrorDialog(ex);
            return new ResponseMaster(ex, 101);
        } catch (Exception ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
            new Utils().showErrorDialog(ex);
            return new ResponseMaster(ex, 101);
        }
    }
}