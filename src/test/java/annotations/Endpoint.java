package annotations;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Endpoint {
    String method() default "GET";
    String value();
    String description() default "";
}
