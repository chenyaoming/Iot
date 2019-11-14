package progress;


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
                glasspane = new InfiniteProgressPanel((JFrame) component,"");
            }else if(component instanceof JDialog){
                glasspane = new InfiniteProgressPanel((JDialog) component,"");
            }else {
                throw new RuntimeException("传的组件错误");
            }
            glasspane.start();

            try {
                //执行业务代码
                invokeBusiness();
            }catch (Exception e){

            }finally {
                // 在合适的地方关闭动画效果
                glasspane.stop();
            }
        }).start();
    }

    public abstract void invokeBusiness();


    public void process(){

    }

}
