package fnote.filebrowser.web.controller;

import farticle.domain.view.TreeService;
import fly4j.common.file.FileUtil;
import fly4j.common.util.FlyPreconditions;
import fnote.common.StorePathService;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.filebrowser.core.BrowserResult;
import fnote.filebrowser.core.FileComp;
import fnote.filebrowser.core.FileVo4List;
import fly4j.common.domain.FlyResult;
import fly4j.common.http.FileUpload;
import fly4j.common.http.WebUtil;
import fly4j.common.util.RepositoryException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 网盘文件查看控制器
 * 文件管理：已经委托给BrowserService，根据参数管理文件。
 * 上传下载：fly-http提供技术支持，但业务性不强；需要在browser中提供业务支持。
 * fly-brwser模块就会依赖http，
 *
 * @author qryc
 */
@Controller // 用于标识是处理器类
@RequestMapping("/browser") // 请求到处理器功能方法的映射规则
public class BrowserController {
    static final Logger log = LoggerFactory.getLogger(BrowserController.class);

    private int fileuploadMaxSize = 5120000;
    @Resource
    private StorePathService pathService;
    @Resource
    private FlyContextFacade flyContextFacade;
    @Resource
    private TreeService dtreeUtil;

    @RequestMapping(value = "listPic.do")
    public String listPic(HttpServletRequest request, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        this.validate(request, resp);
        Path rootPath = this.getDiskRootPath(request, resp);
        List<String> images = new ArrayList<>();
        FileUtil.walkAllFileIgnoreHidden(rootPath.toFile(), null, file -> {
            if (FileUtil.isImg(file)) {
                images.add(FileVo4List.buildImagHref(file, 300));
            }
        });
        context.put("images", images);
        return "netdisk/listPic";
    }

    @RequestMapping(value = "createDir.do", method = RequestMethod.POST)
    public String createDir(HttpServletRequest request,
                            HttpServletResponse resp, ModelMap modelMap) throws RepositoryException, IOException {

        this.validate(request, resp);
        var currDir = getCurrDirPath(request, resp);
        var dirName = request.getParameter("dirName");
        Objects.requireNonNull(dirName);
        var inputFilePath = currDir.resolve(dirName);
        Files.createDirectories(inputFilePath);
        modelMap.put("operationResult", FlyResult.success("文件夹已经创建"));
        return getRedirectListUrl(request);

    }


    private Path getCurrDirPath(HttpServletRequest request, HttpServletResponse resp) throws RepositoryException {
        String currDirStr = WebUtil.getParameterStr(request, "currDir", "");
        //如果为空默认为当前用户目录
        if (StringUtils.isBlank(currDirStr)) {
            return null;
        }

        //解码
        currDirStr = URLDecoder.decode(currDirStr, StandardCharsets.UTF_8);

        //控制只能操作自己的目录
        return Path.of(currDirStr);
    }

