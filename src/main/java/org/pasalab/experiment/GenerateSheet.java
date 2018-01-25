package org.pasalab.experiment;

import java.util.ArrayList;
import java.util.List;

public abstract class GenerateSheet<T> {
    protected List<String> names = new ArrayList<String>();
    private List<List<T>> sheetRows = new ArrayList<List<T>>();
    public void appendSheet(String name, List<T> rows) {
        names.add(name);
        sheetRows.add(rows);
    }
    public abstract List<OutputSheetXlsx<T>> generate(String file);
    public List<List<T>> sheetRows(){
        return sheetRows;
    }
}
