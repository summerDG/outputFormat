package org.pasalab.experiment.automj
import java.util


/**
  * Created by summerDG on 2018/1/23.
  */
class PerformanceExcel(
                        filePath: String,
                        sheetName: String)
  extends OutputXlsx(filePath, sheetName) {
  override def appendList(listdata: util.List[Nothing]): Unit = {
    if(isFile){
      val listData=getLastDataRowNum()
      setListData(listData, listdata)
    }else{
      setListData(rownum, listdata)
    }
    export()
  }

  override def append(data: Nothing): Unit = {
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