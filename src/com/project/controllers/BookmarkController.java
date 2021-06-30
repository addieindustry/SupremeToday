package com.project.controllers;

import com.project.JavaHelper;
import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.interfaces.BookmarkSearchListener;
import com.project.interfaces.ConfirmationInterface;
import com.project.model.BookmarkModel;
import com.project.model.TitleIdListModel;
import com.project.utils.PTableColumn;
import com.project.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
//import org.controlsfx.control.action.Action;
//import org.controlsfx.dialog.Dialogs;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ResourceBundle;

public class BookmarkController implements Initializable {

    @FXML
    Button btnDelete, btnImport, btnExport, btnClose;

    @FXML
    private ComboBox<String> comboboxCategory;

    @FXML
    TableView tableBookmark;

    @FXML
    private PTableColumn columnId, columnTitle, columnCategory, columnDescription;

    ObservableList<BookmarkModel> tableData = FXCollections.observableArrayList();

    String bookMarkId = "";
    private Stage primaryStage;

    /*INIT SEARCH INTERFACE*/
//    private static final List<BookmarkSearchListener> searchListenerList = Lists.newArrayList();

    public void setBookmarkSearchInterface(BookmarkSearchListener searchListener) {
//        Preconditions.checkNotNull(searchListener);
//        searchListenerList.add(searchListener);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
            btnDelete.setStyle("-fx-background-color: steelblue");
            btnImport.setStyle("-fx-background-color: steelblue");
            btnExport.setStyle("-fx-background-color: steelblue");
            btnClose.setStyle("-fx-background-color: steelblue");
        }

        columnId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BookmarkModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BookmarkModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().idProperty();
            }
        });
        columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BookmarkModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BookmarkModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().titleProperty();
            }
        });
        columnCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BookmarkModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BookmarkModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().categoryProperty();
            }
        });
        columnDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BookmarkModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BookmarkModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().noteProperty();
            }
        });

//        columnId.setCellValueFactory(new PropertyValueFactory("Id"));
//        columnTitle.setCellValueFactory(new PropertyValueFactory("title"));
//        columnCategory.setCellValueFactory(new PropertyValueFactory("category"));
//        columnDescription.setCellValueFactory(new PropertyValueFactory("note"));

        //Add change listener
        tableBookmark.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tableBookmark.getSelectionModel().getSelectedItem() != null) {
                BookmarkModel bm = (BookmarkModel) newValue;
                bookMarkId = bm.getId();
            }
        });

        /*Table row double click event to open judgement view*/
        tableBookmark.setRowFactory(tv -> {
            TableRow<BookmarkModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {

                    BookmarkModel rowData = row.getItem();
                    showJudgementDialogWindow(rowData.getDocId());
                     /*CALL LISTERNER TO OPEN SEARCH RESULT*/
//                    for (BookmarkSearchListener listner : searchListenerList) {
//                        listner.openBookmarkResultListener(rowData.getSearchType());
//                    }
                    handleCancel();
                }
            });
            return row;
        });

        /*Button cancel*/
        btnClose.setOnAction(event -> {
            handleCancel();
        });

        btnDelete.setOnAction(event -> {
            if (!bookMarkId.isEmpty()) {
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Do you want to close the application?", yesButton, noButton);
                alert.setTitle("Confirm Dialog");
                alert.setHeaderText(Queries.APPLICATION_NAME);
//        alert.setContentText("I have a great message for you!");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        String res = ServiceHelper.removeBookmark(bookMarkId);
                        new Utils().showDialogAlert(res);
                        loadBookmarks();
                    }
                });

//                Action response = Dialogs.create()
//                        .title("Confirm Dialog")
//                        .masthead(null)
//                        .message(Queries.MESSAGE_DELETE_BOOKMARK)
//                        .showConfirm();
//                //System.out.println(response.toString());
//
//                if (response.toString().equals("DialogAction.YES")) {
//                    try {
//                        String res = ServiceHelper.removeBookmark(bookMarkId);
//                        new Utils().showDialogAlert(res);
//                        loadBookmarks();
////                        System.exit(0);
////                        Object obj = new Object();
////                        WeakReference ref = new WeakReference<Object>(obj);
////                        obj = null;
////                        while (ref.get() != null) {
////                            System.out.println(ref.toString());
////                            System.gc();
////                        }
//                    } catch (Exception exception) {
//                        exception.printStackTrace();
//                    }
//                } else {
//                    event.consume();
//                }


