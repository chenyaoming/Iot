package uitl;


import bean.TbFinger;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import dao.DaoFactory;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FingerDevice {

    private static final String MSG = "，请检查指纹设备是否插上电脑";

    private static final Map<String,String> retCodeMap = new HashMap<>();
    static {
        retCodeMap.put("-1001","失败");
        retCodeMap.put("-1002","连接设备失败");
        retCodeMap.put("-1003","设备未连接");
        retCodeMap.put("-1","初始化算法库失败");
        retCodeMap.put("-2","初始化采集库失败");
        retCodeMap.put("-3","无设备连接");
        retCodeMap.put("-4","接口暂不支持");
        retCodeMap.put("-5","无效参数");
        retCodeMap.put("-6","打开设备失败");
        retCodeMap.put("-7","无效句柄");
        retCodeMap.put("-8","取像失败");
        retCodeMap.put("-9","提取指纹模板失败");
        retCodeMap.put("-10","中断操作");
        retCodeMap.put("-11","内存不足");
        retCodeMap.put("-12","当前正在采集指纹");
        retCodeMap.put("-13","添加指纹模板到内存失败");
        retCodeMap.put("-14","添加指纹模板失败");
        retCodeMap.put("-17","操作失败");
        retCodeMap.put("-18","取消采集");
        retCodeMap.put("-20","比对指纹失败");
        retCodeMap.put("-22","合并登记指纹模板失败");
        retCodeMap.put("-24","处理图像失败");
        retCodeMap.put("-27","无效图像");
    }

    //the width of fingerprint image 指纹宽度
    private int fpWidth = 0;

    //the height of fingerprint image 指纹高度
    private int fpHeight = 0;


    //防假开关(1 打开 ,0 关闭)
    private int nFakeFunOn = 1;

    //图像数据(预分配 width*height Bytes)
    private byte[] imgbuf = null;

    //设备是否关闭着
    private boolean mbStop = true;
    //设备句柄， 值为 0 时打开失败,跟下标不是一个概念
    private long mhDevice = 0;
    //算法句柄
    private long mhDB = 0;

    public FingerDevice(){
        int initCode ;
        try {
            if (FingerprintSensorErrorCode.ZKFP_ERR_OK != (initCode = FingerprintSensorEx.Init())) {
                JOptionPane.showMessageDialog(new JPanel(),retCodeMap.get(initCode+"")+MSG,"提示",1);
                throw new RuntimeException("初始化失败错误");
            }
            int ret = FingerprintSensorEx.GetDeviceCount();
            if (ret < 0) {
                JOptionPane.showMessageDialog(new JPanel(),MSG,"提示",1);
                throw new RuntimeException("初始化失败错误");
            }
            if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0))) {
                JOptionPane.showMessageDialog(new JPanel(),"打开设备失败"+MSG,"提示",1);
                throw new RuntimeException("初始化失败错误");
            }
            if (0 == (mhDB = FingerprintSensorEx.DBInit())) {
                JOptionPane.showMessageDialog(new JPanel(),"初始化算法库失败"+MSG,"提示",1);
                throw new RuntimeException("初始化失败错误");
            }

            FingerprintSensorEx.SetParameters(mhDevice, 2002, changeByte(nFakeFunOn), 4);

            //For ISO/Ansi
            //int nFmt = 0;    //Ansi
            int nFmt = 1;    //ISO
            FingerprintSensorEx.DBSetParameter(mhDB, 5010, nFmt);
            //For ISO/Ansi End

            //set fakefun off
            //FingerprintSensorEx.SetParameter(mhDevice, 2002, changeByte(nFakeFunOn), 4);

            //初始化图像数据(预分配 width*height Bytes)大小
            initImgBufSize();
            //打开设备
            mbStop = false;

            //双重保险初始化高速内存数据
            addZkRam();

        }catch (Exception e){
            FreeSensor();
            //disposeDialog();
            throw e;
        }
    }

    /**
     * 释放传感器
     */
    private void FreeSensor()
    {
        mbStop = true;
        /*try {		//wait for thread stopping
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        if (0 != mhDB)
        {
            FingerprintSensorEx.DBFree(mhDB);
            mhDB = 0;
        }
        if (0 != mhDevice)
        {
            FingerprintSensorEx.CloseDevice(mhDevice);
            mhDevice = 0;
        }
        FingerprintSensorEx.Terminate();
    }

    /**
     * 添加到设备高速缓存
     */
    public void addZkRam(){
        if(FingerprintSensorEx.DBCount(mhDB) >0 ){
            //表示里面有高速缓存
            return;
        }
        List<TbFinger> fingerList = DaoFactory.getFingerDao().findAll();
        int ret = 0;
        if(CollectionUtils.isNotEmpty(fingerList)){
            for (TbFinger finger :fingerList){
                ret = FingerprintSensorEx.DBAdd( mhDB, finger.getId(), finger.getTemplate());
                if(ret != 0){
                    throw new RuntimeException("初始化指纹数据到高速缓存错误");
                }
            }
        }
    }

    private void initImgBufSize() {
        byte[] paramValue = new byte[4];
        int[] size = new int[1];
        //GetFakeOn
        //size[0] = 4;
        //FingerprintSensorEx.GetParameters(mhDevice, 2002, paramValue, size);
        //nFakeFunOn = byteArrayToInt(paramValue);

        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
        fpWidth = byteArrayToInt(paramValue);
        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
        fpHeight = byteArrayToInt(paramValue);

        imgbuf = new byte[fpWidth*fpHeight];
        //btnImg.resize(fpWidth, fpHeight);
    }

    public static int byteArrayToInt(byte[] bytes) {
        // "|="按位或赋值。
        int number = bytes[0] & 0xFF;
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }

    public static byte[] changeByte(int data) {
        return intToByteArray(data);
    }

    public static byte[] intToByteArray (final int number) {
        byte[] abyte = new byte[4];
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[0] = (byte) (0xff & number);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    public boolean checkFakeFinger() {
        int ret;
        if (nFakeFunOn == 1)
        {
            byte[] paramValue = new byte[4];
            int[] size = new int[1];
            size[0] = 4;
            int nFakeStatus = 0;
            //GetFakeStatus
            //低五位全为 1 表 示 真 手 指 (value&31==31)
            ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
            nFakeStatus = byteArrayToInt(paramValue);
            //System.out.println("ret = "+ ret +",nFakeStatus=" + nFakeStatus);
            if (0 == ret && (byte)(nFakeStatus & 31) != 31)
            {
                //System.out.println("Is a fake finger----------------------------------------");
                //textArea.setText("Is a fake finger?\n");
                return true;
            }
        }
        return false;
    }

    /**
     * 合并指纹并返回指纹id
     * @param tmplateList
     * @return
     */
    public byte[] mergerTemplateRetrueId(List<byte[]> tmplateList){
        int[] _retLen = new int[1];
        _retLen[0] = 2048;
        //3次指纹模板合并后返回的一个指纹模板regTemp
        byte[] regTemp = new byte[_retLen[0]];

        //DBMerge 合并3次按手指的指纹模板。  DBAdd 添加登记模板到内存。
        if ( 0 == FingerprintSensorEx.DBMerge(mhDB, tmplateList.get(0), tmplateList.get(1), tmplateList.get(2), regTemp, _retLen)) {

            return regTemp;

        }else{
            throw new RuntimeException("合并指纹报错");
        }
    }

}
