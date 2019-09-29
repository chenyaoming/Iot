package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <b>FieldSort</b> is
 * </p>
 *
 * @author ChenYaoming
 * @version 1.0.0
 * @since 2019/6/6
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSort {
    /**
     * 标注该属性的顺序
     * @return 该属性的顺序
     */
    int order();
}
