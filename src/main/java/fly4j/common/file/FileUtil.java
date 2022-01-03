package fly4j.common.file;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 统一封装文件访问
 *
 * @author qryc
 */
public class FileUtil {
    private static Charset fileCharset = Charset.forName("utf-8");
    /**
     * 特殊文件视作空文件
     * .DS_Store
     * Thumbs.db 是一个用于Microsoft Windows XP或mac os x缓存Windows Explorer的缩略图的文件
     */
    public static final Set<String> emptyFiles = new HashSet<>();

    static {
        emptyFiles.add(".DS_Store");
        emptyFiles.add("Thumbs.db");
    }

    public static void walkAllFileAndDir(File walkDir, Consumer<File> consumer) {
        File[] files = walkDir.listFiles();
        if (null == files) {
            return;
        }
        for (File cfile : files) {
            if (cfile.isDirectory()) {
                consumer.accept(cfile);
                //递归
                walkAllFileAndDir(cfile, consumer);
            } else {
                consumer.accept(cfile);
            }
        }
    }

    public static void walkAllFile(File walkDir, Predicate<File> refusePredicate, Consumer<File> consumer) {
        File[] files = walkDir.listFiles();
        if (null == files) {
            return;
        }
        for (File cfile : files) {
            if (null != refusePredicate && refusePredicate.test(cfile)) {
                continue;
            }
            if (cfile.isDirectory()) {
                //递归
                walkAllFile(cfile, refusePredicate, consumer);
            } else {
                consumer.accept(cfile);
            }
        }
    }

    public static void walkAllDir(File walkDir, Predicate<File> refusePredicate, Consumer<File> consumer) {
        File[] files = walkDir.listFiles();
        if (null == files) {
            return;
        }
        for (File cfile : files) {
            if (null != refusePredicate && refusePredicate.test(cfile)) {
                continue;
            }
            if (cfile.isDirectory()) {
                consumer.accept(cfile);
                //递归
                walkAllDir(cfile, refusePredicate, consumer);
            } else {

            }
        }
    }

    public static void walkAllFileIgnoreMacShadowFile(File walkDir, Predicate<File> refusePredicate, Consumer<File> consumer) {
        walkAllFile(walkDir, refusePredicate, file -> {
            if (!file.getAbsolutePath().contains("._")) {
                consumer.accept(file);
            }
        });
    }

    public static void walkAllDirIgnoreHiddenDir(File walkDir, Consumer<File> consumer) {
        walkAllDir(walkDir, file -> {
            if (file.isDirectory() && file.getName().startsWith(".")) {
                return true;
            }
            return false;
        }, consumer);
    }

    public static void walkFiles(File walkDir, Consumer<File> consumer) throws IOException {
        File[] files = walkDir.listFiles();
        if (null == files) {
            return;
        }
        for (File file : walkDir.listFiles()) {
            consumer.accept(file);
        }
    }

    public static void deleteRepeatFiles(Map<File, File> deleteFileMaps) {
        deleteFileMaps.forEach((deleteFile, repeatFile) -> {
            deleteRepeatFile(deleteFile, repeatFile);
        });
    }

    public static boolean deleteEmptyDirIgnoreSpecial(File file) {
        //删除特殊文件
        for (File f : file.listFiles()) {
            if (f.isFile() && emptyFiles.contains(f.getName())) {
                f.delete();
            }
        }
        if (isEmptyDirIgnoreSpecial(file)) {
            return file.delete();
        } else {
            return false;
        }

    }

    /**
     * @param file
     * @return
     */
    public static boolean isEmptyDirIgnoreSpecial(File file) {
        if (file.exists() && file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.getName().contains("._")) {
                    continue;
                }
                if (emptyFiles.contains(f.getName())) {
                    continue;
                }
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static int deleteEmptyDirsIgnoreSpecial(Path checkPath) throws IOException {
        AtomicInteger count = new AtomicInteger();
        Files.walk(checkPath).forEach(path -> {
            File file = path.toFile();
            if (isEmptyDirIgnoreSpecial(file)) {
                deleteEmptyDirIgnoreSpecial(file);
                count.addAndGet(1);
            }
        });
        return count.get();
    }

    public static void deleteRepeatFile(File delFile, File retainFile) {
        if (delFile.getAbsolutePath().equals(retainFile.getAbsolutePath())) {
            return;
        }
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
