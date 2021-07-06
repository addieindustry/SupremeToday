/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;
import com.project.controllers.DialogLiveUpdateController;
import com.project.controllers.TabViewController;
import com.project.helper.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Duration;
//import org.controlsfx.control.action.Action;
//import org.controlsfx.dialog.Dialogs;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.project.helper.ServiceHelper.getUserDetails;

/**
 *
 * @author addie
 */
public class Main extends Application {

    final String favicon = "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";

//    final com.project.runjetty.server.JettyServer jettyServer = new com.project.runjetty.server.JettyServer();

    private Pane splashLayout;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 518;
    private static final int SPLASH_HEIGHT = 311;

 //    private boolean isAutoUpdateApplication = true;
    private static boolean isAutoUpdateApplication = false;

    public static void main(String[] args) throws Exception {
        Queries.SESSION = new Date().getTime();
        launch(args);
    }

    @Override
    public void init() {
        try {
//            sun.util.logging.PlatformLogger platformLogger = PlatformLogger.getLogger("java.util.prefs");
//            platformLogger.setLevel(PlatformLogger.Level.OFF);

            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

            BASE64Decoder decoder = new BASE64Decoder();
            InputStream is = new FileInputStream(Queries.splashFile);
            ImageView splash = new ImageView(new Image(is));
            splashLayout = new VBox();
            splashLayout.getChildren().addAll(splash);
            if (isAutoUpdateApplication){
//                startAutoUpdateScreen();
                runSingleInstantApplication();
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void licenseFormStart(Stage initStage) {
        try {
            initStage.setTitle("");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(favicon);

            initStage.getIcons().add(new Image(new ByteArrayInputStream(decodedBytes)));

            Scene scene = new Scene(LicenseForm.getForm(initStage), 400, 475);
            initStage.setScene(scene);
            initStage.show();
        } catch (IOException ex) {
            //System.out.println(ex.toString());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cdSelection(Stage primaryStage) {
        try {
            primaryStage.setTitle("");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(favicon);

            primaryStage.getIcons().add(new Image(new ByteArrayInputStream(decodedBytes)));

            Scene scene = new Scene(CDSelection.getForm(primaryStage), 400, 475);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            //System.out.println(ex.toString());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void appStart(final Stage initStage) {
        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> data
                        = FXCollections.observableArrayList();
                Thread.sleep(1000);
                return data;
            }
        };

        if (isAutoUpdateApplication){
            startAutoUpdateScreen();
        }else{
            showSplash(
                    initStage,
                    friendTask,
                    () -> startMainScreen()
            );
            new Thread(friendTask).start();
        }
    }

    @Override
    public void start(final Stage initStage) throws Exception {
            if (new File(Queries.DB_PATH).exists()) {
            /* Check Master Index Folder is Exist or Not */
                if (new File(Queries.INDEX_PATH).exists()) {
                    Queries.PRINT_COUNT = CommanHelper.getCurrentValue(Queries.INDEX_PATH + "/_segment001", "P");
                    Queries.PDF_COUNT = CommanHelper.getCurrentValue(Queries.INDEX_PATH + "/_segment001", "D");
                    PropertyHelper.read_property_file();
                    //System.out.println("PRINT_COUNT::" + Queries.PRINT_COUNT);
//                launch(args);
                    if (LicenseHelper.isLicenseAvail()) {
                        int ret= LicenseHelper.isLicenseValid();
//                    int ret=1;
//                    if (!isAutoUpdateApplication){
//                        ret= LicenseHelper.isLicenseValid();
//                    }
                        if (ret == 1) {
                            //System.out.println("App Start");

                            if (new File(Queries.CONFIG_FILE_PATH).exists()) {
                                appStart(initStage);
                            }else{
                                PropertyHelper.create_property_file();
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error Dialog");
                                alert.setHeaderText(Queries.APPLICATION_NAME);
                                alert.setContentText("configuration file not exits!");
                                alert.showAndWait();

//                                Dialogs.create().owner(initStage).title("Error Dialog").masthead("configuration file not exits!").message(null).showError();
                            }
                        } else if (ret == 2) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Dialog");
                            alert.setHeaderText(Queries.APPLICATION_NAME);
                            alert.setContentText("Dongle is not connected please connect your dongle!");
                            alert.showAndWait();
//                            Dialogs.create().owner(initStage).title("Error Dialog").masthead("Dongle is not connected please connect your dongle!").message(null).showError();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Dialog");
                            alert.setHeaderText(Queries.APPLICATION_NAME);
                            alert.setContentText("Invalid License Key!");
                            alert.showAndWait();
//                            Dialogs.create().owner(initStage).title("Error Dialog").masthead("Invalid License Key!").message(null).showError();
                            licenseFormStart(initStage);
                        }
//                    if (LicenseHelper.isLicenseValid()) {
//                        //System.out.println("App Start");
//                        ServiceHelper.getPrintSetting();
//                        appStart(initStage);
//                    } else {
//                        Dialogs.create().owner(initStage).title("Error Dialog").masthead("Invalid License Key!").message(null).showError();
//                        licenseFormStart(initStage);
//                    }

                    } else {
                        try {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText(Queries.APPLICATION_NAME);
                            alert.setContentText("Not Activated!!!");
                            alert.showAndWait();
//                            Dialogs.create().owner(initStage).title("Information Dialog").masthead("Not Activated!!!").message(null).showInformation();
                            licenseFormStart(initStage);
//                        Dialogs.create().owner(initStage).title("Information Dialog").masthead("Not Activated!!!").message(null).showInformation();
//                        licenseFormStart(initStage);
//                //System.out.println("NOt 3");
                        } catch (Exception e) {
                            //System.out.println(e.toString());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Index Not Exist!");
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Master Data Not Exist!");
                cdSelection(initStage);
            }
    }

    private String getExpireInfo() {
        HashMap<String, String> userDetails = (HashMap<String, String>) getUserDetails();
        String url = Queries.LICENSE_EXPIRE_DATE_API + userDetails.get("sub_id");
        String license_info = "";

        try {
            URL myurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            StringBuilder content_sb;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content_sb = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content_sb.append(line);
                    content_sb.append(System.lineSeparator());
                }
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(content_sb.toString());
            if (Integer.parseInt(json.get("code").toString()) == 200){
                JSONObject json_data = (JSONObject) parser.parse(json.get("data").toString());

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                Date myDate = format.parse(json_data.get("date").toString());

                license_info = "License to " + json_data.get("username").toString() + ", Number : " + json_data.get("clientNumber").toString() + ", Live Facility Till : " + (new SimpleDateFormat("dd MMMM yyyy").format(myDate)).toString();
            }else if (Integer.parseInt(json.get("code").toString()) == 202){
                license_info = "License Information Not Exits, Please Contact to the Software Vendor!";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        } finally {
//            con.disconnect();
        }
        return license_info;
    }

    private void startMainScreen() {
        try {
            mainStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/ui/tab_pan_new.fxml"));
            TabViewController controller = new TabViewController();
//            controller.setPrimaryStage(mainStage);
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            String _expireInfo = getExpireInfo();
            if (_expireInfo.length() > 0){
                mainStage.setTitle(Queries.APPLICATION_NAME + " " +  Queries.APPLICATION_VERSION + " - " +  getExpireInfo());
            }else{
                mainStage.setTitle(Queries.APPLICATION_NAME + " " +  Queries.APPLICATION_VERSION);
            }
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/logo.png")));
            mainStage.setScene(new Scene(root));
            mainStage.setOnCloseRequest(confirmCloseEventHandler);
            //BELOW LINE IS FOR MAC OS X SOLUTION
            if (OSValidator.isMac()){
//                mainStage.setAlwaysOnTop(true);
            }else{
                mainStage.setMaximized(true);
            }
            mainStage.show();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (OSValidator.isWindows())
                    {
                        try {
                            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                                WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\", "ICLFAutoUpdate",  Queries.AUTO_UPDATE_EXE_FILE.replaceAll("SupremeTodayAutoUpdate.exe", "ICLFAutoUpdate.exe"));
                            }else{
                                WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\", "SupremeTodayAutoUpdate",  Queries.AUTO_UPDATE_EXE_FILE);
                            }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    }
//                    mainStage.setMaximized(true);
                }
            });
        } catch (Exception e) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, e);
//            new Utils().showErrorDialog(e);
        }
    }

