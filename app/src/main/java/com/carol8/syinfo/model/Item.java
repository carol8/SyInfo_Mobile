package com.carol8.syinfo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Comparable<Item>, Serializable {
    private final String label;
    private final List<String> values = new ArrayList<>();

    public Item(String label){
        this.label = label;
    }

    public Item(String label, List<String> values){
        this.label = label;
        this.values.addAll(values);
    }

    public String getLabel() {
        return label;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public int compareTo(Item item) {
        return this.label.compareTo(item.label);
    }
}
