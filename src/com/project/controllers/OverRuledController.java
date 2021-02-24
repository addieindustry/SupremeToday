package com.project.controllers;

import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.model.JudgementResultModel;
import com.project.model.OverruledModel;
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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OverRuledController implements Initializable {

    @FXML
    private TextField textFieldTitle;
    @FXML
    private TableView tableTitleList;
    @FXML
    private PTableColumn columnId, columnCourtId, columnredId, columnredName, columnOverruled, columnWhiteName, columnWhiteId;

    private ObservableList<OverruledModel> tableData = FXCollections.observableArrayList();
//    private String currentCentralActId= "";
//    private ObservableList<DictionaryTitleModel> data;
//    private ObservableList<String> entries = FXCollections.observableArrayList();

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public OverRuledController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().idProperty();
            }
        });
        columnCourtId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().courtIdProperty();
            }
        });
        columnredId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().redProperty();
            }
        });
        columnredName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().redTextProperty();
            }
        });
        columnOverruled.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().overruledProperty();
            }
        });
        columnWhiteName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().whiteTextProperty();
            }
        });
        columnWhiteId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OverruledModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OverruledModel, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().whiteProperty();
            }
        });

//        columnId.setCellValueFactory(new PropertyValueFactory("id"));
//        columnCourtId.setCellValueFactory(new PropertyValueFactory("courtId"));
//        columnredId.setCellValueFactory(new PropertyValueFactory("red"));
//        columnredName.setCellValueFactory(new PropertyValueFactory("redText"));
//        columnOverruled.setCellValueFactory(new PropertyValueFactory("overruled"));
//        columnWhiteName.setCellValueFactory(new PropertyValueFactory("whiteText"));
//        columnWhiteId.setCellValueFactory(new PropertyValueFactory("white"));

        // Table cell coloring
        columnredName.setCellFactory(new Callback<TableColumn<OverruledModel, String>, TableCell<OverruledModel, String>>() {
            @Override
            public TableCell<OverruledModel, String> call(TableColumn<OverruledModel, String> param) {
                return new TableCell<OverruledModel, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (!isEmpty()) {
                            if (!item.isEmpty()) {
                                setStyle("-fx-text-fill: "+ Queries.OVERRULED_BACKGROUND_COLOR +";");
//                                setStyle("-fx-background-color: "+ Queries.OVERRULED_BACKGROUND_COLOR +";");
                            }
                        }
                    }
                };
            }
        });

        loadData();

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<OverruledModel> filteredData = new FilteredList<>(tableData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        textFieldTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(searchType -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return searchType.getRedText().toLowerCase().contains(lowerCaseFilter) || searchType.getWhiteText().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<OverruledModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableTitleList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableTitleList.setItems(sortedData);

//        tableTitleList.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                // Get the table header
//                Pane header = (Pane)tableTitleList.lookup("TableHeaderRow");
//                if(header!=null && header.isVisible()) {
//                    header.setMaxHeight(0);
//                    header.setMinHeight(0);
//                    header.setPrefHeight(0);
//                    header.setVisible(false);
//                    header.setManaged(false);
//                }
//            }
//        });

        //Add change listener
        tableTitleList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tableTitleList.getSelectionModel().getSelectedItem() != null) {
                OverruledModel hm = (OverruledModel) newValue;
//                System.out.println();
//                System.out.println(hm.getTitle());
//                currentCentralActId = hm.getId();
//                comboboxType.setItems(ServiceHelper.getCentralActTypeById(currentCentralActId));
//                comboboxType.getSelectionModel().select(0);
//                String content = ServiceHelper.getDictionaryContentById(hm.getId());
//                webViewContent.getEngine().loadContent(content, "text/html");
            }
        });

//            /*Table row double click event */
//        tableTitleList.setRowFactory(tv -> {
//            TableRow<JudgementResultModel> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    if (tableTitleList.getSelectionModel().getSelectedItem() != null) {
//                        OverruledModel om = (OverruledModel) tableTitleList.getSelectionModel().getSelectedItem();
//                        System.out.println("double clicked Overruled : " + om.getRed());
//                        System.out.println("double clicked Overruled : " + om.getWhite());
//                        showJudgementDialogWindow(om.getWhite());
////                        om.getWhite();
//                    }
////                    showDialogWindow("/com/project/ui/dialog_judgement_view.fxml", "Judgement View");
////                    for (ClickEventHandler listner : listeners) {
////                        System.out.println("double clicked search_query : " + search_query);
////                        listner.handleSeachClick(search_query);
////                    }
//                }
//            });
//            return row;
//        });

        tableTitleList.setEditable(true);
        tableTitleList.getSelectionModel().setCellSelectionEnabled(true);  // selects cell only, not the whole row
        tableTitleList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    @SuppressWarnings("rawtypes")
                    TablePosition pos = (TablePosition) tableTitleList.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    int col = pos.getColumn();
                    @SuppressWarnings("rawtypes")
                    TableColumn column = pos.getTableColumn();
                    String val = column.getCellData(row).toString();
                    if ( col == 1 ) {
                        OverruledModel om = (OverruledModel) tableTitleList.getSelectionModel().getSelectedItem();
                        showJudgementDialogWindow(om.getRed());
                    }
                    if ( col == 3 ) {
                        OverruledModel om = (OverruledModel) tableTitleList.getSelectionModel().getSelectedItem();
                        showJudgementDialogWindow(om.getWhite());
                    }
                }
            }
        });

    }

    private void loadData() {
        tableData = ServiceHelper.getAllOverruled();

        tableTitleList.setItems(null);
        if (tableData.size() > 0) {
            tableTitleList.setItems(tableData);
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
            dialogStage.showAndWait();

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
}
