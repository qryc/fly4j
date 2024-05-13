package fly.sample.el.groovy;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


/**
 * @author qryc
 */
public class GroovyJSR223Example {


    public static void main(String args[]) throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        String HelloLanguage = "def hello(language) {return \"Hello $language\"}";
        engine.eval(HelloLanguage);
        Invocable inv = (Invocable) engine;
        Object[] params = {new String("Groovy")};
        Object result = inv.invokeFunction("hello", params);
        System.out.println(result);


    }
}

