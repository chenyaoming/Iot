package frame;


import interfaces.BorrowUserNameFieldFrameOperation;
import interfaces.FrameOperation;
import interfaces.PanelOperation;

import javax.swing.*;
import java.awt.*;

public class FrameUtil {

    public static JFrame currentFrame;


    public static JFrame getCurrentFrame() {
        return currentFrame;
    }

    public static void setCurrentFrame(JFrame currentFrame) {
        FrameUtil.currentFrame = currentFrame;
    }

    public static JButton getCurrentSearchBtn(){
         if (null != currentFrame && currentFrame instanceof FrameOperation){
             return ((FrameOperation) currentFrame).getCurrentSearchButton();
         }
         return null;
    }

    public static void doClickSearchBtn(){
        JButton searchBtn = getCurrentSearchBtn();
        if(null != searchBtn){
            searchBtn.doClick();
        }
    }

    public static JTextField getCurrentBorrowUserNameField() {
        if (null != currentFrame && currentFrame instanceof BorrowUserNameFieldFrameOperation){
            return ((BorrowUserNameFieldFrameOperation) currentFrame).getCurrentBorrowUserNameField();
        }
        return null;
    }

}
