import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
    public Main() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(getWindowAdapter());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Main test = new Main();
    }

    private final JFrame frame = new JFrame();

    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                super.windowClosing(we);
                JOptionPane.showMessageDialog(frame, "Cant Exit");
            }
            @Override
            public void windowIconified(WindowEvent we) {
                frame.setState(JFrame.NORMAL);
                JOptionPane.showMessageDialog(frame, "Cant Minimize");
            }
        };
    }
}
