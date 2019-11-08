package controller.biometric;

import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class ZKFPDemo extends JFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	//打开按钮
	JButton btnOpen = null;
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
	//关闭按钮
	JButton btnClose = null;
	//右侧最大按钮
	JButton btnImg = null;

	JRadioButton radioISO = null;
	JRadioButton radioANSI = null;
	JRadioButton radioZK = null;


	private JTextArea textArea;

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
	private boolean bRegister = false;
	//Identify  校验状态 ,在代码初始化时变为了false,点击校验按钮变为true
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
	private WorkThread workThread = null;

	public void launchFrame(){
		//初始化上面的按钮
		initBtn();
		//打开设备按钮添加监听事件
		btnOpenAddActionListener();
		//关闭设备按钮添加监听事件
		btnCloseAddActionListener();
		//注册按钮添加监听事件
		btnEnrollAddActionListener();
		//校验按钮添加监听事件
		btnVerifyAddActionListener();
		//鉴定按钮添加监听事件
		btnIdentifyAddActionListener();
		//根据图片注册按钮添加监听事件
		btnRegImgAddActionListener();
		//根据图片校验按钮添加监听事件
		btnIdentImgAddActionListener();


		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				FreeSensor();
			}
		});
	}

	private void btnIdentImgAddActionListener() {
		btnIdentImg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(0 ==  mhDB)
				{
					textArea.setText("Please open device first!\n");
				}
				String path = "d:\\test\\fingerprint.bmp";
				byte[] fpTemplate = new byte[2048];
				int[] sizeFPTemp = new int[1];
				sizeFPTemp[0] = 2048;
				int ret = FingerprintSensorEx.ExtractFromImage(mhDB, path, 500, fpTemplate, sizeFPTemp);
				if (0 == ret)
				{
					if (bIdentify)
					{
						int[] fid = new int[1];
						int[] score = new int [1];
						ret = FingerprintSensorEx.DBIdentify(mhDB, fpTemplate, fid, score);
						if (ret == 0)
						{
							textArea.setText("Identify succ, fid=" + fid[0] + ",score=" + score[0] + "\n");
						}
						else
						{
							textArea.setText("Identify fail, errcode=" + ret +"\n");
						}

					}
					else
					{
						if(cbRegTemp <= 0)
						{
							textArea.setText("Please register first!\n");
						}
						else
						{
							ret = FingerprintSensorEx.DBMatch(mhDB, lastRegTemp, fpTemplate);
							if(ret > 0)
							{
								textArea.setText("Verify succ, score=" + ret + "\n");
							}
							else
							{
								textArea.setText("Verify fail, ret=" + ret +"\n");
							}
						}
					}
				}
				else
				{
					textArea.setText("ExtractFromImage fail, ret=" + ret + "\n");
				}
			}
		});
	}

	private void btnRegImgAddActionListener() {
		btnRegImg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(0 == mhDB)
				{
					textArea.setText("Please open device first!\n");
				}
				String path = "d:\\test\\fingerprint.bmp";
				byte[] fpTemplate = new byte[2048];
				int[] sizeFPTemp = new int[1];
				sizeFPTemp[0] = 2048;
				int ret = FingerprintSensorEx.ExtractFromImage( mhDB, path, 500, fpTemplate, sizeFPTemp);
				if (0 == ret)
				{
					ret = FingerprintSensorEx.DBAdd( mhDB, iFid, fpTemplate);
					if (0 == ret)
					{
						//String base64 = fingerprintSensor.BlobToBase64(fpTemplate, sizeFPTemp[0]);
						iFid++;
						cbRegTemp = sizeFPTemp[0];
						System.arraycopy(fpTemplate, 0, lastRegTemp, 0, cbRegTemp);
						//Base64 Template
						//String strBase64 = Base64.encodeToString(regTemp, 0, ret, Base64.NO_WRAP);
						textArea.setText("enroll succ\n");
					}
					else
					{
						textArea.setText("DBAdd fail, ret=" + ret + "\n");
					}
				}
				else
				{
					textArea.setText("ExtractFromImage fail, ret=" + ret + "\n");
				}
			}
		});
	}

	private void btnIdentifyAddActionListener() {
		btnIdentify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//判断是否有打开设备
				if(0 == mhDevice)
				{
					textArea.setText("Please Open device first!\n");
					return;
				}
				//把注册状态关掉，手指模板次数下标从0开始
				if(bRegister)
				{
					enroll_idx = 0;
					bRegister = false;
				}
				//开启鉴定状态
				if(!bIdentify)
				{
					bIdentify = true;
				}
			}
		});
	}

	private void btnVerifyAddActionListener() {
		btnVerify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(0 == mhDevice)
				{
					textArea.setText("Please Open device first!\n");
					return;
				}
				//把注册状态关掉，手指模板次数下标从0开始
				if(bRegister)
				{
					enroll_idx = 0;
					bRegister = false;
				}
				//关掉鉴定状态
				if(bIdentify)
				{
					bIdentify = false;
				}
			}
		});
	}

	private void btnEnrollAddActionListener() {
		btnEnroll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(0 == mhDevice)
				{
					textArea.setText("Please Open device first!\n");
					return;
				}
				if(!bRegister)
				{
					enroll_idx = 0;
					bRegister = true;
					textArea.setText("Please your finger 3 times!\n");
				}
			}
		});
	}

	private void btnCloseAddActionListener() {
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FreeSensor();

				textArea.setText("Close succ!\n");
			}
		});
	}

	private void btnOpenAddActionListener() {
		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (0 != mhDevice)
				{
					//already inited
					textArea.setText("Please close device first!\n");
					return;
				}

				//Initialize 初始化一些字段
				initializeField();

				if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init())
				{
					textArea.setText("Init failed!\n");
					return;
				}
				int ret = FingerprintSensorEx.GetDeviceCount();
				if (ret < 0)
				{
					textArea.setText("No devices connected!\n");
					FreeSensor();
					return;
				}
				if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0)))
				{
					textArea.setText("Open device fail, ret = " + ret + "!\n");
					FreeSensor();
					return;
				}
				if (0 == (mhDB = FingerprintSensorEx.DBInit()))
				{
					textArea.setText("Init DB fail, ret = " + ret + "!\n");
					FreeSensor();
					return;
				}

				//For ISO/Ansi
				int nFmt = 0;	//Ansi
				if (radioISO.isSelected())
				{
					nFmt = 1;	//ISO
				}
				FingerprintSensorEx.DBSetParameter(mhDB,  5010, nFmt);
				//For ISO/Ansi End

				//set fakefun off
				//FingerprintSensorEx.SetParameter(mhDevice, 2002, changeByte(nFakeFunOn), 4);

				//初始化图像数据(预分配 width*height Bytes)大小
				initImgBufSize();
				//打开设备
				mbStop = false;

				workThread = new WorkThread();
				workThread.start();// 线程启动
				textArea.setText("Open succ! Finger Image Width:" + fpWidth + ",Height:" + fpHeight +"\n");
			}
		});
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

	private void initializeField() {
		cbRegTemp = 0;
		bRegister = false;
		bIdentify = false;
		iFid = 1;
		enroll_idx = 0;
	}


	private void FreeSensor()
	{
		mbStop = true;
		try {		//wait for thread stopping
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight,
								   String path) throws IOException {
		java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
		java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

		int w = (((nWidth+3)/4)*4);
		int bfType = 0x424d; // 位图文件类型（0—1字节）
		int bfSize = 54 + 1024 + w * nHeight;// bmp文件的大小（2—5字节）
		int bfReserved1 = 0;// 位图文件保留字，必须为0（6-7字节）
		int bfReserved2 = 0;// 位图文件保留字，必须为0（8-9字节）
		int bfOffBits = 54 + 1024;// 文件头开始到位图实际数据之间的字节的偏移量（10-13字节）

		dos.writeShort(bfType); // 输入位图文件类型'BM'
		dos.write(changeByte(bfSize), 0, 4); // 输入位图文件大小
		dos.write(changeByte(bfReserved1), 0, 2);// 输入位图文件保留字
		dos.write(changeByte(bfReserved2), 0, 2);// 输入位图文件保留字
		dos.write(changeByte(bfOffBits), 0, 4);// 输入位图文件偏移量

		int biSize = 40;// 信息头所需的字节数（14-17字节）
		int biWidth = nWidth;// 位图的宽（18-21字节）
		int biHeight = nHeight;// 位图的高（22-25字节）
		int biPlanes = 1; // 目标设备的级别，必须是1（26-27字节）
		int biBitcount = 8;// 每个像素所需的位数（28-29字节），必须是1位（双色）、4位（16色）、8位（256色）或者24位（真彩色）之一。
		int biCompression = 0;// 位图压缩类型，必须是0（不压缩）（30-33字节）、1（BI_RLEB压缩类型）或2（BI_RLE4压缩类型）之一。
		int biSizeImage = w * nHeight;// 实际位图图像的大小，即整个实际绘制的图像大小（34-37字节）
		int biXPelsPerMeter = 0;// 位图水平分辨率，每米像素数（38-41字节）这个数是系统默认值
		int biYPelsPerMeter = 0;// 位图垂直分辨率，每米像素数（42-45字节）这个数是系统默认值
		int biClrUsed = 0;// 位图实际使用的颜色表中的颜色数（46-49字节），如果为0的话，说明全部使用了
		int biClrImportant = 0;// 位图显示过程中重要的颜色数(50-53字节)，如果为0的话，说明全部重要

		dos.write(changeByte(biSize), 0, 4);// 输入信息头数据的总字节数
		dos.write(changeByte(biWidth), 0, 4);// 输入位图的宽
		dos.write(changeByte(biHeight), 0, 4);// 输入位图的高
		dos.write(changeByte(biPlanes), 0, 2);// 输入位图的目标设备级别
		dos.write(changeByte(biBitcount), 0, 2);// 输入每个像素占据的字节数
		dos.write(changeByte(biCompression), 0, 4);// 输入位图的压缩类型
		dos.write(changeByte(biSizeImage), 0, 4);// 输入位图的实际大小
		dos.write(changeByte(biXPelsPerMeter), 0, 4);// 输入位图的水平分辨率
		dos.write(changeByte(biYPelsPerMeter), 0, 4);// 输入位图的垂直分辨率
		dos.write(changeByte(biClrUsed), 0, 4);// 输入位图使用的总颜色数
		dos.write(changeByte(biClrImportant), 0, 4);// 输入位图使用过程中重要的颜色数

		for (int i = 0; i < 256; i++) {
			dos.writeByte(i);
			dos.writeByte(i);
			dos.writeByte(i);
			dos.writeByte(0);
		}

		byte[] filter = null;
		if (w > nWidth)
		{
			filter = new byte[w-nWidth];
		}

		for(int i=0;i<nHeight;i++)
		{
			dos.write(imageBuf, (nHeight-1-i)*nWidth, nWidth);
			if (w > nWidth)
				dos.write(filter, 0, w-nWidth);
		}
		dos.flush();
		dos.close();
		fos.close();
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

	public static int byteArrayToInt(byte[] bytes) {
		int number = bytes[0] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[1] << 8) & 0xFF00);
		number |= ((bytes[2] << 16) & 0xFF0000);
		number |= ((bytes[3] << 24) & 0xFF000000);
		return number;
	}

	private class WorkThread extends Thread {
		@Override
		public void run() {
			super.run();
			int ret = 0;
			while (!mbStop) {
				templateLen[0] = 2048;
				//采集指纹图像，指纹模板
				System.out.println("采集指纹图像，指纹模板.......");
				if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen)))
				{
					System.out.println(ret);
					//检查是否是假手指
					if (checkFakeFinger()) {
						return;
					}
					//图片数据写到文件
					OnCatpureOK(imgbuf);

					OnExtractOK(template, templateLen[0]);
				}
				try {
					System.out.println(ret);
					//每0.5秒搜集一次
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
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
				textArea.setText("Is a fake finger?\n");
				return true;
			}
		}
		return false;
	}

	private void OnCatpureOK(byte[] imgBuf)
	{
		try {
			writeBitmap(imgBuf, fpWidth, fpHeight, "fingerprint.bmp");
			btnImg.setIcon(new ImageIcon(ImageIO.read(new File("fingerprint.bmp"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void OnExtractOK(byte[] template, int len)
	{
		//这里的代码表示，点击打开设备按钮时还要点击打开注册按钮（Enroll）才能注册下面流程
		if(bRegister)
		{
			//注册指纹模板
			register(template);
		}
		else
		{
			//校验
			if (bIdentify){
				identify(template);
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
		}
	}

	private void identify(byte[] template) {
		int[] fid = new int[1];
		int[] score = new int [1];
		//1:N识别
		int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
		if (ret == 0)
		{
			textArea.setText("Identify succ, fid=" + fid[0] + ",score=" + score[0] +"\n");
		}
		else
		{
			textArea.setText("Identify fail, errcode=" + ret + "\n");
		}
	}

	private void register(byte[] template) {
		int[] fid = new int[1];
		int[] score = new int [1];
		//1:N识别
		int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
		//enroll_idx表示按手指的次数
		if (ret == 0)
		{
			textArea.setText("the finger already enroll by " + fid[0] + ",cancel enroll\n");
			//关闭注册状态
			bRegister = false;
			enroll_idx = 0;
			return;
		}
		//有一次按失败时提示重新按
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
				textArea.setText("enroll succ:\n");
			} else {
				textArea.setText("enroll fail, error code=" + ret + "\n");
			}
			//关闭注册状态
			bRegister = false;
		} else {
			textArea.setText("You need to press the " + (3 - enroll_idx) + " times fingerprint\n");
		}
	}

	private void initBtn() {
		this.setLayout (null);
		btnOpen = new JButton("Open");
		this.add(btnOpen);
		int nRsize = 20;
		btnOpen.setBounds(30, 10 + nRsize, 140, 30);

		btnEnroll = new JButton("Enroll");
		this.add(btnEnroll);
		btnEnroll.setBounds(30, 60 + nRsize, 140, 30);

		btnVerify = new JButton("Verify");
		this.add(btnVerify);
		btnVerify.setBounds(30, 110 + nRsize, 140, 30);

		btnIdentify = new JButton("Identify");
		this.add(btnIdentify);
		btnIdentify.setBounds(30, 160 + nRsize, 140, 30);

		btnRegImg = new JButton("Register By Image");
		this.add(btnRegImg);
		btnRegImg.setBounds(30, 210 + nRsize, 140, 30);

		btnIdentImg = new JButton("Verify By Image");
		this.add(btnIdentImg);
		btnIdentImg.setBounds(30, 260 + nRsize, 140, 30);


		btnClose = new JButton("Close");
		this.add(btnClose);
		btnClose.setBounds(30, 310 + nRsize, 140, 30);


		//For ISO/Ansi/ZK
		radioANSI = new JRadioButton("ANSI", true);
		this.add(radioANSI);
		radioANSI.setBounds(30, 360 + nRsize, 60, 30);

		radioISO = new JRadioButton("ISO");
		this.add(radioISO);
		radioISO.setBounds(120, 360 + nRsize, 60, 30);

		radioZK = new JRadioButton("ZK");
		this.add(radioZK);
		radioZK.setBounds(210, 360 + nRsize, 60, 30);

		ButtonGroup group = new ButtonGroup();
		group = new ButtonGroup();
		group.add(radioANSI);
		group.add(radioISO);
		group.add(radioZK);
		//For End

		btnImg = new JButton();
		btnImg.setBounds(200, 5, 288, 375);
		btnImg.setDefaultCapable(false);
		this.add(btnImg);

		textArea = new JTextArea();
		this.add(textArea);
		textArea.setBounds(10, 420, 490, 150);
		textArea.setLineWrap(true);
		textArea.setSelectedTextColor(Color.RED);

		this.setSize(520, 620);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setTitle("ZKFingerSDK Demo");
		this.setResizable(false);
	}

	public static void main(String[] args) {
		new ZKFPDemo().launchFrame();
	}
}
