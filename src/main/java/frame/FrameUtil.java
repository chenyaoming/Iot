package frame;


import javax.swing.*;

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

}