//                new Utils().confirmationDialog(Queries.MESSAGE_DELETE_BOOKMARK, confirmationInterface);
            }
        });

        btnImport.setOnAction(event -> {
            new JavaHelper().importBookmark();
            loadBookmarks();
        });

        btnExport.setOnAction(event -> {
            new JavaHelper().exportBookmark();
        });

        loadBookmarks();

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<BookmarkModel> filteredData = new FilteredList<>(tableData, p -> true);

        comboboxCategory.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filteredData.setPredicate(searchType -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        return searchType.getCategory().toLowerCase().contains(lowerCaseFilter);
                    });
            }
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<BookmarkModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableBookmark.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableBookmark.setItems(sortedData);

//        btnUpdate.setOnAction(event -> {
//            //       bookmarkId,    String sessionId, String searchType, String fileType,
//// String parentId, String childId, String remark, String title, String createDon, String isActive
////            BookmarkModel bookmarkModel = new BookmarkModel(bookMarkId, "", "", "", "", "", textFieldDetails.getText(), textFieldTitle.getText(), "", "");
////            int rowAffected = new BookmarksSQLiteHelper().updateRow(bookmarkModel);
//            int rowAffected=0;
//            if (rowAffected != -1) {
//                loadBookmarks();
//            }
//        });
    }

    /*Take delete confirmation from dialog*/
//    ConfirmationInterface confirmationInterface = new ConfirmationInterface() {
//        @Override
//        public void yesHeandale() {
//
//            String response = ServiceHelper.removeBookmark(bookMarkId);
//            new Utils().showDialogAlert(response);
//            loadBookmarks();
//        }
//
//        @Override
//        public void NoHeandale() {
//
//        }
//    };

    private void loadBookmarks() {
        //get table data from bookmark table
        tableData = ServiceHelper.getAllBookmarks();
        comboboxCategory.setItems(ServiceHelper.getAllBookmarkCategory());

        tableBookmark.setItems(null);
        if (tableData.size() > 0) {
            tableBookmark.setItems(tableData);
        }
    }

    public boolean showJudgementDialogWindow(String caseId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/dialog_judgement_view.fxml"));
            JudgementViewController controller = new JudgementViewController();
            loader.setController(controller);
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Judgement View");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/project/resources/logo.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setHeight(screenBounds.getHeight());
            dialogStage.setWidth(screenBounds.getWidth());
            /*disabled maximaize and minimize except close use*/
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

//             Set the person into the controller
//            JudgementViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            String filter_query = "";
//            for(Map.Entry<String, String> entry : filterBy.entrySet()){
//                filter_query += "+"+ entry.getKey() + ":" + entry.getValue() + " ";
//            }

            controller.initData("caseId:"+caseId, "", "", "", 0);
            //            controller.setPerson(person);



               /*Close window on Escap key press*/
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent evt) {
                    if (evt.getCode().equals(KeyCode.ESCAPE)) {
                        dialogStage.close();
                    }
                }
            });

            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
            dialogStage.show();

//            if (!controller.isOkClicked()) {
//
//            }

//            return controller.isOkClicked();
            return true;
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            new Utils().showErrorDialog(e);
            return true;
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


//    private Stage bookMarkDialogStage;
//    private boolean okClickedOnBookMarkDialog = false;
//
//    public boolean isOkClickedOnBookMarkDialog() {
//        return okClickedOnBookMarkDialog;
//    }
//
//    public void setBookMarkDialogStage(Stage bookMarkDialogStage) {
//        this.bookMarkDialogStage = bookMarkDialogStage;
//    }
//
//    private void handleCancel() {
//        bookMarkDialogStage.close();
//    }


}
