///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.project.utility;
//
//
//import com.google.gson.Gson;
////import com.project.extrautility.PropertyMaster;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class Main2 {
//
//    public static void main2(String[] args) throws SQLException, ClassNotFoundException {
//        ////System.out.println(SortField.Type.DOC.toString());
////        ////System.out.println(SortField.Type.INT.toString());
//////        ////System.out.println(SortField.Type.DOC.toString());
////        ////System.out.println(SortField.Type.STRING.toString());
////        exit();
////        DOC
////INT
////STRING
//        String indexPath = "F:\\iconflux\\projects\\supreme_desktop\\Builds\\Data\\Index_07_01";//args[0];
//        String databaseString = "jdbc:sqlserver://192.168.0.210\\SQLEXPRESS:1433;user=sa;password=iconflux;database=AllJudgementFinal";//args[1];
////        String databaseString = "jdbc:sqlserver://192.168.1.30:1160;user=sa;password=iconflux;database=AllJudges";//args[1];
//        String courtIds = "1";//args[2];
//        String propPath = "F:\\iconflux\\projects\\supreme_desktop\\SupremeUtility\\properties.json";//args[2];
//        String isCommentry = "0";//args[2];
//        int batchCount = 500;//args[2];
////        String INDEX_TAXO = "F:\\iconflux\\clients\\vikasinfo\\supreme_desktop\\Index\\Supreme_taxo_21_05";
//        Integer Counter = 0;
//
////      String courtId = args[5];
//        for (int i = 0; i < args.length; i++) {
//            if ("-indexpath".equals(args[i])) {
//                indexPath = args[i + 1];
//                i++;
//            }
//            if ("-connstring".equals(args[i])) {
//                databaseString = args[i + 1];
//                i++;
//            }
//            if ("-courtids".equals(args[i])) {
//                courtIds = args[i + 1];
//                i++;
//            }
//            if ("-batchcount".equals(args[i])) {
//                batchCount = Integer.parseInt(args[i + 1]);
//                i++;
//            }
//            if ("-prop".equals(args[i])) {
//                propPath = args[i + 1];
//                i++;
//            }
//            if ("-iscommentry".equals(args[i])) {
//                isCommentry = args[i + 1];
//                i++;
//            }
//        }
//        ////System.out.println(indexPath);
//        ////System.out.println(databaseString);
//        ////System.out.println(courtIds);
//        FileReader reader;
//        PropertyMaster pm = null;
//        try {
////                reader = new FileReader("properties.json");
//            reader = new FileReader(propPath);
//
//            Gson g = new Gson();
//            pm = g.fromJson(reader, PropertyMaster.class);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        com.microsoft.sqlserver.jdbc.
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        Connection conn = DriverManager.getConnection(databaseString);
//        Statement sta = conn.createStatement();
//
//        try {
//            IndexCreator creator = new IndexCreator(indexPath, true);
//            creator.open(true);
//            for (PropertyMaster.TableMaster table : pm.getSchema()) {
//                if (table.getTableName().equals("Commentary") && !isCommentry.equals("1")) {
//                    continue;
//                }
//                System.out.println(table.getTableName() + " Index Started");
//                ArrayList<String> cases_Id_List = new ArrayList<String>();
//                ////System.out.println(table.getWhere());
//
//                String outerQuery = "select caseId from " + table.getTableName();
//                if (!table.getWhere().isEmpty()) {
//                    outerQuery += " WHERE " + String.format(table.getWhere(), courtIds);
//                }
//                ////System.out.println(outerQuery);
//                // select all caseids from the table
//                ResultSet rss = sta.executeQuery(outerQuery);
//                while (rss.next()) {
//                    cases_Id_List.add(rss.getString(1));
//                }
//                System.out.println("count:" + cases_Id_List.size());
//
//                for (String caseId : cases_Id_List) {
//                    // get one by one case from the table
//                    String Sql = "select * from " + table.getTableName();
//                    Sql += " WHERE caseId = '" + caseId + "'";
//                    ResultSet rs = sta.executeQuery(Sql);
//                    while (rs.next()) {
//                        try {
//                            List<DocMaster> docs = new ArrayList<DocMaster>();
//                            for (PropertyMaster.ColumnMaster col : table.getCols()) {
//                                docs.add(new DocMaster(col.getTitle(), rs.getString(col.getTitle()), col.isIsIndex(), col.isIsStore(), col.isIsFacet(), col.getFieldType(), col.isIsSort(), col.isIsEncrypt(), col.isHl()));
//                            }
//                            creator.create(docs);
//                            System.out.println(Counter + " Docs completed");
//                        } catch (Exception ex) {
//                            LogHelper.add(ex.toString() + "###############1\n");
//                            LogHelper.add(ex.getStackTrace().toString() + "###############1\n");
//                            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                    Counter++;
//                    if (Counter % batchCount == 0) {
//                        creator.close();
//                        ////System.out.println("close call");
//                        creator.open(false);
//                    }
//                    rs.close();
//                }
//            }
//            LogHelper.add(Counter + " Documents Completed!");
//            creator.close();
//
//            creator.createAutoSuggest();
//            creator.createSpellChecker();
//
//        } catch (Exception ex) {
//            LogHelper.add(ex.toString() + "###############2\n");
//            LogHelper.add(ex.getStackTrace().toString() + "###############2\n");
//            ////System.out.println(ex.toString());
//        }
//        conn.close();
//    }
//}
