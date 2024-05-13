package fly.sample.iffor;

/**
 * Created by qryc on 2021/11/22
 */
public class Guard {
    public static void main(String[] args) {

    }

    public void more(String paramStr) {
        //参数校验
        if (!"参数异常".equals(paramStr)) {
            //正常逻辑在一层IF嵌套中
            System.out.println("执行正常逻辑");
        }
    }

    public void guardOk(String paramStr) {
        //参数校验
        if ("参数异常".equals(paramStr)) {
            return;
        }

        //正常逻辑减少了一层嵌套
        System.out.println("执行正常逻辑");

    }

    public void guardWrong(boolean isMan) {
        if (isMan) {
            System.out.println("执行男人的逻辑");
        }
        System.out.println("执行女人的逻辑");
    }

    public void ok(boolean isMan) {
        if (isMan) {
            System.out.println("执行男人的逻辑");
        } else {
            System.out.println("执行女人的逻辑");
        }

    }
}