    private TrayIcon trayIcon;
    private static final int SINGLE_INSTANCE_LISTENER_PORT = 12345;
    private static final String SINGLE_INSTANCE_FOCUS_MESSAGE = "focus";
    private static final String instanceId = UUID.randomUUID().toString();

    private static final int FOCUS_REQUEST_PAUSE_MILLIS = 500;

    public void runSingleInstantApplication() {
        CountDownLatch instanceCheckLatch = new CountDownLatch(1);

        Thread instanceListener = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(SINGLE_INSTANCE_LISTENER_PORT, 10)) {
                instanceCheckLatch.countDown();

                while (true) {
                    try (
                            Socket clientSocket = serverSocket.accept();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        String input = in.readLine();
                        System.out.println("Received single instance listener message: " + input);
                        if (input.startsWith(SINGLE_INSTANCE_FOCUS_MESSAGE) && mainStage != null) {
                            Thread.sleep(FOCUS_REQUEST_PAUSE_MILLIS);
                            Platform.runLater(() -> {
                                System.out.println("To front " + instanceId);
                                mainStage.setIconified(false);
                                mainStage.show();
                                mainStage.toFront();
                            });
                        }
                    } catch (IOException e) {
                        System.out.println("Single instance listener unable to process focus message from client");
                        e.printStackTrace();
                    }
                }
            } catch(java.net.BindException b) {
                System.out.println("SingleInstanceApp already running");

                try (
                        Socket clientSocket = new Socket(InetAddress.getLocalHost(), SINGLE_INSTANCE_LISTENER_PORT);
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
                ) {
                    System.out.println("Requesting existing app to focus");
                    out.println(SINGLE_INSTANCE_FOCUS_MESSAGE + " requested by " + instanceId);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Aborting execution for instance " + instanceId);
                Platform.exit();
            } catch(Exception e) {
                System.out.println(e.toString());
            } finally {
                instanceCheckLatch.countDown();
            }
        }, "instance-listener");
        instanceListener.setDaemon(true);
        instanceListener.start();

        try {
            instanceCheckLatch.await();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    private void startAutoUpdateScreen() {
/*        try {
            Thread.sleep(3 *   // minutes to sleep
                    60 *   // seconds to a minute
                    1000); // milliseconds to a second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        try {
            Platform.setImplicitExit(false);
            mainStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_live_update.fxml"));
            DialogLiveUpdateController controller = new DialogLiveUpdateController();
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            mainStage.setTitle(Queries.APPLICATION_NAME + " - Live Update");
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/logo.png")));
            mainStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            System.out.println("Scene scene = new Scene(root);");
            mainStage.setScene(scene);
            createTrayIcon(mainStage);
            mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    autoHide(mainStage);
                }
            });
            mainStage.setResizable(false);
            // Set the person into the controller
            controller.setDialogStage(mainStage);

               /*Close window on Escap key press*/
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent evt) {
                    if (evt.getCode().equals(KeyCode.ESCAPE)) {
                        autoHide(mainStage);
                    }
                }
            });
