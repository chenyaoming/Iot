/**
 * java swing 之JScrollPane面板
 * 在设置界面时，可能会遇到在一个较小的容器窗体中显示一个较大部分的内容，这时可以使用
 * JScrollPane面板，JscrollPane面板是带滚动条的面板，也是一种容器，但是常用于布置单个
 * 控件，并且不可以使用布局管理器。如果需要在JScrollPane面板中放置多个控件，需要将多个
 * 控件放置到JPanel 面板上，然后将JPanel面板作为一个整体控件添加到JScrollPane控件上。
 *
 * @author gao
 */
import frame.device.JScrollImagePanel;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class JScrollPaneDemo extends JFrame{
    private JPanel contentPane;
    private JScrollPane scrollPane;
    //private JTextArea textArea;
    public JScrollPaneDemo(){
        contentPane=new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        this.setContentPane(contentPane);
        scrollPane=new JScrollPane();
        contentPane.add(scrollPane,BorderLayout.CENTER);
        //textArea=new JTextArea();
        //scrollPane.add(textArea);

        //ImageIcon aboutIcon = new ImageIcon();

        JScrollImagePanel jScrollImagePanel = new JScrollImagePanel("C:\\Users\\mg\\Desktop\\bbb.jpg");


        scrollPane.setViewportView(jScrollImagePanel);
        this.setTitle("滚动面板使用");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 250, 200);
        this.setVisible(true);
    }
    public static void main(String []args){
        JScrollPaneDemo example=new JScrollPaneDemo();
    }
}