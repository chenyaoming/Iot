package uitl;


import bean.TbFinger;
import bean.TbUser;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import dao.DaoFactory;
import frame.user.FingerDialog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FingerHelper extends Thread {

    //注册按钮 (用来注册指纹到高速缓存里)
    JButton btnEnroll = null;
    //校验按钮(这里的按钮用法是：先用按注册按钮按三次指纹模板后，（或从图片注册也可以），然后打开校验按钮，再按一次指纹，与前三次的合并的指纹模板对比是否校验成功)
    JButton btnVerify = null;
    //鉴定按钮 (用来鉴定传来的指纹模板是否存在在高速缓存里,1:n匹配)
    JButton btnIdentify = null;
    //根据图片注册按钮
    JButton btnRegImg = null;
    //根据图片校验按钮
    JButton btnIdentImg = null;

    JTextArea textArea = new JTextArea();

    //the width of fingerprint image 指纹宽度
    int fpWidth = 0;

    //the height of fingerprint image 指纹高度
    int fpHeight = 0;

    //for verify test 存储实际返回指纹模板数据
    private byte[] lastRegTemp = new byte[2048];

    //the length of lastRegTemp 实际返回指纹模板的长度
    private int cbRegTemp = 0;

    //pre-register template 用存储按3次手指的3个指纹模板的数据
    private byte[][] regtemparray = new byte[3][2048];
    //Register 点击注册按钮后（Enroll） 就变成true
    //Identify  校验状态 ,在代码初始化时变为了false,点击校验按钮变为true
    private boolean bRegister = false;
    private boolean bIdentify = true;
    //finger id
    private int iFid = 1;
    //防假开关(1 打开 ,0 关闭)
    private int nFakeFunOn = 1;
    //must be 3
    static final int enroll_cnt = 3;
    //the index of pre-register function
    private int enroll_idx = 0;

    //图像数据(预分配 width*height Bytes)
    private byte[] imgbuf = null;
    //指纹模板大小
    private byte[] template = new byte[2048];
    //指纹模板长度大小 2048
    private int[] templateLen = new int[1];

    //设备是否关闭着
    private boolean mbStop = true;
    //设备句柄， 值为 0 时打开失败,跟下标不是一个概念
    private long mhDevice = 0;
    //算法句柄
    private long mhDB = 0;

    private TbUser user;

    private FingerDialog fingerDialog ;

    //按下指纹次数合并
    public static final int PRESS_TIMES = 3;



    private static Map<String,String> retCodeMap = new HashMap<>();
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

    private static final String MSG = "，请检查指纹设备是否插上电脑";


    public FingerHelper(TbUser user){
        if(null == user){
            throw new RuntimeException("用户信息不能为空");
        }
        this.user = user;
        initFingerDevice();
    }


    public void initFingerDevice() {
     /*if (0 != mhDevice) {
            //already inited
            textArea.setText("Please close device first!\n");
            return;
        }*/

        //初始化一些字段
        cbRegTemp = 0;
        bRegister = false;
        bIdentify = false;
        iFid = 1;
        enroll_idx = 0;

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
        }catch (Exception e){
            FreeSensor();
            disposeDialog();
            throw e;
        }


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
    }

    @Override
    public void run() {
        /**
         * 是否已经按完指纹和成功新增了用户
         */
        boolean fingerFinish = false;

        //指纹模板大小
        byte[] template = new byte[2048];

        List<byte[]> templateList = new ArrayList<>();

        long totalMillis = 0;
        while (!mbStop && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            System.out.println("采集指纹图像，指纹模板.......");
            int ret =0;
            if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLength)))
            {
                System.out.println(ret);
                //检查是否是假手指
                if (checkFakeFinger()) {
                    continue;
                }
                //图片数据写到文件
                //OnCatpureOK(imgbuf);

                //OnExtractOK(template, templateLen[0]);

                //收集指纹
                register(templateList,template);

                // TODO: 2019/11/9 要确认是否返回2048长度的指纹;
                if(templateList.size() == PRESS_TIMES){

                    byte[] mergerTemp = new byte[2048];
                    System.arraycopy(mergerTemplateRetrueId(templateList), 0, mergerTemp, 0, 2048);

                    int[] fid = new int[1];
                    int[] score = new int [1];
                    //1:n识别
                    ret = FingerprintSensorEx.DBIdentify(mhDB, mergerTemp,fid, score);
                    if(ret == 0){
                       // TbUser user = DaoFactory.getUserDao().queryByFingerId(fid[0]);
                        clearTempAndSetText(templateList);
                        JOptionPane.showMessageDialog(new JPanel(),"指纹已经注册过","提示",1);
                        continue;
                    }

                    Integer fingerId = saveUserAndFingerData(mergerTemp);

                    if(0 == FingerprintSensorEx.DBAdd(mhDB, fingerId, mergerTemp)){
                        System.out.println("成功添加到高速缓存");
                    }

                    fingerFinish = true;
                    break;
                }else {
                    fingerDialog.getTipLabel().setText(String.format(FingerDialog.TIPTEMP,PRESS_TIMES-templateList.size()));
                }
            }
            try {
                System.out.println(ret);
                //每0.5秒搜集一次
                long millis = 500;
                Thread.sleep(millis);
                totalMillis += millis;
                //五分钟超时
                if(totalMillis > 600*millis){
                    // 重新设置中断标志位
                    this.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 重新设置中断标志位
                this.interrupt();
            }

        }

        FreeSensor();
        disposeDialog();

        //System.out.println("准备提示");

        if(fingerFinish){

            fingerDialog.getSearchBtn().doClick();
            System.out.println("2132132");
            JOptionPane.showMessageDialog(new JPanel(),"添加用户操作成功","提示",1);
        }
        System.out.println("线程执行完了");
    }

    private Integer saveUserAndFingerData(byte[] mergerTemp) {
        TbFinger finger = new TbFinger();
        finger.setTemplate(mergerTemp);
        Integer fingerId = DaoFactory.getFingerDao().insert(finger);

        user.setFingerId(fingerId);
        DaoFactory.getUserDao().insert(user);
        return fingerId;
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

            TbFinger finger = fingerList.get(fingerList.size()-1);


            for (int i=1 ; i<=10000; i++){
                ret = FingerprintSensorEx.DBAdd( mhDB, finger.getId() +i , finger.getTemplate());
                System.out.println(i);
                if(ret != 0){
                    throw new RuntimeException("初始化指纹数据到高速缓存错误");
                }
            }

        }
    }


    private void disposeDialog(){
        if(null != fingerDialog && fingerDialog.isShowing()){
            fingerDialog.dispose();
        }
    }



    private void OnExtractOK(byte[] template, int len)
    {

        register(template);

        //这里的代码表示，点击打开设备按钮时还要点击打开注册按钮（Enroll）才能注册下面流程
        /*if(bRegister)
        {
            //注册指纹模板
           // register(template);
        }
        else
        {
            //校验
            if (bIdentify){
                //identify(template);
            }else{
                if(cbRegTemp <= 0)
                {
                    textArea.setText("Please register first!\n");
                }
                else
                {
                    //比对两枚指纹模板
                    int ret = FingerprintSensorEx.DBMatch(mhDB, lastRegTemp, template);
                    if(ret > 0)
                    {
                        textArea.setText("Verify succ, score=" + ret + "\n");
                    }
                    else
                    {
                        textArea.setText("Verify fail, ret=" + ret + "\n");
                    }
                }
            }
        }*/
    }

    private void register(byte[] template) {
        int[] fid = new int[1];
        int[] score = new int [1];
        //1:N识别
        int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
        //enroll_idx表示按手指的次数
        if (ret == 0)
        {
            JOptionPane.showMessageDialog(new JPanel(),"此手指已经注册，请勿重复注册","提示",1);
            //textArea.setText("the finger already enroll by " + fid[0] + ",cancel enroll\n");
            enroll_idx = 0;
            return;
        }
        //对比匹配不上前一次的就要时提示重新按
        if (enroll_idx > 0 && FingerprintSensorEx.DBMatch(mhDB, regtemparray[enroll_idx-1], template) <= 0)
        {
            textArea.setText("please press the same finger 3 times for the enrollment\n");
            return;
        }
        //拷贝传过来的指纹模板拷贝到regtemparray （指定下标）
        System.arraycopy(template, 0, regtemparray[enroll_idx], 0, 2048);
        //按指纹次数加1
        enroll_idx++;
        //按了三次后
        if (enroll_idx == 3) {
            int[] _retLen = new int[1];
            _retLen[0] = 2048;
            //3次指纹模板合并后返回的一个指纹模板regTemp
            byte[] regTemp = new byte[_retLen[0]];
            //DBMerge 合并3次按手指的指纹模板。  DBAdd 添加登记模板到内存。
            if (0 == (ret = FingerprintSensorEx.DBMerge(mhDB, regtemparray[0], regtemparray[1], regtemparray[2], regTemp, _retLen)) &&
                    0 == (ret = FingerprintSensorEx.DBAdd(mhDB, iFid, regTemp))) {
                System.out.println("登记成功的指纹id:"+iFid);
                iFid++;
                //实际返回指纹模板的长度
                cbRegTemp = _retLen[0];
                //拷贝合并后返回的指纹模板拷贝到lastRegTemp (存到lastRegTemp给其它地方用)
                System.arraycopy(regTemp, 0, lastRegTemp, 0, cbRegTemp);
                //Base64 Template
                //textArea.setText("enroll succ:\n");
                System.out.println("enroll succ");
            } else {
                //textArea.setText("enroll fail, error code=" + ret + "\n");
                System.out.println("enroll fail");
            }
            //关闭注册状态
            bRegister = false;
        } else {
            System.out.println("你还需要按"+(3 - enroll_idx)+" times fingerprint");
            //textArea.setText("You need to press the " + (3 - enroll_idx) + " times fingerprint\n");
        }
    }

    //收集添加模板指纹到list
    private void register(List<byte[]> tmplateList, byte[] template) {
       //双重保险初始化高速内存数据
        addZkRam();

        // TODO: 2019/11/10  这里的1：n匹配用数据库的数据吧
        //List<TbFinger> fingerList = DaoFactory.getFingerDao().findAll();

        /*int retScore = 0;
        if(CollectionUtils.isNotEmpty(fingerList)){
            for (TbFinger finger :fingerList){
                retScore = FingerprintSensorEx.DBMatch(mhDB, finger.getTemplate(), template);
                if(retScore >0){

                    TbUser user = DaoFactory.getUserDao().queryByFingerId(finger.getId());
                    System.out.println(user.getName()+"-------------");
                    break;
                }
            }
        }*/

        int[] fid = new int[1];
        int[] score = new int [1];

        //1:N识别
        //enroll_idx表示按手指的次数
        if (FingerprintSensorEx.DBIdentify(mhDB, template, fid, score) ==0 ){

            TbUser user = DaoFactory.getUserDao().queryByFingerId(fid[0]);

            JOptionPane.showMessageDialog(new JPanel(),user.getName()+"指纹已经注册，请勿重复注册","提示",1);
            //清除之前录入的指纹，和设置还需录入的指纹数
            clearTempAndSetText(tmplateList);

            //textArea.setText("the finger already enroll by " + fid[0] + ",cancel enroll\n");
            //enroll_idx = 0;
            return;
        }


        //对比匹配不上前一次的就要时提示重新按
        if (tmplateList.size() > 0 && FingerprintSensorEx.DBMatch(mhDB, tmplateList.get(tmplateList.size()-1),template) <= 0)
        {
            JOptionPane.showMessageDialog(new JPanel(),"请重新用同一个手指按三次","提示",1);
            //textArea.setText("please press the same finger 3 times for the enrollment\n");
            return;
        }
        //拷贝传过来的指纹模板拷贝到regtemparray （指定下标）

        byte[] newTemp = new byte[2048];
        System.arraycopy(template, 0, newTemp, 0, 2048);

        tmplateList.add(template);
    }

    private void clearTempAndSetText(List<byte[]> tmplateList) {
        tmplateList.clear();
        fingerDialog.getTipLabel().setText(String.format(FingerDialog.TIPTEMP,PRESS_TIMES));
    }


    /**
     * 合并指纹并返回指纹id
     * @param tmplateList
     * @return
     */
    private byte[] mergerTemplateRetrueId(List<byte[]> tmplateList){
        int[] _retLen = new int[1];
        _retLen[0] = 2048;
        //3次指纹模板合并后返回的一个指纹模板regTemp
        byte[] regTemp = new byte[_retLen[0]];

        //DBMerge 合并3次按手指的指纹模板。  DBAdd 添加登记模板到内存。
        if ( 0 == FingerprintSensorEx.DBMerge(mhDB, tmplateList.get(0), tmplateList.get(1), tmplateList.get(2), regTemp, _retLen)) {

            //实际返回指纹模板的长度
            //cbRegTemp = _retLen[0];
            //拷贝合并后返回的指纹模板拷贝到lastRegTemp (存到lastRegTemp给其它地方用)
            //System.arraycopy(regTemp, 0, lastRegTemp, 0, cbRegTemp);



            /*if(0 == FingerprintSensorEx.DBAdd(mhDB, fingerId, regTemp)){
                System.out.println("成功添加到高速缓存");
            }*/
            //Base64 Template
            //textArea.setText("enroll succ:\n");
            //System.out.println("成功注册指纹");
            System.out.println("成功合并指纹");

            return regTemp;

        }else{
            throw new RuntimeException("合并指纹报错");
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

    private boolean checkFakeFinger() {
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
            System.out.println("ret = "+ ret +",nFakeStatus=" + nFakeStatus);
            if (0 == ret && (byte)(nFakeStatus & 31) != 31)
            {
                System.out.println("Is a fake finger----------------------------------------");
                //textArea.setText("Is a fake finger?\n");
                return true;
            }
        }
        return false;
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


    public FingerDialog getFingerDialog() {
        return fingerDialog;
    }

    public void setFingerDialog(FingerDialog fingerDialog) {
        this.fingerDialog = fingerDialog;
    }

}
