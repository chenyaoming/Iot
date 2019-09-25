package uitl;


import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class JFileChooserUtil {

    public static File getSelectedOpenFile(final String type, Container container) {
        String name = container.getName();

        JFileChooser pathChooser = new JFileChooser();
        pathChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    if (f.getName().toLowerCase().endsWith(type)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public String getDescription() {
                return "文件格式（" + type + "）";
            }
        });
        pathChooser.setSelectedFile(new File(name + type));
        int showSaveDialog = pathChooser.showOpenDialog(container);
        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            return pathChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static File getSelectedFile(final String type, Container container) {
        String name = container.getName();

        JFileChooser pathChooser = new JFileChooser();
        pathChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    if (f.getName().toLowerCase().endsWith(type)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public String getDescription() {
                return "文件格式（" + type + "）";
            }
        });
        pathChooser.setSelectedFile(new File(name + type));
        int showSaveDialog = pathChooser.showSaveDialog(container);
        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            return pathChooser.getSelectedFile();
        } else {
            return null;
        }
    }
}
