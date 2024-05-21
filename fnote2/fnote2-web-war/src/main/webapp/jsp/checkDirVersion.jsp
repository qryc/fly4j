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

<form action="/jsp/checkDirVersion.jsp" method="post">
    文件： <input type="text" name="checkDir" value="<%=null==request.getParameter("checkDir")?"":request.getParameter("checkDir")%>" size="200"> <br/>
    MD5：
    <textarea rows="20" cols="100" name="inputStr"><%=null==request.getParameter("inputStr")?"":request.getParameter("inputStr")%></textarea> <br/>
    <input type="submit" value="检查文件清单">
    <br/>


    <br/>
    <%
        String checkDir = request.getParameter("checkDir");
        String inputStr = request.getParameter("inputStr");
        String result = DirCompareService.checkDirChangeByStr(checkDir, inputStr, StringConst.LF);
    %>
    <textarea rows="20" cols="100"><%=result%></textarea>
</form>
</body>
</html>
