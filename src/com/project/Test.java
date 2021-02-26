package com.project;

import com.project.utility.EncryptionHelper;
import com.project.utility.EncryptionHelperLatest;

//import java.io.File;
//import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Test {

    public static void main (String[] arg) {
        String KEY = "1234567890";
        String INPUT_TEXT = "HELLO addie, How are you Addie";

        try {
            INPUT_TEXT = new String(Files.readAllBytes(Paths.get("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp.html")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        INPUT_TEXT = INPUT_TEXT.replaceAll("<", " <").replaceAll(">", "> ");

//        List<String> supreme_dictionary = new ArrayList<String>();
//        supreme_dictionary.add("");

        String ENCRYPTED_TEXT = EncryptionHelperLatest.encrypt(new StringBuilder(INPUT_TEXT), KEY);
//        System.out.println("ENCRYPTED_TEXT");
//        System.out.println(ENCRYPTED_TEXT);

//        WriteObjectToFile(supreme_dictionary);
//
//        supreme_dictionary = ReadObjectToFile();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp_out.html"), "utf-8"))) {
            writer.write(ENCRYPTED_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String DECRYPTED_TEXT = EncryptionHelperLatest.decrypt(new StringBuilder(ENCRYPTED_TEXT), KEY);
//        System.out.println("DECRYPTED_TEXT");
//        System.out.println(DECRYPTED_TEXT);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp_dec.html"), "utf-8"))) {
            writer.write(DECRYPTED_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}