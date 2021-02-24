package com.project;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.license4j.util.FileUtils;
import com.project.utility.EncryptionHelper;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jsoup.Jsoup;
import com.itextpdf.html2pdf.HtmlConverter;

public class Test {

    private static String toXHTML(String html) {
        final org.jsoup.nodes.Document document = Jsoup.parse(html);
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        return document.html();
    }

    // 1674
    public static void savePdf() throws FileNotFoundException // crates pdf fuction
    {
        String html = "";
        try {
            html = new String(Files.readAllBytes(Paths.get("D:\\For Print Testing\\37594.htm")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HtmlConverter.convertToPdf(html, new FileOutputStream("D:\\For Print Testing\\37594.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Document document = new Document();
//            PdfWriter writer =  PdfWriter.getInstance(document, new FileOutputStream("D:\\For Print Testing\\64467.pdf"));
//
//            // PdfWriter.getInstance(document, new
//            // FileOutputStream(Environment.getExternalStorageDirectory().toString()
//            // + "/" + "judgement.pdf"));
//            document.open();
//            // addMetaData(document);
//
//            // Changes for html to pdf conversation on 13-02-2019
//            try {
//
////                html = html.replace(script, "");
//                String xhtml = toXHTML(html);
//                xhtml = xhtml.replace("<hr>", "<p></p>");
//                xhtml = xhtml.replace("<hr/>", "<p></p>");
//                xhtml = xhtml.replace("<hr />", "<p></p>");
//
//                InputStream is = new ByteArrayInputStream(xhtml.getBytes());
//
//                XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void encrypt1() throws FileNotFoundException // crates pdf fuction
    {
        String html = "";
        try {
            html = new String(Files.readAllBytes(Paths.get("E:\\test.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String seed = "ipNumber";
//        String myIpValue = "192.168.0.1";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(seed);
        String encrypted= encryptor.encrypt(html);
        System.out.println(encrypted);

        try (PrintStream out = new PrintStream(new FileOutputStream("E:\\test_ENC.txt"))) {
            out.print(encrypted);
        }



//        try {
//            HtmlConverter.convertToPdf(html, new FileOutputStream("D:\\For Print Testing\\37594.pdf"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static void encrypt2() throws FileNotFoundException // crates pdf fuction
    {
        String html = "";
        try {
            html = new String(Files.readAllBytes(Paths.get("E:\\test.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str,Newstr=" ";
//        System.out.print("Enter the String you want to Encrypt: ");
        try {
            Scanner in=new Scanner(html);
            str=in.nextLine();
            for (int i=0;i<str.length();i++)
            {
                char ch=Character.toLowerCase(str.charAt(i));
                switch (ch)
                {
                    case 'a':
                        Newstr=Newstr+"{";
                        break;
                    case 'b':
                        Newstr=Newstr+"}";
                        break;
                    case 'c':
                        Newstr=Newstr+"#";
                        break;
                    case 'd':
                        Newstr=Newstr+"~";
                        break;
                    case 'e':
                        Newstr=Newstr+"+";
                        break;
                    case 'f':
                        Newstr=Newstr+"-";
                        break;
                    case 'g':
                        Newstr=Newstr+"*";
                        break;
                    case 'h':
                        Newstr=Newstr+"@";
                        break;
                    case 'i':
                        Newstr=Newstr+"/";
                        break;
                    case 'j':
                        Newstr=Newstr+"\\";
                        break;
                    case 'k':
                        Newstr=Newstr+"?";
                        break;
                    case 'l':
                        Newstr=Newstr+"$";
                        break;
                    case 'm':
                        Newstr=Newstr+"!";
                        break;
                    case 'n':
                        Newstr=Newstr+"^";
                        break;
                    case 'o':
                        Newstr=Newstr+"(";
                        break;
                    case 'p':
                        Newstr=Newstr+")";
                        break;
                    case 'q':
                        Newstr=Newstr+"<";
                        break;
                    case 'r':
                        Newstr=Newstr+">";
                        break;
                    case 's' :
                        Newstr=Newstr+"=";
                        break;
                    case 't':
                        Newstr=Newstr+";";
                        break;
                    case 'u':
                        Newstr=Newstr+",";
                        break;
                    case 'v' :
                        Newstr=Newstr+"_";
                        break;
                    case 'w':
                        Newstr=Newstr+"[";
                        break;
                    case 'x' :
                        Newstr=Newstr+"]";
                        break;
                    case 'y':
                        Newstr=Newstr+":";
                        break;
                    case 'z' :
                        Newstr=Newstr+"\"";
                        break;
                    case ' ' :
                        Newstr=Newstr+" ";
                        break;
                    case '.':
                        Newstr=Newstr+'3';
                        break;
                    case ',':
                        Newstr=Newstr+"1";
                        break;
                    case '(':
                        Newstr=Newstr+'4';
                        break;
                    case '\"':
                        Newstr=Newstr+'5';
                        break;
                    case ')' :
                        Newstr=Newstr+"7";
                        break;
                    case '?' :
                        Newstr= Newstr+"2";
                        break;
                    case '!':
                        Newstr= Newstr+"8";
                        break;
                    case '-' :
                        Newstr= Newstr+"6";
                        break;
                    case '%' :
                        Newstr = Newstr+"9";
                        break;
                    case '1':
                        Newstr=Newstr+"r";
                        break;
                    case '2':
                        Newstr=Newstr+"k";
                        break;
                    case '3':
                        Newstr=Newstr+"b";
                        break;
                    case '4':
                        Newstr = Newstr+"e";
                        break;
                    case '5':
                        Newstr = Newstr+"q";
                        break;
                    case '6':
                        Newstr = Newstr+"h";
                        break;
                    case '7':
                        Newstr = Newstr+"u";
                        break;
                    case '8' :
                        Newstr= Newstr+"y";
                        break;
                    case '9':
                        Newstr = Newstr+"w";
                        break;
                    case '0':
                        Newstr = Newstr+"z";
                        break;
                    default:
                        Newstr=Newstr+"0";
                        break;
                }
            }
        }
        catch(Exception ioe)
        {
            ioe.printStackTrace();
        }
        System.out.println("The encrypted string is: \n" +Newstr);
        String NewStr2 = Newstr;
//        String NewstrEnc=Newstr;
        Newstr=" ";
//        System.out.print("Enter the String you want to Decrypt: ");
        try {
//            Scanner in=new Scanner(NewStr2);
            str=NewStr2;
            for (int i=0;i<str.length();i++)
            {
                char ch=Character.toLowerCase(str.charAt(i));
                switch (ch)
                {
                    case '{':
                        Newstr=Newstr+"A";
                        break;
                    case '}':
                        Newstr=Newstr+"B";
                        break;
                    case '#':
                        Newstr=Newstr+"C";
                        break;
                    case '~':
                        Newstr=Newstr+"D";
                        break;
                    case '+':
                        Newstr=Newstr+"E";
                        break;
                    case '-':
                        Newstr=Newstr+"F";
                        break;
                    case '*':
                        Newstr=Newstr+"G";
                        break;
                    case '@':
                        Newstr=Newstr+"H";
                        break;
                    case '/':
                        Newstr=Newstr+"I";
                        break;
                    case '\\':
                        Newstr=Newstr+"J";
                        break;
                    case '?':
                        Newstr=Newstr+"K";
                        break;
                    case '$':
                        Newstr=Newstr+"L";
                        break;
                    case '!':
                        Newstr=Newstr+"M";
                        break;
                    case '^':
                        Newstr=Newstr+"N";
                        break;
                    case '(':
                        Newstr=Newstr+"O";
                        break;
                    case ')':
                        Newstr=Newstr+"P";
                        break;
                    case '<':
                        Newstr=Newstr+"Q";
                        break;
                    case '>':
                        Newstr=Newstr+"R";
                        break;
                    case '=' :
                        Newstr=Newstr+"S";
                        break;
                    case ';':
                        Newstr=Newstr+"T";
                        break;
                    case ',':
                        Newstr=Newstr+"U";
                        break;
                    case '_' :
                        Newstr=Newstr+"V";
                        break;
                    case '[':
                        Newstr=Newstr+"W";
                        break;
                    case ']' :
                        Newstr=Newstr+"X";
                        break;
                    case ':':
                        Newstr=Newstr+"Y";
                        break;
                    case '\"' :
                        Newstr=Newstr+"Z";
                        break;
                    case '1':
                        Newstr=Newstr+"r";
                        break;
                    case '2':
                        Newstr=Newstr+"k";
                        break;
                    case '3':
                        Newstr=Newstr+"b";
                        break;
                    case '4':
                        Newstr = Newstr+"e";
                        break;
                    case '5':
                        Newstr = Newstr+"q";
                        break;
                    case '6':
                        Newstr = Newstr+"h";
                        break;
                    case '7':
                        Newstr = Newstr+"u";
                        break;
                    case '8' :
                        Newstr= Newstr+"y";
                        break;
                    case '9':
                        Newstr = Newstr+"w";
                        break;
                    case '0':
                        Newstr = Newstr+"z";
                        break;
                    default:
                        Newstr=Newstr+"0";
                        break;
                }
            }
        }
        catch(Exception ioe)
        {
            ioe.printStackTrace();
        }
        System.out.println("The decrypted string is: " +Newstr);



        try (PrintStream out = new PrintStream(new FileOutputStream("E:\\test_ENC.txt"))) {
            out.print(Newstr);
        }



//        try {
//            HtmlConverter.convertToPdf(html, new FileOutputStream("D:\\For Print Testing\\37594.pdf"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static void encrypt3() throws FileNotFoundException // crates pdf fuction
    {
        String html = "";
        try {
            html = new String(Files.readAllBytes(Paths.get("E:\\test.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(html);
        String encStr = EncryptionHelper.encrypt(sb, "12345");


//        String encStr = EncryptionHelper.Decrypt()

//        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setPassword(seed);
//        String encrypted= encryptor.encrypt(html);
//        System.out.println(encrypted);

        try (PrintStream out = new PrintStream(new FileOutputStream("E:\\test_ENC.txt"))) {
            out.print(encStr);
        }



//        try {
//            HtmlConverter.convertToPdf(html, new FileOutputStream("D:\\For Print Testing\\37594.pdf"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


    public static void main(String[] args) throws Exception {
//        savePdf();
//        encrypt1();
//        encrypt2();
        encrypt3();


//        // IO
//        File htmlSource = new File("input.html");
//        File pdfDest = new File("output.pdf");
//        // pdfHTML specific code
//        ConverterProperties converterProperties = new ConverterProperties();
//        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);









//        String _filename = "64250";
//        String _output_folder = "D:\\Addie\\SupremeToday\\Sample\\";
//
//        Queries.SESSION = new Date().getTime();
////        byte[] encryptionKey = "MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8);
//        String temp = readFile(_output_folder + _filename + ".htm", StandardCharsets.UTF_8);
//
//        byte[] encText = EncryptionHelper.encrypt(new StringBuilder(temp), "879865464").getBytes(StandardCharsets.UTF_8);
//
//        FileOutputStream out = new FileOutputStream(_output_folder + _filename + "_ENC.htm");
//        out.write(encText);
//        out.close();
//
//        String _enc = new String(encText);
//        System.out.println(String.valueOf(_enc).contains("--SupremeToday Splitter--"));
//
//        byte[] plainText = EncryptionHelper.Decrypt(new StringBuilder(_enc), "879865464").getBytes(StandardCharsets.UTF_8);
//
//        out = new FileOutputStream(_output_folder + _filename + "_DEC.htm");
//        out.write(plainText);
//        out.close();








//        byte[] encryptionKey = "MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8);
//        String temp = readFile("F:\\ST\\Projects\\JavaFX\\Data\\HTML File\\61395.zip", StandardCharsets.UTF_8);
//        byte[] plainText = temp.getBytes(StandardCharsets.UTF_8);
//        AdvancedEncryptionStandard advancedEncryptionStandard = new AdvancedEncryptionStandard(
//                encryptionKey);
//        byte[] cipherText = advancedEncryptionStandard.encrypt(plainText);
//
//        FileOutputStream out = new FileOutputStream("F:\\ST\\Projects\\JavaFX\\Data\\HTML File\\61395_ENC.zip");
//        out.write(cipherText);
//        out.close();
//
//        byte[] decryptedCipherText = advancedEncryptionStandard.decrypt(cipherText);
//
//        System.out.println(new String(plainText));
//        System.out.println(new String(cipherText));
//        System.out.println(new String(decryptedCipherText));

//        Queries.SESSION = new Date().getTime();
//        byte[] encryptionKey = "MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8);
//        byte[] plainText = "Hello world!".getBytes(StandardCharsets.UTF_8);
//        AdvancedEncryptionStandard advancedEncryptionStandard = new AdvancedEncryptionStandard(
//                encryptionKey);
//        byte[] cipherText = advancedEncryptionStandard.encrypt(plainText);
//        byte[] decryptedCipherText = advancedEncryptionStandard.decrypt(cipherText);
//
//        System.out.println(new String(plainText));
//        System.out.println(new String(cipherText));
//        System.out.println(new String(decryptedCipherText));
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
