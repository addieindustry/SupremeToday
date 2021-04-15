package com.project;

import com.license4j.util.FileUtils;
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

    public static byte[] integersToBytes(int[] values)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for(int i=0; i < values.length; ++i)
        {
            try {
                dos.writeInt(values[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return baos.toByteArray();
    }

    public static void main (String[] arg) {
//        EncryptionHelperLatest e = new EncryptionHelperLatest();
//        e.EncryptDictionary();

        String KEY = "1234567890";
        String INPUT_TEXT = "HELLO addie, How are you Addie";

        try {
            INPUT_TEXT = new String(Files.readAllBytes(Paths.get("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp.html")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        INPUT_TEXT = INPUT_TEXT.replaceAll("<", " <").replaceAll(">", "> ");
        INPUT_TEXT = INPUT_TEXT.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ").replaceAll("href=", "href= ");

//        List<String> supreme_dictionary = new ArrayList<String>();
//        supreme_dictionary.add("");

        String ENCRYPTED_TEXT = EncryptionHelperLatest.encrypt(new StringBuilder(INPUT_TEXT), KEY);

//        ArrayList<Integer> ENCRYPTED_ARRAY = EncryptionHelperLatest.encrypt_binary(new StringBuilder(INPUT_TEXT), KEY);
//        System.out.println("ENCRYPTED_TEXT");
//        System.out.println(ENCRYPTED_TEXT);
//        WriteObjectToFile(supreme_dictionary);
//        supreme_dictionary = ReadObjectToFile();

//        /**/byte[] BYTES_ARR = integersToBytes(ENCRYPTED_ARRAY.stream().mapToInt(i->i).toArray());
//        try (FileOutputStream fos = new FileOutputStream("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp_bite.html")) {
//            fos.write(BYTES_ARR);
//            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        OutputStream os = null;
//        try {
//            os = new FileOutputStream("D:\\Projects\\SupremeToday\\SupremeToday\\res\\temp_bite.html");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // Starts writing the bytes in it
//        try {
//            os.write(BYTES_ARR);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Successfully byte inserted");
//        try {
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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