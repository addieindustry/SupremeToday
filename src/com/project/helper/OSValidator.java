/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

/**
 *
 * @author addie
 */
public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean test() {
        try {
//            //System.out.println("try"+ 1/0);
return true;
        } catch (Exception e) {
            //System.out.println("Catch");
        } finally {
            //System.out.println("Finally");
        }
        return false;
    }

    

    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

    }

    public static boolean isSolaris() {

        return (OS.indexOf("sunos") >= 0);

    }
}
