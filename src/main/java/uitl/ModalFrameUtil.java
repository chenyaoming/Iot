package uitl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class ModalFrameUtil {
    public ModalFrameUtil() {
        //
    }

    static class EventPump implements InvocationHandler {
        Frame frame;

        public EventPump(Frame frame) {
            this.frame = frame;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws
                Throwable {
            return frame.isShowing() ? Boolean.TRUE : Boolean.FALSE;
        }

        // when the reflection calls in this method has to be
        // replaced once Sun provides a public API to pump events.
        public void start() throws Exception {
            Class clazz = Class.forName("java.awt.Conditional");
            Object conditional = Proxy.newProxyInstance(
                    clazz.getClassLoader(),
                    new Class[] {clazz},
                    this);
            Method pumpMethod = Class.forName("java.awt.EventDispatchThread")
                    .getDeclaredMethod("pumpEvents", new Class[] {clazz});
            pumpMethod.setAccessible(true);
            pumpMethod.invoke(Thread.currentThread(), new Object[] {conditional});
        }
    }

    // 调用方法
    public static void showAsModal(final Frame frame, final Frame owner) {
        if(null != owner){
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    owner.setEnabled(false);
                }

                //当调用frame.dispose();时  会调用这个
                @Override
                public void windowClosed(WindowEvent e) {
                    owner.setEnabled(true);
                    owner.removeWindowListener(this);
                    owner.setState(JFrame.NORMAL);
                    owner.toFront();
                }

                //当按关闭窗口按钮时调用
                @Override
                public void windowClosing(WindowEvent e) {
                    owner.setEnabled(true);
                    owner.removeWindowListener(this);
                    owner.setState(JFrame.NORMAL);

                }

                @Override
                public void windowIconified(WindowEvent we) {
                    frame.setState(JFrame.NORMAL);
                    //JOptionPane.showMessageDialog(frame, "Cant Minimize");
                }
            });

            owner.addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    if (frame != null && frame.isShowing()) {
                        frame.setExtendedState(JFrame.NORMAL);
                        frame.toFront();
                    }
                    else {
                        owner.removeWindowListener(this);
                    }
                }
            });
        }
        frame.setVisible(true);
        try {
            new EventPump(frame).start();
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}