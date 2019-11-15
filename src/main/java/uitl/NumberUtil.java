package uitl;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class NumberUtil {

    public static boolean isNumeric(String string){

        if(null == string || StringUtils.isBlank(string)){
            return false;
        }

        Pattern pattern = compile("[0-9]*");
        if(pattern.matcher(string.trim()).matches()){
            try {
                Integer.valueOf(string.trim());
                return true;
            }catch (Exception e){
                return false;
            }
        }else {
            return false;
        }
    }

}
