<%@ page import="backtool.service.compare.DirCompareService" %>
<%@ page import="fly4j.common.util.StringConst" %>
<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>检查文件夹差异</title>
</head>
<body>
<a href="/jsp/check.jsp">检查文件夹差异</a> <a href="zip.jsp">压缩文件夹</a> <a href="checkRemote.jsp">远程对比</a>

<form action="/jsp/checkVersion.jsp" method="post">
    文件： <input type="text" name="leftDirInput" value="<%=null==request.getParameter("leftDirInput")?"":request.getParameter("leftDirInput")%>" size="200"> <br/>
    文件夹： <input type="text" name="rightDirInput" value="<%=null==request.getParameter("rightDirInput")?"":request.getParameter("rightDirInput")%>" size="200"> <br/>
    <input type="submit" value="输出文件清单">
    <br/>

    /root/FlyData2023

    <br/>
    <%
        String leftDirInput = request.getParameter("leftDirInput");
        String rightDirInput = request.getParameter("rightDirInput");
        String result = DirCompareService.calMd5(leftDirInput, rightDirInput, StringConst.LF);
    %>
    <textarea rows="20" cols="100"><%=result%></textarea>
</form>
</body>
</html>
