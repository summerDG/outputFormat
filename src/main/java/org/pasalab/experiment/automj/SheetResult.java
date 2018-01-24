package org.pasalab.experiment.automj;

/**
 * Created by summerDG on 2018/1/23.
 */
public class SheetResult<T> {
  String sheetName;
  T obj;
  public SheetResult(String sheetName, T obj) {
    this.sheetName = sheetName;
    this.obj = obj;
  }
}
