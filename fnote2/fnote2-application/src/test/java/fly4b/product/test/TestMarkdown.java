package fly4b.product.test;


import farticle.domain.view.CommonMdDecorator;
import farticle.domain.view.Md4jDecorator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

/**
 * Created by qryc on 2017/5/16.
 */
@RunWith(JUnit4ClassRunner.class)
public class TestMarkdown {
    //    Markdown4jProcessor processor = new Markdown4jProcessor();
    static final String tableMdstr = "| a    | b    | c    |\n" +
            "| ---- | ---- | ---- |\n" +
            "| 1    | 2    | 3    |\n" +
            "| 4    | 5    | 6    |";

    @Test
    public void process() throws Exception {

//        System.out.println(CommonMdDecorator.parse("#程序安装说明"));
        Assert.assertEquals("<p><del>deleted</del></p>\n", CommonMdDecorator.parse("~~deleted~~"));
        Assert.assertEquals("<h1>程序安装说明</h1>\n", CommonMdDecorator.parse("#程序安装说明"));
        Assert.assertEquals("<h1>程序安装说明</h1>\n", CommonMdDecorator.parse("# 程序安装说明"));
        Assert.assertEquals("<ol>\n" +
                "<li>程序安装说明</li>\n" +
                "</ol>\n", CommonMdDecorator.parse("1. 程序安装说明"));
        Assert.assertEquals("<pre><code>程序安装说明\n" +
                "</code></pre>\n", CommonMdDecorator.parse("    程序安装说明"));
        Assert.assertEquals("<pre><code>程序安装说明\n" +
                "</code></pre>\n", CommonMdDecorator.parse("    程序安装说明"));
//        System.out.println(CommonMdDecorator.parse("|a|b|"));
        System.out.println(CommonMdDecorator.parse(tableMdstr));

        System.out.println(CommonMdDecorator.parse("程序安装说明\na\n"));
//        Assert.assertEquals("<p>程序安装说明</p>\n",CommonMdDecorator.parse("程序安装说明\na\n"));
    }

    @Test
    public void j4Parse() throws Exception {

        Assert.assertEquals("<h1>程序安装说明</h1>\n", Md4jDecorator.parse("#程序安装说明"));
        Assert.assertEquals("<h1>程序安装说明</h1>\n", Md4jDecorator.parse("# 程序安装说明"));
        Assert.assertEquals("<h1>程序安装说明</h1>\n", Md4jDecorator.parse("#     程序安装说明"));
        Assert.assertEquals("<p># 程序安装说明</p>\n", Md4jDecorator.parse("<p># 程序安装说明</p>"));
        Assert.assertEquals("<p>1.1 <a href=\"/article/viewArticle.do?noteFileStr=51\">linuxC,helloWord与gcc原理</a>\n<br  />a</p>\n", Md4jDecorator.parse("1.1 [linuxC,helloWord与gcc原理](/article/viewArticle.do?noteFileStr=51)\na"));
    }

    @Test
    public void commonParse() throws Exception {

        Assert.assertEquals("<h1>程序安装说明</h1>\n", CommonMdDecorator.parse("#程序安装说明"));
        Assert.assertEquals("<h1>程序安装说明</h1>\n", CommonMdDecorator.parse("# 程序安装说明"));
        Assert.assertEquals("<h1>程序安装说明</h1>\n", CommonMdDecorator.parse("#     程序安装说明"));
        Assert.assertEquals("<p># 程序安装说明</p>\n", CommonMdDecorator.parse("<p># 程序安装说明</p>"));

        Assert.assertEquals("<p>1.1 <a href=\"/article/viewArticle.do?noteFileStr=51\">linuxC,helloWord与gcc原理</a><br/>\n" +
                "a</p>\n", CommonMdDecorator.parse("1.1 [linuxC,helloWord与gcc原理](/article/viewArticle.do?noteFileStr=51)\na"));
    }
}
