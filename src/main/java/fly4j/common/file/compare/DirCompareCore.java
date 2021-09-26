package fly4j.common.file.compare;

import fly4j.common.file.FilenameUtil;
import fly4j.common.file.version.BackModel;
import fly4j.common.file.version.DirDigestCalculate;
import fly4j.common.util.map.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DirCompareCore {
    public static record LeftRightSameObj(List<File> lefts, List<File> rights, String md5) {
    }

    public static record OneSameObj(List<File> sames, String md5) {
    }

    public static record LeftAloneObj(List<File> sames, String md5) {
    }

    public static record RightAloneObj(List<File> sames, String md5) {
    }

    public static record LeftRightModifyObj(File left, File right) {
    }

    public static enum CompareType {
        ONE, TWO_SAME, TWO_FULL;
    }

    public static class CompareResult {
        /**
         * 一个文件夹对比
         **/
        //一个文件夹内相等的部分
        public List<OneSameObj> oneSameObjs = new ArrayList<>();

        /**
         * 两个文件夹对比，不区分修改
         **/
        //左边右边相等的文件
        public List<LeftRightSameObj> leftRightSameObjs = new ArrayList<>();
        //左边独立的文件
        public List<File> leftAloneFiles = new ArrayList<>();
        public List<LeftAloneObj> leftAloneObjs = new ArrayList<>();
        public List<LeftAloneObj> leftAloneSameObjs = new ArrayList<>();//文件夹内重复
        //右边独立的文件
        public List<File> rightAloneFiles = new ArrayList<>();
        public List<RightAloneObj> rightAloneObjs = new ArrayList<>();
        public List<RightAloneObj> rightAloneSameObjs = new ArrayList<>();//文件夹内重复


        /**
         * 两个文件夹对比，区分修改
         **/
        public List<LeftRightModifyObj> leftRightModifyObjs = new ArrayList<>(); //修改
        public List<File> leftAloneNotModifyFiles = new ArrayList<>();
        public List<File> rightAloneNotModifyFiles = new ArrayList<>();


    }

    //md5 or size
    public static CompareResult compare(CompareType compareType, String leftDirStr, String rightDirStr, Predicate<File> noNeedCalMd5FileFilter) {

        if (StringUtils.isBlank(leftDirStr) && StringUtils.isBlank(rightDirStr)) {
            throw new RuntimeException("left & right is empty");
        }
        switch (compareType) {
            case ONE: //单文件对比
                return compareOneDir(leftDirStr, noNeedCalMd5FileFilter);
            case TWO_SAME://两个文件夹，使用长度加速
                return compareTwoSame(leftDirStr, rightDirStr, noNeedCalMd5FileFilter);
            case TWO_FULL://完整对比，必须使用MD5
                return compareTwo(leftDirStr, rightDirStr, noNeedCalMd5FileFilter);
            default:
                throw new UnsupportedOperationException();
        }


    }

    private static CompareResult compareTwoSame(String leftDirStr, String rightDirStr, Predicate<File> noNeedCalMd5FileFilter) {
        CompareResult compreResult = new CompareResult();
        /**第一步：通过长度进行第一轮筛选长度一致的可疑文件**/
        //Ready的md5
        Map<File, String> leftLenMap_file = DirDigestCalculate.getDirDigestFileMap(leftDirStr, BackModel.DigestType.LEN, noNeedCalMd5FileFilter);
        //取得新文件夹的Md5
        Map<File, String> rightLenMap_file = DirDigestCalculate.getDirDigestFileMap(rightDirStr, BackModel.DigestType.LEN, noNeedCalMd5FileFilter);


        //查找新文件夹中和老的文件夹长度一致的文件
        List<File> leftDoubleFilesFromLen = new ArrayList<>();
        List<File> rightDoubleFileFromLen = new ArrayList<>();
        LinkedHashMap<String, List<File>> leftGroupFileMap_len = MapUtil.convert2ValueMap(leftLenMap_file);
        rightLenMap_file.forEach((file, lenStr) -> {
            if (leftGroupFileMap_len.containsKey(lenStr)) {
                //长度相等的嫌疑统统加入
                leftDoubleFilesFromLen.addAll(leftGroupFileMap_len.get(lenStr));
                rightDoubleFileFromLen.add(file);
            }
        });

        //进一步删选MD5一致的文件
        //先生成新的反向
        LinkedHashMap<String, List<File>> leftRevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(leftDoubleFilesFromLen);
        LinkedHashMap<String, List<File>> rightRevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(rightDoubleFileFromLen);
        //遍历老的，如果新的有存在重复，则删除
        //key 要删除的，value，重复文件
        leftRevertMap_md5.forEach((leftMd5, leftFiles) -> {
            if (rightRevertMap_md5.containsKey(leftMd5)) {
                LeftRightSameObj leftRightSameObj = new LeftRightSameObj(leftFiles, rightRevertMap_md5.get(leftMd5), leftMd5);
                compreResult.leftRightSameObjs.add(leftRightSameObj);
//                        //要删除的
            }
        });
        return compreResult;
    }

    private static CompareResult compareOneDir(String leftDirStr, Predicate<File> noNeedCalMd5FileFilter) {
        CompareResult compreResult = new CompareResult();
        /**第一步，使用Length初筛**/
        //取得文件长度的Map
        LinkedHashMap<File, String> fileLengthMapAll = DirDigestCalculate.getDirDigestFileMap(leftDirStr, BackModel.DigestType.LEN, noNeedCalMd5FileFilter);
        //转换Map：Key:文件长度，value：长度相等的文件列表
        LinkedHashMap<String, List<File>> lengthGroupFileMap_len = MapUtil.convert2ValueMap(fileLengthMapAll);
        //从ValueMap中查找重复文件
        LinkedHashMap<String, List<File>> doubleFileMapByLen_len = MapUtil.filterLinkedHashMap(lengthGroupFileMap_len, e -> e.getValue().size() > 1);
        //简化结构，统一放入，不用按长度分堆 ，以前算法保持分堆，后续的二次检验在分堆中查找
        List<File> doubleFilesByLen = doubleFileMapByLen_len.entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toList());


        /**第二步，二次MD5检验**/
        //长度相等的一堆数据，生成新的反相Map
        LinkedHashMap<String, List<File>> md5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(doubleFilesByLen);
        //删选MD5相等的
        MapUtil.filterLinkedHashMap(md5RevertMap_md5, e -> e.getValue().size() > 1).forEach((md5Str, files) -> {
            compreResult.oneSameObjs.add(new OneSameObj(files, md5Str));
        });
        return compreResult;
    }

    private static CompareResult compareTwo(String leftDirStr, String rightDirStr, Predicate<File> noNeedCalMd5FileFilter) {
        CompareResult compreResult = new CompareResult();
        //取得上次的md5
        Map<File, String> leftMap_File = DirDigestCalculate.getDirDigestFileMap(leftDirStr, BackModel.DigestType.MD5, noNeedCalMd5FileFilter);
        //取得文件夹的Md5
        Map<File, String> rightMap_File = DirDigestCalculate.getDirDigestFileMap(rightDirStr, BackModel.DigestType.MD5, noNeedCalMd5FileFilter);

        LinkedHashMap<String, List<File>> leftGroupFileMap_md5 = MapUtil.convert2ValueMap(leftMap_File);
        LinkedHashMap<String, List<File>> rightGroupFileMap_md5 = MapUtil.convert2ValueMap(rightMap_File);


        /**
         * 区分是否相同
         */
        //遍历历史的，查找删除的Key
        leftGroupFileMap_md5.forEach((leftMd5, leftFiles) -> {
            List<File> rightFiles = rightGroupFileMap_md5.get(leftMd5);
            if (null == rightFiles) {//左边独立
                compreResult.leftAloneFiles.addAll(leftFiles);
                compreResult.leftAloneObjs.add(new LeftAloneObj(leftFiles, leftMd5));
                if (leftFiles.size() > 1) {//独立的重复文件
                    compreResult.leftAloneSameObjs.add(new LeftAloneObj(leftFiles, leftMd5));
                }
            } else {//两边相等
                compreResult.leftRightSameObjs.add(new LeftRightSameObj(leftFiles, rightFiles, leftMd5));
            }
        });

        //右边独立
        rightGroupFileMap_md5.forEach((rightMd5, rightFiles) -> {
            List<File> leftFiles = leftGroupFileMap_md5.get(rightMd5);
            if (null == leftFiles) {//右边独立
                compreResult.rightAloneFiles.addAll(rightFiles);
                compreResult.rightAloneObjs.add(new RightAloneObj(rightFiles, rightMd5));
                if (rightFiles.size() > 1) {//独立的重复文件
                    compreResult.rightAloneSameObjs.add(new RightAloneObj(rightFiles, rightMd5));
                }
            }
        });
        /**
         * 区分修改
         */
        //左右独立有可能是同样的路径，逻辑上应该看做修改
        Set<String> modifyPaths = new HashSet<>();
        Map<String, File> leftFileMap_path = new HashMap<>();
        compreResult.leftAloneFiles.forEach(leftFile -> {
            String leftPath = FilenameUtil.getSubPathUnix(leftFile.getAbsolutePath(), leftDirStr);
            leftFileMap_path.put(leftPath, leftFile);
        });
        compreResult.rightAloneFiles.forEach(rightFile -> {
            String rightPath = FilenameUtil.getSubPathUnix(rightFile.getAbsolutePath(), rightDirStr);
            if (leftFileMap_path.containsKey(rightPath)) {
                //修改
                compreResult.leftRightModifyObjs.add(new LeftRightModifyObj(leftFileMap_path.get(rightPath), rightFile));
                modifyPaths.add(rightPath);
            }
        });
        compreResult.leftAloneNotModifyFiles = compreResult.leftAloneFiles.stream().filter(leftFile -> {
            String leftPath = FilenameUtil.getSubPathUnix(leftFile.getAbsolutePath(), leftDirStr);
            return !modifyPaths.contains(leftPath);
        }).collect(Collectors.toList());
        compreResult.rightAloneNotModifyFiles = compreResult.rightAloneFiles.stream().filter(rightFile -> {
            String rightPath = FilenameUtil.getSubPathUnix(rightFile.getAbsolutePath(), rightDirStr);
            return !modifyPaths.contains(rightPath);
        }).collect(Collectors.toList());
        //左右相等，有可能路径改变，逻辑上应该看做移动，此处不做细分，认为是相等的。
        return compreResult;
    }

}