    private Path getDefaultPath(HttpServletRequest request, HttpServletResponse resp) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(request, resp);
        FlyPreconditions.requireNotEmpty(flyContext.loginUser(), "guest can't browser");
        if (flyContext.isAdmin()) {
            return Path.of(System.getProperty("user.home"));
        }
//        if ("company".equals(flyContext.clientConfig().getExtStringValue("workspace"))) {
//            return pathService.getUserDirPath(FlyConst.PATH_ARTICLE,flyContext.getPin()).resolve("pubDir");
//        }
        return pathService.getURootPath(flyContext.getPin());
    }


    @RequestMapping(value = "move.do")
    public String move(HttpServletRequest request,
                       HttpServletResponse resp, ModelMap modelMap) throws RepositoryException, IOException {
        this.validate(request, resp);
        var currDir = getCurrDirPath(request, resp);
        var destName = request.getParameter("destName");
        if (StringUtils.isNotBlank(destName)) {
            String[] selectFiles = request.getParameterValues("selectFiles");
            for (String fileName : selectFiles) {
                var rootPath = this.getDiskRootPath(request, resp);
                var sourcePath = rootPath.resolve(URLDecoder.decode(fileName));
                Files.move(sourcePath, rootPath.resolve(destName).resolve(sourcePath.getFileName()));
            }
            modelMap.put("operationResult", FlyResult.success("成功"));
        } else {
            modelMap.put("operationResult", FlyResult.success("destName is Empty"));
        }

        return getRedirectListUrl(request);

    }

    //重命名文件
    @RequestMapping(value = "rename.do")
    public String rename(HttpServletRequest request,
                         HttpServletResponse resp, ModelMap modelMap) throws RepositoryException, IOException {
        this.validate(request, resp);
        var currDir = getCurrDirPath(request, resp);
        var newName = request.getParameter("newName");
        String[] selectFiles = request.getParameterValues("selectFiles");
        var sourcePath = this.getDiskRootPath(request, resp).resolve(URLDecoder.decode(selectFiles[0]));
        Files.move(sourcePath, sourcePath.getParent().resolve(newName));
        modelMap.put("operationResult", FlyResult.success("重命名成功"));
        return getRedirectListUrl(request);

    }

    /**
     * 总结：与其建立个大的对象来承载显示内容，不如直接每个地方拼接。
     * 文件列表
     * //        URLEncoder.encode(f.getAbsolutePath())
     * //        browserResult.dir2linkdir = BroserUtil.dir2linkdir(currDir, "/browser/list.do.vm", browserResult.getSortMode());
     */
    @RequestMapping(value = "list.do")
    public String list(HttpServletRequest request,
                       HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        try {
            this.validate(request, resp);

            return showListData(request, resp, modelMap);
        } catch (Exception e) {
            log.error("list", e);
            throw new RuntimeException(e);
        }
    }


    // 文件上传
    @RequestMapping(value = "upload.do")
    public String upload(HttpServletRequest request,
                         HttpServletResponse resp, ModelMap modelMap) throws Exception {
        this.validate(request, resp);
        FileUpload.uploadFile(request, getCurrDirPath(request, resp), this.fileuploadMaxSize);
        modelMap.put("operationResult", FlyResult.success("文件上传成功"));

        return getRedirectListUrl(request);
    }

    private static String getRedirectListUrl(HttpServletRequest request) {
        return "redirect:/browser/list.do?currDir=" + URLEncoder.encode(WebUtil.getParameterStr(request, "currDir", ""), StandardCharsets.UTF_8);
    }

    // 文件上传,tinymce结合
    @RequestMapping(value = "uploadImag.do")
    public @ResponseBody
    Map<String, Object> uploadImag(HttpServletRequest request,
                                   HttpServletResponse resp, ModelMap modelMap) throws Exception {
        Map<String, Object> result = new HashMap<>();
        this.validate(request, resp);
        String relativeWebRootPath = FileUpload.uploadFileNewName(request, getCurrDirPath(request, resp), this.fileuploadMaxSize);
        modelMap.put("operationResult", FlyResult.success("文件上传成功"));
        result.put("location", "/viewFile?absolutePath=" + relativeWebRootPath);
        return result;
    }


    //删除文件
    @RequestMapping(value = "delete.do")
    public String delete(HttpServletRequest request,
                         HttpServletResponse resp, ModelMap modelMap) throws RepositoryException, IOException {
        this.validate(request, resp);
        String[] selectFiles = request.getParameterValues("selectFiles");

        for (String fileName : selectFiles) {
            var path = this.getDiskRootPath(request, resp).resolve(URLDecoder.decode(fileName));
            Files.deleteIfExists(path);
        }
        modelMap.put("operationResult", FlyResult.success("All files deleted"));
        return getRedirectListUrl(request);

    }

    /**
     * 显示文件列表
     *
     * @param fileStr pic/fileStr
     */
    @RequestMapping(value = "delOne.do")
    public String delOne(String fileStr, HttpServletRequest req,
                         HttpServletResponse resp, ModelMap modelMap) throws RepositoryException, IOException {
        //权限判定
        this.validate(req, resp);
        var filePath = this.getDiskRootPath(req, resp).resolve(fileStr);
        if (Files.notExists(filePath)) {
            return "redirect:/prompt/promptMsg?msg=File not Exsit:" + fileStr + "result";
        }
        FileUtils.forceDelete(filePath.toFile());
        return "redirect:/prompt/promptMsg?msg=file:redisDel:" + fileStr + "result ok";

    }

    private String showListData(HttpServletRequest request, HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(request, resp);
        Path currDirPath = getCurrDirPath(request, resp);
        Integer sortMode = WebUtil.getParameterInt(request, "sortModes", 1);
        Path rootPath = this.getDiskRootPath(request, resp);

        var browserResult = new BrowserResult(currDirPath, rootPath)
                .setSortMode4Pre(sortMode);
        modelMap.put("browserResult", browserResult);

        modelMap.put("currLocation", "netdisk");
        List<File> files = new ArrayList<>();

        //如果为空默认为当前用户目录
        if (null != currDirPath) {

            files.addAll(Arrays.asList(currDirPath.toFile().listFiles()));
            List<FileVo4List> fileInfos = files.stream()
                    .sorted(new FileComp(sortMode))
                    .map(file -> FileVo4List.buildFileInfo(browserResult, file, rootPath, flyContext.getPin())).collect(Collectors.toList());
            modelMap.put("fileInfos", fileInfos);
        }

        modelMap.put("rootPath", FlyContext.getCurrentWorkRootPath(flyContext.getPin()));

        List<Path> paths = new ArrayList<>();
//        paths.add(this.getDefaultPath(request, resp));
        paths.addAll(pathService.getUserDiskDirPaths(flyContext));
        modelMap.put("dtreeObjs", dtreeUtil.getDtree4Browser(flyContext, paths));

        return "netdisk/browser";
    }

