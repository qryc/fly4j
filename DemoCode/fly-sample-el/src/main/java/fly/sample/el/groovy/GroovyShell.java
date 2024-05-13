package fly.sample.el.groovy;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * @author qryc
 */
public class GroovyShell {

    public static void main(String args[]) throws Exception {

        //脚本定义
        var script1 = """
                return jsonObject.level.name;
                """;
        //程序中调用脚本
        String json = """
                 {"id":2,"name":"guest","level":{"name":"浏览用户"}}
                """;
        JSONObject jsonObject = JSON.parseObject(json);
        System.out.println(GroovyShell.eval(script1
                , jsonObject));
    }

    public static Object eval(String script, JSONObject jsonObject) {
        try {

            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("groovy");
            String HelloLanguage = "def comp(jsonObject) {" + script + "}";
            engine.eval(HelloLanguage);
            Invocable inv = (Invocable) engine;
            Object[] params = {jsonObject};
            Object result = inv.invokeFunction("comp", params);
            return result;
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
