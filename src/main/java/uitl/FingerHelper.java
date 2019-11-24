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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FingerHelper extends Thread {

    private static Logger LOG = LoggerFactory.getLogger(FingerHelper.class);

    private FingerDialog fingerDialog ;

    private BorrowFingerDialog borrowFingerDialog;

    private BorrowSearchDialog borrowSearchDialog;

    private FingerDevice fingerDevice = null;


    //按下指纹次数合并
    public static final int PRESS_TIMES = 3;


    public FingerHelper(BorrowFingerDialog borrowFingerDialog){
        if(null == borrowFingerDialog || null == borrowFingerDialog.getRecord()){
            throw new RuntimeException("传值错误");
        }
        this.borrowFingerDialog = borrowFingerDialog;
        //initFingerDevice();
        getFingerDeviceInstance();

    }

    private void getFingerDeviceInstance() {
        fingerDevice = FingerDeviceFactory.getFingerDeviceIntance();
        if(null == fingerDevice){
            disposeDialog();
            throw new RuntimeException("获取指纹设备实例错误");
        }
    }


    public FingerHelper(FingerDialog fingerDialog){
        if(null ==fingerDialog || null == fingerDialog.getNewUser()){
            throw new RuntimeException("用户信息不能为空");
        }
        this.fingerDialog =fingerDialog;
        //initFingerDevice();
        getFingerDeviceInstance();
    }

    public FingerHelper(BorrowSearchDialog borrowSearchDialog){
        if(null == borrowSearchDialog){
            throw new RuntimeException("用户信息不能为空");
        }
        this.borrowSearchDialog = borrowSearchDialog;
        //initFingerDevice();
        getFingerDeviceInstance();
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
            LOG.error("执行录入指纹操作报错：",e);
            disposeDialog();
            JOptionPane.showMessageDialog(new JPanel(),"系统繁忙，请联系管理员","错误 ",0);
        }finally {
            //FreeSensor();
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
        while (!fingerDevice.isMbStop() && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            //System.out.println("采集指纹图像，指纹模板.......");
            if (0 == FingerprintSensorEx.AcquireFingerprint(fingerDevice.getMhDevice(), fingerDevice.getImgbuf(), template, templateLength))
            {
                //图片数据写到文件
                //OnCatpureOK(imgbuf);

                //OnExtractOK(template, templateLen[0]);
                //双重保险初始化高速内存数据
                fingerDevice.addZkRam();

                int[] fid = new int[1];
                int[] score = new int [1];

                //1:N识别
                if (FingerprintSensorEx.DBIdentify(fingerDevice.getMhDB(), template, fid, score) ==0 ){

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

        //System.out.println("线程执行完了");
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
        while (!fingerDevice.isMbStop() && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            //System.out.println("采集指纹图像，指纹模板.......");
            if (0 == FingerprintSensorEx.AcquireFingerprint(fingerDevice.getMhDevice(), fingerDevice.getImgbuf(), template, templateLength))
            {
                //双重保险初始化高速内存数据
                fingerDevice.addZkRam();

                int[] fid = new int[1];
                int[] score = new int [1];

                //1:N识别
                if (FingerprintSensorEx.DBIdentify(fingerDevice.getMhDB(), template, fid, score) ==0 ){

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

        //ystem.out.println("线程执行完了");
    }

    private void returnDeviceAndUpdateRecord() {

        TbBorrowRecord record = borrowFingerDialog.getRecord();

        TbDevice device = DaoFactory.getDeviceDao().queryById(record.getDeviceId());

        //借出数量等于已归还数量时就设置已归还状态
        if(record.getBorrowNum().compareTo(record.getReturnNum()) == 0){
            record.setStatus(Status.RETURNED.name());
        }

        new JDBCTemplate<Void>(){

            @Override
            public Void sqlTask(Connection connection) throws SQLException {
                //保存借用记录
                DaoFactory.getBorrowRecordDao().updateReturnData(connection,record);

                //此次实际归还的数量
                Integer nowReturnNum = record.getReturnNum() -  record.getOldReturnNum();

                //加上设备的库存
                DaoFactory.getDeviceDao().update(connection,device.getId(),"count",device.getCount() + nowReturnNum);
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
        while (!fingerDevice.isMbStop() && !this.isInterrupted()) {
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            //System.out.println("采集指纹图像，指纹模板.......");
            if (0 == FingerprintSensorEx.AcquireFingerprint(fingerDevice.getMhDevice(), fingerDevice.getImgbuf(), template, templateLength))
            {
                //双重保险初始化高速内存数据
                fingerDevice.addZkRam();

                int[] fid = new int[1];
                int[] score = new int [1];

                //1:N识别
                if (FingerprintSensorEx.DBIdentify(fingerDevice.getMhDB(), template, fid, score) ==0 ){

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
        //System.out.println("线程执行完了");
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
                LOG.info("录指纹超时了");

            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
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
        while (!fingerDevice.isMbStop() && !this.isInterrupted()) {
            //指纹模板大小
            byte[] template = new byte[2048];
            int[] templateLength = new int[1];
            templateLength[0] = 2048;
            //采集指纹图像，指纹模板
            //System.out.println("采集指纹图像，指纹模板.......");
            int ret =0;
            if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(fingerDevice.getMhDevice(), fingerDevice.getImgbuf(), template, templateLength)))
            {
                //System.out.println(ret);
                //检查是否是假手指
                if (fingerDevice.checkFakeFinger()) {
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
                    System.arraycopy(fingerDevice.mergerTemplateRetrueId(templateList), 0, mergerTemp, 0, 2048);

                    int[] fid = new int[1];
                    int[] score = new int [1];
                    //1:n识别
                    ret = FingerprintSensorEx.DBIdentify(fingerDevice.getMhDB(), mergerTemp,fid, score);
                    if(ret == 0){
                       // TbUser user = DaoFactory.getUserDao().queryByFingerId(fid[0]);
                        clearTempAndSetText(templateList);
                        JOptionPane.showMessageDialog(new JPanel(),"指纹已经注册过","提示",1);
                        continue;
                    }
                    Integer fingerId = saveUserAndFingerData(mergerTemp);

                    if(0 == FingerprintSensorEx.DBAdd(fingerDevice.getMhDB(), fingerId, mergerTemp)){
                        LOG.info("成功添加到高速缓存，fingerId：{}",fingerId);
                        //System.out.println("成功添加到高速缓存");
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
        fingerDevice.addZkRam();

        int[] fid = new int[1];
        int[] score = new int [1];

        //1:N识别
        //enroll_idx表示按手指的次数
        if (FingerprintSensorEx.DBIdentify(fingerDevice.getMhDB(), template, fid, score) ==0 ){

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
        if (tmplateList.size() > 0 && FingerprintSensorEx.DBMatch(fingerDevice.getMhDB(), tmplateList.get(tmplateList.size()-1),template) <= 0)
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



    public FingerDialog getFingerDialog() {
        return fingerDialog;
    }

    public void setFingerDialog(FingerDialog fingerDialog) {
        this.fingerDialog = fingerDialog;
    }

}
