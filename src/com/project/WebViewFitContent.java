//package com.project;
//
////ref: http://tech.chitgoks.com/2014/09/13/how-to-fit-webview-height-based-on-its-content-in-java-fx-2-2/
//
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.ListChangeListener;
//import javafx.concurrent.Worker;
//import javafx.geometry.HPos;
//import javafx.geometry.VPos;
//import javafx.scene.Node;
//import javafx.scene.layout.Region;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
//import netscape.javascript.JSException;
//import org.w3c.dom.events.Event;
//
//import java.util.Set;
//
//public final class WebViewFitContent extends Region {
//
//    public final WebView webview = new WebView();
//    public final WebEngine webEngine = webview.getEngine();
//
//    public WebViewFitContent(String content) {
//        webview.setPrefHeight(5);
//
//        widthProperty().addListener(new ChangeListener<Object>() {
//            @Override
//            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
//                Double width = (Double)newValue;
//                webview.setPrefWidth(width);
//                adjustHeight();
//            }
//        });
//
//        webview.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
//            @Override
//            public void changed(ObservableValue<? extends Worker.State> arg0, Worker.State oldState, Worker.State newState)         {
//                if (newState == Worker.State.SUCCEEDED) {
//                    adjustHeight();
//                }
//            }
//        });
//
//        webview.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
//            @Override
//            public void onChanged(Change<? extends Node> change) {
//                Set<Node> scrolls = webview.lookupAll(".scroll-bar");
//                for (Node scroll : scrolls) {
//                    scroll.setVisible(false);
//                }
//            }
//        });
//
////        /*Webview click event*/
////        webview.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
////            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
////                if (newState == Worker.State.SUCCEEDED) {
////                    // note next classes are from org.w3c.dom domain
////                    org.w3c.dom.events.EventListener listener = new org.w3c.dom.events.EventListener() {
////                        public void handleEvent(Event ev) {
////                            String href = ((org.w3c.dom.Element) ev.getTarget()).getAttribute("href");
////                            System.out.println("href : "+href);
////                            if (!href.isEmpty())
////                            {
//////                                resultViewHelper.showJudgementDialogWindow(Integer.parseInt(href), search_query, sort_by, hl_fields, filterBy);
////                            }
////                        }
////                    };
////                    org.w3c.dom.Document doc = webEngine.getDocument();
////                    org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");
////                    for (int i = 0; i < nodeList.getLength(); i++) {
////                        org.w3c.dom.Node node = (org.w3c.dom.Node) nodeList.item(i);
////                        org.w3c.dom.events.EventTarget eventTarget = (org.w3c.dom.events.EventTarget) node;
////                        eventTarget.addEventListener("click", listener, false);
////                    }
////                }
////            }
////        });
//
//        setContent(content);
//        getChildren().add(webview);
//    }
//
//    public void setContent(final String content) {
//        Platform.runLater(new Runnable(){
//            @Override
//            public void run() {
//                webEngine.loadContent(getHtml(content));
//                Platform.runLater(new Runnable(){
//                    @Override
//                    public void run() {
//                        adjustHeight();
//                    }
//                });
//            }
//        });
//    }
//
//
//    @Override
//    protected void layoutChildren() {
//        double w = getWidth();
//        double h = getHeight();
//        layoutInArea(webview,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
//    }
//
//    private void adjustHeight() {
//        Platform.runLater(new Runnable(){
//            @Override
//            public void run() {
//                try {
//                    Object result = webEngine.executeScript("document.getElementById('mydiv').offsetHeight");
//                    if (result instanceof Integer) {
//                        Integer i = (Integer) result;
//                        double height = new Double(i);
//                        height = height + 20;
//                        webview.setPrefHeight(height);
//                        webview.getPrefHeight();
//                    }
//                } catch (JSException e) { }
//            }
//        });
//    }
//
//    private String getHtml(String content) {
//        return "<html><body>" +
//                "<div id=\"mydiv\">" + content + "</div>" +
//                "</body></html>";
//    }
//
//}