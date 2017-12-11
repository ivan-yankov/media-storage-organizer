package org.yankov.mso.application.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PieceTableDataBean<T extends PieceProperties> {

    private final ObservableList<T> items;

    public PieceTableDataBean() {
        this.items = FXCollections.observableArrayList();
    }

    public ObservableList<T> getItems() {
        return items;
    }

    public void add(T item) {
        items.add(item);
    }

    public void remove(int index) {
        items.remove(index);
    }

    public void copy(int index) {
        T item = (T) items.get(index).clone();
        items.add(item);
    }

}
