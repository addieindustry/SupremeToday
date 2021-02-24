/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iconflux006
 */
public class LogHelper {
private static final String LOG_FILE = "log.txt";

    public static void add(String content) {
        try {
            content += new Date() + "\n###############";
            if (!Files.exists(Paths.get(LOG_FILE))) {
                Files.write(Paths.get(LOG_FILE), content.getBytes(), StandardOpenOption.CREATE);
            } else {
                Files.write(Paths.get(LOG_FILE), content.getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException ex) {
            Logger.getLogger(LogHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
