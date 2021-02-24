/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.extrautility.PropertyMaster;
import com.project.utility.DocMaster;
import com.project.utility.IndexCreator;

import com.project.utility.SearchUtility;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author addie
 */
public class UpdateHelper {

    public static boolean update(String url, String path, String fileName, String indexPath, PropertyMaster.TableMaster propData) throws Exception {

        new File(path).mkdir();
        path = path + File.separator;
        if (downloadZipFile(url, fileName, path)) {
            if (unzip(path + fileName, path)) {
                String stNewJarPath = path + "SupremeToday.jar";
                File stNewFile = new File(stNewJarPath);

                if (stNewFile.exists()){
                    String stOldJarPath = Queries.CURRENT_PATH + "SupremeToday.jar";
                    String stOldJarPathCopyTo = Queries.CURRENT_PATH + "SupremeToday_old.jar";
                    File stOldFile = new File(stOldJarPath);
                    File stOldFileCopyTo = new File(stOldJarPathCopyTo);
                    if (stOldFile.exists()){
                        stOldFile.renameTo(stOldFileCopyTo);
                    }
                    Files.copy(stNewFile.toPath(), stOldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                String f = path + fileName + ".Json";
//                String f = path + fileName;
                HashMap<String, Object> liveData = getLiveData(f);
//                liveData.get("casesAll");
//                liveData.get("caseRefered");
                ArrayList<HashMap<String, String>> dataCaseAll = (ArrayList<HashMap<String, String>>) liveData.get("casesAll");
                ArrayList<HashMap<String, String>> dataCaseRefered = (ArrayList<HashMap<String, String>>) liveData.get("caseRefered");
                HashMap<String, Integer> distinctCaseRefered = (HashMap<String, Integer>) liveData.get("distinctCaseRefered");
//                //System.out.println(new Gson().toJson(liveData));
                IndexCreator creator = new IndexCreator(indexPath, true);
                creator.open(false);
                SqliteHelper sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
                sqliteHelper.open();
                int cnt = 1;
                for (HashMap<String, String> liveDataObj : dataCaseRefered) {
                    try {
                        boolean flg = true;
                        int checkedCounter = (Integer) distinctCaseRefered.get(liveDataObj.get("caseId"));
//                            String.format(Queries.GET_COURTID_FROM_CASE_REFFERED_BY_BOTH_CASEID,liveDataObj.get("caseId"),liveDataObj.get("targetId"));
                        if (checkedCounter == 0) {
//                        distinctCaseRefered.set(liveDataObj.get("caseId"))
                            ResultSet rs = sqliteHelper.select(String.format(Queries.GET_COURTID_FROM_CASE_REFFERED_BY_BOTH_CASEID, liveDataObj.get("caseId")));
                            if (rs.next()) {
                                flg = false;
                            }
                            distinctCaseRefered.put(liveDataObj.get("caseId"), 1);
                            //System.out.println(liveDataObj.get("caseId"));
                        }
                        if (flg) {
                            String q = String.format(Queries.INSERT_CASE_REFFERED, liveDataObj.get("cdType"), liveDataObj.get("caseId"), liveDataObj.get("targetId"), liveDataObj.get("id1cdtype"), liveDataObj.get("Case_referred"), liveDataObj.get("caseCitator"));
                            int ret = sqliteHelper.insert(q);
                            //System.out.println(cnt++);
                        }
                    } catch (Exception e) {
                    }
                }
                sqliteHelper.close();
                for (HashMap<String, String> liveDataObj : dataCaseAll) {
                    List<DocMaster> docs = new ArrayList<DocMaster>();
                    for (PropertyMaster.ColumnMaster col : propData.getCols()) {
                        docs.add(new DocMaster(col.getTitle(), liveDataObj.get(col.getTitle()), col.isIsIndex(),
                                col.isIsStore(), col.isIsFacet(),
                                col.getFieldType(), col.isIsSort(),
                                col.isIsEncrypt(), col.isHl()));
                    }
                    //System.out.println("citation:" + liveDataObj.get("citation"));
                    String citationData = liveDataObj.get("citation_store");
                    //System.out.println("judgementHeader_store:" + liveDataObj.get("judgementHeader_store"));

                    sqliteHelper.open();
                    for (String s : citationData.split(";")) {
                        String[] citationArray = s.trim().split(" ");
                        String publisher = "", year = "", volume = "", pageNo = "";
                        try {
                            if (citationArray.length > 3) {
                                publisher = citationArray[2];
                                year = citationArray[0];
                                volume = citationArray[1];
                                pageNo = citationArray[3];
//                                System.out.println(liveDataObj.get("caseId"));
                                String q = String.format(Queries.INSERT_CITATION, publisher, year, volume, pageNo);
//                                System.out.println(q);
                                int rs = sqliteHelper.insert(q);
                            }
                        } catch (Exception e) {
//                            //System.out.println(e.toString());
                        }
                    }
                    sqliteHelper.close();
                    if (!docs.isEmpty()) {
                        creator.delete("+caseId:" + liveDataObj.get("caseId"));
                        creator.create(docs);
                    }
                }
                creator.close();
                new File(f).delete();
                new File(path + fileName).delete();
            }
            return true;
        }
//            List<DocMaster> docs = new ArrayList<DocMaster>();
        return false;
    }

    public static HashMap<String, Object> getLiveData(String f) throws Exception {
        ArrayList<HashMap<String, String>> dataCaseAll = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> dataCaseRefered = new ArrayList<HashMap<String, String>>();
        HashMap<String, Object> dataAll = new HashMap<String, Object>();
        HashMap<String, Integer> distinctCaseRefered = new HashMap<String, Integer>();

//            String f = "F:\\iconflux\\projects\\supreme_desktop\\Builds\\Data\\LiveUpdate.json";
        JsonParser parser = new JsonParser();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(f), "UTF8"));

//        JsonElement jsonElement = parser.parse(new FileReader(f));
        JsonElement jsonElement = parser.parse(in);
        JsonArray casesAllJsonArray = jsonElement.getAsJsonObject().get("AllCases").getAsJsonArray();
        JsonArray casesReferedJsonArray = jsonElement.getAsJsonObject().get("AllCaseReffered").getAsJsonArray();
//        JsonArray jsonArray = jsonElement.getAsJsonArray();
//            //System.out.println(jsonArray.toString());

        for (int i = 0; i < casesAllJsonArray.size(); i++) {
            JsonElement ele = casesAllJsonArray.get(i);
            JsonObject obj = (JsonObject) ele;
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();

            HashMap<String, String> data = new HashMap<String, String>();
            for (Map.Entry<String, JsonElement> entrySet1 : entrySet) {
                String key = entrySet1.getKey();
                JsonElement value = entrySet1.getValue();
                if (value != null && !value.isJsonNull()) {
                    data.put(key, value.getAsString());
                }
            }
            if (data.keySet().size() > 0) {
                dataCaseAll.add(data);
            }
        }
        for (int i = 0; i < casesReferedJsonArray.size(); i++) {
            JsonElement ele = casesReferedJsonArray.get(i);
            JsonObject obj = (JsonObject) ele;
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();

            HashMap<String, String> data = new HashMap<String, String>();
            for (Map.Entry<String, JsonElement> entrySet1 : entrySet) {
                String key = entrySet1.getKey();
                JsonElement value = entrySet1.getValue();
                if (value != null && !value.isJsonNull()) {
                    if (key.equals("caseId")) {
                        distinctCaseRefered.put(value.getAsString(), 0);
                    }
                    data.put(key, value.getAsString());
                }
            }
            if (data.keySet().size() > 0) {
                dataCaseRefered.add(data);
            }
        }
        dataAll.put("casesAll", dataCaseAll);
        dataAll.put("caseRefered", dataCaseRefered);
        dataAll.put("distinctCaseRefered", distinctCaseRefered);
        return dataAll;
    }