//            mainStage.show();
//            autoHide(mainStage);
        } catch (Exception e) {
            System.out.println(e);
//            new Utils().showErrorDialog(e);
        }
    }

    public void createTrayIcon(final Stage stage) {
        System.out.println("hello world createTrayIcon");
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                URL url = new File(Queries.LOGO_PATH).toURI().toURL();
                image = ImageIO.read(url);
            } catch (IOException ex) {
                Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
//                System.out.println(ex);
            }

            // create a action listener to listen for default action executed on the tray icon
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };

            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Open");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Exit");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Title", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        }
    }
    public void showProgramIsMinimizedMsg() {
        trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
                "Double Click to Maximize",
                TrayIcon.MessageType.INFO);
    }

    private void autoHide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            }
        });
    }


    private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Do you want to close the application?", yesButton, noButton);
        alert.setTitle("Confirm Dialog");
        alert.setHeaderText(Queries.APPLICATION_NAME);
//        alert.setContentText("I have a great message for you!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs.getText() == "Yes") {
                System.exit(0);
                Object obj = new Object();
                WeakReference ref = new WeakReference<Object>(obj);
                obj = null;
                while (ref.get() != null) {
                    System.out.println(ref.toString());
                    System.gc();
                }
            }else{
                event.consume();
            }
        });

