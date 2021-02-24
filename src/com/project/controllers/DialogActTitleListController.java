package com.project.controllers;

import com.project.model.ActTitleModel;
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

public class DialogActTitleListController implements Initializable {

    @FXML
    private TextField txtFilter;
    @FXML
    private TableView tableData;
    @FXML
    private TableColumn columnActiTitle;

    @FXML
    Button btnCancel, btnOk;

    private Stage dialogStage;
    private String okClicked = "";

    private ObservableList<ActTitleModel> data;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public DialogActTitleListController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        data = FXCollections.observableArrayList();

//        btnOk.setOnAction(event -> getValues());
        btnCancel.setOnAction(event -> handleCancel());

        //Add change listener
        tableData.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tableData.getSelectionModel().getSelectedItem() != null) {
                ActTitleModel bm = (ActTitleModel) newValue;
                getValues(bm.getActTitle());
            }
        });

        try {
            /*columnStatus.setCellValueFactory(new PropertyValueFactory<ActTitleModel, Boolean>("checked"));*/
            columnActiTitle.setCellValueFactory(new PropertyValueFactory("actTitle"));
            tableData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (Exception e) {
//            System.out.println(e);
        }
        getTableData();


        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ActTitleModel> filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(judge -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return judge.getActTitle().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ActTitleModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableData.setItems(sortedData);

    }

    private void setSelectedText(int index) {

        int i = 0;
        for (ActTitleModel item : data) {
            if (index == i) {
                if (item.getChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
            } else {
                item.setChecked(false);
            }

            i++;
        }

        tableData.setItems(null);
        tableData.setItems(data);
    }

    // @FXML
    public void getValues(String selectedVal) {
       /* ObservableList<ActTitleModel> data = tableData.getItems();

        String courtNames = "";

        for (ActTitleModel item : data) {

            if (item.getChecked()) {
                courtNames = courtNames + " " + item.getActTitle();
            }
        }*/

        okClicked = selectedVal;
        dialogStage.close();
    }

    private void getTableData() {
//        data = new ActResultSQLiteHelper().getActList();
        data = null;
        tableData.setItems(null);
        tableData.setItems(data);
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
