package progress;

import frame.InfiniteProgressPanel;

import javax.swing.*;
import java.awt.*;


public abstract class MySwingWorker extends SwingWorker {

    /**
     * 这里只能是Frame和Dialog
     */
    private Component component;
    InfiniteProgressPanel glasspane =null;

    public MySwingWorker(Component component){
        //this.component = component;

        if(component instanceof JFrame){
            glasspane = new InfiniteProgressPanel((JFrame) component,"");
        }else if(component instanceof JDialog){
            glasspane = new InfiniteProgressPanel((JDialog) component,"");
        }else {
            throw new RuntimeException("传的组件错误");
        }
    }

    @Override
    protected Object doInBackground() {

        glasspane.start();
        try {
            //执行业务代码
            invokeBusiness();
        }catch (Exception e){

        }
        return null;
    }

    public abstract void invokeBusiness();

    @Override
    protected void done() {
        //将耗时任务执行完得到的结果移至done来进行处理，处理完关闭旋转等待框
        try {
            Object result = get();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            // 关闭旋转等待框
            if(glasspane != null) {
                // 在合适的地方关闭动画效果
                glasspane.stop();
            }
        }

    }
}
