package org.pasalab.experiment.automj;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by summerDG on 2018/1/23.
 */
public abstract class OutputXlsx<T> {
  private Class<?> clazz;

  public HSSFWorkbook wb;

  public HSSFSheet sheet;

  private FileInputStream fileInput=null;

  private FileChannel fileChannel=null;
  private FileLock fileLock=null;

  /**
   * 路径是否是文件
   */
  public boolean isFile=false;

  /**
   * 当前行号
   */
  public int rownum;

  private List<String> headerList=new ArrayList<String>();

  private int headnum=0;

  /**
   * 样式列表
   */
  private Map<String, CellStyle> styles;

  private String filePath;

  /**
   * 注解列表（Object[]{ ExcelField, Field/Method }）
   */
  List<Object[]> annotationList = new ArrayList<Object[]>();

  public OutputXlsx(String filePath, String sheetName) {
    this(filePath, sheetName,1);
  }

  //检测文件大小。
  private File checkFilePath(String filePath) {
    try {
      File file=new File(filePath);
      if(!file.exists()){
        return file;
      }else{
        long fileSize=file.length();
        if(fileSize > 1328427){//1.26MB左右，大概4-5万行左右
          SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
          String fileName=format.format(new Date())+"-"+file.getName();
          File path=new File(file.getParent());
          FileUtils.copyFile(file, new File(path,fileName));
          file.delete();
        }
        return file;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public OutputXlsx(String filePath, String sheetName, int headnum) {
    File file=checkFilePath(filePath);
    try {
      clazz=getClass();
      if(file.exists() && file.isFile()){//文件存在//追加
        isFile=true;
        fileInput= new FileInputStream(file);
//              fileChannel=new RandomAccessFile(file, "rw").getChannel();
        wb=new HSSFWorkbook(fileInput);
        sheet=wb.getSheet(sheetName);
//              fileLock=fileChannel.lock();
      }else{//文件不存在 // 新建
        isFile=false;
        wb=new HSSFWorkbook();
        sheet=wb.createSheet(sheetName);
      }
      this.filePath=filePath;
      this.headnum=headnum;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * 获取最后一个数据行号
   * @return
   */
  public int getLastDataRowNum(){
    return this.sheet.getLastRowNum()+headnum;
  }

  public void setData(Row row, T data){
    int column = 0;
    Object val = null;
    for (Object[] os : annotationList){
      if(os[1] instanceof Field){
        val = Reflections.invokeGetter(data, ((Field)os[1]).getName());
      }else if (os[1] instanceof Method){
        val = Reflections.invokeMethod(data, ((Method)os[1]).getName(), new Object[] {});
      }
      this.addCell(row, column++, val, 1, Class.class);
    }
  }

  public void setListData(int rownum,List<T> data){
    for(T m:data){
      Row row= sheet.createRow(rownum++);
      setData(row, m);
    }
  }

  public abstract void appendList(List<T> listdata);

  /**
   * 添加一个单元格
   * @param row 添加的行
   * @param column 添加列号
   * @param val 添加值
   * @param align 对齐方式（1：靠左；2：居中；3：靠右）
   * @return 单元格对象
   */
  private Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
    Cell cell = row.createCell(column);
    String cellFormatString = "@";
    try {
      if(val == null){
        cell.setCellValue("");
      }else if(fieldType != Class.class){
        // 一些get方法是getValue(String xxx)的形式
        cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
      }else{
        if(val instanceof String) {
          cell.setCellValue((String) val);
        }else if(val instanceof Integer) {
          cell.setCellValue((Integer) val);
          cellFormatString = "0";
        }else if(val instanceof Long) {
          cell.setCellValue((Long) val);
          cellFormatString = "0";
        }else if(val instanceof Double) {
          cell.setCellValue((Double) val);
          cellFormatString = "0.00";
        }else if(val instanceof Float) {
          cell.setCellValue((Float) val);
          cellFormatString = "0.00";
        }else if(val instanceof Date) {
          cell.setCellValue((Date) val);
          cellFormatString = "yyyy-MM-dd HH:mm";
        }else {
          cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
            "fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
        }
      }
      if (val != null){
        CellStyle style = styles.get("data_column_"+column);
        if (style == null){
          style = wb.createCellStyle();
          style.cloneStyleFrom(styles.get("data"+(align>=1&&align<=3?align:"")));
          style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
          styles.put("data_column_" + column, style);
        }
        cell.setCellStyle(style);
      }
    } catch (Exception ex) {
      cell.setCellValue(val.toString());
    }
    return cell;
  }


  public void export(){
    try {
      if(fileInput!=null){
        fileInput.close();
      }
      if(fileLock!=null){
        fileLock.release();
      }
      if(fileChannel!=null){
        fileChannel.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    write();
  }

  public void write(){
    try {
      FileOutputStream outputStream=new FileOutputStream(new File(filePath));
      wb.write(outputStream);
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public abstract void append(T data);
}
