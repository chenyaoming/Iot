package uitl;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <b>ExcleHelper</b> is
 * </p>
 *
 * @author ChenYaoming
 * @version 1.0.0
 * @since 2019/6/3
 */
public class ExcleHelper {

    private static Logger LOG = LoggerFactory.getLogger(ExcleHelper.class);

    public static final String YYYY_MM_DD = "yyyy-MM-dd";


    /**
     * 读取出filePath中的所有数据信息
     * @param sheetNum 第几个sheet

     */
    public static List<List<String>> getDataFromExcel(String absolutePathfile,int sheetNum,int dataStartRow, Map<String, PictureData> pictureDataMap)
    {
        FileInputStream fis =null;
        Workbook wookbook = null;
        Sheet sheet = null;
        try
        {
            fis = new FileInputStream(absolutePathfile);
            //判断是否为excel类型文件
            if(absolutePathfile.endsWith(".xlsx"))
            {
                //2007版本的excel，用.xlsx结尾
                wookbook = new XSSFWorkbook(fis);//得到工作簿
                //得到一个工作表
                sheet = wookbook.getSheetAt(sheetNum);
                getPictures2((XSSFSheet) sheet,pictureDataMap);

            }else if(absolutePathfile.endsWith(".xls")){
                wookbook = new HSSFWorkbook(fis);//得到工作簿
                //得到一个工作表
                sheet = wookbook.getSheetAt(sheetNum);
                getPictures1((HSSFSheet) sheet,pictureDataMap);

            }else{
                throw new RuntimeException("");
            }

        } catch (Exception e){
            LOG.error("读取表格报错",e);
            throw new RuntimeException("读取表格报错");
        }


        //获得表头(跟数据对应的表头)
        Row rowHead = sheet.getRow(dataStartRow);

        //判断表头是否正确
        /*if(rowHead.getPhysicalNumberOfCells() != 3)
        {
            System.out.println("表头的数量不对!");
        }*/

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();

        int celNum = rowHead.getPhysicalNumberOfCells();

        List<List<String>> sheetValues = new ArrayList<>();
        //获得所有数据
        for(int i = dataStartRow+1 ; i <= totalRowNum ; i++)
        {
            List<String> rowValues = new ArrayList<>();
            //获得第i行对象
            Row row = sheet.getRow(i);
            for (int j = 0; j < celNum; j++){
                Cell cell = row.getCell((short)j);
                if(null == cell){
                    //throw new ServiceException("报错啦");
                    rowValues.add(null);
                }else if(HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType() && cell.getCellStyle().getDataFormat() == 14){

                    rowValues.add(new SimpleDateFormat(YYYY_MM_DD).format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())));
                }
                else{
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    rowValues.add(cell.getStringCellValue());
                }
            }
            sheetValues.add(rowValues);
        }
        try {
            wookbook.close();
        } catch (IOException e) {
            LOG.error("关闭表格报错",e);
        }
        return sheetValues;
    }


    /**
     * 读取某个文件夹下的所有文件
     */
    public static boolean readfile(String filepath){
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {

                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                System.out.println(file.getPath());
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath="
                                + readfile.getAbsolutePath());
                        System.out.println("name=" + getFileName(readfile.getName()));

                    } else if (readfile.isDirectory()) {
                        //readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }

    public static String getFileName(String filename){
        int pos = filename.lastIndexOf(".");
        if(pos >=0){
            return filename.substring(0, pos);
        }
        return filename;
    }

    /**
     * 获取图片和位置 (xls)
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures1 (HSSFSheet sheet,Map<String, PictureData> pictureDataMap) {
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                PictureData pdata = picture.getPictureData();
                String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
                pictureDataMap.put(key, pdata);
            }
        }
        return pictureDataMap;
    }

    /**
     * 获取图片和位置 (xlsx)
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures2 (XSSFSheet sheet,Map<String, PictureData> pictureDataMap){
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "-" + marker.getCol();
                    pictureDataMap.put(key, picture.getPictureData());

                    /*FileOutputStream out = new FileOutputStream("D:\\pic" + 1 + ".jpg");
                    out.write(picture.getPictureData().getData());
                    out.close();*/
                }
            }
        }
        return pictureDataMap;
    }

}
