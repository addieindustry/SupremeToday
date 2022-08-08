package com.project.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.project.JavaHelper;
import com.project.helper.Queries;
import com.project.helper.ResponseMaster;
import com.project.helper.ServiceHelper;
import com.project.interfaces.BookmarkSearchListener;
import com.project.interfaces.ConfirmationInterface;
import com.project.model.*;
import com.project.utils.PTableColumn;
import com.project.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class DialogLiveUpdateController implements Initializable {

    @FXML
    Button btnUpdate, btnClose;

    @FXML
    TableView tableUpdateStatus;

    @FXML
    ProgressBar progressStatus;

    @FXML
    Label labelStatus;

    @FXML
    private PTableColumn columnVersion, columnCourtId, columnTitle, columnStatus;

    ObservableList<LiveUpdateModel> tableData;

    public Thread th;

    private TrayIcon trayIcon;

//    private Stage primaryStage;
    /*INIT SEARCH INTERFACE*/
//    private static final List<BookmarkSearchListener> searchListenerList = Lists.newArrayList();
//    public void setBookmarkSearchInterface(BookmarkSearchListener searchListener) {
////        Preconditions.checkNotNull(searchListener);
////        searchListenerList.add(searchListener);
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("live update INITIALIZE");
//        System.out.println("INITIALIZE");

        progressStatus.setVisible(false);
        columnVersion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveUpdateModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveUpdateModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().versionProperty();
            }
        });
        columnCourtId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveUpdateModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveUpdateModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().courtIdProperty();
            }
        });
        columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveUpdateModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveUpdateModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().courtsProperty();
            }
        });
        columnStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveUpdateModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveUpdateModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().statusProperty();
            }
        });

        /*Table row double click event to open judgement view*/
        tableUpdateStatus.setRowFactory(tv -> {
            TableRow<VersionResponseModel.Data> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleCancel();
                }
            });
            return row;
        });

        /*Button cancel*/
        btnClose.setOnAction(event -> {
            if (th != null)
            {
                th.stop();
            }
            handleCancel();
        });

//        Task longTask = new Task<Void>() {
//            @Override public Void call() {
//                int i=1;
//                int max=tableData.size();
//                if (max==0){
//                    loadLiveUpdates();
//                }else{
//                    Queries.LIVE_UPDATE_PAUSED = Boolean.FALSE;
//                    btnUpdate.setText("Pause Update");
////                    btnUpdate.setDisable(true);
//                };
//
//                for (LiveUpdateModel d : tableData) {
//                    if (isCancelled() || Queries.LIVE_UPDATE_PAUSED == Boolean.TRUE) {
//                        btnUpdate.setText("Resume Update");
//                        break;
//                    }
//                    updateProgress(i, max);
//                    LiveUpdateModel lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Processing...");
//                    tableUpdateStatus.getItems().set(i-1, lum);
//                    updateMessage("Updating version " + d.getVersion() + " started");
////                    ServiceHelper.getUpdate(d.getVersion(), d.getCourtId());
//                    if (ServiceHelper.getUpdate(d.getVersion(), d.getCourtId()).getCode()==200){
//                        ServiceHelper.versionUpdate(d.getVersion());
//                        lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Completed");
//                        tableUpdateStatus.getItems().set(i-1, lum);
//                        updateMessage("Updating version " + d.getVersion() + " completed");
//                    }else{
//                        lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Failed");
//                        tableUpdateStatus.getItems().set(i-1, lum);
//                        updateMessage("Updating version " + d.getVersion() + " failed");
//                    }
//                    i+=1;
//                }
//                updateMessage("Updation completed");
//                return null;
//            }
//        };

        btnUpdate.setOnAction(event -> {
            if (Queries.LIVE_UPDATE_PAUSED == Boolean.TRUE){
                Queries.LIVE_UPDATE_PAUSED = Boolean.FALSE;
                btnUpdate.setText("Pause Update");

//                progressStatus.setVisible(true);
//                progressStatus.progressProperty().bind(longTask.progressProperty());
//                labelStatus.textProperty().bind(longTask.messageProperty());
//                new Thread(longTask).start();

//                Platform.runLater(() -> {
//                    new Thread(longTask).start();
//
//                });

                loadLiveUpdates();
                runTask();
//                progressStatus.setVisible(true);
//                progressStatus.progressProperty().bind(longTask.progressProperty());
//                labelStatus.textProperty().bind(longTask.messageProperty());
//                new Thread(longTask).start();
                Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
                        "Live Update (In Background) Started!",
                        TrayIcon.MessageType.INFO);
            }else{
                Queries.LIVE_UPDATE_PAUSED = Boolean.TRUE;
                btnUpdate.setText("Resume Update");
            }

        });

        loadLiveUpdates();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (Queries.LIVE_UPDATE_PAUSED == Boolean.TRUE) {
                        Queries.LIVE_UPDATE_PAUSED = Boolean.FALSE;
                        runTask();
                    }
//                    btnUpdate.fire();
//                    new Thread(longTask).start();
                });
            }
        }, 60 * 1000, 2 * 60 * 60 * 1000);

        //        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    runTask();
