import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by hgg on 2019/5/7.
 */
public class POIExcel {
    public static void getDataFromExcel(String filePath) throws IOException
    {
        //判断是否为excel类型文件
        if(!filePath.endsWith(".xls")&&!filePath.endsWith(".xlsx"))
        {
            System.out.println("文件不是excel类型");
        }
        FileInputStream fis =null;
        Workbook wookbook = null;
        Sheet sheet =null;
        try
        {
            //获取一个绝对地址的流
            fis = new FileInputStream(filePath);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            //2003版本的excel，用.xls结尾
            wookbook = new HSSFWorkbook(fis);//得到工作簿
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            try
            {
                //2007版本的excel，用.xlsx结尾
                fis = new FileInputStream(filePath);
                wookbook = new XSSFWorkbook(fis);//得到工作簿
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Map<String, PictureData> maplist=null;
        sheet = wookbook.getSheetAt(0);
        // 判断用07还是03的方法获取图片
        if (filePath.endsWith(".xls")) {
            maplist = getPictures1((HSSFSheet) sheet);
        } else if(filePath.endsWith(".xlsx")){
            maplist = getPictures2((XSSFSheet) sheet);
        }
        try {
            printImg(maplist);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放map
            if (maplist != null){
                maplist = null;
            }
        }
        //得到一个工作表
        //获得表头
        Row rowHead = sheet.getRow(0);

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        //要获得属性
        String proName="";
        String space="";
        String size="";
        String brand="";
        String unit="";
        Integer num=null;
        Double unitPrice=null;
        Double total=null;
        String material="";
        String remark="";
        String pic="";
        //获得所有数据
        System.out.println("产品名称\t\t空间\t\t规格/尺寸\t\t品牌\t\t单位\t\t数量\t\t单价\t\t金额\t\t材质\t\t备注");
        for(int i = 1 ; i < totalRowNum ; i++)
        {
            //获得第i行对象
            Row row = sheet.getRow(i);

            //空间位置(为空则停止解析)
            Cell cell = row.getCell(0);
            if (cell == null){
                break;
            }
            cell.setCellType(CellType.STRING);
            space =cell.getStringCellValue().toString();
            if (StringUtils.isBlank(space)){
                break;
            }

            //产品名称
            cell = row.getCell(1);
            if (cell != null){
                cell.setCellType(CellType.STRING);
                proName =  cell.getStringCellValue();
            }
            //规格/尺寸
            cell = row.getCell(3);
            if (cell != null){
                cell.setCellType(CellType.STRING);
                size =cell.getStringCellValue()+"";
            }
            //品牌
            cell = row.getCell(4);
            if (cell != null){
                cell.setCellType(CellType.STRING);
                brand =cell.getStringCellValue()+"";
            }
            //单位
            cell = row.getCell(5);
            if (cell != null){
                cell.setCellType(CellType.STRING);
                unit =cell.getStringCellValue()+"";
            }
            //数量
            cell = row.getCell(6);
            if (cell != null){
                num =(int)cell.getNumericCellValue();
            }
            //单价
            cell = row.getCell(7);
            if (cell != null){
                unitPrice =cell.getNumericCellValue();
            }
            //金额
            cell = row.getCell(8);
            if (cell != null){
                total =cell.getNumericCellValue();
            }
            //材质
            cell = row.getCell(9);
            if (cell != null){
                cell.setCellType(CellType.STRING);
                material =cell.getStringCellValue()+"";
            }
            //备注
            cell = row.getCell(10);
            if (cell != null){
                cell.setCellType(CellType.STRING);
                remark =cell.getStringCellValue()+"";
            }
            System.out.println(proName+"\t\t"+space+"\t\t"+size+"\t\t"+brand+"\t\t"+unit+"\t\t"+num+"\t\t"
                    +unitPrice+"\t\t"+total+"\t\t"+material+"\t\t"+remark);
        }
        for (Map.Entry<String, PictureData> entry : maplist.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        //使用完成关闭
        wookbook.close();
        if (fis != null){
            fis.close();
        }
    }
    /**
     * 获取图片和位置 (xls)
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures1 (HSSFSheet sheet) throws IOException {
        Map<String, PictureData> map = new HashMap<String, PictureData>();
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                PictureData pdata = picture.getPictureData();
                String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
                map.put(key, pdata);
            }
        }
        return map;
    }
    /**
     * 获取图片和位置 (xlsx)
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures2 (XSSFSheet sheet) throws IOException {
        Map<String, PictureData> map = new HashMap<String, PictureData>();
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
                    map.put(key, picture.getPictureData());

                    /*FileOutputStream out = new FileOutputStream("D:\\pic" + 1 + ".jpg");
                    out.write(picture.getPictureData().getData());
                    out.close();*/
                }
            }
        }
        return map;
    }
    //图片写出
    public static void printImg(Map<String, PictureData> sheetList) throws Exception {
        Object key[] = sheetList.keySet().toArray();
        String filePath = "";
        for (int i = 0; i < sheetList.size(); i++) {
            // 获取图片流
            PictureData pic = sheetList.get(key[i]);
            // 获取图片索引
            String picName = key[i].toString();
            // 获取图片格式
            String ext = pic.suggestFileExtension();
            byte[] data = pic.getData();
            //文件上传七牛
//            QiNiuUtils.uploadOneObject(data,"111_"+picName + "." + ext);
            //图片保存路径
            filePath = "D:\\img\\pic" + picName + "." + ext;
            System.out.println(filePath);
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(data);
            out.close();
        }
    }
    public static void main(String[] args) throws Exception {
        getDataFromExcel("C:\\Users\\mg\\Desktop"+ File.separator +"test.xlsx");
    }
}