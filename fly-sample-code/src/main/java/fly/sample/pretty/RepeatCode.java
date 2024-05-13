package fly.sample.pretty;

/**
 * 对应《代码整洁知道》3.6.2 标识参数
 */
public class RepeatCode {
    class Complex {
        void methodCommon(Boolean doA, Boolean doB, Boolean doC, Boolean doD, Boolean doE) {
            if (doA) {
                System.out.println("something with A");
            }
            if (doB) {
                System.out.println("something with B");
            }
            if (doC) {
                System.out.println("something with C");
            }
            if (doD) {
                System.out.println("something with D");
            }
            if (doE) {
                System.out.println("something with E");
            }
        }

        void groupX() {
            methodCommon(true, true, true, true, false);
        }

        void groupY() {
            methodCommon(true, true, false, true, true);
        }
    }

    class Pretty {
        void doA() {
            System.out.println("something with A");
        }

        void doB() {
            System.out.println("something with B");
        }

        void doC() {
            System.out.println("something with C");
        }

        void doD() {
            System.out.println("something with D");
        }

        void doE() {
            System.out.println("something with E");
        }

        void groupX() {
            doA();
            doB();
            doC();
            doD();
        }

        void groupY() {
            doA();
            doB();
            doD();
            doE();
        }
    }

}
