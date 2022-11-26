/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author addie
 */
public class KeyHelper {

    public static String KEY = "SiUcPoRnEfMlEux8974630";

    public static String getRegKey() {
        if (OSValidator.isUnix() || OSValidator.isSolaris()) {
            String k = LinuxHardwareHelper.getSerialNumber();
            if (k != null) {
                k = k.replaceAll("[^A-Z0-9]+", "");
                if (k.length() > 12) {
                    k = k.substring(k.length() - 12);
                }
            }
            return k;
        } else if (OSValidator.isMac()) {
            String k = getMacRegKey();
            if (k != null) {
                k = k.replaceAll("[^A-Z0-9]+", "");
                if (k.length() > 12) {
                    k = k.substring(k.length() - 12);
                }else if (k.length() < 12){
                    k = k + StringUtils.repeat("A", 12 - k.length());;
                }
            }
            return k;
//            return getMacRegKey();
        }

//        if (OSValidator.isMac()) {
//            return getMacRegKey();
//        }


        String HardwareIDFromHostName = com.license4j.HardwareID.getHardwareIDFromHostName();
        String HardwareIDFromVolumeSerialNumber = com.license4j.HardwareID.getHardwareIDFromVolumeSerialNumber();

        String regKey = "";

        if (HardwareIDFromVolumeSerialNumber != null && HardwareIDFromHostName != null) {
            if (HardwareIDFromVolumeSerialNumber.length() > 6 && HardwareIDFromHostName.length() > 6) {
                regKey = HardwareIDFromVolumeSerialNumber.substring(HardwareIDFromVolumeSerialNumber.length() - 6, HardwareIDFromVolumeSerialNumber.length())
                        + HardwareIDFromHostName.substring(HardwareIDFromHostName.length() - 6, HardwareIDFromHostName.length());
            } else if (HardwareIDFromVolumeSerialNumber.length() < 6) {
                regKey = HardwareIDFromHostName.substring(HardwareIDFromHostName.length() - 12, HardwareIDFromHostName.length());
            } else if (HardwareIDFromHostName.length() < 6) {
                regKey = HardwareIDFromVolumeSerialNumber.substring(HardwareIDFromVolumeSerialNumber.length() - 12, HardwareIDFromVolumeSerialNumber.length());
            }
        }
        regKey = regKey.toUpperCase();
        return regKey;
    }

//    public static String getRegKey1() {
//        if (OSValidator.isUnix() || OSValidator.isSolaris()) {
//            String k = LinuxHardwareHelper.getSerialNumber();
//            if (k != null) {
//                k = k.replaceAll("[^A-Z0-9]+", "");
//                if (k.length() > 12) {
//                    k = k.substring(k.length() - 12);
//                }
//            }
//            return k;
//        } else if (OSValidator.isMac()) {
//            String k = getMacRegKey();
//            if (k != null) {
//                k = k.replaceAll("[^A-Z0-9]+", "");
//                if (k.length() > 12) {
//                    k = k.substring(k.length() - 12);
//                }else if (k.length() < 12){
//                    k = k + StringUtils.repeat("a", 12 - k.length());;
//                }
//            }
//            return k;
////            return getMacRegKey();
//        } else if (OSValidator.isWindows()) {
//            return getWinRegKey();
////            return "";
//        }
//    }