//        Action response = Dialogs.create()
//                .owner(mainStage)
//                .title("Confirm Dialog")
//                .masthead(null)
//                .message("Do you want to close the application?")
//                .showConfirm();
//        //System.out.println(response.toString());
//
//        if (response.toString().equals("DialogAction.YES")) {
//                try {
//                    System.exit(0);
//                    Object obj = new Object();
//                    WeakReference ref = new WeakReference<Object>(obj);
//                    obj = null;
//                    while (ref.get() != null) {
//                        System.out.println(ref.toString());
//                        System.gc();
//                    }
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//        } else {
//            event.consume();
//        }

//        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, Queries.MESSAGE_CONFIRM_TO_EXIT);
//        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
//        exitButton.setText("Exit");
//        closeConfirmation.setHeaderText(Queries.TITLE_CONFIRM_TO_EXIT_WINDOW);
//        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
//        closeConfirmation.initOwner(mainStage);
//
//        // normally, you would just use the default alert positioning,
//        // but for this simple sample the main stage is small,
//        // so explicitly position the alert so that the main window can still be seen.
//        closeConfirmation.setX(mainStage.getX());
////        closeConfirmation.setY(primaryStage.getY() + primaryStage.getHeight());
//
//        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
//        if (!ButtonType.OK.equals(closeResponse.get())) {
//            event.consume();
//        }
//        else
//        {
//            System.exit(0);
//        }
    };

//    ContextMenu menu = null;

