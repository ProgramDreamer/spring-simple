package annotation;

import java.lang.annotation.*;

/**
 * Created by where
 * 2019/7/28 13:25
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YLWService {

    String value() default "";
}
