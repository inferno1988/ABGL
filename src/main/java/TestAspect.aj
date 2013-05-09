import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 10.05.13
 * Time: 0:03
 * Palamarchuk Maksym © 2013
 */
public aspect TestAspect {
    pointcut mainMethod() : execution(public void onCreate(..));

    after() returning : mainMethod() {
        Log.d(new String("ASPECT"), "Aspect works");
    }
}
