package uitl;

import jodd.io.FileUtil;
import org.apache.poi.ss.usermodel.PictureData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class JFileChooserUtil {

    private static Logger LOG = LoggerFactory.getLogger(JFileChooserUtil.class);

    private static final String UPLOAD_PATH;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("file");

        UPLOAD_PATH = bundle.getString("uploadPath");
    }

    public static File getSelectedOpenFile(final String[] types, Container container) {
        //String name = container.getName();

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

    public static File getSelectedFile(String fileName,final String type, Container container) {

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
        pathChooser.setSelectedFile(new File(fileName + type));
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

        String lastPath = UPLOAD_PATH+new SimpleDateFormat("yyyyMM").format(new Date())+"/";

        File toFile = new File(lastPath);
        if (!toFile.getParentFile().exists()) {
            // when file is not existed, will create.
            toFile.mkdirs();
        }
        // write to target file.
        try {
            FileUtil.copyFile(imgFile,new File(lastPath+fileName));

            //FileOutputStream fops = new FileOutputStream(new File(desPathName));
            /*fops.write(imgFile.to);
            fops.close();*/

            return lastPath+fileName;
        } catch (Exception e) {
            LOG.error("拷贝图片时报错:",e);
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
            LOG.error("保存图片时报错:",e);
        }finally {
            //关闭的时候只需要关闭最外层的流就行了
            if(null != bos){
                try {
                    bos.close();
                } catch (IOException e) {
                    LOG.error("关闭输出流时报错:",e);
                }
            }

        }
        return null;
    }


}
