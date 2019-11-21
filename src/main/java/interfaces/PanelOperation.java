package interfaces;


import org.apache.xmlbeans.impl.jam.JField;

import javax.swing.*;

public interface PanelOperation {

    /**
     * 获取当前Frame查询按钮
     * @return
     */
    JButton getSearchButton();

    JTextField getFirstSearchField();

}
