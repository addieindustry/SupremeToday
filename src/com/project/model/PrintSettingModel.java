package com.project.model;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PrintSettingModel {

    private SimpleStringProperty id;
    private SimpleStringProperty pageType;
    private SimpleStringProperty displayFontSize;
    private SimpleStringProperty printFontSize;
    private SimpleStringProperty resultFontSize;
    private SimpleFloatProperty marginTop;
    private SimpleFloatProperty marginBottom;
    private SimpleFloatProperty marginRight;
    private SimpleFloatProperty marginLeft;
    private SimpleBooleanProperty printLogo;
    private SimpleBooleanProperty useNativeBrowser;
    private SimpleFloatProperty filterSplitter;
    private SimpleFloatProperty globalResultSplitter;
    private SimpleFloatProperty advancedResultSplitter;

    public PrintSettingModel(String id, String pageType, String displayFontSize, String printFontSize, String resultFontSize, Float marginTop, Float marginBottom, Float marginRight, Float marginLeft,  Boolean printLogo, Boolean useNativeBrowser, Float filterSplitter, Float globalResultSplitter, Float advancedResultSplitter) {
        this.id = new SimpleStringProperty(id);
        this.pageType = new SimpleStringProperty(pageType);
        this.displayFontSize = new SimpleStringProperty(displayFontSize);
        this.printFontSize = new SimpleStringProperty(printFontSize);
        this.resultFontSize = new SimpleStringProperty(resultFontSize);
        this.marginTop=new SimpleFloatProperty(marginTop);
        this.marginBottom=new SimpleFloatProperty(marginBottom);
        this.marginRight=new SimpleFloatProperty(marginRight);
        this.marginLeft=new SimpleFloatProperty(marginLeft);
        this.printLogo = new SimpleBooleanProperty(printLogo);
        this.useNativeBrowser = new SimpleBooleanProperty(useNativeBrowser);
        this.filterSplitter = new SimpleFloatProperty(filterSplitter);
        this.globalResultSplitter = new SimpleFloatProperty(globalResultSplitter);
        this.advancedResultSplitter = new SimpleFloatProperty(advancedResultSplitter);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getPageType() {
        return pageType.get();
    }

    public SimpleStringProperty pageTypeProperty() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType.set(pageType);
    }

    public String getDisplayFontSize() { return displayFontSize.get(); }

    public SimpleStringProperty displayFontSizeProperty() {
        return displayFontSize;
    }

    public void setDisplayFontSize(String displayFontSize) { this.displayFontSize.set(displayFontSize); }

    public String getPrintFontSize() { return printFontSize.get(); }

    public SimpleStringProperty printFontSizeProperty() {
        return printFontSize;
    }

    public void setPrintFontSize(String printFontSize) {
        this.printFontSize.set(printFontSize);
    }




    public String getResultFontSize() { return resultFontSize.get(); }

    public SimpleStringProperty resultFontSizeProperty() {
        return resultFontSize;
    }

    public void setResultFontSize(String resultFontSize) {
        this.resultFontSize.set(resultFontSize);
    }




    public Float getMarginTop() {
        return marginTop.get();
    }

    public SimpleFloatProperty marginTopProperty() {
        return marginTop;
    }

    public void setMarginTop(Float marginTop) {
        this.marginTop.set(marginTop);
    }

    public Float getMarginBottom() {
        return marginBottom.get();
    }

    public SimpleFloatProperty marginBottomProperty() {
        return marginBottom;
    }

    public void setMarginBottom(Float marginBottom) {
        this.marginBottom.set(marginBottom);
    }

    public Float getMarginRight() {
        return marginRight.get();
    }

    public SimpleFloatProperty marginRightProperty() {
        return marginRight;
    }

    public void setMarginRight(Float marginRight) {
        this.marginRight.set(marginRight);
    }

    public Float getMarginLeft() {
        return marginLeft.get();
    }

    public SimpleFloatProperty marginLeftProperty() {
        return marginLeft;
    }

    public void setMarginLeft(Float marginLeft) {
        this.marginLeft.set(marginLeft);
    }


    public Boolean getPrintLogo() {
        return printLogo.get();
    }

    public SimpleBooleanProperty printLogoSizeProperty() {
        return printLogo;
    }

    public void setPrintLogo(Boolean printLogo) {
        this.printLogo.set(printLogo);
    }

    public Boolean getUseNativeBrowser() {
        return useNativeBrowser.get();
    }

    public SimpleBooleanProperty useNativeBrowserProperty() {
        return useNativeBrowser;
    }

    public void setUseNativeBrowser(Boolean useNativeBrowser) {
        this.useNativeBrowser.set(useNativeBrowser);
    }

    public Float getFilterSplitter() {
        return filterSplitter.get();
    }

    public SimpleFloatProperty filterSplitterProperty() {
        return filterSplitter;
    }

    public void setFilterSplitter(Float filterSplitter) {
        this.filterSplitter.set(filterSplitter);
    }

    public Float getGlobalResultSplitter() {
        return globalResultSplitter.get();
    }

    public SimpleFloatProperty globalResultSplitterProperty() {
        return globalResultSplitter;
    }

    public void setGlobalResultSplitter(Float globalResultSplitter) {
        this.globalResultSplitter.set(globalResultSplitter);
    }

    public Float getAdvancedResultSplitter() {
        return advancedResultSplitter.get();
    }

    public SimpleFloatProperty advancedResultSplitterProperty() {
        return advancedResultSplitter;
    }

    public void setAdvancedResultSplitter(Float advancedResultSplitter) {
        this.advancedResultSplitter.set(advancedResultSplitter);
    }

}
