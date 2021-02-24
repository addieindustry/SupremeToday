package com.project.controllers;

import com.project.model.ActModel;
import com.project.model.ActTitleModel;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogActListController implements Initializable {

    @FXML
    private TextField txtFilter;
    @FXML
    private TableView tableData;
    @FXML
    private TableColumn columnStatus;
    @FXML
    private TableColumn columnCourtName;
    @FXML
    Button btnCancel, btnOk;

    private Stage dialogStage;
    private String okClicked = "";

    private ObservableList<ActModel> data;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public DialogActListController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnOk.setOnAction(event -> getValues());
        btnCancel.setOnAction(event -> handleCancel());

//        try {
            data = FXCollections.observableArrayList();
//            data = new JudgementSearchLuceneHelper().getAllActList();
            // Initialize the person table
            columnStatus.setCellValueFactory(new PropertyValueFactory<ActModel, Boolean>("checked"));
            columnCourtName.setCellValueFactory(new PropertyValueFactory("actName"));

            tableData.setItems(null);
            tableData.setItems(data);
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ActModel> filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(courts -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return courts.getActName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ActModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableData.setItems(sortedData);

        columnStatus.setCellFactory(column -> new TableCell<ActTitleModel, Boolean>() {
                    public void updateItem(Boolean check, boolean empty) {
                        super.updateItem(check, empty);
                        if (check == null || empty) {
                            setGraphic(null);
                        } else {
                            CheckBox box = new CheckBox();
                            BooleanProperty checked = (BooleanProperty) columnStatus.getCellObservableValue(getIndex());
                            box.setSelected(checked.get());
                            box.selectedProperty().bindBidirectional(checked);
                            setGraphic(box);
                            setSelectedText();
                        }
                    }
                }
        );
    }

    @FXML
    Label labelSelectedCourts;

    private void setSelectedText() {

        String str = "";
        labelSelectedCourts.setText("");

        for (ActModel item : data) {
            if (item.getChecked()) {
                if (!str.isEmpty()) {
                    str = str + ",";
                }
                str = str + item.getActName();
            }
        }
        labelSelectedCourts.setText(str);
    }


    // @FXML
    public void getValues() {
        ObservableList<ActModel> data = tableData.getItems();

        String actNames = "";

        for (ActModel item : data) {

            if (item.getChecked()) {
                if (!actNames.isEmpty()) {
                    actNames = actNames + ",";
                }
                actNames = actNames + item.getActName();
            }
        }

        okClicked = actNames.trim();
        dialogStage.close();
    }


    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public String isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        /*if (isInputValid()) {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(CalendarUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }*/
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
