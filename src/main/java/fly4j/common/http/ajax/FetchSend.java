package fly4j.common.http.ajax;

import fly4j.common.http.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@FunctionalInterface
public interface FetchSend {
    static final Logger log = LoggerFactory.getLogger(FetchSend.class);

    default public void doAjax(HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter writer = WebUtil.getPrintWriter(resp);
        try {
            writer.write(this.getAjaxPrintStr());
        } catch (Exception e) {
            writer.write(e.getMessage());
        } finally {
            if (null != writer) {
                writer.close();
            }
        }

    }


    public abstract String getAjaxPrintStr();
}
