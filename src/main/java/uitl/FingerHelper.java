package uitl;


import bean.TbBorrowRecord;
import bean.TbDevice;
import bean.TbFinger;
import bean.TbUser;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import dao.DaoFactory;
import dao.TbFingerDao;
import dao.TbUserDao;
import enums.Status;
import frame.FrameUtil;
import frame.borrow.BorrowFingerDialog;
import frame.borrow.BorrowFinishDialog;
import frame.borrow.BorrowSearchDialog;
import frame.borrow.ReturnFinishDialog;
import frame.user.FingerDialog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FingerHelper extends Thread {

    //the width of fingerprint image 指纹宽度
    int fpWidth = 0;

    //the height of fingerprint image 指纹高度
    int fpHeight = 0;


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

    private FingerDialog fingerDialog ;

    private BorrowFingerDialog borrowFingerDialog;

    private BorrowSearchDialog borrowSearchDialog;


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


    public FingerHelper(BorrowFingerDialog borrowFingerDialog){
        if(null == borrowFingerDialog || null == borrowFingerDialog.getRecord()){
            throw new RuntimeException("传值错误");
        }
        this.borrowFingerDialog = borrowFingerDialog;
        initFingerDevice();
    }



    public FingerHelper(FingerDialog fingerDialog){
        if(null ==fingerDialog || null == fingerDialog.getNewUser()){
            throw new RuntimeException("用户信息不能为空");
        }
        this.fingerDialog =fingerDialog;
        initFingerDevice();
    }

    public FingerHelper(BorrowSearchDialog borrowSearchDialog){
        if(null == borrowSearchDialog){
            throw new RuntimeException("用户信息不能为空");
        }
        this.borrowSearchDialog = borrowSearchDialog;
        initFingerDevice();
    }


    public void initFingerDevice() {
     /*if (0 != mhDevice) {
            //already inited
            textArea.setText("Please close device first!\n");
            return;
        }*/

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
            disposeDialog();
            throw e;
        }
    }

    @Override
    public void run() {
        try {
            if(null != fingerDialog && null != fingerDialog.getNewUser()){
                //处理用户输入的指纹
                addUserFinger();
            } else if(null != borrowFingerDialog && null != borrowFingerDialog.getRecord()){
                if(borrowFingerDialog.getRecord().getId() == null){
                    //处理借出指纹
                    addBorrowFinger();
                }else {
                    //处理归还指纹
                    addReturnFinger();
                }
            }else if(null != borrowSearchDialog){
                dealBorrowSearchFinger();
            }
        }catch (Exception e){
            e.printStackTrace();
            disposeDialog();
            JOptionPane.showMessageDialog(new JPanel(),"系统繁忙，请联系管理员","错误 ",0);
        }finally {
            FreeSensor();
        }

    }

    private void addBorrowFinger() {
        //借出时按的指纹次数，分别是借出人和保管人
        int borrowPressTimes = 2;

        /**
         * 是否已经按完指纹
         */
        boolean fingerFinish = false;

        //指纹模板大小
        byte[] template = new byte[2048];

        long totalMillis = 0;
        while (!mbStop && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            System.out.println("采集指纹图像，指纹模板.......");
            if (0 == FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLength))
            {
                //图片数据写到文件
                //OnCatpureOK(imgbuf);

                //OnExtractOK(template, templateLen[0]);
                //双重保险初始化高速内存数据
                addZkRam();

                int[] fid = new int[1];
                int[] score = new int [1];

                //1:N识别
                if (FingerprintSensorEx.DBIdentify(mhDB, template, fid, score) ==0 ){

                    TbFinger finger = DaoFactory.getFingerDao().queryById(fid[0]);
                    TbUser user = DaoFactory.getUserDao().queryById(finger.getUserId());

                    if(null != user){
                        if(borrowPressTimes == 2){
                            borrowFingerDialog.getRecord().setBorrowUserId(user.getId());
                            borrowFingerDialog.getRecord().setBorrowUserName(user.getName());

                            borrowPressTimes--;
                            borrowFingerDialog.getTipLabel().setText(String.format(BorrowFingerDialog.TIPTEMP,"借出保管员"));
                        }else if(borrowPressTimes == 1){
                            borrowFingerDialog.getRecord().setBorrowClerkUserId(user.getId());
                            borrowFingerDialog.getRecord().setBorrowClerkUserName(user.getName());
                            borrowFingerDialog.getRecord().setBorrowDate(new Date());

                            int yesOrNo = JOptionPane.showConfirmDialog(new JPanel(),"请确定 借用人："+borrowFingerDialog.getRecord().getBorrowUserName()
                                    +"  出借保管员："+borrowFingerDialog.getRecord().getBorrowClerkUserName()+"？","借用信息提示",0);

                            if(yesOrNo == 0){
                                //保存借用记录

                                updateRecordAndDevice();

                                borrowPressTimes--;
                                fingerFinish = true;
                                break;
                            }else {
                                borrowPressTimes = 2;
                                borrowFingerDialog.getTipLabel().setText(String.format(BorrowFingerDialog.TIPTEMP,"借用人"));
                            }
                        }
                    }else {
                        JOptionPane.showMessageDialog(new JPanel(),"此指纹还未注册, 请到人员管理新增人员信息","提示",0);
                    }
                }else {
                    JOptionPane.showMessageDialog(new JPanel(),"此指纹还未注册, 请到人员管理新增人员信息","提示",0);
                }

            }
            totalMillis = dealSleepAndInterrupt(totalMillis);
        }

        TbBorrowRecord record = borrowFingerDialog.getRecord();
        disposeDialog();
        if(fingerFinish){

            //JOptionPane.showMessageDialog(new JPanel(),"借出成功","提示",1);
            FrameUtil.getCurrentSearchBtn().doClick();
            new BorrowFinishDialog(record).showDialog();
        }

        System.out.println("线程执行完了");
    }

    private void updateRecordAndDevice() {
        //减去设备的库存
        TbDevice device = DaoFactory.getDeviceDao().queryById(borrowFingerDialog.getRecord().getDeviceId());

        new JDBCTemplate<Void>(){

            @Override
            public Void sqlTask(Connection connection) throws SQLException {

                DaoFactory.getBorrowRecordDao().insertBorrowRecord(connection,borrowFingerDialog.getRecord());
                DaoFactory.getDeviceDao().update(connection,device.getId(),"count",device.getCount() - borrowFingerDialog.getRecord().getBorrowNum());

                return null;
            }
        }.doTask();

        //DaoFactory.getBorrowRecordDao().insert(borrowFingerDialog.getRecord());
       // DaoFactory.getDeviceDao().update(device.getId(),"count",device.getCount() - borrowFingerDialog.getRecord().getBorrowNum());
    }


    private void addReturnFinger() {

        //归还时按的指纹次数，分别是归还人和归还保管人
        int returnPressTimes = 2;

        /**
         * 是否已经按完指纹
         */
        boolean fingerFinish = false;

        //指纹模板大小
        byte[] template = new byte[2048];

        long totalMillis = 0;
        while (!mbStop && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            System.out.println("采集指纹图像，指纹模板.......");
            if (0 == FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLength))
            {
                //双重保险初始化高速内存数据
                addZkRam();

                int[] fid = new int[1];
                int[] score = new int [1];

                //1:N识别
                if (FingerprintSensorEx.DBIdentify(mhDB, template, fid, score) ==0 ){

                    TbFinger finger = DaoFactory.getFingerDao().queryById(fid[0]);
                    TbUser user = DaoFactory.getUserDao().queryById(finger.getUserId());

                    if(null != user){
                        if(returnPressTimes == 2){
                            borrowFingerDialog.getRecord().setReturnUserId(user.getId());
                            borrowFingerDialog.getRecord().setReturnUserName(user.getName());

                            returnPressTimes--;
                            borrowFingerDialog.getTipLabel().setText(String.format(BorrowFingerDialog.TIPTEMP,"归还保管员"));
                        }else if(returnPressTimes == 1){
                            borrowFingerDialog.getRecord().setReturnClerkUserId(user.getId());
                            borrowFingerDialog.getRecord().setReturnClerkUserName(user.getName());
                            borrowFingerDialog.getRecord().setReturnDate(new Date());

                            int yesOrNo = JOptionPane.showConfirmDialog(new JPanel(),"请确定 归还人："+borrowFingerDialog.getRecord().getReturnUserName()
                                    +"  归还保管员："+borrowFingerDialog.getRecord().getReturnClerkUserName()+"？","归还信息提示",0);

                            if(yesOrNo == 0){

                                returnDeviceAndUpdateRecord();

                                returnPressTimes--;
                                fingerFinish = true;
                                break;
                            }else{
                                returnPressTimes = 2;
                                borrowFingerDialog.getTipLabel().setText(String.format(BorrowFingerDialog.TIPTEMP,"归还人"));
                            }
                        }
                    }else {
                        JOptionPane.showMessageDialog(new JPanel(),"此指纹还未注册, 请到人员管理新增人员信息","提示",0);
                    }
                }else {
                    JOptionPane.showMessageDialog(new JPanel(),"此指纹还未注册, 请到人员管理新增人员信息","提示",0);
                }

            }
            totalMillis = dealSleepAndInterrupt(totalMillis);
        }

        TbBorrowRecord record = borrowFingerDialog.getRecord();
        disposeDialog();
        if(fingerFinish){
            new ReturnFinishDialog(record).showDialog();

            //JOptionPane.showMessageDialog(new JPanel(),"归还成功","提示",1);
            //FrameUtil.getCurrentSearchBtn().doClick();

        }

        System.out.println("线程执行完了");
    }

    private void returnDeviceAndUpdateRecord() {
        TbDevice device = DaoFactory.getDeviceDao().queryById(borrowFingerDialog.getRecord().getDeviceId());

        borrowFingerDialog.getRecord().setStatus(Status.RETURNED.name());

        new JDBCTemplate<Void>(){

            @Override
            public Void sqlTask(Connection connection) throws SQLException {
                //保存借用记录
                DaoFactory.getBorrowRecordDao().updateReturnData(connection,borrowFingerDialog.getRecord());
                //加上设备的库存
                DaoFactory.getDeviceDao().update(connection,device.getId(),"count",device.getCount() + borrowFingerDialog.getRecord().getBorrowNum());
                return null;
            }
        }.doTask();

        //保存借用记录
        //DaoFactory.getBorrowRecordDao().updateReturnData(borrowFingerDialog.getRecord());
        //加上设备的库存
        //DaoFactory.getDeviceDao().update(device.getId(),"count",device.getCount() + borrowFingerDialog.getRecord().getBorrowNum());
    }

    private void dealBorrowSearchFinger() {

        /**
         * 是否已经按完指纹
         */
        boolean fingerFinish = false;

        //指纹模板大小
        byte[] template = new byte[2048];

        long totalMillis = 0;
        while (!mbStop && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            System.out.println("采集指纹图像，指纹模板.......");
            if (0 == FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLength))
            {
                //双重保险初始化高速内存数据
                addZkRam();

                int[] fid = new int[1];
                int[] score = new int [1];

                //1:N识别
                if (FingerprintSensorEx.DBIdentify(mhDB, template, fid, score) ==0 ){

                    TbFinger finger = DaoFactory.getFingerDao().queryById(fid[0]);
                    TbUser user = DaoFactory.getUserDao().queryById(finger.getUserId());

                    if(null != user){
                        FrameUtil.getCurrentBorrowUserNameField().setText(user.getName());
                        FrameUtil.getCurrentSearchBtn().doClick();
                    }else {
                        JOptionPane.showMessageDialog(new JPanel(),"此指纹还未注册, 请到人员管理新增人员信息","提示",1);
                    }
                    break;
                }else {
                    JOptionPane.showMessageDialog(new JPanel(),"此指纹还未注册, 请到人员管理新增人员信息","提示",1);
                    break;
                }

            }
            totalMillis = dealSleepAndInterrupt(totalMillis);
        }
        disposeDialog();
        System.out.println("线程执行完了");
    }

    private long dealSleepAndInterrupt(long totalMillis) {
        try {
            //每0.3秒搜集一次
            long millis = 300;
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
        return totalMillis;
    }

    private void addUserFinger() {
        /**
         * 是否已经按完指纹和成功新增了用户
         */
        boolean fingerFinish = false;



        List<byte[]> templateList = new ArrayList<>();

        long totalMillis = 0;
        while (!mbStop && !this.isInterrupted()) {
            //指纹模板大小
            byte[] template = new byte[2048];
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
            totalMillis = dealSleepAndInterrupt(totalMillis);

        }

        disposeDialog();

        if(fingerFinish){

            FrameUtil.doClickSearchBtn();
            JOptionPane.showMessageDialog(new JPanel(),"添加用户操作成功","提示",1);
        }
    }

    private Integer saveUserAndFingerData(byte[] mergerTemp) {

       return new JDBCTemplate<Integer>(){
           @Override
           public Integer sqlTask(Connection connection) throws SQLException {
               Integer userId = DaoFactory.getUserDao().insertUser(connection,fingerDialog.getNewUser());
               TbFinger finger = new TbFinger();
               finger.setTemplate(mergerTemp);
               finger.setUserId(userId);

               return DaoFactory.getFingerDao().insertFinger(connection, finger);
           }

       }.doTask();
        /*Integer userId = DaoFactory.getUserDao().insert(fingerDialog.getNewUser());

        TbFinger finger = new TbFinger();
        finger.setTemplate(mergerTemp);
        finger.setUserId(userId);
        return DaoFactory.getFingerDao().insert(finger);*/
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


    private void disposeDialog(){
        if(null != fingerDialog && fingerDialog.isShowing()){
            fingerDialog.dispose();
        }
        if(null != borrowFingerDialog && borrowFingerDialog.isShowing()){
            borrowFingerDialog.dispose();
        }
        if(null != borrowSearchDialog && borrowSearchDialog.isShowing()){
            borrowSearchDialog.dispose();
        }
    }


    //收集添加模板指纹到list
    private void register(List<byte[]> tmplateList, byte[] template) {
       //双重保险初始化高速内存数据
        addZkRam();

        int[] fid = new int[1];
        int[] score = new int [1];

        //1:N识别
        //enroll_idx表示按手指的次数
        if (FingerprintSensorEx.DBIdentify(mhDB, template, fid, score) ==0 ){

            TbFinger finger = DaoFactory.getFingerDao().queryById(fid[0]);
            TbUser user = DaoFactory.getUserDao().queryById(finger.getUserId());

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

            //清除之前录入的指纹，和设置还需录入的指纹数
            clearTempAndSetText(tmplateList);
            //textArea.setText("please press the same finger 3 times for the enrollment\n");
            return;
        }
        //拷贝传过来的指纹模板拷贝到regtemparray （指定下标）

        byte[] newTemp = new byte[2048];
        System.arraycopy(template, 0, newTemp, 0, 2048);

        tmplateList.add(newTemp);
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
