package action;


import java.util.HashMap;
import java.util.Map;

public class ActionPerformBuilderUtil {

    private static Map<String,ActionPerformInterface> actionPerformMap = null;
    static {
        actionPerformMap = new HashMap<>();
        actionPerformMap.put("add",new AddActionPerform());
        actionPerformMap.put("delete",new DeleteActionPerform());
        actionPerformMap.put("save",new SaveActionPerform());
        actionPerformMap.put("reset",new ResetActionPerform());

        actionPerformMap.put("FormExcel",new FormExcelActionPerform());
        actionPerformMap.put("ToExcel",new ToExcelActionPerform());

    }

    public ActionPerformInterface builderAcionPerform(String name){
        return actionPerformMap.get(name);
    }
}
