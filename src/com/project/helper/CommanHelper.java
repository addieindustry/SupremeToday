/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.project.utility.LogHelper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author addie
 */
public class CommanHelper {

    public static boolean isInternetAvail() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface interf = interfaces.nextElement();
                if (interf.isUp() && !interf.isLoopback()) //    return true;
                {
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CommanHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void setCurrentValue(String fileName, String key, int value) {
        String sep = "Lucene";
        String c = "ÿ";
        String v = "";
        for (int i = 0; i < value; i++) {
            v += c;
        }
        if (Files.exists(Paths.get(fileName))) {
            String data = readFile(fileName);
            String[] dataArr = data.split(sep);
//             if()
            String content = "";
            boolean flg = false;
            for (String s : dataArr) {

                String[] fArray = s.split("L");

                if (fArray.length == 2) {
                    String k = fArray[0];
                    String val = fArray[1];
                    //System.out.println(k + " " + v);
                    if (fArray[0].equals(key)) {
//                        //System.out.println(fArray[1]);
                        val = v;
                        flg = true;
                    }
                    content += k + "L" + val + sep;
//                  //System.out.println(content);
                }
            }
            if (!flg) {
                content += key + "L" + v + sep;
            }
            if (!content.isEmpty()) {
//                    //System.out.println(content);
                writeFile(fileName, content);
            } else {
                writeFile(fileName, data + sep + key + "L" + v + sep);

            }
        } else {
            writeFile(fileName, key + "L" + v + sep);
        }

    }

    public static int getCurrentValue(String fileName, String key) {
        try {
            if (Files.exists(Paths.get(fileName))) {
                BasicFileAttributes attr = Files.readAttributes(Paths.get(fileName), BasicFileAttributes.class);

                if (attr.creationTime() != null) {
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String creationDate = sdf.format(new Date(attr.creationTime().toMillis()));
                    String todayDate = sdf.format(new Date(Queries.SESSION));
                    long diff = sdf.parse(creationDate).getTime() - sdf.parse(todayDate).getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    if (diffDays == 0) {
                        String data = readFile(fileName);
                        String[] dataArr = data.split("Lucene");
                        int totalCount = 0;
                        for (String s : dataArr) {
                            String[] fArray = s.split("L");

                            if (fArray.length == 2) {
                                if (fArray[0].equals(key)) {
//                                    //System.out.println(fArray[1]);
                                    totalCount = fArray[1].length();
                                }
                            }
                        }                        
                        return totalCount;
                    } else {
                        Files.delete(Paths.get(fileName));
                    }
//                    else if(diffDays<0){
//                    
//                    }else{
//                    
//                    }

//                    if (creationDate.equals(td)) {
//                        String data = readFile(fileName);
//                        String[] dataArr = data.split("Lucene");
//                        for (String s : dataArr) {
//                            String[] fArray = s.split("L");
//
//                            if (fArray.length == 2) {
//                                if (fArray[0].equals(key)) {
////                                    //System.out.println(fArray[1]);
//                                    return fArray[1].length();
//                                }
//                            }
//                        }
//                    }else{
////                    Files.delete(Paths.get(fileName));
//                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(CommanHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    public static void writeFile(String fileName, String content) {
        try {

            if (!Files.exists(Paths.get(fileName))) {
                Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
            } else {

                Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.WRITE);
            }
        } catch (IOException ex) {
            Logger.getLogger(LogHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
//                //System.out.println(sCurrentLine);
                sb.append(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