////                    btnUpdate.fire();
////                    new Thread(longTask).start();
//                });
//            }
//        }, 120 * 1000, 1 * 30 * 60 * 1000);



//        System.out.println("EXIT");
//        new Thread(longTask).start();

//        System.out.println("LIVE UPDATE STARTING");
//        progressStatus.setVisible(true);
//        progressStatus.progressProperty().bind(longTask.progressProperty());
//        labelStatus.textProperty().bind(longTask.messageProperty());
//        new Thread(longTask).start();
//        runTask();
//        System.out.println("LIVE UPDATE STARTED");
    }

    private void runTask(){
        int max=tableData.size();
        if (max==0){
            loadLiveUpdates();
        }else{
//            Queries.LIVE_UPDATE_PAUSED = Boolean.FALSE;
            btnUpdate.setText("Pause Update");
        };

        new Thread(() -> {
            int i=1;
            for (LiveUpdateModel d : tableData) {
                if (Queries.LIVE_UPDATE_PAUSED == Boolean.TRUE) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            btnUpdate.setText("Resume Update");
                        }
                    });
                    break;
                }
//            updateProgress(i, max);
                LiveUpdateModel lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Processing...");
                tableUpdateStatus.getItems().set(i-1, lum);

                Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
                        "Updating version " + d.getVersion() + " started",
                        TrayIcon.MessageType.INFO);
//                    ServiceHelper.getUpdate(d.getVersion(), d.getCourtId());
                if (ServiceHelper.getUpdate(d.getVersion(), d.getCourtId()).getCode()==200){
                    ServiceHelper.versionUpdate(d.getVersion());
                    lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Completed");
                    tableUpdateStatus.getItems().set(i-1, lum);
                    Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
                            "Updating version " + d.getVersion() + " completed",
                            TrayIcon.MessageType.INFO);
                }else{
                    lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Failed");
                    tableUpdateStatus.getItems().set(i-1, lum);
                    Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
                            "Updating version " + d.getVersion() + " failed",
                            TrayIcon.MessageType.INFO);
                }
                i+=1;
            }
//            Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
//                    "Updation completed",
//                    TrayIcon.MessageType.INFO);
        }).start();



//        for (LiveUpdateModel d : tableData) {
//            if (Queries.LIVE_UPDATE_PAUSED == Boolean.TRUE) {
//                btnUpdate.setText("Resume Update");
//                break;
//            }
////            updateProgress(i, max);
//            LiveUpdateModel lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Processing...");
//            tableUpdateStatus.getItems().set(i-1, lum);
//
//            Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
//                    "Updating version " + d.getVersion() + " started",
//            TrayIcon.MessageType.INFO);
////                    ServiceHelper.getUpdate(d.getVersion(), d.getCourtId());
//            if (ServiceHelper.getUpdate(d.getVersion(), d.getCourtId()).getCode()==200){
//                ServiceHelper.versionUpdate(d.getVersion());
//                lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Completed");
//                tableUpdateStatus.getItems().set(i-1, lum);
//                Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
//                        "Updating version " + d.getVersion() + " completed",
//                        TrayIcon.MessageType.INFO);
//            }else{
//                lum = new LiveUpdateModel(d.getVersion(), d.getCourtId(), d.getCourts(), "Failed");
//                tableUpdateStatus.getItems().set(i-1, lum);
//                Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
//                        "Updating version " + d.getVersion() + " failed",
//                        TrayIcon.MessageType.INFO);
//            }
//            i+=1;
//        }
//        Queries.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
//                "Updation completed",
//                TrayIcon.MessageType.INFO);
    }

    private void loadLiveUpdates() {
        ObservableList<CourtModel> courtList = ServiceHelper.getAllCourts();
        tableData = FXCollections.observableArrayList();
        ResponseMaster data = ServiceHelper.checkUpdate();
        if (data.getCode() == 200)
        {
            JsonObject j = (JsonObject)data.getData();
            JsonArray jsonArray = j.get("versions").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                String courtTitle="";
                for (CourtModel court : courtList)
                {
                    if (court.getCourtId().equals(obj.get("courts").getAsString()))
                    {
                        courtTitle = court.getCourtName();
                    }
                }
                tableData.add(new LiveUpdateModel(obj.get("version").getAsString(), obj.get("courts").getAsString(), courtTitle, "Updates Available"));
            }
        }
        tableUpdateStatus.setItems(null);
        if (tableData.size() > 0) {
            tableUpdateStatus.setItems(tableData);
//            btnUpdate.setDisable(false);
        }else{
            tableUpdateStatus.setVisible(false);
            final SystemTray tray = SystemTray.getSystemTray();

            labelStatus.setText("No updates available");
//            import com.project.Main;
//            Main.trayIcon.displayMessage(Queries.APPLICATION_NAME + " - Auto Update",
//                    "Double Click to Maximize",
//                    TrayIcon.MessageType.INFO);

//            btnUpdate.setDisable(true);
        }
    }

    private Stage dialogStage;

    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void handleCancel() {
        dialogStage.close();
    }

}
