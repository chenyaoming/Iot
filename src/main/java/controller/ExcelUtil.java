package controller;

import bean.TbDevice;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import table.device.DeviceTable;
import uitl.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：Excel写操作帮助类
 *
 *
 * */
public class ExcelUtil {

    private static Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 功能：创建HSSFSheet工作簿
     * @param     wb    HSSFWorkbook
     * @param     sheetName    String
     * @return    HSSFSheet
     */
    public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName){
        HSSFSheet sheet=wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(12);
        sheet.setGridsPrinted(false);
        sheet.setDisplayGridlines(false);
        return sheet;
    }




    /**
     * 功能：创建HSSFRow
     * @param     sheet    HSSFSheet
     * @param     rowNum    int
     * @param     height    int
     * @return    HSSFRow
     */
    public static HSSFRow createRow(HSSFSheet sheet, int rowNum, int height){
        HSSFRow row=sheet.createRow(rowNum);
        row.setHeight((short)height);
        return row;
    }



    public static HSSFCell createCell0(HSSFRow row, int cellNum){
        HSSFCell cell=row.createCell(cellNum);
        return cell;
    }


    /**
     * 功能：创建CELL
     * @param     row        HSSFRow
     * @param     cellNum    int
     * @param     style    HSSFStyle
     * @return    HSSFCell
     */
    public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style){
        HSSFCell cell=row.createCell(cellNum);
        cell.setCellStyle(style);
        return cell;
    }



    /**
     * 功能：创建CellStyle样式
     * @param     wb                HSSFWorkbook
     * @param     backgroundColor    背景色
     * @param     foregroundColor    前置色
     * @param    font            字体
     * @return    CellStyle
     */
    public static CellStyle createCellStyle(HSSFWorkbook wb,short backgroundColor,short foregroundColor,short halign,Font font){
        CellStyle cs=wb.createCellStyle();
        cs.setAlignment(halign);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setFillBackgroundColor(backgroundColor);
        cs.setFillForegroundColor(foregroundColor);
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setFont(font);
        return cs;
    }


    /**
     * 功能：创建带边框的CellStyle样式
     * @param     wb                HSSFWorkbook
     * @param     backgroundColor    背景色
     * @param     foregroundColor    前置色
     * @param    font            字体
     * @return    CellStyle
     */
    public static CellStyle createBorderCellStyle(HSSFWorkbook wb,short backgroundColor,short foregroundColor,short halign,Font font){
        CellStyle cs=wb.createCellStyle();
        cs.setAlignment(halign);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setFillBackgroundColor(backgroundColor);
        cs.setFillForegroundColor(foregroundColor);
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setFont(font);
        cs.setBorderLeft(CellStyle.BORDER_DASHED);
        cs.setBorderRight(CellStyle.BORDER_DASHED);
        cs.setBorderTop(CellStyle.BORDER_DASHED);
        cs.setBorderBottom(CellStyle.BORDER_DASHED);
        return cs;
    }





    /**
     * 功能：多行多列导入到Excel并且设置标题栏格式
     */
    public static void writeArrayToExcel(HSSFSheet sheet,int rows,int cells,Object [][]value){

        Row row[]=new HSSFRow[rows];
        Cell cell[]=new HSSFCell[cells];

        for(int i=0;i<row.length;i++){
            row[i]=sheet.createRow(i);


            for(int j=0;j<cell.length;j++){
                cell[j]=row[i].createCell(j);
                cell[j].setCellValue(convertString(value[i][j]));

            }

        }
    }



    /**
     * 功能：多行多列导入到Excel并且设置标题栏格式
     */
    public static void writeArrayToExcel(HSSFWorkbook wb,HSSFSheet sheet,int rows,int cells,Object [][]value){

        Row row[]=new HSSFRow[rows];
        Cell cell[]=new HSSFCell[cells];


        HSSFCellStyle ztStyle =  (HSSFCellStyle)wb.createCellStyle();

        Font ztFont = wb.createFont();
        ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //ztFont.setItalic(true);                     // 设置字体为斜体字
        // ztFont.setColor(Font.COLOR_RED);            // 将字体设置为“红色”
        ztFont.setFontHeightInPoints((short)10);    // 将字体大小设置为18px
        ztFont.setFontName("华文行楷");             // 将“华文行楷”字体应用到当前单元格上
        // ztFont.setUnderline(Font.U_DOUBLE);
        ztStyle.setFont(ztFont);

        for(int i=0;i<row.length;i++){
            row[i]=sheet.createRow(i);


            for(int j=0;j<cell.length;j++){
                cell[j]=row[i].createCell(j);
                cell[j].setCellValue(convertString(value[i][j]));

                if(i==0){
                    cell[j].setCellStyle(ztStyle);
                }

            }

        }
    }



    /**
     * 功能：合并单元格
     * @param     sheet        HSSFSheet
     * @param     firstRow    int
     * @param     lastRow        int
     * @param     firstColumn    int
     * @param     lastColumn    int
     * @return    int            合并区域号码
     */
    public static int mergeCell(HSSFSheet sheet,int firstRow,int lastRow,int firstColumn,int lastColumn){
        return sheet.addMergedRegion(new CellRangeAddress(firstRow,lastRow,firstColumn,lastColumn));
    }



    /**
     * 功能：创建字体
     * @param     wb            HSSFWorkbook
     * @param     boldweight    short
     * @param     color        short
     * @return    Font
     */
    public static Font createFont(HSSFWorkbook wb,short boldweight,short color,short size){
        Font font=wb.createFont();
        font.setBoldweight(boldweight);
        font.setColor(color);
        font.setFontHeightInPoints(size);
        return font;
    }


    /**
     * 设置合并单元格的边框样式
     * @param    sheet    HSSFSheet
     * @param     ca        CellRangAddress
     * @param     style    CellStyle
     */
    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress ca,CellStyle style) {
        for (int i = ca.getFirstRow(); i <= ca.getLastRow(); i++) {
            HSSFRow row = HSSFCellUtil.getRow(i, sheet);
            for (int j = ca.getFirstColumn(); j <= ca.getLastColumn(); j++) {
                HSSFCell cell = HSSFCellUtil.getCell(row, j);
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 功能：将HSSFWorkbook写入Excel文件
     * @param     wb        HSSFWorkbook
     * @param     fileName    写入文件的相对路径
     * @param     fileName    文件名
     */
    public static void writeWorkbook(XSSFWorkbook wb,String fileName){
        FileOutputStream fos=null;
        File f =new File(fileName);

        String name = fileName.substring(0,fileName.lastIndexOf("."));
        String sufix = fileName.substring(fileName.lastIndexOf("."));

        int i = 1;
        while (f.exists()&& i < 1000){
            fileName = name+"（"+i+"）"+sufix;
            f = new File(fileName);
            i++;
        }

        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                LOG.error("创建文件错误：",e);
            }
        }
        try {
            fos=new FileOutputStream(f);
            wb.write(fos);
            int dialog = JOptionPane.showConfirmDialog(null,
                    f.getName()+"导出成功！是否打开？",
                    "温馨提示", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.YES_OPTION) {

                Runtime.getRuntime().exec("cmd /c start \"\" \"" + fileName + "\"");
            }


        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "导入数据前请关闭工作表");

        } catch ( Exception e) {
            JOptionPane.showMessageDialog(null, "没有进行筛选");

        } finally{
            try {
                if(fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }



    public static String convertString(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    public static XSSFWorkbook getWorkBook(List<TbDevice> deviceList, String filePath) {
        //DeviceExcelColum colum = new DeviceExcelColum(true);

        String[] alias = { "序号", "名称", "型号", "管理编码","库存数量",
                "存放位置", "图片", "功用"};


        //String[] keys = colum.colums;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        //sheet.setDefaultColumnWidth(100*256);
        //sheet.setDefaultRowHeight((short) 2000);

        workbook.setSheetName(0, "设备表格");
        XSSFRow row = sheet.createRow(0);

       // sheet.setColumnWidth(0, 2048);

        XSSFCell cell;
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 居中
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 加粗
        cellStyle.setFont(font);
        //创建标题
        for (int i = 0; i < alias.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(alias[i]);
            cell.setCellStyle(cellStyle);
        }

        cellStyle = workbook.createCellStyle();
        // 居中
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 写入各条记录,每条记录对应excel表中的一行
        for (int i = 0; i < deviceList.size(); i++) {
            //数据行从1开始，标题行是1
            row = sheet.createRow(i+1);
            TbDevice device = deviceList.get(i);
            //设置列宽和行高
            sheet.setColumnWidth(i, (short)4000);
            row.setHeight((short)1000);

            setCellValue(row.createCell(0), cellStyle, i+1);
            setCellValue(row.createCell(1), cellStyle, device.getName());
            setCellValue(row.createCell(2), cellStyle, device.getTypeNum());
            setCellValue(row.createCell(3), cellStyle, device.getCode());
            setCellValue(row.createCell(4), cellStyle, device.getCount());

            setCellValue(row.createCell(5), cellStyle, device.getSavePosition());
            cell = row.createCell(6);

            //sheet.addMergedRegion(new CellRangeAddress(i + 1,i + 2,5,6)) ;
            if(StringUtils.isNotBlank(device.getImage())){
                String suffix = device.getImage().trim().substring(device.getImage().trim().lastIndexOf(".")+1);

                if(!ImageUtil.PIC_SUFIX_LIST.contains(suffix)){
                    LOG.error("不支持的图片格式："+device.getImage());
                    continue;
                }
                // 头像
                File photoFile = new File(device.getImage().trim()) ;
                if (photoFile.exists()){
                    try {
                        BufferedImage bufferedImage = ImageIO.read(photoFile) ;
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

                        ImageIO.write(bufferedImage, suffix, byteArrayOut);
                        byte[] data = byteArrayOut.toByteArray();
                        XSSFDrawing drawingPatriarch = sheet.createDrawingPatriarch();
                        //图片位置的调整可设置XSSFClientAnchor的前四个参数：分别是图片距离单元格left，top，right，bottom的像素距离
                        //单位最小为10000

                        XSSFClientAnchor anchor = new XSSFClientAnchor(10000*10, 10000*10, 10000*110, 10000*55, (short)(DeviceTable.IMAGE_COLUM - 1), i + 1, (short) (DeviceTable.IMAGE_COLUM - 1), i + 1);
                        //todo限制图片格式
                        drawingPatriarch.createPicture(anchor, workbook.addPicture(data, XSSFWorkbook.PICTURE_TYPE_JPEG));

                    } catch (IOException e) {
                        LOG.error("导出表格时，图片处理报错：",e);
                    }

                } else {
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue("");
                }
            }
            setCellValue(row.createCell(7), cellStyle, device.getFeatures());

        }
        /*// 设置列宽
        for (int i = 1; i < alias.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // 处理中文不能自动调整列宽的问题
        setSizeColumn(sheet, alias.length);*/


        return workbook;
    }

    private static void setCellValue(XSSFCell cell, XSSFCellStyle cellStyle, Object value) {
        cell.setCellType(XSSFCell.CELL_TYPE_STRING);

        cell.setCellValue(value == null ? "" : value.toString());
        cell.setCellStyle(cellStyle);
    }

    // 自适应宽度(中文支持)
    private static void setSizeColumn(XSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                XSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(columnNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            columnWidth = columnWidth * 256 ;
            sheet.setColumnWidth(columnNum, columnWidth >= 65280 ? 6000 : columnWidth);
        }
    }




}