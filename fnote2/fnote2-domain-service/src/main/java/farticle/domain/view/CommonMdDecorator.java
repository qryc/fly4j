package farticle.domain.view;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by qryc on 2019/12/14.
 */
public class CommonMdDecorator {

    public static String parse(String mdStr) {
        var stringBuilder = new StringBuilder();
        for (var lineStr : mdStr.split(StringUtils.LF)) {
            if (lineStr.startsWith("######")) {
                if (!lineStr.startsWith("###### ")) {
                    lineStr = lineStr.replaceAll("######", "###### ");
                }

            } else if (lineStr.startsWith("#####")) {
                if (!lineStr.startsWith("##### ")) {
                    lineStr = lineStr.replaceAll("#####", "##### ");
                }

            } else if (lineStr.startsWith("####")) {
                if (!lineStr.startsWith("#### ")) {
                    lineStr = lineStr.replaceAll("####", "#### ");
                }

            } else if (lineStr.startsWith("###")) {
                if (!lineStr.startsWith("### ")) {
                    lineStr = lineStr.replaceAll("###", "### ");
                }

            } else if (lineStr.startsWith("##")) {
                if (!lineStr.startsWith("## ")) {
                    lineStr = lineStr.replaceAll("##", "## ");
                }

            } else if (lineStr.startsWith("#")) {
                if (!lineStr.startsWith("# ")) {
                    lineStr = lineStr.replaceAll("#", "# ");
                }

            }
            stringBuilder.append(lineStr).append(StringUtils.LF);
        }
        mdStr = stringBuilder.toString();

        List<Extension> extensions = Arrays.asList(TablesExtension.create(), StrikethroughExtension.create());
        Parser parser = Parser.builder()
                .extensions(extensions)
                .build();
        HtmlRenderer renderer = HtmlRenderer.builder().softbreak("<br/>\n")
                .extensions(extensions)
                .build();

        Node document = parser.parse(mdStr);
        return renderer.render(document);
    }

    private static void testParese() {
        CommonMdDecorator.parse("#程序安装说明a");
        CommonMdDecorator.parse("程序安装\r\n说明");
        CommonMdDecorator.parse("````\n code \n````\na");
        System.out.println(CommonMdDecorator.parse("a\r\n\r\n" + "First Header | Second Header | Third Header  \r\n" +
                "------------ | ------------- | ------------  \r\n" +
                "Content Cell | Content Cell  | Content Cell  \r\n" +
                "Content Cell | Content Cell  | Content Cell"));
    }
}
