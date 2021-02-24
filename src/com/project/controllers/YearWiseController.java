package com.project.controllers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.project.helper.ServiceHelper;
import com.project.interfaces.ClickEventHandler;
import com.project.interfaces.ResetClickEventHandler;
import com.project.model.CourtModel;
import com.project.utils.AutoCompleteComboBoxListener;
import com.project.utils.FxUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class YearWiseController implements Initializable {

    @FXML
    private ComboBox<CourtModel> comboboxCourt;

    @FXML
    private ComboBox<String> comboboxYear;

    @FXML
    private Hyperlink hpAll, hpJan, hpFeb, hpMar, hpApr, hpMay, hpJun, hpJul, hpAug, hpSep, hpOct, hpNov, hpDec;

    @FXML
    private Button btnReset;

    @FXML
    private SplitPane split_pane;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane result_view_pane;

    private static final List<ClickEventHandler> listeners = Lists.newArrayList();

    public void addListener(ClickEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }

    private static final List<ResetClickEventHandler> listenersReset = Lists.newArrayList();

    public void addListener(ResetClickEventHandler listener) {
        Preconditions.checkNotNull(listener);
        listenersReset.add(listener);
    }


    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public YearWiseController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        split_pane.setResizableWithParent(root, false);
        split_pane.setResizableWithParent(result_view_pane, false);
        split_pane.setDividerPositions(0.08);

        comboboxCourt.setConverter(new StringConverter<CourtModel>() {
            @Override
            public String toString(CourtModel object) {
                return object.getCourtName();
            }

            @Override
            public CourtModel fromString(String string) {
                return null;
            }
        });

        comboboxCourt.valueProperty().addListener(new ChangeListener<CourtModel>() {
            @Override
            public void changed(ObservableValue<? extends CourtModel> observable, CourtModel oldValue, CourtModel newValue) {
                comboboxYear.setItems(ServiceHelper.getYearListByCourtId(newValue.getCourtId()));
            }
        });

        comboboxCourt.setItems(ServiceHelper.getAllCourts());

        /*Combobox on focus expand code*/
        for (final ComboBox<String> choiceBox : Arrays.asList(comboboxYear)) {
            final ChangeListener<? super Boolean> showHideBox = (__, ___, isFocused) ->
            {
                if (isFocused.booleanValue()) {
                    choiceBox.show();
                } else {
                    choiceBox.hide();
                }
            };
            choiceBox.focusedProperty().addListener(showHideBox);
            choiceBox.addEventFilter(MouseEvent.MOUSE_RELEASED, release ->
            {
//                release.consume();
                //choiceBox.requestFocus();
            });
        }

//        /*Combobox on focus expand code*/
//        for (final ComboBox<CourtModel> choiceBox : Arrays.asList(comboboxCourt)) {
//            final ChangeListener<? super Boolean> showHideBox = (__, ___, isFocused) ->
//            {
//                if (isFocused.booleanValue()) {
//                    choiceBox.show();
//                } else {
//                    choiceBox.hide();
//                }
//            };
//            choiceBox.focusedProperty().addListener(showHideBox);
//            choiceBox.addEventFilter(MouseEvent.MOUSE_RELEASED, release ->
//            {
////                release.consume();
//                //choiceBox.requestFocus();
//            });
//        }

//        /*FILTER COMBOBOX VALUE*/
//        FxUtil.autoCompleteComboBox(comboboxCourt, FxUtil.AutoCompleteMode.STARTS_WITH);
//        FxUtil.getComboBoxValue(comboboxCourt);

//        new AutoCompleteComboBoxListener<>(comboboxCourt);
        new AutoCompleteComboBoxListener<>(comboboxYear);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/ui/result_view.fxml"));
        YearWiseResultViewController controller = new YearWiseResultViewController();
        loader.setController(controller);

        try {
            result_view_pane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_view_pane.setVisible(false);

        btnReset.setOnAction(event ->{
            for (ResetClickEventHandler listner : listenersReset) {
                listner.handleResetClick();
                comboboxYear.getSelectionModel().clearSelection();
                comboboxYear.setValue(null);
                comboboxCourt.getSelectionModel().clearSelection();
                comboboxCourt.setValue(null);
                result_view_pane.setVisible(false);
            }
        });

        hpAll.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-01-01";
                    String endDate = comboboxYear.getValue() + "-12-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
//                    System.out.println(searchQuery);
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpJan.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-01-01";
                    String endDate = comboboxYear.getValue() + "-01-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpFeb.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-02-01";
                    String endDate = comboboxYear.getValue() + "-02-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpMar.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-03-01";
                    String endDate = comboboxYear.getValue() + "-03-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpApr.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-04-01";
                    String endDate = comboboxYear.getValue() + "-04-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpMay.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-05-01";
                    String endDate = comboboxYear.getValue() + "-05-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpJun.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-06-01";
                    String endDate = comboboxYear.getValue() + "-06-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpJul.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-07-01";
                    String endDate = comboboxYear.getValue() + "-07-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpAug.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-08-01";
                    String endDate = comboboxYear.getValue() + "-08-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpSep.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-09-01";
                    String endDate = comboboxYear.getValue() + "-09-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpOct.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-10-01";
                    String endDate = comboboxYear.getValue() + "-10-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpNov.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-11-01";
                    String endDate = comboboxYear.getValue() + "-11-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

        hpDec.setOnAction(event -> {
            for (ClickEventHandler listner : listeners) {
                if ((comboboxYear.getValue() != null) && (comboboxCourt.getSelectionModel() != null))
                {
                    result_view_pane.setVisible(true);
                    String startDate = comboboxYear.getValue() + "-12-01";
                    String endDate = comboboxYear.getValue() + "-12-31";
                    String searchQuery = "+courtId:"+comboboxCourt.getSelectionModel().getSelectedItem().getCourtId()+" +decisionDate:[" + startDate + " TO " + endDate + "]";
                    listner.handleSeachClick(searchQuery);
                }
            }
        });

    }

}
