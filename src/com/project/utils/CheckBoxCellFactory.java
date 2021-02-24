package com.project.utils;

import com.project.model.CourtModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class CheckBoxCellFactory implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<CourtModel, Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }

}