package farticle.domain.view;

import farticle.domain.entity.DtreeComparator;
import fly4j.common.file.FileUtil;
import fly4j.common.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PdfService {
    static File outputDir = new File("/Users/gw/QRYC/KnPdf/");
    static File outputPicDir = new File("/Users/gw/QRYC/KnPdf/pic/");

    public static void main(String[] args) throws IOException {

        System.out.println("gen start");
        FileUtils.deleteDirectory(outputDir);
        File file = new File("/Volumes/HomeWork/2-article/DocPublic/0 我的知识库PDF-");
        StringBuilder pdfStr = new StringBuilder();
        addFoldersWithChildren2Pdf(file, pdfStr);
        File pdfFile = write2Pdf(pdfStr.toString());
//        System.out.println(pdfStr.toString().substring(0, 1000));
        System.out.println("gen end" + pdfFile.getAbsolutePath());

    }


    public static void addFoldersWithChildren2Pdf(File file, StringBuilder pdfStrBuilder) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        if (file.getName().startsWith(".")) {
            return;
        }
        File[] files = file.listFiles();
        if (null == files) {
            return;
        }
        //得到子文件
        List<File> fileList = Arrays.asList(files);
        //排序子文件，文件夹靠前，文件名排序;20240707改文件靠前
        Collections.sort(fileList, new DtreeComparator());
        //遍历子文件
        for (File cfile : fileList) {
            //忽略隐藏文件
            if (cfile.getName().startsWith(".")) {
                continue;
            }
            if (cfile.isDirectory()) {
                if (cfile.getName().contains("-hidden")) {
                    continue;
                }
                if (cfile.getName().equals(".git")) {
                    continue;
                }
                if (cfile.getName().equals("pic")) {
                    //copy
                    FileUtils.copyDirectory(cfile, outputPicDir);
                    System.out.println("copy:" + cfile.getAbsolutePath() + " name:");
                    continue;
                }
                System.out.println(cfile.getAbsolutePath());
                //
                pdfStrBuilder.append("# ").append(cfile.getName()).append(StringUtils.LF);
                //递归
                addFoldersWithChildren2Pdf(cfile, pdfStrBuilder);
            } else {
                if (cfile.getName().endsWith(".md")) {
                    String name = cfile.getName();
//                    String name = cfile.getName().substring(0, cfile.getName().length() - 3);
                    //文件名称二级目录
                    pdfStrBuilder.append("## ").append(name).append(StringUtils.LF);
                    /**
                     * 文件内容处理，降级处理，文件夹1级，标题二级别，内容从三级别开始。
                     * 至少是3级
                     */
                    String content = FileUtils.readFileToString(cfile, StandardCharsets.UTF_8);
                    content = content.replaceAll("##### ", "######--");
                    content = content.replaceAll("#### ", "#####--");
                    content = content.replaceAll("### ", "####--");
                    content = content.replaceAll("## ", "###--");
                    content = content.replaceAll("# ", "###--");
                    content = content.replaceAll("######--", "###### ");
                    content = content.replaceAll("#####--", "###### ");
                    content = content.replaceAll("####--", "##### ");
                    content = content.replaceAll("###--", "#### ");
                    pdfStrBuilder.append(content).append(StringUtils.LF);
                }

            }
        }
    }


    public static File write2Pdf(String str) throws IOException {
        File pdfFile = new File("/Users/gw/QRYC/KnPdf/Kn" + DateUtil.getDayStr(new Date()) + ".md");
        FileUtils.writeStringToFile(pdfFile, str, StandardCharsets.UTF_8);
        return pdfFile;


    }

}
