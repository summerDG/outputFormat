package org.pasalab.experiment.automj

import java.util

import org.pasalab.experiment.OutputSheetXlsx


/**
  * Created by summerDG on 2018/1/23.
  */
class PerformanceSheet(
                        filePath: String,
                        sheetName: String)
  extends OutputSheetXlsx[Performance](filePath, sheetName) {
  classOf[Performance].getDeclaredFields.foreach{
    f =>
      annotationList.add(f)}
  override def appendList(listdata: util.List[Performance]): Unit = {
    if(isFile){
      val listData=getLastDataRowNum()
      setListData(listData, listdata)
    }else{
      setListData(rownum, listdata)
    }
    export()
  }

  override def append(data: Performance): Unit = {
    if(isFile){
      val listData=getLastDataRowNum()
      val row= sheet.createRow(listData)
      setData(row, data);
    }else{
      val row=sheet.createRow(rownum)
      rownum += 1
      setData(row, data)
    }
    export()
  }
}