//    private void showMainStage(
//            ReadOnlyObjectProperty<ObservableList<String>> friends
//    ) {
//        try {
//            BASE64Decoder decoder = new BASE64Decoder();
//            byte[] decodedBytes = decoder.decodeBuffer(favicon);
//            mainStage = new Stage(StageStyle.DECORATED);
//            mainStage.setTitle("Supreme Today");
////            mainStage.getIcons().add(new Image(APPLICATION_ICON));
////            mainStage.getIcons().add(new Image(new ByteArrayInputStream(decodedBytes)));
//            mainStage.setMaximized(true);
//
//            final javafx.scene.web.WebView root = new javafx.scene.web.WebView();
//
////            root.getEngine().load("http://192.168.1.4:8989");
////            root.getEngine().load("http://google.com");
////            root.getEngine().load("http://localhost:8989/web?t=" + Queries.SESSION);
//            root.getEngine().load("http://localhost:8989/web?t=" + Queries.SESSION);
//
//            root.setZoom(javafx.stage.Screen.getPrimary().getDpi() / 96);
//
////        mainStage.setTitle("Supreme Today");
////            ContextMenu menu;
//            JSObject win = (JSObject) root.getEngine().executeScript("window");
//
//            win.setMember("app", new JavaHelper());
//
////            MenuItem menuItem = new MenuItem("Open");
////menuItem.setOnAction(new EventHandler<ActionEvent>() {
////    @Override public void handle(ActionEvent e) {
////        //System.out.println("Opening Database Connection...");
////    }
////});
//            root.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//                @Override
//                public void handle(MouseEvent mouse) {
//                    if (mouse.getButton() == MouseButton.SECONDARY) {
//                        String menuList = root.getEngine().executeScript("getMenu()").toString();
//                        //System.out.println(menuList);
//                        List<HashMap<String, String>> mapList = new Gson().fromJson(menuList, new TypeToken<List<HashMap<String, String>>>() {
//                        }.getType());
//                        menu = new ContextMenu();
//                        for (HashMap<String, String> map : mapList) {
//
//                            //System.out.println(map.get("menu_name"));
//                            //System.out.println(map.get("action"));
//                            MenuItem printItem = new MenuItem(map.get("menu_name").toString());
//                            printItem.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent e) {
////                                    JavaHelper.
//                                    //System.out.println(map.get("action").toString());
////                                    //System.out.println(root.getEngine().executeScript("PrintData('" + menuList + "')"));
//                                    if (map.get("menu_name").toString().equals("Print")) {
//                                        new JavaHelper().print(map.get("action").toString());
//                                    } else if (map.get("menu_name").toString().equals("Copy")) {
//                                        String myString = map.get("action").toString();
//                                        if (myString.length() > Queries.COPY_LIMIT) {
//                                            JOptionPane.showMessageDialog(null, "copy limit " + Queries.COPY_LIMIT + " character");
//                                        }
//
//                                        myString = myString.substring(0, myString.length() < Queries.COPY_LIMIT ? myString.length() : Queries.COPY_LIMIT);
//                                        StringSelection stringSelection = new StringSelection(myString);
//                                        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
//                                        clpbrd.setContents(stringSelection, null);
//                                    } else {
//                                        root.getEngine().executeScript(map.get("action").toString());
//                                    }
//                                    // root.getEngine().executeScript("sap.m.MessageToast.show(\"Please Select Act first\");");
//                                }
//                            });
//                            menu.getItems().add(printItem);
//                        }
//                        menu.show(mainStage, mouse.getScreenX(), mouse.getScreenY());
////                        if (!menuList.isEmpty()) {
//
////                        menu = new ContextMenu();
//////                        if (!menuList.isEmpty()) {
////                            MenuItem printItem = new MenuItem("Print");
////                            printItem.setOnAction(new EventHandler<ActionEvent>() {
////                                @Override
////                                public void handle(ActionEvent e) {
//////                                    JavaHelper.
//////                                    //System.out.println(root.getEngine().executeScript("PrintData('" + menuList + "')"));
////                                    //System.out.println(root.getEngine().executeScript(map.get("action").toString()));
////                                }
////                            });
////                                                        menu.getItems().addAll(printItem);
////                            MenuItem searchItem = new MenuItem("Search");
////                            searchItem.setOnAction(new EventHandler<ActionEvent>() {
////                                @Override
////                                public void handle(ActionEvent e) {
////                                    //System.out.println(root.getEngine().executeScript("globalSearch('" + menuList + "')"));
////                                }
////                            });
////                            menu.getItems().addAll(printItem, searchItem);
////                        }
////                        MenuItem topItem = new MenuItem("Top");
////                        topItem.setOnAction(new EventHandler<ActionEvent>() {
////                            @Override
////                            public void handle(ActionEvent e) {
//////                                    JavaHelper.
////                                //System.out.println("Top call");
////                                    //System.out.println(root.getEngine().executeScript("goToTop()"));
////                            }
////                        });
////                        menu.getItems().add(topItem);
////
////                        //add some menu items here
////                        menu.show(mainStage);
////                        menu.show(mainStage, mouse.getScreenX(), mouse.getScreenY());
////                    } else {
////                        if (menu != null) {
////                            menu.hide();
////                        }
//                    }
////
//                }
//            });
//
//            root.setContextMenuEnabled(false);
////            root.setContextMenu();
////            root.setco
//
//            mainStage.setScene(new javafx.scene.Scene(root, 1000, 520));
//
//            mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                public void handle(WindowEvent we) {
//                    //System.out.println("Stage is closing");
////                    int selectOption = JOptionPane.showConfirmDialog(null, "Do you want to close the application?", "Supreme Today", JOptionPane.YES_NO_OPTION);
////                    if (selectOption == JOptionPane.YES_OPTION) {
////                        if (jettyServer.isStarted()) {
////                            try {
////                                jettyServer.stop();
////                            } catch (Exception exception) {
////                                exception.printStackTrace();
////                            }
////                        }
////                    } else {
////                        we.consume();
////                    }
//
//                    Action response = Dialogs.create()
//                            .owner(mainStage)
//                            .title("Confirm Dialog")
//                            .masthead(null)
//                            .message("Do you want to close the application?")
//                            .showConfirm();
//                    //System.out.println(response.toString());
//
//                    if (response.toString().equals("DialogAction.YES")) {
////                        if (jettyServer.isStarted()) {
////                            try {
////                                jettyServer.stop();
////                                System.exit(0);
////                                Object obj = new Object();
////                                WeakReference ref = new WeakReference<Object>(obj);
////                                obj = null;
////                                while (ref.get() != null) {
////                                    System.out.println(ref.toString());
////                                    System.gc();
////                                }
////                            } catch (Exception exception) {
////                                exception.printStackTrace();
////                            }
////                        }
//                    } else {
//                        we.consume();
//                    }
//                }
//            });
//            mainStage.show();
//
//        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
//        progressText.textProperty().bind(task.messageProperty());
//        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
//                loadProgress.progressProperty().unbind();
//                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
//        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
//        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();
    }

    public interface InitCompletionHandler {
        void complete();
    }
}
