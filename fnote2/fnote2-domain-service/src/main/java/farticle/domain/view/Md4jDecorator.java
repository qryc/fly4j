package farticle.domain.view;


import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;

/**
 * Created by qryc on 2019/12/14.
 */
public class Md4jDecorator {

    public static String parse(String mdStr) {
        try {
            return new Markdown4jProcessor().process(mdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mdStr;
    }
}
