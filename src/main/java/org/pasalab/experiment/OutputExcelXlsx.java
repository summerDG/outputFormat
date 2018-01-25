package org.pasalab.experiment;

import java.util.List;

public class OutputExcelXlsx<T> {
    private List<OutputSheetXlsx<T>> sheets;
    private List<List<T>> sheetRows;
    public OutputExcelXlsx(String file, GenerateSheet<T> generateSheet) {
        this.sheets = generateSheet.generate(file);
        this.sheetRows = generateSheet.sheetRows();
    }
    public void output(){
        for (int i = 0; i < sheets.size(); i++) {
            sheets.get(i).appendList(sheetRows.get(i));
        }
    }
}
