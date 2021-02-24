package com.project.controllers;

import com.project.interfaces.HomeSearchListener;
import com.project.model.CourtModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeSearchController implements Initializable {


    /*INTERFACE FOR judgement & ACT search*/
//    private static final List<HomeSearchListener> homeSearchList = Lists.newArrayList();

    public void setHomeSearchListener(HomeSearchListener homeSearchListener) {
//        Preconditions.checkNotNull(homeSearchListener);
//        homeSearchList.add(homeSearchListener);
    }

    private ObservableList<CourtModel> dataCourt = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
