package bean;

import annotation.FieldSort;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 设备表
 */
public class DeviceExcelColum {


    /**
     * 序号,程序不处理它
     */
    @FieldSort(order = 2) public String serial;

    /**
     * 名称
     */
    @FieldSort(order = 2) public String name;

    /**
     * 型号
     */
    @FieldSort(order = 4) public String typeNum;

    /**
     * 编码
     */
    @FieldSort(order = 6) public String code;

    /**
     * 存放位置
     */
    @FieldSort(order = 8) public String savePosition;

    /**
     * 图片
     */
    @FieldSort(order = 10) public String image;

    /**
     * 功能
     */
    @FieldSort(order = 12) public String features;


    public String[] colums;

    public List<String> columList;

    public DeviceExcelColum(boolean isInitField){
        super();
        if(isInitField){
            initFieldName();
        }
    }

    private void initFieldName(){
        List<String> columList = new ArrayList<>();
        //public 的字段
        List<Field> fields = getOrderedField();
        for (Field field :fields){
            try {
                field.set(this,field.getName());
                columList.add(field.getName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("字段赋值错误");
            }
        }
        //排序后的字段名称
        colums = new String[columList.size()];
        columList.toArray(colums);

        this.columList = columList;
    }

    private List<Field> getOrderedField(){
        // 用来存放所有的属性域
        List<Field> fieldList = new ArrayList<>();
        // 过滤带有注解的Field
        for(Field f:this.getClass().getFields()){
            if(f.getAnnotation(FieldSort.class) != null){
                fieldList.add(f);
            }
        }
        // 这个比较排序的语法依赖于java 1.8
        fieldList.sort(Comparator.comparingInt(
                m -> m.getAnnotation(FieldSort.class).order()
        ));
        return fieldList;
    }
}