    public static Map<String, String> getDataByCaseId(String caseId) throws Exception {
//        00100000009
        Map<String, String> data = new HashMap<String, String>();

        SearchUtility searchUtility = new SearchUtility("F:\\iconflux\\projects\\supreme_desktop\\Builds\\Data\\Index_1");
        String query = "+caseId:" + caseId, sortBy = "", facet_fields = "", fields = "all", hl_fields = "false", filter_query = "";
//            String query = "+content:court",sortBy = "",facet_fields = "",fields = "all",hl_fields = "false",filter_query = "";
        JsonObject jo = searchUtility.search(query, 0, 1, sortBy, facet_fields, fields, hl_fields, filter_query,"");
//            //System.out.println(jo.toString());
        JsonArray jData = jo.getAsJsonArray("docs");
//            //System.out.println(facets);
//            //System.out.println("COUNT:" + jo.get("numFound"));
        for (JsonElement ele : jData) {

            JsonObject obj = (JsonObject) ele;
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
            for (Map.Entry<String, JsonElement> entrySet1 : entrySet) {
                String key = entrySet1.getKey();
//                    //System.out.println(key);
//                    Object value = entrySet1.getValue();
                data.put(key, entrySet1.getValue().getAsString());
            }

//                //System.out.println(obj.get("caseId").getAsString());
//                LogHelper.add(obj.get("DocType").getAsString());
//                //System.out.println(obj.get("content_store").getAsString().length());
////                //System.out.println(obj.get("summary").getAsString());
//                //System.out.println(obj.get("title").getAsString());
        }

        return data;

    }

    public static boolean downloadZipFile(String url, String fileName, String path) throws Exception {

        URL url1 = new URL(url);
        URLConnection conn = url1.openConnection();
        InputStream in = conn.getInputStream();
        FileOutputStream out = new FileOutputStream(path + fileName);
        byte[] b = new byte[1024];
        int count;
        while ((count = in.read(b)) >= 0) {
            out.write(b, 0, count);
        }
        out.flush();
        out.close();
        in.close();
        return true;

    }

    public static boolean unzip(String zipFilePath, String destDirectory) throws Exception {

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();

        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        return true;

    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}
