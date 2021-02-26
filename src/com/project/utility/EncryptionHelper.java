/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;

//import com.project.AdvancedEncryptionStandard;
//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author addie
 */
public class EncryptionHelper {

    public static String sep = "--SupremeToday Splitter--";

    public static String sep_img = "--ImageSplitter--";

//    public static String encrypt_New(StringBuilder stemp, String key) {
//        key = "MZygpewJsCpRrfOr";
//        byte[] encryptionKey = key.getBytes(StandardCharsets.UTF_8);
//        byte[] plainText = stemp.toString().getBytes(StandardCharsets.UTF_8);
//        AdvancedEncryptionStandard advancedEncryptionStandard = new AdvancedEncryptionStandard(
//                encryptionKey);
//        byte[] cipherText = new byte[0];
//        try {
//            cipherText = advancedEncryptionStandard.encrypt(plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new String(cipherText);
//    }

    public static String encrypt_backup(StringBuilder stemp, String key) {
        if (stemp.toString().trim().isEmpty()) {
            return "";
        }
        try {
            int iii = 0;
            int i = 0;
            for (i = 0; i <= key.length() - 1; i++) {
                iii += Integer.parseInt(key.substring(i, i + 1).toString());
            }

            java.util.ArrayList arr = new java.util.ArrayList();
            StringBuilder cs = new StringBuilder();
            StringBuilder acs = new StringBuilder();
            String[] temp = stemp.toString().split("[ ]", -1);

            for (i = 0; i <= temp.length - 1; i++) {
                if (arr.contains(" " + temp[i]) == false) {
                    arr.add(" " + temp[i]);
                    acs.append(getencstr(" " + temp[i], iii));
                    cs.append(Character.toChars(arr.size()));
                } else {
                    cs.append(Character.toChars(arr.indexOf(" " + temp[i]) + 1));
                }
            }
            return cs.toString() + sep + acs.toString();

        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    public static String encrypt(StringBuilder stemp, String key) {
        if (stemp.toString().trim().isEmpty()) {
            return "";
        }

        Pattern p = Pattern.compile("<img.*>");
        Matcher m = p.matcher(stemp.toString());
//        String s = "";
        StringBuilder sbImg = new StringBuilder("");
        int _counter = 1;
        while (m.find()) {
            String s = m.group(0);
            System.out.println(s);
            System.out.println(stemp.indexOf(s));
            System.out.println(s.length());
            stemp.replace(stemp.indexOf(s), stemp.indexOf(s) + s.length(), "<IMAGE" + _counter + ">");
            _counter+=1;
            sbImg.append(s + "|");
            // s now contains "BAR"
        }

        try {
            int iii = 0;
            int i = 0;
            for (i = 0; i <= key.length() - 1; i++) {
                iii += Integer.parseInt(key.substring(i, i + 1).toString());
            }

            java.util.ArrayList arr = new java.util.ArrayList();
            StringBuilder cs = new StringBuilder();
            StringBuilder acs = new StringBuilder();
            String[] temp = stemp.toString().split("[ ]", -1);

            for (i = 0; i <= temp.length - 1; i++) {
                if (arr.contains(" " + temp[i]) == false) {
                    arr.add(" " + temp[i]);
                    acs.append(getencstr(" " + temp[i], iii));
                    cs.append(Character.toChars(arr.size()));
                } else {
                    cs.append(Character.toChars(arr.indexOf(" " + temp[i]) + 1));
                }
            }
            if (sbImg.toString().length() > 0){
                return cs.toString() + sep + acs.toString() + sep_img + sbImg.toString();
            }else{
                return cs.toString() + sep + acs.toString();
            }
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

//    public static String Decrypt_New(StringBuilder sb, String key) {
//        key = "MZygpewJsCpRrfOr";
//        byte[] encryptionKey = key.getBytes(StandardCharsets.UTF_8);
//        byte[] plainText = sb.toString().getBytes(StandardCharsets.UTF_8);
//        AdvancedEncryptionStandard advancedEncryptionStandard = new AdvancedEncryptionStandard(
//                encryptionKey);
//        byte[] cipherText = new byte[0];
//        try {
//            cipherText = advancedEncryptionStandard.decrypt(plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new String(cipherText);
//    }

    public static String Decrypt_backup(StringBuilder sb, String key) {
        //try {
        if (sb.toString().trim().isEmpty() || sb.toString().trim().equals(sep)) {
            return "";
        }
        String[] str = sb.toString().split(sep);
        StringBuilder sb1 = new StringBuilder(str[0].toString());
        StringBuilder sb2 = new StringBuilder(str[1].toString());

        int iii = 0;
        int i = 0;
        for (i = 0; i <= key.length() - 1; i++) {
            iii += Integer.parseInt(key.charAt(i) + "");
        }
        StringBuilder cs = new StringBuilder();
        String spltStr = "\\" + (char) (iii - 1) + "";
        String spltStr2 = (char) (iii - 1) + "";
        if(((char)iii-1)=='0'){
            spltStr = (char) (iii - 1) + "";
        }
        if (sb2.toString().split(spltStr).length==1){
            spltStr=spltStr2;
        }
        String[] temp = sb2.toString().split(spltStr);
        for (i = 0; i <= sb1.length() - 1; i++) {
            int int_Renamed = ((int) (sb1.charAt(i)));
            if (temp.length > int_Renamed)
            {
                cs.append(getdecstr(temp[int_Renamed], iii));
            }
        }
        return cs.toString();
    }

    public static String Decrypt(StringBuilder sb, String key) {
        if (sb.toString().trim().isEmpty() || sb.toString().trim().equals(sep)) {
            return "";
        }
        StringBuilder sb_html = new StringBuilder("");
        StringBuilder sb_img = new StringBuilder("");
        if (sb.toString().contains(sep_img)){
            String[] strTemp = sb.toString().split(sep_img);
            sb_html = new StringBuilder(strTemp[0].toString());
            sb_img = new StringBuilder(strTemp[1].toString());
        }else{
            sb_html = sb;
        }

        String[] str = sb_html.toString().split(sep);
        StringBuilder sb1 = new StringBuilder(str[0].toString());
        StringBuilder sb2 = new StringBuilder(str[1].toString());

        int iii = 0;
        int i = 0;
        for (i = 0; i <= key.length() - 1; i++) {
            iii += Integer.parseInt(key.charAt(i) + "");
        }
        StringBuilder cs = new StringBuilder();
        String spltStr = "\\" + (char) (iii - 1) + "";
        String spltStr2 = (char) (iii - 1) + "";
        if(((char)iii-1)=='0'){
            spltStr = (char) (iii - 1) + "";
        }
        if (sb2.toString().split(spltStr).length==1){
            spltStr=spltStr2;
        }
        String[] temp = sb2.toString().split(spltStr);
        for (i = 0; i <= sb1.length() - 1; i++) {
            int int_Renamed = ((int) (sb1.charAt(i)));
            if (temp.length > int_Renamed)                {
                cs.append(getdecstr(temp[int_Renamed], iii));
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

    private static String getencstr(String str, int iii) {
        int i = 0;
        StringBuilder stb = new StringBuilder("");
        for (i = 0; i <= str.length() - 1; i++) {
            switch ((int) str.charAt(i)) {
                case 65:
                    stb.append((char) (i + iii + 1));
                    break;
                case 66:
                    stb.append((char) (i + iii + 2));
                    break;
                case 67:
                    stb.append((char) (i + iii + 4));
                    break;
                case 68:
                    stb.append((char) (i + iii + 3));
                    break;
                case 69:
                    stb.append((char) (i + iii + 5));
                    break;
                case 70:
                    stb.append((char) (i + iii + 6));
                    break;
                case 71:
                    stb.append((char) (i + iii + 8));
                    break;
                case 72:
                    stb.append((char) (i + iii + 7));
                    break;
                case 73:
                    stb.append((char) (i + iii + 9));
                    break;
                case 74:
                    stb.append((char) (i + iii + 10));
                    break;
                case 75:
                    stb.append((char) (i + iii + 12));
                    break;
                case 76:
                    stb.append((char) (i + iii + 11));
                    break;
                case 77:
                    stb.append((char) (i + iii + 13));
                    break;
                case 78:
                    stb.append((char) (i + iii + 14));
                    break;
                case 79:
                    stb.append((char) (i + iii + 16));
                    break;
                case 80:
                    stb.append((char) (i + iii + 15));
                    break;
                case 81:
                    stb.append((char) (i + iii + 17));
                    break;
                case 82:
                    stb.append((char) (i + iii + 18));
                    break;
                case 83:
                    stb.append((char) (i + iii + 20));
                    break;
                case 84:
                    stb.append((char) (i + iii + 19));
                    break;
                case 85:
                    stb.append((char) (i + iii + 21));
                    break;
                case 86:
                    stb.append((char) (i + iii + 22));
                    break;
                case 87:
                    stb.append((char) (i + iii + 24));
                    break;
                case 88:
                    stb.append((char) (i + iii + 23));
                    break;
                case 89:
                    stb.append((char) (i + iii + 25));
                    break;
                case 90:
                    stb.append((char) (i + iii + 26));
                    break;
                case 97:
                    stb.append((char) (i + iii + 28));
                    break;
                case 98:
                    stb.append((char) (i + iii + 27));
                    break;
                case 99:
                    stb.append((char) (i + iii + 29));
                    break;
                case 100:
                    stb.append((char) (i + iii + 30));
                    break;
                case 101:
                    stb.append((char) (i + iii + 32));
                    break;
                case 102:
                    stb.append((char) (i + iii + 31));
                    break;
                case 103:
                    stb.append((char) (i + iii + 33));
                    break;
                case 104:
                    stb.append((char) (i + iii + 34));
                    break;
                case 105:
                    stb.append((char) (i + iii + 36));
                    break;
                case 106:
                    stb.append((char) (i + iii + 35));
                    break;
                case 107:
                    stb.append((char) (i + iii + 37));
                    break;
                case 108:
                    stb.append((char) (i + iii + 38));
                    break;
                case 109:
                    stb.append((char) (i + iii + 40));
                    break;
                case 110:
                    stb.append((char) (i + iii + 39));
                    break;
                case 111:
                    stb.append((char) (i + iii + 41));
                    break;
                case 112:
                    stb.append((char) (i + iii + 42));
                    break;
                case 113:
                    stb.append((char) (i + iii + 44));
                    break;
                case 114:
                    stb.append((char) (i + iii + 43));
                    break;
                case 115:
                    stb.append((char) (i + iii + 45));
                    break;
                case 116:
                    stb.append((char) (i + iii + 46));
                    break;
                case 117:
                    stb.append((char) (i + iii + 48));
                    break;
                case 118:
                    stb.append((char) (i + iii + 47));
                    break;
                case 119:
                    stb.append((char) (i + iii + 49));
                    break;
                case 120:
                    stb.append((char) (i + iii + 50));
                    break;
                case 121:
                    stb.append((char) (i + iii + 52));
                    break;
                case 122:
                    stb.append((char) (i + iii + 51));
                    break;
                case 32:
                    stb.append((char) (iii - 1));
                    break;
                case 50:
                    stb.append((char) (i + iii + 54));
                    break;
                case 51:
                    stb.append((char) (i + iii + 55));
                    break;
                case 52:
                    stb.append((char) (i + iii + 57));
                    break;
                case 53:
                    stb.append((char) (i + iii + 56));
                    break;
                case 54:
                    stb.append((char) (i + iii + 58));
                    break;
                case 55:
                    stb.append((char) (i + iii + 59));
                    break;
                case 56:
                    stb.append((char) (i + iii + 61));
                    break;
                case 57:
                    stb.append((char) (i + iii + 60));
                    break;
                case 58:
                    stb.append((char) (i + iii + 62));
                    break;
                case 59:
                    stb.append((char) (i + iii + 63));
                    break;
                case 61:
                    stb.append((char) (i + iii + 65));
                    break;
                case 63:
                    stb.append((char) (i + iii + 64));
                    break;
                case 91:
                    stb.append((char) (i + iii + 66));
                    break;
                case 93:
                    stb.append((char) (i + iii + 67));
                    break;
                case 60:
                    stb.append((char) (i + iii + 69));
                    break;
                case 62:
                    stb.append((char) (i + iii + 68));
                    break;
                case 48:
                    stb.append((char) (i + iii + 70));
                    break;
                case 49:
                    stb.append((char) (i + iii + 71));
                    break;
                case 46:
                    stb.append((char) (i + iii + 73));
                    break;
                case 33:
                    stb.append((char) (i + iii + 72));
                    break;
                case 40:
                    stb.append((char) (i + iii + 74));
                    break;
                case 41:
                    stb.append((char) (i + iii + 75));
                    break;
                case 44:
                    stb.append((char) (i + iii + 76));
                    break;

                default:
//                    stb.append((char) (i + iii + (((int) (str.substring(i, i + 1).toString()))) + 100));
                    stb.append((char) (i + iii + (((int) (str.charAt(i)))) + 100));
                    // stb.Append(Convert.ToChar(i + iii + Convert.ToInt32(str.ToCharArray(i, 1)) + 100));break;
            }
        }
        return stb.toString();
    }

    private static String getdecstr(String str, int iii) {
        //new
        int i = 0;
        StringBuilder stb = new StringBuilder("");
        for (i = 0; i <= str.length() - 1; i++) {
            switch (((int) str.charAt(i)) - (i + iii + 1)) {
                case 0:
                    stb.append((char) 32);
                    break;
                case 1:
                    stb.append((char) 65);
                    break;
                case 2:
                    stb.append((char) 66);
                    break;
                case 3:
                    stb.append((char) 68);
                    break;
                case 4:
                    stb.append((char) 67);
                    break;
                case 5:
                    stb.append((char) 69);
                    break;
                case 6:
                    stb.append((char) 70);
                    break;
                case 7:
                    stb.append((char) 72);
                    break;
                case 8:
                    stb.append((char) 71);
                    break;
                case 9:
                    stb.append((char) 73);
                    break;
                case 10:
                    stb.append((char) 74);
                    break;
                case 11:
                    stb.append((char) 76);
                    break;
                case 12:
                    stb.append((char) 75);
                    break;
                case 13:
                    stb.append((char) 77);
                    break;
                case 14:
                    stb.append((char) 78);
                    break;
                case 15:
                    stb.append((char) 80);
                    break;
                case 16:
                    stb.append((char) 79);
                    break;
                case 17:
                    stb.append((char) 81);
                    break;
                case 18:
                    stb.append((char) 82);
                    break;
                case 19:
                    stb.append((char) 84);
                    break;
                case 20:
                    stb.append((char) 83);
                    break;
                case 21:
                    stb.append((char) 85);
                    break;
                case 22:
                    stb.append((char) 86);
                    break;
                case 23:
                    stb.append((char) 88);
                    break;
                case 24:
                    stb.append((char) 87);
                    break;
                case 25:
                    stb.append((char) 89);
                    break;
                case 26:
                    stb.append((char) 90);
                    break;
                case 27:
                    stb.append((char) 98);
                    break;
                case 28:
                    stb.append((char) 97);
                    break;
                case 29:
                    stb.append((char) 99);
                    break;
                case 30:
                    stb.append((char) 100);
                    break;
                case 31:
                    stb.append((char) 102);
                    break;
                case 32:
                    stb.append((char) 101);
                    break;
                case 33:
                    stb.append((char) 103);
                    break;
                case 34:
                    stb.append((char) 104);
                    break;
                case 35:
                    stb.append((char) 106);
                    break;
                case 36:
                    stb.append((char) 105);
                    break;
                case 37:
                    stb.append((char) 107);
                    break;
                case 38:
                    stb.append((char) 108);
                    break;
                case 39:
                    stb.append((char) 110);
                    break;
                case 40:
                    stb.append((char) 109);
                    break;
                case 41:
                    stb.append((char) 111);
                    break;
                case 42:
                    stb.append((char) 112);
                    break;
                case 43:
                    stb.append((char) 114);
                    break;
                case 44:
                    stb.append((char) 113);
                    break;
                case 45:
                    stb.append((char) 115);
                    break;
                case 46:
                    stb.append((char) 116);
                    break;
                case 47:
                    stb.append((char) 118);
                    break;
                case 48:
                    stb.append((char) 117);
                    break;
                case 49:
                    stb.append((char) 119);
                    break;
                case 50:
                    stb.append((char) 120);
                    break;
                case 51:
                    stb.append((char) 122);
                    break;
                case 52:
                    stb.append((char) 121);
                    break;
                case 53:
                    stb.append((char) 32);
                    break;
                case 54:
                    stb.append((char) 50);
                    break;
                case 55:
                    stb.append((char) 51);
                    break;
                case 56:
                    stb.append((char) 53);
                    break;
                case 57:
                    stb.append((char) 52);
                    break;
                case 58:
                    stb.append((char) 54);
                    break;
                case 59:
                    stb.append((char) 55);
                    break;
                case 60:
                    stb.append((char) 57);
                    break;
                case 61:
                    stb.append((char) 56);
                    break;
                case 62:
                    stb.append((char) 58);
                    break;
                case 63:
                    stb.append((char) 59);
                    break;
                case 64:
                    stb.append((char) 63);
                    break;
                case 65:
                    stb.append((char) 61);
                    break;
                case 66:
                    stb.append((char) 91);
                    break;
                case 67:
                    stb.append((char) 93);
                    break;
                case 68:
                    stb.append((char) 62);
                    break;
                case 69:
                    stb.append((char) 60);
                    break;
                case 70:
                    stb.append((char) 48);
                    break;
                case 71:
                    stb.append((char) 49);
                    break;
                case 72:
                    stb.append((char) 33);
                    break;
                case 73:
                    stb.append((char) 46);
                    break;
                case 74:
                    stb.append((char) 40);
                    break;
                case 75:
                    stb.append((char) 41);
                    break;
                case 76:
                    stb.append((char) 44);
                    break;

                default:
                    stb.append((char) ((((int) (str.charAt(i)))) - (i + iii + 101)));

                    break;
            }
        }
        return " " + stb.toString();
    }
}
