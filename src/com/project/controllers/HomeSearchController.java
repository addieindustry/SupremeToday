package com.project.controllers;

import com.project.helper.Queries;
import com.project.interfaces.HomeSearchListener;
import com.project.model.CourtModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeSearchController implements Initializable {


    /*INTERFACE FOR judgement & ACT search*/
//    private static final List<HomeSearchListener> homeSearchList = Lists.newArrayList();
    @FXML
    private Image homeImg;

    @FXML
    private WebView webViewContent;
    private WebEngine engine;

    @FXML
    private StackPane stackPane;


    public void setHomeSearchListener(HomeSearchListener homeSearchListener) {
//        Preconditions.checkNotNull(homeSearchListener);
//        homeSearchList.add(homeSearchListener);
    }

    private ObservableList<CourtModel> dataCourt = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = webViewContent.getEngine();
        File html_file = new File(Queries.HOME_PAGE_HTML_PATH);
        File image_file = new File(Queries.HOME_PAGE_IMAGE_PATH);
        if (html_file.exists()){
            System.out.println(html_file.toURI().toString());
            engine.load(html_file.toURI().toString());
        }else{
            Image image=new Image(image_file.toURI().toString());
            ImageView view=new ImageView(image);
            stackPane.getChildren().add(view);//This is the added code.
        }


//        engine.load(Queries.HOME_PAGE_HTML_PATH);
//        engine.load("https://www.google.com");

//        engine.loadContent(htmlContent, "text/html");

//        try {
//            InputStream is = new FileInputStream(Queries.splashFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        File fil=new File("src\\see\\unshadedLaptop.png");
//        Image im=new Image(fil.toURI().toString());
//        homeImg.url
    }

}
