package helper;


import bean.DeviceExcelColum;
import bean.TbDevice;
import jodd.bean.BeanUtil;
import uitl.ExcleHelper;

import java.util.ArrayList;
import java.util.List;

public class DeviceExportHelper {

    public static final DeviceExcelColum d = new DeviceExcelColum(true);

    public static List<TbDevice> getDeviceData(String absolutePathfile){
        List<List<String>> dataList = ExcleHelper.getDataFromExcel(absolutePathfile,1,0);
        return getDeviceData(dataList);
    }



    private static List<TbDevice> getDeviceData(List<List<String>> deviceValues) {

        List<TbDevice> deviceList = new ArrayList<>();
        for (List<String> values :deviceValues) {
            TbDevice device = new TbDevice();
            for (int i = 0;i < values.size() ; i++){
                if(d.image.equals(d.colums[i])){
                    //processStudioIdField(v.colums[i] , values.get(i), invoiceData);
                }else{
                    BeanUtil.pojo.setProperty(device,d.colums[i], values.get(i));
                }
            }
            deviceList.add(device);
        }
        return deviceList;
    }
}
