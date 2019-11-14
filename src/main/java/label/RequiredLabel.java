package label;

import sun.swing.SwingUtilities2;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLabelUI;
/**
 * Created at 2006-8-7 9:46:32<br>
 * 必填项标签
 *
 * @author Brad.Wu
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RequiredLabel extends JLabel {
    public RequiredLabel() {
    }
    public RequiredLabel(String text) {
        super("* " + text);
    }
    /**
     * (非 Javadoc)
     *
     * @see javax.swing.JLabel#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        if (text.startsWith("* ")) {
            super.setText(text);
        } else {
            super.setText("* " + text);
        }
    }
    /**
     * (非 Javadoc)
     *
     * @see javax.swing.JLabel#updateUI()
     */
    @Override
    public void updateUI() {
        setUI(new RequiredLabelUI());
    }
    /**
     * Created at 2006-8-7 9:53:32<br>
     * 必填项标签UI
     *
     * @author Brad.Wu
     * @version 1.0
     */
    static class RequiredLabelUI extends MetalLabelUI {
        protected static RequiredLabelUI labelUI = new RequiredLabelUI();
        public static ComponentUI createUI(JComponent c) {
            return labelUI;
        }
        /**
         * (非 Javadoc)
         *
         * @see javax.swing.plaf.basic.BasicLabelUI#paintEnabledText(javax.swing.JLabel,
         *      java.awt.Graphics, java.lang.String, int, int)
         */
        @Override
        protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
            int mnemIndex = l.getDisplayedMnemonicIndex();
            g.setColor(l.getForeground());
            int x = l.getFontMetrics(l.getFont()).stringWidth("*");
            SwingUtilities2.drawStringUnderlineCharAt(l, g, s.substring(1), mnemIndex, textX + x,
                    textY);
            g.setColor(new Color(255, 100, 100));
            SwingUtilities2.drawString(l, g, "*", textX, textY + 2);
        }
    }
}