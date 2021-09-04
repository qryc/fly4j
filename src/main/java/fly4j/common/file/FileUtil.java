package fly4j.common.file;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 统一封装文件访问
 *
 * @author qryc
 */
public class FileUtil {

    public static void deleteOneLittleImg(File file) {
        if (file.isDirectory()) {
            return;
        }
        if (file.length() > 5 * FileUtils.ONE_MB) {
            return;
        }
        if (isImg(file)) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static void deleteOneRepeatFile(File delFile, File retainFile) {
        if (delFile.length() == retainFile.length() && getMD5(delFile).equals(getMD5(retainFile))) {
            try {
                FileUtils.forceDelete(delFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isImg(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }


    public static String getMd5ByFile(FileInputStream fis) throws Exception {
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
        IOUtils.closeQuietly(fis);
        return md5;
    }

    /**
     * 获取一个文件的md5值(可处理大文件)
     *
     * @return md5 value
     */
    public static String getMD5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * baseDirFile：/export/资料/
     * baseDirFile：/export/资料
     * baseDirFile：/export/资料/
     * file :      /export/资料/文件/a.txt
     * 输出：文件/a.txt
     *
     * @param filePathStr
     * @param baseDirPathStr
     * @return
     */
    public static String getSubPathUnix(String filePathStr, String baseDirPathStr) {
        filePathStr = FilenameUtils.separatorsToUnix(filePathStr.toString());
        baseDirPathStr = FilenameUtils.separatorsToUnix(baseDirPathStr.toString());
        //文件或文件夹如果以/结尾，去除，因为名字不带/结尾，干脆统一都不以/结尾，需要统一再加，便于理解
        if (filePathStr.endsWith("/")) {
            filePathStr = filePathStr.substring(0, filePathStr.length() - 1);
        }
        if (baseDirPathStr.endsWith("/")) {
            baseDirPathStr = baseDirPathStr.substring(0, baseDirPathStr.length() - 1);
        }

        //如果baseDirPathStr不以/结尾和filePathStr相等，则返回""
        if (filePathStr.equals(baseDirPathStr)) {
            return "";
        }

        return filePathStr.replace(baseDirPathStr + "/", "");

    }


    public static void writeFileToResponse(OutputStream servletOutputStream, File file) {

        /***
         * 把文件流写入客户端
         */
        try (InputStream inputStream = new FileInputStream(file)) {
            // 创建输入流，读取文件到内存
            // 创建输出流，输出内存到客户端
//            int readLength;
//            byte[] buf = new byte[4096];
//            while (((readLength = inputStream.read(buf)) != -1)) {
//                servletOutputStream.write(buf, 0, readLength);
//            }
            inputStream.transferTo(servletOutputStream);
//            IOUtils.copy(inputStream, servletOutputStream);
            servletOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException("文件输出到客户端异常", e);
        } finally {
            if (null != servletOutputStream) {
                try {
                    servletOutputStream.close();
                } catch (IOException e1) {
//                    LogUtil.error(LogUtil.FILE_EXCEPTION, "servletOutputStream.close exception", e1);
                }
            }
        }
    }


    public static boolean rename(File file, String newNameNoWithEx) {
        if (!file.isDirectory()) {
            String prefix = FilenameUtils.getExtension(file.getName());
            String newAbsolutePath = FilenameUtils.getFullPath(file.getAbsolutePath()) + newNameNoWithEx + "." + prefix;
            return (file.canWrite() && file.renameTo(new File(newAbsolutePath)));
        }
        return false;
    }

    public static File getClassPathFile(String path) {
        try {
            String fullPath = FileUtil.class.getResource(path).getFile();
            return new File(fullPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<File> getDirSortFiles(File md5Dir, String endWith) {
        File[] filesArray = md5Dir.listFiles(((dir, name) -> name.endsWith(endWith)));
        if (null == filesArray) {
            return List.of();
        }
        List<File> md5Files = Arrays.asList(filesArray);
        Collections.sort(md5Files, (f1, f2) -> {
            long t1 = f1.lastModified();
            long t2 = f2.lastModified();
            Long c = t2 - t1;
            return c.intValue();
        });
        return md5Files;
    }

    public static File getDirLastModifyFile(File md5Dir, String endWith) {
        List<File> md5Files = getDirSortFiles(md5Dir, endWith);
        if (null == md5Files || md5Files.size() == 0) {
            return null;
        }

        File md5File = md5Files.get(0);
        return md5File;
    }

    public static void deleteMoreBeforeFiles(File beZipSourceDir, int maxCount, String endWith) {
        //删除多余的Md5文件 deleteMore md5
        var md5Files = getDirSortFiles(beZipSourceDir, endWith);
        if (md5Files.size() > maxCount) {
            for (var i = maxCount - 1; i < md5Files.size(); i++) {
                md5Files.get(i).delete();
            }
        }
    }
}
