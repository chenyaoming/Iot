package helper;


import bean.DeviceExcelColum;
import bean.TbDevice;
import jodd.bean.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.PictureData;
import uitl.ExcleHelper;
import uitl.JFileChooserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                if(d.image.equals(d.colums[i])){
                    //processStudioIdField(v.colums[i] , values.get(i), invoiceData);
                }else{
                    BeanUtil.pojo.setProperty(device,d.colums[i], values.get(i));
                }

                String picKey = (n+1)+"-"+i;
                PictureData pictureData = pictureDataMap.get(picKey);
                if(null != pictureData){
                    device.setImage(JFileChooserUtil.writeImgToUpload(pictureData));
                }

            }
            deviceList.add(device);
        }
        return deviceList;
    }
}
