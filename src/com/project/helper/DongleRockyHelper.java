/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.rockey2.JRockey2;

/**
 *
 * @author addie
 */ 
public class DongleRockyHelper {

    public boolean isDongleConnected() {

        JRockey2 rockey = new JRockey2();
        int iRetcode;
        iRetcode = rockey.RY2_Find();
        return iRetcode > 0;
    }

    public String getHardwareId(String uid) {
        int[] iUid = new int[1];
        int[] iHid = new int[1];
        JRockey2 rockey = new JRockey2();
        int iRetcode;
        int iHandle;
        iRetcode = rockey.RY2_Find();
        if (iRetcode < 0) {
//            System.out.println("ERROR: " + Integer.toHexString(iRetcode));
            return "";
        }
        if (iRetcode == 0) {
//            System.out.println("ERROR: Not found any rockey2");
            return "";
        }
        try {
            iUid[0] = Integer.parseInt(uid);//1605550357;
        } catch (Exception e) {
//            System.out.println("Read uid error");
            return "";
        }
        iRetcode = rockey.RY2_Open(1, iUid[0], iHid);
        if (iRetcode < 0) {
//            System.out.println("ERROR: " + Integer.toHexString(iRetcode));
            return "";
        }
        iHandle = iRetcode;
//        System.out.println("Hardware Id: " + iHid);
        String hardwareId = "";
        for (int i : iHid) {
            hardwareId += Integer.toHexString(i);
//            System.out.println("Hardware Id: " + Integer.toHexString(i));
        }
        rockey.RY2_Close(iHandle);
        return hardwareId;
    }
}
