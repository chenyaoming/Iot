package helper;


import bean.DeviceExcelColum;
import bean.TbDevice;
import frame.FrameUtil;
import jodd.bean.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.PictureData;
import uitl.ExcleHelper;
import uitl.JFileChooserUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class DeviceExportHelper {

    public static final DeviceExcelColum d = new DeviceExcelColum(true);

    public static List<TbDevice> getDeviceData(String absolutePathfile){

        Map<String, PictureData> pictureDataMap = new HashMap<>();

        List<List<String>> dataList = ExcleHelper.getDataFromExcel(absolutePathfile,0,0,pictureDataMap);
        return getDeviceData(dataList,pictureDataMap);
    }



    private static List<TbDevice> getDeviceData(List<List<String>> deviceValues, Map<String, PictureData> pictureDataMap) {

        List<TbDevice> deviceList = new ArrayList<>();
        if(CollectionUtils.isEmpty(deviceValues)){
            return deviceList;
        }
        for (int n = 0; n < deviceValues.size();n++){
            List<String> values = deviceValues.get(n);
            TbDevice device = new TbDevice();
            for (int i = 0;i < values.size() ; i++){

                if(d.serial.equals(d.colums[i])){

                }else if(d.count.equals(d.colums[i])){

                    if(StringUtils.isBlank(values.get(i))){
                        JOptionPane.showMessageDialog(FrameUtil.currentFrame,"库存数量不能为空","错误",0);
                        throw new RuntimeException("库存数量不能为空");
                    }
                    if(!isNumeric(values.get(i))){
                        JOptionPane.showMessageDialog(FrameUtil.currentFrame,"库存数量请输入正整数","错误",0);
                        throw new RuntimeException("库存数量请输入正整数");
                    }
                    BeanUtil.pojo.setProperty(device,d.colums[i], values.get(i));
                }else if(d.image.equals(d.colums[i]) ){
                    //处理图片字段
                    String picKey = (n+1)+"-"+i;
                    PictureData pictureData = pictureDataMap.get(picKey);
                    if(null != pictureData){
                        device.setImage(JFileChooserUtil.writeImgToUpload(pictureData));
                    }

                }else{
                    BeanUtil.pojo.setProperty(device,d.colums[i], values.get(i));
                }
            }

            /**
             * 检查字段
             */
            checkDevice(device);

            deviceList.add(device);
        }
        return deviceList;
    }

    public static boolean isNumeric(String string){
        Pattern pattern = compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    private static void checkDevice(TbDevice device) {
        if(StringUtils.isBlank(device.getName())){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"设备名称不能为空","错误",0);
            throw new RuntimeException("设备名称不能为空");
        }
        if(StringUtils.isBlank(device.getTypeNum())){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"设备类型不能为空","错误",0);
            throw new RuntimeException("设备类型不能为空");
        }
        if(StringUtils.isBlank(device.getCode())){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"设备编码不能为空","错误",0);
            throw new RuntimeException("设备编码不能为空");
        }
        if(null == device.getCount()){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"库存数量不能为空","错误",0);
            throw new RuntimeException("库存数量不能为空");
        }
    }
}
