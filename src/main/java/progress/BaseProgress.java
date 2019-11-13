package progress;


import frame.FrameUtil;
import frame.InfiniteProgressPanel;

import javax.swing.*;
import java.awt.*;

public abstract class BaseProgress {

    /**
     * 这里只能是Frame和Dialog
     */
    private Component component;
    private String text;

    public BaseProgress(Component component,String text){
        this.component = component;
        this.text = text;
    }

    public void doAsynWork(){

        new Thread(() -> {

            InfiniteProgressPanel glasspane =null;
            if(component instanceof JFrame){
                glasspane = new InfiniteProgressPanel((JFrame) component,text);
            }else if(component instanceof JDialog){
                glasspane = new InfiniteProgressPanel((JDialog) component,text);
            }else {
                throw new RuntimeException("传的组件错误");
            }
            glasspane.start();
            //执行业务代码
            invokeBusiness();
            // 在合适的地方关闭动画效果
            glasspane.stop();
        }).start();
    }

    public abstract void invokeBusiness();

}
