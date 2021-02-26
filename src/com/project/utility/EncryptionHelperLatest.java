/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;
import java.io.*;
import java.util.ArrayList;
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

    public static String supreme_dictionary_index_file_path  = "D:\\Projects\\SupremeToday\\SupremeToday\\res\\index.txt";

    public static List<String> supreme_dictionary  = ReadObjectToFile();

    public static List<String> ReadObjectToFile(){
        File f = new File(supreme_dictionary_index_file_path);
        if(!f.exists()) {
            List<String> supreme_dictionary_init = new ArrayList<>();
            supreme_dictionary_init.add("");
//            supreme_dictionary_init.add("");
            WriteObjectToFile(supreme_dictionary_init);
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(supreme_dictionary_index_file_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> supreme_dictionary = null;
        try {
            supreme_dictionary = (List<String>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supreme_dictionary;
    }

    public static void WriteObjectToFile(List<String> supreme_dictionary){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(supreme_dictionary_index_file_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(supreme_dictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if (supreme_dictionary.indexOf(temp[i]) < 0){
                    supreme_dictionary.add(temp[i]);
                }
                int dictionary_index = supreme_dictionary.indexOf(temp[i]);
                System.out.println("ENC | " + dictionary_index + " | " + temp[i]);
                acs.append((char) (dictionary_index));

                //                cs.append(Character.toChars(arr.size()));
//                if (arr.contains(" " + temp[i]) == false) {
//                    arr.add(" " + temp[i]);
//                    if (supreme_dictionary.indexOf(temp[i]) < 0){
//                        supreme_dictionary.add(temp[i]);
//                    }
//                    int dictionary_index = supreme_dictionary.indexOf(temp[i]);
//                    System.out.println("ENC | " + dictionary_index + " | " + temp[i]);
////                    acs.append((char) (dictionary_index));
//                    cs.append(Character.toChars(arr.size()));
//                } else {
//                    System.out.println(temp[i]);
//                    cs.append(Character.toChars(arr.indexOf(" " + temp[i]) + 1));
//                }
            }
            WriteObjectToFile(supreme_dictionary);
            if (sbImg.toString().length() > 0){
                return acs.toString() + sep_img + sbImg.toString();
            }else{
                return acs.toString();
            }

//            if (sbImg.toString().length() > 0){
//                return cs.toString() + sep + acs.toString() + sep_img + sbImg.toString();
//            }else{
//                return cs.toString() + sep + acs.toString();
//            }
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
}