//    @RequestMapping(value = "uploadImg.do")
//    public void uploadImg(HttpServletRequest req,
//                          HttpServletResponse resp, ModelMap context) throws RepositoryException {
//        var flyContext = flyContextFacade.getFlyContext(req, resp);
//        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");
//        PrintWriter writer = null;
//        resp.setCharacterEncoding("utf-8");
//        resp.setContentType("text/html; charset=utf-8");
//        boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
//
//        FlyPreconditions.checkStateTrue(!ajax, "not Ajax request");
//        //	System.out.println("ajax"+ajax+req.getHeader("X-Requested-With"));
//        try {
//            writer = resp.getWriter();
//
//            upload1(req);
//            writer.write("保存成功！");
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    private void upload1(HttpServletRequest request) {
//        //判断是否是多段数据（只有是多段数据才是文件上传）
//        //多段数据返回true，否则返回false
//        if (ServletFileUpload.isMultipartContent(request)) {
//            //创建FileItemFactory工厂实现类
//            FileItemFactory factory = new DiskFileItemFactory();
//            //创建用于解析数据的工具类ServletFileUpload类
//            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
//            //设置编码格式，解决上传文件名乱码问题
//            servletFileUpload.setHeaderEncoding("utf-8");
//            try {
//                //解析上传的数据，得到每一个表单项FileItem
//                List<FileItem> list = servletFileUpload.parseRequest(request);
//                //判断每个表单项是普通类型还是上传的文件
//                for (FileItem item : list) {
//                    if (item.isFormField()) {
//                        //普通表单项
//                        System.out.println("表单项的name：" + item.getFieldName());
//                        //使用UTF-8解析，防止乱码
//                        System.out.println("表单项的value：" + item.getString("UTF-8"));
//                    } else {
//                        //上传的文件
//                        System.out.println("表单项的name：" + item.getFieldName());
//                        System.out.println("上传的文件名为：" + item.getName());
//                        //将此文件写到d盘根目录，如果FileItem对象中的主体内容是保存在某个临时文件中，该方法顺利完成后，临时文件有可能会被清除
//                        item.write(new File("/Volumes/HomeWork/test/" + item.getName()));
//                        //删除临时文件，尽管当FileItem对象被垃圾收集器收集时会自动清除临时文件，但及时调用delete方法可以更早的清除临时文件，释放系统存储资源。
//                        item.delete();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public void validate(HttpServletRequest request, HttpServletResponse resp) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(request, resp);
        FlyPreconditions.requireNotEmpty(flyContext.loginUser(), "guest can't browser");

    }

    public Path getDiskRootPath(HttpServletRequest request, HttpServletResponse resp) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(request, resp);
        FlyPreconditions.requireNotEmpty(flyContext.loginUser(), "guest can't browser");
        if (flyContext.isAdmin()) {
            return Path.of(System.getProperty("user.home"));
        }
        Path currDirPath = getCurrDirPath(request, resp);
//     `   if (currDirPath != null && currDirPath.toString().contains("FlyCustom")) {
//            return pathService.getURootPath(StorePathService.PATH_CUSTOM, flyContext.getPin());
//        }`
        return pathService.getURootPath( flyContext.getPin());
    }

}
