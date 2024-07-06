package farticle.domain.entity;

import farticle.domain.view.FileFilter;
import fnote.domain.config.FlyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class Dtree {
    static final Logger log = LoggerFactory.getLogger(Dtree.class);
    private List<DtreeObj> dtreeObjs = new ArrayList<>();
    private AtomicInteger idGen = new AtomicInteger(0);
    private int rootId = -1;

    public Dtree() {
    }

    public Dtree(String name, String url) {
        this.addRoot(name, url);
    }

    public DtreeObj genNextDtreeObj(int pid, String name, String url) {
        int id = idGen.getAndIncrement();
        DtreeObj dtreeObj = new DtreeObj(id, pid, name, url);
        return dtreeObj;
    }

    private DtreeObj genNextDtreeObjNotTrim(int pid, String name, String url) {
        int id = idGen.getAndIncrement();
        DtreeObj dtreeObj = new DtreeObj(id, pid, name, url, false);
        return dtreeObj;
    }

    public DtreeObj addDtreeObjToRoot(String name, String url) {
        return this.addDtreeObj(this.getRoot().getId(), name, url);
    }

    public DtreeObj addDtreeObj(int pid, String name, String url) {
        DtreeObj dtreeObj = genNextDtreeObj(pid, name, url);
        dtreeObjs.add(dtreeObj);
        return dtreeObj;
    }

    public DtreeObj addDtreeObjNotTrim(int pid, String name, String url) {
        DtreeObj dtreeObj = genNextDtreeObjNotTrim(pid, name, url);
        dtreeObjs.add(dtreeObj);
        return dtreeObj;
    }

    public DtreeObj addRoot(String name, String url) {
        if (dtreeObjs.size() != 0) {
            throw new RuntimeException("already had root");
        }
        int id = idGen.getAndIncrement();
        DtreeObj dtreeObj = new DtreeObj(id, rootId, name, url);
        dtreeObjs.add(dtreeObj);
        return dtreeObj;
    }

    public void remove(DtreeObj dtreeObj) {
        dtreeObjs.remove(dtreeObj);
    }

    public List<DtreeObj> getDtreeObjs() {
        return dtreeObjs;
    }

    public DtreeObj getRoot() {
        if (dtreeObjs.size() == 0) {
            return null;
        }
        return dtreeObjs.get(0);
    }

    private static Map<String, Integer> sortMap = new HashMap<>();

    {
        sortMap.put("第一章", 1);
        sortMap.put("第二章", 2);
        sortMap.put("第三章", 3);
        sortMap.put("第四章", 4);
        sortMap.put("第五章", 5);
        sortMap.put("第六章", 6);
        sortMap.put("第七章", 7);
        sortMap.put("第八章", 8);
        sortMap.put("第九章", 9);
        sortMap.put("第十章", 10);
        sortMap.put("第十一章", 11);

    }

    public int isSpe(String name) {
        for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {
            if (name.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public int addFoldersWithChildren(File file, int parentTreeTd, BiConsumer<DtreeObj, File> floderConsumer, BiConsumer<DtreeObj, File> fileConsumer, FileFilter filePredicate, boolean delEmpty, FlyContext flyContext) {
        AtomicInteger count = new AtomicInteger(0);
        if (!file.exists()) {
            log.error("not exists:" + file.getAbsolutePath());
            return 0;
        }
        if (!file.isDirectory()) {
            return 0;
        }
        if (file.getName().startsWith(".")) {
            return 0;
        }
        if (!filePredicate.test(file, flyContext)) {
            return 0;
        }
        File[] files = file.listFiles();
        if (null == files) {
            return 0;
        }
        //得到子文件
        List<File> fileList = Arrays.asList(files);
        //排序子文件，文件夹靠前，文件名排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {

                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                int s1 = isSpe(o1.getName());
                int s2 = isSpe(o2.getName());
                if (s1 != -1 && s2 != -1) {
                    return s1 - s2;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        //遍历子文件
        for (File cfile : fileList) {
            //忽略隐藏文件
            if (cfile.getName().startsWith(".")) {
                continue;
            }
            if (!filePredicate.test(cfile, flyContext)) {
                continue;
            }
            if (cfile.isDirectory()) {
                if (cfile.getName().contains("-hidden")) {
                    continue;
                }
                if (cfile.getName().equals(".git")) {
                    continue;
                }
                String url = "";
                String name = cfile.getName().replaceAll("'", "-");
                count.incrementAndGet();
                DtreeObj dtreeObj = this.addDtreeObj(parentTreeTd, name, "");
                floderConsumer.accept(dtreeObj, cfile);
                //递归
                if (addFoldersWithChildren(cfile, dtreeObj.getId(), floderConsumer, fileConsumer, filePredicate, delEmpty, flyContext) <= 0 && delEmpty) {
                    this.dtreeObjs.remove(dtreeObj);
                }
            } else {
                if (filePredicate.test(cfile, flyContext)) {
                    DtreeObj dtreeObj = this.addFile2Dtree(parentTreeTd, "", cfile);
                    fileConsumer.accept(dtreeObj, cfile);
                    count.incrementAndGet();

                }

            }
        }
        return count.get();
    }


    public DtreeObj addFile2Dtree(int parentTreeTd, String fileUrl, File cfile) {
        String url = fileUrl + URLEncoder.encode(cfile.getAbsolutePath(), StandardCharsets.UTF_8);
        String name = cfile.getName().replaceAll("'", "-");
        return this.addDtreeObj(parentTreeTd, name, url);
    }


    public static void main(String[] args) {
        System.out.println("### 1.1 个人随想".replaceAll("#", ""));
    }


}
