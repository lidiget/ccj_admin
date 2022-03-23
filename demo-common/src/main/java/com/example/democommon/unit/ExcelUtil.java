package com.example.democommon.unit;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExcelUtil {
    /*private static NumberFormat format              = NumberFormat.getInstance();
    *//** 日志 *//*
    private static final Logger LOGGER              = Logger.getLogger(String.valueOf(ExcelUtil.class));
    *//** 列默认宽度 *//*
    private static final int    DEFAUL_COLUMN_WIDTH = 4000;

    *//**
     * 1.创建 workbook
     *
     * @return {@link HSSFWorkbook}
     *//*
    private HSSFWorkbook getHSSFWorkbook() {
        LOGGER.info("【创建 workbook】");
        return new HSSFWorkbook();
    }

    *//**
     * 2.创建 sheet
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param sheetName sheet 名称
     * @return {@link HSSFSheet}
     *//*
    private HSSFSheet getHSSFSheet(HSSFWorkbook hssfWorkbook, String sheetName) {
        LOGGER.info("【创建 sheet】sheetName ： " + sheetName);
        return hssfWorkbook.createSheet(sheetName);
    }

    *//**
     * 3.写入表头信息
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet {@link HSSFSheet}
     * @param headers 列标题，数组形式，
     *   			   如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     * 				   其中参数@columnWidth可选，columnWidth为整型数值
     * @param title 标题
     *//*
    private void writeHeader(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, String title) {
        LOGGER.info("【写入表头信息】");

        // 头信息处理
        String[] newHeaders = headersHandler(headers);

        // 初始化标题和表头单元格样式
        HSSFCellStyle titleCellStyle = createTitleCellStyle(hssfWorkbook);
        // 标题栏
        HSSFRow titleRow = hssfSheet.createRow(0);
        titleRow.setHeight((short) 500);
        HSSFCell titleCell = titleRow.createCell(0);
        // 设置单元格样式
        titleCell.setCellStyle(titleCellStyle);

        // 处理单元格合并，四个参数分别是：起始行，终止行，起始行，终止列
        hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0, (short) (newHeaders.length - 1)));

        // 设置合并后的单元格的样式
        titleRow.createCell(newHeaders.length - 1).setCellStyle(titleCellStyle);

        // 表头
        HSSFRow headRow = hssfSheet.createRow(1);
        headRow.setHeight((short) 500);
        HSSFCell headCell = null;
        String[] headInfo = null;
        // 处理excel表头
        for (int i = 0, len = newHeaders.length; i <len; i++) {
            headInfo = newHeaders[i].split("@");
            headCell = headRow.createCell(i);
            headCell.setCellValue(headInfo[0]);
            headCell.setCellStyle(titleCellStyle);
            // 设置列宽度
            setColumnWidth(i, headInfo, hssfSheet);
        }
    }

    *//**
     * 头信息校验和处理
     * @param headers
     *//*
    private String[] headersHandler(String[] headers) {
        List<String> newHeaders = new ArrayList<String>();
        for (String string : headers) {
            if (StringUtils.isNotBlank(string)) {
                newHeaders.add(string);
            }
        }
        int size = newHeaders.size();

        return newHeaders.toArray(new String[size]);
    }

    *//**
     * 4.写入内容部分
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet {@link HSSFSheet}
     * @param headers 列标题，数组形式，
     * 				   如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     * 				   其中参数@columnWidth可选，columnWidth为整型数值
     * @param dataList 要导出的数据集合
     * @throws Exception
     *//*
    private void writeContent(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, List<Map<Object,Object>> dataList)
            throws Exception {
        LOGGER.info("【写入Excel内容部分】");
        // 2015-8-13 增加，当没有数据的时候，把原来抛异常的方式修改成返回一个只有头信息，没有数据的空Excel
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        HSSFRow row = null;
        HSSFCell cell = null;
        // 单元格的值
        Object cellValue = null;
        // 数据写入行索引
        int rownum = 2;
        // 单元格样式
        HSSFCellStyle cellStyle = createContentCellStyle(hssfWorkbook);
        // 遍历集合，处理数据
        for (int j = 0, size = dataList.size(); j < size; j++) {
            row = hssfSheet.createRow(rownum);
            for (int i = 0, len = headers.length; i < len; i++) {
                cell = row.createCell(i);
                //cellValue = ReflectUtils.getCellValue(dataList.get(j), headers[i].split("@")[1]);
                Map<Object,Object> map1 = new HashMap<>();
                map1.put(headers[i],dataList.get(j).get("shop_goods_id"));
                map1.put(headers[i],dataList.get(j).get("shop_goods_picture"));
                map1.put(headers[i],dataList.get(j).get("shop_goods_name"));
                map1.put(headers[i],dataList.get(j).get("shop_goods_market"));
                map1.put(headers[i],dataList.get(j).get("shop_specification_price"));
                List<Object> list = new ArrayList<>();
                list.add(dataList.get(j).get("shop_goods_id"));
                list.add(dataList.get(j).get("shop_goods_picture"));
                list.add(dataList.get(j).get("shop_goods_name"));
                list.add(dataList.get(j).get("shop_goods_market"));
                list.add(dataList.get(j).get("shop_specification_price"));
                cellValue = list;
                cellValueHandler(cell, cellValue);
                cell.setCellStyle(cellStyle);
            }
            rownum++;
        }
    }

    *//**
     * 设置列宽度
     * @param i 列的索引号
     * @param headInfo 表头信息，其中包含了用户需要设置的列宽
     *//*
    private void setColumnWidth(int i, String[] headInfo, HSSFSheet hssfSheet) {
        if (headInfo.length < 3) {
            // 用户没有设置列宽，使用默认宽度
            hssfSheet.setColumnWidth(i, DEFAUL_COLUMN_WIDTH);
            return;
        }
        if (StringUtils.isBlank(headInfo[2])) {
            // 使用默认宽度
            hssfSheet.setColumnWidth(i, DEFAUL_COLUMN_WIDTH);
            return;
        }
        // 使用用户设置的列宽进行设置
        hssfSheet.setColumnWidth(i, Integer.parseInt(headInfo[2]));
    }

    *//**
     * 单元格写值处理器
     * @param {{@link HSSFCell}
     * @param cellValue 单元格值
     *//*
    private void cellValueHandler(HSSFCell cell, Object cellValue) {
        // 2015-8-13 修改，判断cellValue是否为空，否则在cellValue.toString()会出现空指针异常
        if (cellValue == null) {
            cell.setCellValue("");
            return;
        }
        if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        } else if (cellValue instanceof Calendar) {
            cell.setCellValue((Calendar) cellValue);
        } else if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
        } else if (cellValue instanceof Integer || cellValue instanceof Long || cellValue instanceof Short
                || cellValue instanceof Float) {
            cell.setCellValue((Double.parseDouble(cellValue.toString())));
        } else if (cellValue instanceof HSSFRichTextString) {
            cell.setCellValue((HSSFRichTextString) cellValue);
        }
        cell.setCellValue(cellValue.toString());
    }

    *//**
     * 创建标题和表头单元格样式
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @return {@link HSSFCellStyle}
     *//*
    private HSSFCellStyle createTitleCellStyle(HSSFWorkbook hssfWorkbook) {
        LOGGER.info("【创建标题和表头单元格样式】");
        // 单元格的样式
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        // 设置字体样式，改为不变粗
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 13);
        cellStyle.setFont(font);
        // 单元格垂直居中
        // 设置通用的单元格属性
        setCommonCellStyle(cellStyle);
        return cellStyle;
    }

    *//**
     * 创建内容单元格样式
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @return {@link HSSFCellStyle}
     *//*
    private HSSFCellStyle createContentCellStyle(HSSFWorkbook hssfWorkbook) {
        LOGGER.info("【创建内容单元格样式】");
        // 单元格的样式
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        // 设置字体样式，改为不变粗
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);
        // 设置单元格自动换行
        cellStyle.setWrapText(true);
        // 单元格垂直居中
        //水平居中
        //        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置通用的单元格属性
        setCommonCellStyle(cellStyle);
        return cellStyle;
    }

    *//**
     * 设置通用的单元格属性
     * @param cellStyle 要设置属性的单元格
     *//*
    private void setCommonCellStyle(HSSFCellStyle cellStyle) {

    }

    *//**
     * 将生成的Excel输出到指定目录
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param filePath 文件输出目录，包括文件名（.xls）
     *//*
    private void write2FilePath(HSSFWorkbook hssfWorkbook, String filePath) {
        LOGGER.info("【将生成的Excel输出到指定目录】filePath ：" + filePath);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOut);
        } catch (Exception e) {
            throw new RuntimeException("将生成的Excel输出到指定目录失败");
        } finally {
        }
    }

    *//**
     * 生成Excel，存放到指定目录
     * @param sheetName sheet名称
     * @param title 标题
     * @param filePath 要导出的Excel存放的文件路径
     * @param headers 列标题，数组形式，
     * 				   如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     * 				   其中参数@columnWidth可选，columnWidth为整型数值
     * @param dataList 要导出数据的集合
     * @throws Exception
     *//*
    public static void createExcel2FilePath(String sheetName, String title, String filePath, String[] headers,
                                            List<Map<Object,Object>> dataList) throws Exception {
        if (ArrayUtils.isEmpty(headers)) {
            throw new RuntimeException("表头不能为空");
        }
        ExcelUtil poiExcelUtil = new ExcelUtil();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写入 head
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, title);
        // 4.写入内容
        poiExcelUtil.writeContent(hssfWorkbook, hssfSheet, headers, dataList);
        // 5.保存文件到filePath中
        poiExcelUtil.write2FilePath(hssfWorkbook, filePath);
    }

    *//**
     * 生成Excel的WorkBook，用于导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param headers 列标题，数组形式，
     * 				   如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     * 				   其中参数@columnWidth可选，columnWidth为整型数值
     * @param dataList 要导出数据的集合
     * @throws Exception
     *//*
    public static HSSFWorkbook createExcel2Export(String sheetName, String title, String[] headers, List<Map<Object,Object>> dataList)
            throws Exception {
        if (ArrayUtils.isEmpty(headers)) {
            throw new RuntimeException("表头不能为空");
        }
        ExcelUtil poiExcelUtil = new ExcelUtil();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写入 head
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, title);
        // 4.写入内容
        poiExcelUtil.writeContent(hssfWorkbook, hssfSheet, headers, dataList);

        return hssfWorkbook;
    }

    *//**
     * 创建知识库TOP3系统渠道统计的Excel数据
     * @param sheetName sheet名称
     * @param title 标题
     * @param headers 列标题，数组形式，
     *                 如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     *                 其中参数@columnWidth可选，columnWidth为整型数值
     * @param dataList 要导出数据的集合
     *//*
    public static HSSFWorkbook createknowledgeTop3Excel(String sheetName, String title, String[] headers,
                                                        List<List<?>> dataList) {
        if (ArrayUtils.isEmpty(headers)) {
            throw new RuntimeException("表头不能为空");
        }
        ExcelUtil poiExcelUtil = new ExcelUtil();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写入 head
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, title);
        // 4.写入内容
        try {
            poiExcelUtil.writeComplexContent(hssfWorkbook, hssfSheet, headers, dataList);
        } catch (Exception e) {
            throw new RuntimeException("写入内容部分失败");
        }
        return hssfWorkbook;
    }

    *//**
     * 生成内容部分(复杂内容，每列来自不同的数据集合)
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet {@link HSSFSheet}
     * @param headers 列标题，数组形式，
     *                 如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     *                 其中参数@columnWidth可选，columnWidth为整型数值
     * @param dataList 要导出数据的集合
     * @throws Exception
     *//*
    private void writeComplexContent(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers,
                                     List<List<?>> dataList) throws Exception {
        LOGGER.info("【写入Excel内容部分】");
        // 2015-8-13 增加，当没有数据的时候，把原来抛异常的方式修改成返回一个只有头信息，没有数据的空Excel
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        HSSFRow row = null;
        HSSFCell cell = null;
        // 单元格的值
        Object cellValue = null;
        // 数据写入行索引
        int rownum = 2;
        // 单元格样式
        HSSFCellStyle cellStyle = createContentCellStyle(hssfWorkbook);
        // 遍历集合，处理数据

        // 要写的内容的行数
        int rows = dataList.get(0).size();
        // 除去固定列“日期”外，还需要写的列数
        int columns = dataList.size();

        *//**
         * 思路说明
         * 如果是top3，那么dataList的长度应该是3（即除去固定列“日期”剩下的列数）
         * 其次索引依次是top1的数据、top2的数据、top3的数据
         * top1/top2/top3的数据长度表示行数
         * 第一层循环，肯定以行数为基准来进行
         * 然后第一层循环的第一步是完成固定列的数据填充
         * 第二步是填充对应行每个列的数据，而每个列的数据应该分别从top1、top2、top3中取
         *//*
        for (int j = 0; j < rows; j++) {
            row = hssfSheet.createRow(rownum);
            // 写固定列“日期”
            cell = row.createCell(0);
            //cellValue = ReflectUtils.getCellValue(dataList.get(0).get(j), headers[0].split("@")[1]);
            cellValueHandler(cell, cellValue);
            cell.setCellStyle(cellStyle);

            for (int i = 1; i <= columns; i++) {
                cell = row.createCell(i);
                //cellValue = ReflectUtils.getCellValue(dataList.get(i - 1).get(j), headers[i].split("@")[1]);
                cellValueHandler(cell, cellValue);
                cell.setCellStyle(cellStyle);
            }
            rownum++;
        }
    }

    *//**
     * 根据文件路径读取excel文件
     * @param excelPath excel的路径
     * @param skipRows 需要跳过的行数
     * @return List&lt;String[]&gt; 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java bean
     * @throws Exception
     *//*
    public static List<String[]> readExcel(String excelPath, int skipRows, int columCount) throws Exception {
        LOGGER.info("【读取Excel】excelPath : " + excelPath + " , skipRows : " + skipRows);
        FileInputStream is = new FileInputStream(new File(excelPath));
        POIFSFileSystem fs = new POIFSFileSystem(is);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        List<String[]> list = new ArrayList<String[]>();
        HSSFSheet sheet = wb.getSheetAt(0);
        // 得到总共的行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        try {
            for (int i = skipRows; i < rowNum; i++) {
                String[] vals = new String[columCount];
                HSSFRow row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }
                for (int j = 0; j < columCount; j++) {
                    HSSFCell cell = row.getCell(j);
                    String val = getStringCellValue(cell);
                    vals[j] = val;
                }
                list.add(vals);
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败");
        } finally {
            wb.close();
        }
        return list;
    }

    *//**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     *//*
    private static String getStringCellValue(HSSFCell cell) {
        if (cell == null)
            return "";
        String strCell = "";
        switch (cell.getCellType()) {
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    *//**
     * 生成车票销售总量统计表格
     * @param sheetName sheet名称
     * @param title 标题
     * @param headers 列标题，数组形式，
     *                 如{"列标题1@beanFieldName1@columnWidth","列标题2@beanFieldName2@columnWidth","列标题3@beanFieldName3@columnWidth"}
     *                 其中参数@columnWidth可选，columnWidth为整型数值
     * @param dataList 要导出数据的集合
     * @return {@link HSSFWorkbook}
     * @Author : luolin. create at 2015年12月22日 下午5:17:04
     *//*
    public static HSSFWorkbook createTicketReportExcel(String sheetName, String title, String[] headers,
                                                       List<List<?>> dataList) {
        if (ArrayUtils.isEmpty(headers)) {
            throw new RuntimeException("表头不能为空");
        }
        ExcelUtil poiExcelUtil = new ExcelUtil();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写入 head
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, title);
        // 4.写入内容
        try {
            poiExcelUtil.writeComplexContent(hssfWorkbook, hssfSheet, headers, dataList);
        } catch (Exception e) {
            throw new RuntimeException("写入内容部分失败");
        }
        return hssfWorkbook;
    }

    public void exportExcel(HSSFWorkbook workbook,HSSFSheet sheet,String file,List<Map<Object,Object>> list) throws IOException, ServletException {
    *//**
     * 创建一个Excel文件
     *//*
        System.out.println("进来了");
    *//**
     * 创建一个sheet
     *//*
    *//**
     * 在sheet表中添加0行
     *//*
    *//**
     * 创建单元格，设置表头
     *//*
    *//**
     *  把list集合里面的数据写入工作表
     *//*
        for (int i = 0; i < list.size(); i++) {
        *//**
         * 创建行
         *//*
        HSSFRow row1 = sheet.createRow(i + 1);
        Map<Object,Object> doctor = list.get(i);
        *//**
         *  为单元格写入值，从0开始
         *//*
        row1.createCell(0).setCellValue(doctor.get("shop_goods_id").toString());
        row1.createCell(1).setCellValue(doctor.get("shop_goods_picture").toString());
        row1.createCell(2).setCellValue(doctor.get("shop_goods_name").toString());
        row1.createCell(3).setCellValue(doctor.get("shop_goods_market").toString());
        row1.createCell(4).setCellValue(doctor.get("shop_specification_price").toString());
    }
    OutputStream out = null;
    *//**
     * 将文件保存到指定的位置
     *//*
    try {
        out = new FileOutputStream(file);
        workbook.write(out);
        workbook.close();
        out.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    //request.getRequestDispatcher("/success.jsp").forward(request, response)
}

    public void exportExcels(HSSFWorkbook workbook,HSSFSheet sheet,String file,List<Map<Object,Object>> list,int a) throws IOException, ServletException {
        *//**
         * 创建一个Excel文件
         *//*
        System.out.println("进来了");
        *//**
         * 创建一个sheet
         *//*
        *//**
         * 在sheet表中添加0行
         *//*
        *//**
         * 创建单元格，设置表头
         *//*
        *//**
         *  把list集合里面的数据写入工作表
         *//*
        for (int i = 0; i < list.size(); i++) {
            *//**
             * 创建行
             *//*
            HSSFRow row1 = sheet.createRow(a+i + 1);
            Map<Object,Object> doctor = list.get(i);
            *//**
             *  为单元格写入值，从0开始
             *//*
            row1.createCell(0).setCellValue(doctor.get("shop_goods_id").toString());
            row1.createCell(1).setCellValue(doctor.get("shop_goods_picture").toString());
            row1.createCell(2).setCellValue(doctor.get("shop_goods_name").toString());
            row1.createCell(3).setCellValue(doctor.get("shop_goods_market").toString());
            row1.createCell(4).setCellValue(doctor.get("shop_specification_price").toString());
        }
        OutputStream out = null;
        *//**
         * 将文件保存到指定的位置
         *//*
        try {
            out = new FileOutputStream(file);
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //request.getRequestDispatcher("/success.jsp").forward(request, response)
    }

   // 创建一个sheet
public void sheet(HSSFSheet sheet){
    HSSFRow row = sheet.createRow(0);
    HSSFCell cell = row.createCell(0);
    cell.setCellValue("商品编号");
    cell = row.createCell(1);
    cell.setCellValue("商品图片");
    cell = row.createCell(2);
    cell.setCellValue("商品名");

    cell = row.createCell(3);
    cell.setCellValue("商品销量");

    cell = row.createCell(4);
    cell.setCellValue("商品价格");
}

    *//**
     *
     * @param stuList 从数据库中查询需要导入excel文件的信息列表
     * @return 返回生成的excel文件的路径
     * @throws Exception
     *//*
    public static String stuList2Excel(List<?> stuList) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd hhmmss");
        Workbook wb = new HSSFWorkbook();
        //标题行抽出字段
        String[] title = {"序号","学号", "姓名", "性别", "入学时间", "住址", "手机号", "其他信息"};
        //设置sheet名称，并创建新的sheet对象
        String sheetName = "学生信息一览";
        Sheet stuSheet = wb.createSheet(sheetName);
        //获取表头行
        Row titleRow = stuSheet.createRow(0);
        //创建单元格，设置style居中，字体，单元格大小等
        CellStyle style = wb.createCellStyle();
        Cell cell = null;
        //把已经写好的标题行写入excel文件中
        for (int i = 0; i < title.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //把从数据库中取得的数据一一写入excel文件中
        Row row = null;
        for (int i = 0; i < stuList.size(); i++) {
            //创建list.size()行数据
            row = stuSheet.createRow(i + 1);
            //把值一一写进单元格里
            //设置第一列为自动递增的序号
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(stuList.get(i).getStuId());
            row.createCell(2).setCellValue(stuList.get(i).getStuName());
            row.createCell(3).setCellValue(stuList.get(i).getGender());
            //把时间转换为指定格式的字符串再写入excel文件中
            if (stuList.get(i).getEnterTime() != null) {
                row.createCell(4).setCellValue(sdf.format(stuList.get(i).getEnterTime()));
            }
            row.createCell(5).setCellValue(stuList.get(i).getAddress());
            row.createCell(6).setCellValue(stuList.get(i).getPhone());
            row.createCell(7).setCellValue(stuList.get(i).getOtherInfo());

        }
        //设置单元格宽度自适应，在此基础上把宽度调至1.5倍
        for (int i = 0; i < title.length; i++) {
            stuSheet.autoSizeColumn(i, true);
            stuSheet.setColumnWidth(i, stuSheet.getColumnWidth(i) * 15 / 10);
        }
        //获取配置文件中保存对应excel文件的路径，本地也可以直接写成F：excel/stuInfoExcel路径
        String folderPath = ResourceBundle.getBundle("systemconfig").getString("downloadFolder") + File.separator + "stuInfoExcel";

        //创建上传文件目录
        File folder = new File(folderPath);
        //如果文件夹不存在创建对应的文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //设置文件名
        String fileName = sdf1.format(new Date()) + sheetName + ".xlsx";
        String savePath = folderPath + File.separator + fileName;
        // System.out.println(savePath);
        OutputStream fileOut = new FileOutputStream(savePath);
        wb.write(fileOut);
        fileOut.close();
        //返回文件保存全路径
        return savePath;
    }


*/
}