    public static String getMacRegKey() {
        String sn = null;
        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"/usr/sbin/system_profiler", "SPHardwareDataType"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String marker = "Serial Number";
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains(marker)) {
                    sn = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    public static String getWinRegKey() {
        String diskDriveSerial = null;
        String diskDrivePNB = null;
        System.out.println(getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,serialnumber"}, "IDE"));
        if (diskDriveSerial == null || diskDriveSerial.isEmpty()) {
            diskDriveSerial = getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,serialnumber"}, "IDE").replaceAll("[^A-Z0-9]+", "");
//            System.out.println("serialnumber:" + diskDriveSerial);
        }
        if (diskDriveSerial == null || diskDriveSerial.isEmpty()) {

            diskDriveSerial = getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,serialnumber"}, "SCSI").replaceAll("[^A-Z0-9]+", "");
        }
        if (diskDriveSerial == null || diskDriveSerial.isEmpty()) {
            diskDriveSerial = getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,PNPDeviceID"}, "IDE").replaceAll("[^A-Z0-9]+", "");
//            System.out.println("PNPDeviceID:" + diskDriveSerial);

        }

        if (diskDriveSerial == null || diskDriveSerial.isEmpty()) {
            diskDriveSerial = getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,PNPDeviceID"}, "SCSI").replaceAll("[^A-Z0-9]+", "");
        }

//        System.out.println("InterfaceType,PNPDeviceID::"+getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,PNPDeviceID"}, "IDE"));
//        System.out.println("InterfaceType,serialnumber::"+getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,serialnumber"}, "IDE"));
//        System.out.println("ProcessorId::"+getDeviceData(new String[]{"wmic", "cpu", "get", "ProcessorId"}, "ProcessorId"));
//        String diskDriveSerial = getDeviceData(new String[]{"wmic", "diskdrive", "get", "InterfaceType,serialnumber"}, "IDE").replaceAll("[^A-Z0-9]+", "");
        String cpuProcessorId = getDeviceData(new String[]{"wmic", "cpu", "get", "ProcessorId"}, "ProcessorId").replaceAll("[^A-Z0-9]+", "");
//        System.out.println("cpuProcessorId:" + cpuProcessorId);

//        System.out.println("cpuProcessorId::" + cpuProcessorId);
//        System.out.println("diskDriveSerial::" + diskDriveSerial);
//        String baseBoardSerial = getDeviceData(new String[]{"wmic", "baseboard", "get", "serialnumber"}, "SerialNumber").replaceAll("[^A-Z0-9]+", "");
//        String diskDriveModel = getDeviceData(new String[]{"wmic", "diskdrive", "get", "model"}, "Model");
//        String biosSerial = getDeviceData(new String[]{"wmic", "bios", "get", "serialnumber"}, "SerialNumber");
//        String regKey = diskDriveSerial + diskDriveModel + baseBoardSerial + biosSerial;
//        String regKey = diskDriveSerial + diskDriveModel + baseBoardSerial;
//        regKey
//        System.out.println("baseBoardSerial: " + cpuProcessorId);
//        System.out.println("diskDriveSerial: " + diskDriveSerial);
        String regKey = "";
        if (diskDriveSerial != null && cpuProcessorId != null) {
            if (diskDriveSerial.length() > 6 && cpuProcessorId.length() > 6) {
                regKey = diskDriveSerial.substring(diskDriveSerial.length() - 6, diskDriveSerial.length())
                        + cpuProcessorId.substring(cpuProcessorId.length() - 6, cpuProcessorId.length());
            } else if (diskDriveSerial.length() < 6) {
                regKey = cpuProcessorId.substring(cpuProcessorId.length() - 12, cpuProcessorId.length());
            } else if (cpuProcessorId.length() < 6) {
                regKey = diskDriveSerial.substring(diskDriveSerial.length() - 12, diskDriveSerial.length());

            }
        }
//        String regKey = diskDriveSerial;
//        System.out.println("regKey: "+regKey);

//        //System.out.println("----");
        //System.out.println("regKey: " + regKey);
        return regKey;
    }

    public static final String getDeviceData(String[] arg, String key) {
        String sn = null;
        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
//			process = runtime.exec(new String[] { "wmic", "bios", "get", "serialnumber" });
//            			process = runtime.exec(new String[] { "wmic", "baseboard", "get", "serialnumber" });

            process = runtime.exec(arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNext()) {
                String next = sc.next();
                //System.out.println("next:" + next);
                if (key.equals(next)) {
                    sn = sc.next().trim();
                    //System.out.println("sn: " + sn);
//                    break;
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            sn = "";
//            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    public static String encrypt(String text, final String key) {
        String res = "";
        String alphaKey = key.replaceAll("[^A-Z]+", "");
        String numericKey = key.replaceAll("[^0-9]+", "");

        text = text.toUpperCase();
//        //System.out.println("encrypt");
        for (int i = 0, jAlpha = 0, jNum = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if ((c < 'A' || c > 'Z') && (c < '0' || c > '9')) {
                continue;
            }
            if ((c >= '0' && c <= '9')) {
//                System.out.print(key.charAt(j));
//                res += (char) ((c + key.charAt(j) - 2 * '0') % 10 + '0');
                res += (char) ((c + numericKey.charAt(jNum) - 2 * '0') % 10 + '0');
                jNum = ++jNum % numericKey.length();
            } else {
                res += (char) ((c + alphaKey.charAt(jAlpha) - 2 * 'A') % 26 + 'A');
                jAlpha = ++jAlpha % alphaKey.length();

            }
//            System.out.print(c);
        }

//        //System.out.println("End encrypt");
//        System.out.print("");
        return res;
    }

    public static String decrypt(String text, final String key) {
        String res = "";
        String alphaKey = key.replaceAll("[^A-Z]+", "");
        String numericKey = key.replaceAll("[^0-9]+", "");
        text = text.toUpperCase();
        for (int i = 0, jAlpha = 0, jNum = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((c < 'A' || c > 'Z') && (c < '0' || c > '9')) {
                continue;
            }
            if ((c >= '0' && c <= '9')) {
//                res += (char) ((c + KEY.charAt(1) - 2 * '0') % 10 + '0');
//                System.out.print(key.charAt(j));
//                res += (char) (((c - key.charAt(j) + 10) % 10 + '0')+10);
                res += (char) (((c - numericKey.charAt(jNum) + 10) % 10 + '0'));
                jNum = ++jNum % numericKey.length();
            } else {
                res += (char) ((c - alphaKey.charAt(jAlpha) + 26) % 26 + 'A');
                jAlpha = ++jAlpha % alphaKey.length();
            }
//            j = ++j % key.length();
        }
//        //System.out.println("dec");
        return res;
    }

    public static String getRandom(int len) {
        Random r = new Random();
        String s = "";
        String stringSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < len; i++) {
            s += stringSet.charAt(r.nextInt(36));

        }
        return s;
    }

    public static String orgActivationKey(String regKey, String packageCode) {
        String uID = regKey.substring(0, 12);
        String randomNo = regKey.substring(12);

        String activationKey = KeyHelper.encrypt(uID + packageCode, KEY) + KeyHelper.encrypt(randomNo, KEY);
        return activationKey;
    }
}
