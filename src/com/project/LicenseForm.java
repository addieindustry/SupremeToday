/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.helper.DongleRockyHelper;
import com.project.helper.HttpClientHelper;
import com.project.helper.KeyHelper;

import com.project.helper.Queries;
import com.project.helper.SqliteHelper;
import com.project.utility.DocMaster;
import com.project.utility.IndexCreator;

import java.net.StandardSocketOptions;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author addie
 */
public class LicenseForm {

    static String packageCode = "";
    static String packageName = "";
    static String uId = "";

    public static GridPane getForm(Stage primaryStage) {
        SqliteHelper sqliteHelper = null;
        try {
//            System.out.println("licenseFormStart 5");
            sqliteHelper = new SqliteHelper(Queries.DB_PATH, false);
//            System.out.println("licenseFormStart 6");
            sqliteHelper.open();
//            System.out.println("licenseFormStart 7");
            ResultSet rs = sqliteHelper.select(Queries.GET_VERSION_MASTER);
//            System.out.println("licenseFormStart 8");
            while (rs.next()) {
//                System.out.println("licenseFormStart 9");
                packageCode = rs.getString("packageCode");
//                System.out.println("licenseFormStart 10");
                packageName = rs.getString("packageName");
//                System.out.println("licenseFormStart 11");
                uId = rs.getString("dId");
            }
        } catch (Exception e) {

        } finally {
            sqliteHelper.close();

        }

        try {
            //System.out.println(packageCode + " " + packageName);
//            System.out.println(packageCode + " " + packageName);
//            System.out.println(KeyHelper.getRegKey());
            String regKey = KeyHelper.getRegKey() + KeyHelper.getRandom(4);
            //        //System.out.println("REG KEY======");
            //System.out.println("REG KEY======");
            //System.out.println(regKey);
            //System.out.println(regKey.length());
//        tempActivationKey(regKey);
//        System.out.println("licenseFormStart 12");
            GridPane grid = new GridPane();
//        System.out.println("licenseFormStart 13");
            grid.setAlignment(Pos.CENTER);
//        System.out.println("licenseFormStart 14");
            grid.setHgap(10);
//        System.out.println("licenseFormStart 15");
            grid.setVgap(10);
            grid.setPadding(new Insets(15, 15, 15, 15));
//        System.out.println("licenseFormStart 16");

//        Text scenetitle = new Text("Welcome");
//        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//        grid.add(scenetitle, 0, 0, 2, 1);
            CheckBox cb1 = new CheckBox("Dongle");
            grid.add(cb1, 0, 0, 2, 1);

            ObservableList<String> pkgOptions = FXCollections.observableArrayList(
                    "New",
                    "Update",
                    "Format"
            );
            ComboBox pkgComboBox = new ComboBox(pkgOptions);
            pkgComboBox.setDisable(true);
            grid.add(pkgComboBox, 1, 0, 1, 1);
//cb1.set

            grid.add(new Label("Agent Code*:"), 0, 1);
            TextField agentCodeTextField = new TextField();
            agentCodeTextField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(agentCodeTextField, 1, 1);

            grid.add(new Label("User Name*:"), 0, 2);
            TextField userNameTextField = new TextField();
            userNameTextField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(userNameTextField, 1, 2);

            grid.add(new Label("Organization:"), 0, 3);
            TextField orgTextField = new TextField();
            orgTextField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(orgTextField, 1, 3);

            grid.add(new Label("Client Number*:"), 0, 4);
            TextField clientNumberField = new TextField();
            clientNumberField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(clientNumberField, 1, 4);

            grid.add(new Label("Client Email:"), 0, 5);
            TextField clientEmailField = new TextField();
            clientEmailField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(clientEmailField, 1, 5);

            grid.add(new Label("City*:"), 0, 6);
            TextField cityField = new TextField();
            cityField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(cityField, 1, 6);

            grid.add(new Label("Package:"), 0, 7);
            grid.add(new Label(packageName), 1, 7);

//        System.out.println("licenseFormStart 17");
            grid.add(new Label("Subscription Id*:"), 0, 8);
            TextField subField = new TextField();
            subField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(subField, 1, 8);

//        System.out.println("licenseFormStart 18");
            grid.add(new Label("Serializtion Code*:"), 0, 9);
            TextField regKeyField = new TextField(regKey);
            regKeyField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

//        regKeyField.setDisable(true);
            grid.add(regKeyField, 1, 9);

            grid.add(new Label("Activation Code*:"), 0, 10);
//        KeyHelper.orgActivationKey(regKey, packageCode)
//            TextField licKeyField = new TextField(KeyHelper.orgActivationKey(regKey, packageCode));
            TextField licKeyField = new TextField("");
            licKeyField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            grid.add(licKeyField, 1, 10);

            Button activateBtn = new Button("Activate");
            //System.out.println(KeyHelper.encrypt(KeyHelper.getRegKey(), KEY));

            cb1.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov,
                                    Boolean old_val, Boolean new_val) {
//                if(new_val){
                    regKeyField.setDisable(new_val);
                    licKeyField.setDisable(new_val);
                    pkgComboBox.setDisable(old_val);
//                }
                }
            });
//        System.out.println("licenseFormStart 19");
            activateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    boolean isDongle = cb1.selectedProperty().get();
                    boolean licenseValid = false;
