package com.project;

import java.io.*;
import java.net.*;

public class GetMyIP {
    public static String Get() {
        URL url = null;
        BufferedReader in = null;
        String ipAddress = "";
        try {
            url = new URL("http://bot.whatismyipaddress.com");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            ipAddress = in.readLine().trim();
            /* IF not connected to internet, then
             * the above code will return one empty
             * String, we can check it's length and
             * if length is not greater than zero,
             * then we can go for LAN IP or Local IP
             * or PRIVATE IP
             */
            if (!(ipAddress.length() > 0)) {
                try {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    ipAddress = (ip.getHostAddress()).trim();
                } catch(Exception exp) {
                    ipAddress = "ERROR";
                }
            }
        } catch (Exception ex) {
            // This try will give the Private IP of the Host.
            try {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                ipAddress = (ip.getHostAddress()).trim();
            } catch(Exception exp) {
                ipAddress = "ERROR";
            }
            //ex.printStackTrace();
        }
        return ipAddress;
    }
}