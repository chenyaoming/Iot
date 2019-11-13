package frame.borrow;

import frame.FrameUtil;

import javax.swing.*;


public class BorrowSelectDialog extends JDialog {


    public BorrowSelectDialog(){

        super(FrameUtil.currentFrame,"选择借出设备",true);

        // 设置对话框的宽高
        //dialog.setSize(400, 400);
        this.setSize(800, 500);
        // 设置对话框大小不可改变
        //this.setResizable(false);
        // 设置对话框相对显示的位置
        this.setLocationRelativeTo(FrameUtil.currentFrame);

        this.setContentPane(new DeviceDialogPanel(this));

    }

    public void showDialog(){
        //ModalFrameUtil.showAsModal(this,parentFrame);
        this.setVisible(true);
    }

}
