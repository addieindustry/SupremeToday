/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;

import com.project.helper.DongleRockyHelper;
import java.util.Date;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 *
 * @author addie
 */
public class ScheduledTask extends TimerTask {

    Date now; // to display current time

    // Add your task here
    public void run() {
        now = new Date(); // initialize date
        if (!new DongleRockyHelper().isDongleConnected()) {
//             JOptionPane.showMessageDialog(null, "Dongle is Disconnected!");
            System.exit(0);
        }
//        System.out.println("Time is :" + now); // Display current time
//            JOptionPane.showMessageDialog(null, "PDF count exceeded!");
    }
}
