package fly.sample.el.aviator;

import com.alibaba.fastjson.JSON;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.HashMap;
import java.util.Map;

/**
 * https://developer.aliyun.com/article/608829
 * https://github.com/killme2008/aviatorscript
 */
public class AviatorSimpleExample {

    public static void main(String[] args) {
        Long result = (Long) AviatorEvaluator.execute("1+2+3");
        System.out.println(result);

        test2();
        test21();
        test3();
        test4();
        test5();
    }

    /**
     * 传入变量
     */
    private static void test2() {
        String name = "李白";
        Map<String, Object> env = new HashMap<>();
        env.put("name", name);
        String result = (String) AviatorEvaluator.execute(" 'Hello ' + name ", env);
        System.out.println(result);
    }

    /**
     * 传入变量-json
     */
    private static void test21() {
        String json = """
                 {"id":2,"name":"guest","level":{"name":"浏览用户"}}
                """;
        Map<String, Object> env = new HashMap<>();
        env.put("user", JSON.parseObject(json));
        String result = (String) AviatorEvaluator.execute(" 'Hello ' + user.name+ user.level.name  ", env);
        System.out.println(result);
    }

    /**
     * 调用函数
     */
    private static void test3() {
        String str = "helloAviator";
        Map<String, Object> env = new HashMap<>();
        env.put("str", str);
        Long length = (Long) AviatorEvaluator.execute("string.length(str)", env);
        System.out.println(length);
    }

    /**
     * compile用法
     */
    private static void test4() {
        String expression = "a-(b-c)>100";
        Expression compiledExp = AviatorEvaluator.compile(expression);
        Map<String, Object> env = new HashMap<>();
        env.put("a", 100.3);
        env.put("b", 45);
        env.put("c", -199.100);
        Boolean result = (Boolean) compiledExp.execute(env);
        System.out.println(result);
    }

    /**
     * compile用法
     */
    private static void test5() {
        AviatorEvaluator.addFunction(new MinFunction());
        String expression = "min(a,b)";
        Expression compiledExp = AviatorEvaluator.compile(expression, true);
        Map<String, Object> env = new HashMap<>();
        env.put("a", 100.3);
        env.put("b", 45);
        Double result = (Double) compiledExp.execute(env);
        System.out.println(result);
    }

    static class MinFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorBigInt(Math.min(left.doubleValue(), right.doubleValue()));
        }

        public String getName() {
            return "min";
        }
    }
}
