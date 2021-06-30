/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.Main;
import com.project.ScheduledTask;
import com.project.utility.IndexCreator;
import com.project.utility.SearchUtility;
import java.io.File;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author addie
 */
public class LicenseHelper {

    public static boolean isLicenseAvail() {
        if (new File(Queries.LOCAL_DB_PATH).exists()) {
            SqliteHelper sqliteHelper = null;
            try {
                //            return false;
                sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
                sqliteHelper.open();
                ResultSet rs = sqliteHelper.select(Queries.GET_USER_DETAILS);
                if (rs.next()) {
//                    sqliteHelper.close();
                    return true;
                }
//                sqliteHelper.close();
//            if (rs.next()) {
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LicenseHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(LicenseHelper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                sqliteHelper.close();
            }
        }

        return false;
    }

    public static int isLicenseValid() {
        String packageCode = "";
        String uId = "";
        SqliteHelper sqliteHelper1 = null;
        try {
            sqliteHelper1 = new SqliteHelper(Queries.DB_PATH, true);
            sqliteHelper1.open();
            ResultSet rs = sqliteHelper1.select(Queries.GET_VERSION_MASTER);
            while (rs.next()) {
                packageCode = rs.getString("packageCode");
                uId = rs.getString("dId");
            }
        } catch (Exception e) {

        } finally {
            sqliteHelper1.close();

        }
        SqliteHelper sqliteHelper = null;
        try {

            sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
            //System.out.println("isLicenseValid 1");
            sqliteHelper.open();
            //System.out.println("isLicenseValid 2");
            ResultSet rs = sqliteHelper.select(Queries.GET_USER_DETAILS);
            //System.out.println("isLicenseValid 3");
            if (rs.next()) {
                String regKey = KeyHelper.getRegKey();
                String lic_key_org = KeyHelper.orgActivationKey(regKey, packageCode);
                String licKey = rs.getString("lic_key");
                if (licKey.length() > 0 && licKey.substring(0, 3).equals("DNG")) {
                    licKey = licKey.replace("DNG_", "");
                    DongleRockyHelper dongleRockyHelper = new DongleRockyHelper();
                    if (dongleRockyHelper.isDongleConnected()) {
                        String dongleId = new DongleRockyHelper().getHardwareId(uId);
                        if (dongleId.equals(licKey)) {
                            SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
                            String query = "+caseId:1 +DocType:LicenseData", sortBy = "", facet_fields = "", fields = "all", hl_fields = "", filter_query = "";
                            JsonObject jo = searchUtility.search(query, 0, 1, sortBy, facet_fields, fields, hl_fields, filter_query, "");

                            int count = Integer.parseInt(jo.get("numFound").toString());
                            if (count == 0) {
                                return 0;
                            }
//                            if (count > 0) {
//                                System.out.println("jo.getAsJsonArray(\"docs\").size() : "+jo.getAsJsonArray("docs").size());
//                                JsonObject obj = (JsonObject) jo.getAsJsonArray("docs").get(0);
//                                String licKey_Index;
//                                if (obj.get("title").getAsString().length()>14){licKey_Index= obj.get("title").getAsString().substring(0, 14);
//                                }else{licKey_Index= obj.get("title").getAsString();}
//                                System.out.println("licKey_Index : " + licKey_Index + " , lic_key_org : " + lic_key_org);
//                                if (!licKey_Index.equals(lic_key_org)) {
//                                    return 0;
//                                }
//                            }else{
//                                return 0;
//                            }

                            Timer time = new Timer(); // Instantiate Timer Object
                            ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
                            time.schedule(st, 0, 1000*60); // Create Repetitively task for every 1 secs
                            return 1;
                        }
                    } else {
                        return 2;
                    }
                } else {
                    licKey = licKey.substring(0, 14);
                    SearchUtility searchUtility = new SearchUtility(Queries.INDEX_PATH);
                    String query = "+caseId:1 +DocType:LicenseData", sortBy = "", facet_fields = "", fields = "all", hl_fields = "", filter_query = "";
                    JsonObject jo = searchUtility.search(query, 0, 1, sortBy, facet_fields, fields, hl_fields, filter_query, "");

                    int count = Integer.parseInt(jo.get("numFound").toString());
                    if (count > 0) {
                        JsonObject obj = (JsonObject) jo.getAsJsonArray("docs").get(0);
                        String licKey_Index;
                        if (obj.get("title").getAsString().length() > 14){licKey_Index= obj.get("title").getAsString().substring(0, 14);
                        }else{licKey_Index= obj.get("title").getAsString();}
                        if (!lic_key_org.equals(licKey)) {
                            return 0;
                        }
                        if (!licKey_Index.equals(lic_key_org)) {
                            return 0;
                        }
                        return 1;
                    }else{
                        return 0;
                    }
                }

//
                //System.out.println(lic_key_org);
                //System.out.println(licKey + "::licKey:");
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            sqliteHelper.close();
        }
        return 0;
    }

    public static boolean checkLicense(String client_name, String client_number, String client_email, String lic_key, String client_package) {
        try {
            String lic_key_org = KeyHelper.encrypt(KeyHelper.getRegKey(), KeyHelper.KEY);
            //System.out.println(lic_key_org);
            if (lic_key_org.equals(lic_key)) {
                SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
                sqliteHelper.open();
                String q = String.format(Queries.INSERT_USER_DETAILS, client_name, client_number, client_email, lic_key, client_package);
                sqliteHelper.insert(q);
                sqliteHelper.close();
                return true;
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
