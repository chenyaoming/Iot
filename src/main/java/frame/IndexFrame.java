package frame;

import controller.ExcelOutandIn;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IndexFrame{

    public JFrame frame = new JFrame("GridLayou布局计算器");

    public JFrame getFrame(){
        return frame;
    }

    public  void setVisible(boolean visible){
        if(null != frame){
            frame.setVisible(visible);
        }
    }

    public IndexFrame(){
        init();
    }

    private void init() {


        JPanel panel=new JPanel();    //创建面板
        //指定面板的布局为GridLayout，4行4列，间隙为5
        panel.setLayout(new GridLayout(2,2,20,20));

        JButton userBtn = new JButton("用户管理");
        userBtn.setActionCommand("user");
        addBtnListener(userBtn);

        JButton devBtn = new JButton("设备管理");
        devBtn.setActionCommand("device");
        addBtnListener(devBtn);


        JButton borrowBtn = new JButton("借入借出管理");
        borrowBtn.setActionCommand("borrow");
        addBtnListener(borrowBtn);

        //userBtn.setSize("20");

        panel.add(userBtn);    //添加按钮
        panel.add(devBtn);
        panel.add(borrowBtn);
        frame.add(panel);    //添加面板到容器
        frame.setBounds(300,200,700,350);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addBtnListener(JButton btn) {

        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if("user".equals(e.getActionCommand())){
                    System.out.println("user");
                    //frame.setVisible(false);
                    getFrame().dispose();
                    ExcelOutandIn tm = new ExcelOutandIn();

                }else if("device".equals(e.getActionCommand())){

                    System.out.println("device");
                }else if("borrow".equals(e.getActionCommand())){

                    System.out.println("borrow");
                }
            }
        });
    }

    public static void main(String[] args){
        IndexFrame indexFrame = new IndexFrame();
    }

}