//                System.out.println(cb1.selectedProperty().get());
                    String agent_code = agentCodeTextField.getText();
                    String user_name = userNameTextField.getText();
                    String organization_name = orgTextField.getText();
                    String client_number = clientNumberField.getText();
                    String client_email = clientEmailField.getText();
                    String city = cityField.getText();
                    String lic_key = licKeyField.getText();
                    String subId = subField.getText();
                    String statusDongle="";
                    if (isDongle) {statusDongle=pkgComboBox.getSelectionModel().getSelectedItem().toString();}
                    String client_package = packageCode;

                    String orgActivationKey = KeyHelper.orgActivationKey(regKey, client_package);
                    if (isDongle) {
                        if (subId.isEmpty() || client_package == null || agent_code.isEmpty()
                                || user_name.isEmpty() || client_number.isEmpty() || city.isEmpty() || pkgComboBox.getSelectionModel().getSelectedItem()== null) {
                            Dialogs.create().owner(primaryStage).title("Warning Dialog").masthead("Empty Fields not allowed!").message(null).showWarning();
                        } else {
                            try {
//                            System.out.println("uId:"+uId);
                                String dongle_id = new DongleRockyHelper().getHardwareId(uId);
                                if (dongle_id.isEmpty()) {
                                    Dialogs.create().owner(primaryStage).title("Warning Dialog").masthead("Dongle is not connected!").message(null).showWarning();
                                } else {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("sub_id", subId);
                                    params.put("package_code", client_package);
                                    params.put("agent_code", agent_code);
                                    params.put("dongle_id", dongle_id);
                                    params.put("user_name", user_name);
                                    params.put("org", organization_name);
                                    params.put("Status", statusDongle);
                                    params.put("client_number", client_number);
                                    params.put("client_email", client_email);
//                            String urlParam = "sub_id=" + URLEncoder.encode("JCZWKH") + "&";
//                                    System.out.println("Queries.DONGLE_API_URL : " + Queries.DONGLE_API_URL);
                                    String ret1 = HttpClientHelper.excutePost(Queries.DONGLE_API_URL, params);
                                    JsonObject res = (JsonObject) new JsonParser().parse(ret1);
                                    if (res.get("code").getAsInt() == 200) {
                                        licenseValid = true;
                                        lic_key = "DNG_" + dongle_id;
                                    } else {
                                        Dialogs.create().owner(primaryStage).title("Warning Dialog").masthead(res.get("data").getAsString()).message(null).showWarning();
                                    }
//                                System.out.println(ret1);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(LicenseForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        if (subId.isEmpty() || client_package == null || agent_code.isEmpty() || user_name.isEmpty() || client_number.isEmpty() || city.isEmpty() || lic_key.isEmpty()) {
                            Dialogs.create().owner(primaryStage).title("Warning Dialog").masthead("Empty Fields not allowed!").message(null).showWarning();
                        } else {
                            if (orgActivationKey.equals(lic_key)) {
                                licenseValid = true;

                            } else {
                                Dialogs.create().owner(primaryStage).title("Error Dialog").masthead("Invalid Activation Key!").message(null).showError();
//                        JOptionPane.showMessageDialog(null, "Invalid Activation Key!");
                            }

                        }
                    }
                    if (licenseValid) {
                        try {
                            activateBtn.setDisable(true);
                            SqliteHelper sqliteHelper = new SqliteHelper(Queries.LOCAL_DB_PATH, true);
                            sqliteHelper.open();

                            sqliteHelper.delete("DELETE FROM user_details");

                            String q = String.format(Queries.INSERT_USER_DETAILS,
                                    agent_code, user_name, organization_name, client_number, client_email,
                                    city, lic_key, client_package, subId);
                            try {
                                IndexCreator creator = new IndexCreator(Queries.INDEX_PATH, true);
                                creator.open(false);
                                List<DocMaster> docs = new ArrayList<DocMaster>();
                                docs.add(new DocMaster("caseId", "1", false, true, false, "DOC", false, false, false));
                                docs.add(new DocMaster("title", lic_key, false, true, false, "DOC", false, false, false));
                                docs.add(new DocMaster("DocType", "LicenseData", false, true, false, "DOC", false, false, false));
                                creator.delete("+caseId:1 +DocType:LicenseData");

                                creator.create(docs);
                                creator.close();
                            } catch (Exception ex) {
                                Logger.getLogger(LicenseForm.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                            }
                            //System.out.println(q);
                            sqliteHelper.insert(q);

                            sqliteHelper.close();



                            Dialogs.create().owner(primaryStage).title("Information Dialog").masthead("Activation Successfully done, Please Reopen the application!").message(null).showInformation();
//                            JOptionPane.showMessageDialog(null, "Activation Successfully done, Please Reopen the application!");
                            System.exit(0);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(LicenseForm.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(LicenseForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

//                if (pkgComboBox.getSelectionModel().getSelectedItem() != null) {
//                    client_package = pkgComboBox.getSelectionModel().getSelectedItem().toString();
//
//                    if (!LicenseHelper.checkLicense(user_name, client_number, client_email, lic_key, client_package)) {
//
//                        JOptionPane.showMessageDialog(null, "Invalid license Key!");
//                    } else {
//
//                        JOptionPane.showMessageDialog(null, "Registration done Successfully!");
////                        primaryStage.close();
//                        Stage primaryStage = new Stage();
////                        appStart(primaryStage);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Empty Fields not allowed!");
//                }
                }
            });
//        System.out.println("licenseFormStart 20");
            HBox hbBtn = new HBox(11);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
//        System.out.println("licenseFormStart 21");
            hbBtn.getChildren().add(activateBtn);
//        System.out.println("licenseFormStart 22");
            grid.add(hbBtn, 1, 11);
//        System.out.println("licenseFormStart 23");

            final Text actiontarget = new Text();
            grid.add(actiontarget, 1, 12);
            return grid;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

    }

    public void createLicense() {

    }

}
