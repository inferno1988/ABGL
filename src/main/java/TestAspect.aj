import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 10.05.13
 * Time: 0:03
 * Palamarchuk Maksym © 2013
 */
public aspect TestAspect {
    pointcut mainMethod() : execution(* org.ifno.graphics.primitives.Rectangle.cloneGraphicObject(..));

    after() returning : mainMethod() {
        System.out.println("AOP test coverage");
    }
}
