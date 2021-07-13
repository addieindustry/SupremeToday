package com.project.helper;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;

public class PropertyHelper {
    public static char[] key = {'j', 'a', 's', 'y', 'p', 't'};
    public static void create_property_file() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setPassword(key.toString()); // could be got from web, env variable...
        encryptor.setPasswordCharArray(key);

        Properties prop = new EncryptableProperties(encryptor);
        OutputStream output = null;

        try {

            output = new FileOutputStream(Queries.CONFIG_FILE_PATH);

//            // set the properties value
//            prop.setProperty("database", encryptor.encrypt("localhost"));
//            prop.setProperty("emailuser", encryptor.encrypt("todaysupreme@gmail.com"));
//            prop.setProperty("emailpassword", encryptor.encrypt("st#5845@256"));
//            prop.setProperty("dongleurl", encryptor.encrypt("http://supreme-today.com:8080/api/set_dongle_license"));

            // set the properties value
            prop.setProperty("database", encryptor.encrypt("localhost"));
            prop.setProperty("emailuser", encryptor.encrypt("apikey"));
            prop.setProperty("emailpassword", encryptor.encrypt("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"));
//            prop.setProperty("dongleurl", encryptor.encrypt("http://supreme-today.com:8080/api/set_dongle_license"));
//            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
//                prop.setProperty("dongleurl", encryptor.encrypt("http://supreme-today.com:8080/api/set_dongle_license"));
//            }else{
//                prop.setProperty("dongleurl", encryptor.encrypt("http://indiancaselawfinder.com:8080/api/set_dongle_license"));
//            }


            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        Properties props = new EncryptableProperties(encryptor);
        try {
            props.load(new FileInputStream(Queries.CONFIG_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Queries.EMAIL_USER_NAME_IN_CHAR = encryptor.decrypt(props.getProperty("emailuser")).toCharArray();
        Queries.EMAIL_PASSWORD_IN_CHAR = encryptor.decrypt(props.getProperty("emailpassword")).toCharArray();
//        System.out.println(String.valueOf(Queries.EMAIL_USER_NAME_IN_CHAR));
//        System.out.println(String.valueOf(Queries.EMAIL_PASSWORD_IN_CHAR));
    }

    public static void read_property_file() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(Queries.CONFIG_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPasswordCharArray(key);

        Queries.EMAIL_USER_NAME_IN_CHAR = encryptor.decrypt(props.getProperty("emailuser")).toCharArray();
        Queries.EMAIL_PASSWORD_IN_CHAR = encryptor.decrypt(props.getProperty("emailpassword")).toCharArray();
//        Queries.DONGLE_API_URL= encryptor.decrypt(props.getProperty("dongleurl")).toString();
//        System.out.println("props.getProperty(\"dongleurl\") : " + props.getProperty("dongleurl"));
//        System.out.println("encryptor.decrypt(props.getProperty(\"dongleurl\")) : " + encryptor.decrypt(props.getProperty("dongleurl")));
//        System.out.println("encryptor.decrypt(props.getProperty(\"dongleurl\")).toString() : " + encryptor.decrypt(props.getProperty("dongleurl")).toString());
//        System.out.println("Queries.DONGLE_API_URL : " + Queries.DONGLE_API_URL);
//        System.out.println(String.valueOf(Queries.EMAIL_USER_NAME_IN_CHAR));
//        System.out.println(String.valueOf(Queries.EMAIL_PASSWORD_IN_CHAR));
//        System.out.println(String.valueOf(Queries.DONGLE_API_URL));
    }

}
