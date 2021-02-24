package com.project.utils;

import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import com.project.model.TitleIdListModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 *
 * @author Caleb Brinkman
 */
public class AutoTextAutoCompleteTextField extends TextField {
    /**
     * The existing autocomplete entries.
     */
    private final List<String> entries;
    /**
     * The popup used to select an entry.
     */
    private ContextMenu entriesPopup;

    /**
     * Construct a new AutoTextAutoCompleteTextField.
     */
    public AutoTextAutoCompleteTextField() {
        super();
        entries = new ArrayList<>();

        entriesPopup = new ContextMenu();

        ObservableList<TitleIdListModel> short_list = ServiceHelper.getAllShortFullActsRefTitle();

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                entries.clear();
                if (!getText().isEmpty() && getText().replaceAll(Queries.REMOVE_SPECIAL_CHAR, "").replace(":", " ").trim().length() > 1) {
//                    entries.addAll(ServiceHelper.getAutoCompleteList("%" + s2.replaceAll(ConstantsClass.REMOVE_SPECIAL_CHAR, "").trim() + "%"));
//                    System.out.println("getId().toString() : " + getId().toString());
//                    System.out.println("getProperties().toString() : " + getProperties().toString());
//                    getId().toString().contains("txt_field_border_actref");
                    if (Queries.HIDE_AUTOSUGGET==false)
                    {
                        if (Queries.AUTO_COMPLETE_TEXTFIELD.equals("fulltext")){
                            entries.addAll(ServiceHelper.getAutoCompleteList(s2.replaceAll(Queries.REMOVE_SPECIAL_CHAR, "").replace(":", " ").trim()));
                        }
                        else if (Queries.AUTO_COMPLETE_TEXTFIELD.equals("judge")){
//                        entries.addAll(ServiceHelper.getAllJudges("%" + s2.replaceAll(Queries.REMOVE_SPECIAL_CHAR, "").trim() + "%"));
                            entries.addAll(ServiceHelper.getAllJudges("%" + s2.trim() + "%"));
                        }
                        else if (Queries.AUTO_COMPLETE_TEXTFIELD.equals("actref")){
                            entries.addAll(ServiceHelper.getAllActsRefTitle(s2.replaceAll(Queries.REMOVE_SPECIAL_CHAR, "").trim(), short_list));
//                            entries.addAll(ServiceHelper.getAllActsRefTitle("%" + s2.replaceAll(Queries.REMOVE_SPECIAL_CHAR, "").trim() + "%", short_list));
                        }
                        else if (Queries.AUTO_COMPLETE_TEXTFIELD.equals("sectionref")){
                            entries.addAll(ServiceHelper.getAllActsRefSectionByTitle("%" + s2.trim() + "%"));
                        }
                    }else{
                        Queries.HIDE_AUTOSUGGET=false;
                    }
                }

                if (getText().length() == 0) {
                    entriesPopup.hide();
                } else {
                    LinkedList<String> searchResult = new LinkedList<>();
//                    searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                    searchResult.addAll(entries);
                    if (entries.size() > 0) {
                        populatePopup(searchResult);
                        if (!entriesPopup.isShowing()) {
                            entriesPopup.show(AutoTextAutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else {
                        entriesPopup.hide();
                    }
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                entriesPopup.hide();
            }
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public List<String> getEntries() {
        return entries;
    }

    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 20;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    setText(result);
                    positionCaret(result.length());
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }
}
