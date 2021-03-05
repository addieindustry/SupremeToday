/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;
import com.license4j.util.FileUtils;
import com.project.AES;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author addie
 */
public class EncryptionHelperLatest {

//    public static String sep = "--SupremeToday Splitter--";

    public static String sep_img = "--ImageSplitter--";

    public static String secret_key = "ASHISHDAVE";

    public static String start_tag = "#";

    public static String end_tag = "|";

    public static String plain_keyword  = "D:\\Projects\\SupremeToday\\plain_keyword.txt";

    public static String st_keyword  = "D:\\Projects\\SupremeToday\\st_keyword.txt";

    public static List<String> supreme_dictionary  = ReadObjectToFile();

    public static List<String> ReadObjectToFile(){
//        File f = new File(supreme_dictionary_index_file_path);
        List<String> supreme_dictionary_init = new ArrayList<>();
//        supreme_dictionary_init.add("");

        try {
            String inputFile = new String(Files.readAllBytes(Paths.get(st_keyword)));
            String lines[] = inputFile.split("\\n");
            for(int i=0; i<lines.length; i++){
                supreme_dictionary_init.add(AES.decrypt(lines[i].trim().toString(), secret_key));
//                supreme_dictionary_init.add(lines[i].trim().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supreme_dictionary_init;
    }

    public static void EncryptDictionary(){
        List<String> supreme_dictionary_init = new ArrayList<>();
        supreme_dictionary_init.add("");

        try {
            String inputFile = new String(Files.readAllBytes(Paths.get(plain_keyword)));
            String lines[] = inputFile.split("\\n");
            for(int i=0; i<lines.length; i++){
                supreme_dictionary_init.add(lines[i].trim().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(st_keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < supreme_dictionary_init.size(); i++) {
            try {
                if (i == (int) (start_tag.charAt(0))){
                    fw.write(AES.encrypt(start_tag, secret_key) + "\n");

//                    String originalString = "howtodoinjava.com";
//                    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
                }else if (i == (int) (end_tag.charAt(0))){
                    fw.write(AES.encrypt(end_tag, secret_key) + "\n");
//                    fw.write(end_tag);
                }
                fw.write(AES.encrypt(supreme_dictionary_init.get(i).toString(), secret_key) + "\n");
//                fw.write();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        return supreme_dictionary_init;
    }

    public static String encrypt(StringBuilder stemp, String key) {
        if (stemp.toString().trim().isEmpty()) {
            return "";
        }

        Pattern p = Pattern.compile("<img.*>");
        Matcher m = p.matcher(stemp.toString());
        StringBuilder sbImg = new StringBuilder("");
        int _counter = 1;
        while (m.find()) {
            String s = m.group(0);
            stemp.replace(stemp.indexOf(s), stemp.indexOf(s) + s.length(), "<IMAGE" + _counter + ">");
            _counter+=1;
            sbImg.append(s + "|");
        }

        try {
            int iii = 0;
            int i = 0;
            for (i = 0; i <= key.length() - 1; i++) {
                iii += Integer.parseInt(key.substring(i, i + 1).toString());
            }

            ArrayList arr = new ArrayList();
            StringBuilder cs = new StringBuilder();
            StringBuilder acs = new StringBuilder();
            String[] temp = stemp.toString().split("[ ]", -1);

            for (i = 0; i <= temp.length - 1; i++) {
                if (supreme_dictionary.indexOf(temp[i]) >= 0){
                    int dictionary_index = supreme_dictionary.indexOf(temp[i]);
//                    System.out.println("ENC | " + dictionary_index + " | " + temp[i]);
                    acs.append((char) (dictionary_index));
                }else{
//                    acs.append(temp[i]);
                    acs.append(start_tag + temp[i] + end_tag);
                }
            }
//            WriteObjectToFile(supreme_dictionary);
            if (sbImg.toString().length() > 0){
                return acs.toString() + sep_img + sbImg.toString();
            }else{
                return acs.toString();
            }
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    public static String decrypt(StringBuilder sb, String key) {
//            if (sb.toString().trim().isEmpty() || sb.toString().trim().equals(sep)) {
//                return "";
//            }
            StringBuilder sb_html = new StringBuilder("");
            StringBuilder sb_img = new StringBuilder("");
            if (sb.toString().contains(sep_img)){
                String[] strTemp = sb.toString().split(sep_img);
                sb_html = new StringBuilder(strTemp[0].toString());
                sb_img = new StringBuilder(strTemp[1].toString());
            }else{
                sb_html = sb;
            }

//            String[] str = sb_html.toString().split(sep);
//            StringBuilder sb1 = new StringBuilder(str[0].toString());
//            StringBuilder sb2 = new StringBuilder(str[1].toString());

            StringBuilder sb1 = new StringBuilder(sb_html.toString());

//            int iii = 0;
            int i = 0;
//            for (i = 0; i <= key.length() - 1; i++) {
//                iii += Integer.parseInt(key.charAt(i) + "");
//            }
            StringBuilder cs = new StringBuilder();

            Boolean skipPlainText = Boolean.FALSE;
            for (i = 0; i <= sb1.length() - 1; i++) {
                if (start_tag.equals(String.valueOf(sb1.charAt(i)))){
                    skipPlainText = Boolean.TRUE;
                    System.out.println(skipPlainText.toString() + " | " + sb1.charAt(i));
                    cs.append(" ");
                }else if (end_tag.equals(String.valueOf(sb1.charAt(i)))){
                    skipPlainText = Boolean.FALSE;
                    System.out.println(skipPlainText.toString() + " | " + sb1.charAt(i));
                    cs.append(" ");
                }else{
                    if (skipPlainText == Boolean.FALSE){
                        int int_Renamed = ((int) (sb1.charAt(i)));
                        if (supreme_dictionary.stream().count() > int_Renamed){
                            System.out.println("DEC | " + int_Renamed +  " | " + supreme_dictionary.get(int_Renamed));
                            cs.append(" " + supreme_dictionary.get(int_Renamed));
                        }
                    }else{
                        cs.append(String.valueOf(sb1.charAt(i)));
//                        cs.append(String.valueOf(sb1.charAt(i)).replace(start_tag, " ").replace(end_tag, " "));
                    }
                }
            }

            String[] tempImg = sb_img.toString().split("[|]");
            for (i = 0; i <= tempImg.length - 1; i++) {
                if (cs.toString().contains("<IMAGE" + (i + 1) + ">")){
                    cs.replace(cs.indexOf("<IMAGE" + (i + 1) + ">"), cs.indexOf("<IMAGE" + (i + 1) + ">") + ("<IMAGE" + (i + 1) + ">").length(), tempImg[i].toString());
                }
            }

            return cs.toString();
    }


    public static String encrypt_old(StringBuilder stemp, String key) {
        if (stemp.toString().trim().isEmpty()) {
            return "";
        }

        Pattern p = Pattern.compile("<img.*>");
        Matcher m = p.matcher(stemp.toString());
        StringBuilder sbImg = new StringBuilder("");
        int _counter = 1;
        while (m.find()) {
            String s = m.group(0);
            stemp.replace(stemp.indexOf(s), stemp.indexOf(s) + s.length(), "<IMAGE" + _counter + ">");
            _counter+=1;
            sbImg.append(s + "|");
        }

        try {
            int iii = 0;
            int i = 0;
            for (i = 0; i <= key.length() - 1; i++) {
                iii += Integer.parseInt(key.substring(i, i + 1).toString());
            }

            ArrayList arr = new ArrayList();
            StringBuilder cs = new StringBuilder();
            StringBuilder acs = new StringBuilder();
            String[] temp = stemp.toString().split("[ ]", -1);

            for (i = 0; i <= temp.length - 1; i++) {
                if (supreme_dictionary.indexOf(temp[i]) < 0){
                    supreme_dictionary.add(temp[i]);
                }
                int dictionary_index = supreme_dictionary.indexOf(temp[i]);
                System.out.println("ENC | " + dictionary_index + " | " + temp[i]);
                acs.append((char) (dictionary_index));
            }
//            WriteObjectToFile(supreme_dictionary);
            if (sbImg.toString().length() > 0){
                return acs.toString() + sep_img + sbImg.toString();
            }else{
                return acs.toString();
            }
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    public static String decrypt_old(StringBuilder sb, String key) {
//            if (sb.toString().trim().isEmpty() || sb.toString().trim().equals(sep)) {
//                return "";
//            }
        StringBuilder sb_html = new StringBuilder("");
        StringBuilder sb_img = new StringBuilder("");
        if (sb.toString().contains(sep_img)){
            String[] strTemp = sb.toString().split(sep_img);
            sb_html = new StringBuilder(strTemp[0].toString());
            sb_img = new StringBuilder(strTemp[1].toString());
        }else{
            sb_html = sb;
        }

//            String[] str = sb_html.toString().split(sep);
//            StringBuilder sb1 = new StringBuilder(str[0].toString());
//            StringBuilder sb2 = new StringBuilder(str[1].toString());

        StringBuilder sb1 = new StringBuilder(sb_html.toString());

//            int iii = 0;
        int i = 0;
//            for (i = 0; i <= key.length() - 1; i++) {
//                iii += Integer.parseInt(key.charAt(i) + "");
//            }
        StringBuilder cs = new StringBuilder();

        for (i = 0; i <= sb1.length() - 1; i++) {
            int int_Renamed = ((int) (sb1.charAt(i)));

            if (supreme_dictionary.stream().count() > int_Renamed){
                System.out.println("DEC | " + int_Renamed +  " | " + supreme_dictionary.get(int_Renamed));
                cs.append(" " + supreme_dictionary.get(int_Renamed));
            }
//                cs.append(" " + supreme_dictionary.get(int_Renamed));
//                if (sb2.toString().length() > int_Renamed){
//                    cs.append(" " + supreme_dictionary.get(int_Renamed));
//                }
        }

        String[] tempImg = sb_img.toString().split("[|]");
        for (i = 0; i <= tempImg.length - 1; i++) {
            if (cs.toString().contains("<IMAGE" + (i + 1) + ">")){
                cs.replace(cs.indexOf("<IMAGE" + (i + 1) + ">"), cs.indexOf("<IMAGE" + (i + 1) + ">") + ("<IMAGE" + (i + 1) + ">").length(), tempImg[i].toString());
            }
        }

        return cs.toString();
    }

    //    public static ArrayList<Integer> encrypt_binary(StringBuilder stemp, String key) {
////        if (stemp.toString().trim().isEmpty()) {
////            return "";
////        }
//
//        Pattern p = Pattern.compile("<img.*>");
//        Matcher m = p.matcher(stemp.toString());
//        StringBuilder sbImg = new StringBuilder("");
//        int _counter = 1;
//        while (m.find()) {
//            String s = m.group(0);
//            stemp.replace(stemp.indexOf(s), stemp.indexOf(s) + s.length(), "<IMAGE" + _counter + ">");
//            _counter+=1;
//            sbImg.append(s + "|");
//        }
//
//        try {
//            int iii = 0;
//            int i = 0;
//            for (i = 0; i <= key.length() - 1; i++) {
//                iii += Integer.parseInt(key.substring(i, i + 1).toString());
//            }
//
//            ArrayList arr = new ArrayList();
//            StringBuilder cs = new StringBuilder();
//            StringBuilder acs = new StringBuilder();
//            String[] temp = stemp.toString().split("[ ]", -1);
//
//            ArrayList<Integer> arl = new ArrayList<Integer>();
//
//
//            for (i = 0; i <= temp.length - 1; i++) {
//                if (supreme_dictionary.indexOf(temp[i]) < 0){
//                    supreme_dictionary.add(temp[i]);
//                }
//                int dictionary_index = supreme_dictionary.indexOf(temp[i]);
//                System.out.println("ENC | " + dictionary_index + " | " + temp[i]);
//                arl.add(dictionary_index);
////                acs.append((char) (dictionary_index));
//            }
//            WriteObjectToFile(supreme_dictionary);
//            return arl;
////            if (sbImg.toString().length() > 0){
////                return acs.toString() + sep_img + sbImg.toString();
////            }else{
////                return acs.toString();
////            }
//        } catch (RuntimeException ex) {
//            throw ex;
//        }
//    }

}
