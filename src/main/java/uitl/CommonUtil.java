package uitl;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class CommonUtil {
    public static void  setlookandfeel() {
        try {

            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (final Exception e) {
            System.out.println("error");

        }
    }
}
