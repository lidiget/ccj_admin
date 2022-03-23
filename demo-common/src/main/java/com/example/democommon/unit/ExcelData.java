package com.example.democommon.unit;

import java.util.List;
import java.util.Map;

public class ExcelData {
    private String                excelName;    // Excel文件名称
    private String                sheetName;    // 工作表的名称
    private String                heads;        // 标题行
    private List<String> data;         // 主数据
    private Map<Integer, Integer> widthAndHeiht;// 单元格的宽高,如:xSheet.setColumnWidth(10,1000);
    // 10表示:map的key, //
    // 10,1000表示:map的value
    private List<List<String>>    assoceData;   // 关联数据行,一张主表对应一个从表，当有多个明细表的时候，那么就全部拼接到这个从表中即可。
    private int                   length;       // 合并单元格的个数 没有就不用填



    public String getHeads() {
        return heads;
    }



    public void setHeads(String heads) {
        this.heads = heads;
    }



    public int getLength() {
        return length;
    }



    public void setLength(int length) {
        this.length = length;
    }



    public List<List<String>> getAssoceData() {
        return assoceData;
    }



    public void setAssoceData(List<List<String>> assoceData) {
        this.assoceData = assoceData;
    }



    public String getExcelName() {
        return excelName;
    }



    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }



    public String getSheetName() {
        return sheetName;
    }



    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<String> getData() {
        return data;
    }
    public void setData(List<String> data) {
        this.data = data;
    }
    public Map<Integer, Integer> getWidthAndHeiht() {
        return widthAndHeiht;
    }
    public void setWidthAndHeiht(Map<Integer, Integer> widthAndHeiht) {
        this.widthAndHeiht = widthAndHeiht;
    }
}
