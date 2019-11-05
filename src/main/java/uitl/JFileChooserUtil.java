package uitl;


import bean.DeviceExcelColum;
import bean.TbDevice;
import jodd.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

public class JFileChooserUtil {

    private static final String UPLOAD_PATH;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("file");

        UPLOAD_PATH = bundle.getString("uploadPath");
    }

    public static File getSelectedOpenFile(final String[] types, Container container) {
        String name = container.getName();

        JFileChooser pathChooser = new JFileChooser();
         pathChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    boolean flag = false;
                    for (String type: types){
                        if (f.getName().toLowerCase().endsWith(type)) {
                            flag = true;
                        }
                    }
                    return flag;
                }
            }

            @Override
            public String getDescription() {
                return "所有文件(*.*)";
            }
        });
        //pathChooser.setSelectedFile(new File(name + type));
        int showSaveDialog = pathChooser.showOpenDialog(container);
        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            return pathChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static File getSelectedFile(final String type, Container container) {
        String name = container.getName();

        JFileChooser pathChooser = new JFileChooser();
        pathChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    if (f.getName().toLowerCase().endsWith(type)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public String getDescription() {
                return "文件格式（" + type + "）";
            }
        });
        pathChooser.setSelectedFile(new File(name + type));
        int showSaveDialog = pathChooser.showSaveDialog(container);
        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            return pathChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * 保存图像
     *
     * @param imgFile
     * @return
     */
    public static String writeImgToUpload(File imgFile) {
        // Tomcat 放在C盘中，可能无读写权限而写入失败
        // 写入目录文件
        // 获取文件格式
        String suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf("."));

        UUID randomUUID = UUID.randomUUID();

        // 目标文件路径+文件名

        String fileName =  randomUUID.toString() + suffix;
        File toFile = new File(UPLOAD_PATH);
        if (!toFile.getParentFile().exists()) {
            // when file is not existed, will create.
            toFile.mkdirs();
        }
        // write to target file.
        try {
            FileUtil.copyFile(imgFile,new File(UPLOAD_PATH+fileName));

            //FileOutputStream fops = new FileOutputStream(new File(desPathName));
            /*fops.write(imgFile.to);
            fops.close();*/

            return UPLOAD_PATH+fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图像
     *
     * @return
     */
    public static String writeImgToUpload(PictureData pictureData) {
        // Tomcat 放在C盘中，可能无读写权限而写入失败
        // 写入目录文件
        // 获取文件格式
        String suffix = "."+pictureData.suggestFileExtension();

        UUID randomUUID = UUID.randomUUID();

        // 目标文件路径+文件名

        String fileName =  randomUUID.toString() + suffix;
        File toFile = new File(UPLOAD_PATH);
        if (!toFile.getParentFile().exists()) {
            // when file is not existed, will create.
            toFile.mkdirs();
        }

        BufferedOutputStream  bos = null;
        // write to target file.
        try {


            FileOutputStream fos = new FileOutputStream(new File(UPLOAD_PATH+fileName));
            bos = new BufferedOutputStream(fos);
            bos.write(pictureData.getData());

            //FileOutputStream fops = new FileOutputStream(new File(desPathName));
            /*fops.write(imgFile.to);
            fops.close();*/

            return UPLOAD_PATH+fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭的时候只需要关闭最外层的流就行了
            if(null != bos){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


}
