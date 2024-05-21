<%@ page import="backtool.service.compare.DirCompareService" %>
<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>检查文件夹差异</title>
</head>
<body>
<a href="check.jsp">检查文件夹差异</a> <a href="zip.jsp">压缩文件夹</a> <a href="/jsp/checkVersion.jsp">对比文件差异</a>

<form action="/jsp/check.jsp" method="post">
    左边： <input type="text" name="leftDirInput" value="<%=null==request.getParameter("leftDirInput")?"":request.getParameter("leftDirInput")%>" size="200"> <br/>
    右边： <input type="text" name="rightDirInput" value="<%=null==request.getParameter("rightDirInput")?"":request.getParameter("rightDirInput")%>" size="200"> <br/>
    <input type="submit" value="检文件夹差异(显示相同)">
    <br/>
    <%
        String leftDirInput = request.getParameter("leftDirInput");
        String rightDirInput = request.getParameter("rightDirInput");
        String result = DirCompareService.dirCompareAll(leftDirInput, rightDirInput, "<br/>");
        out.print(result);
    %>
</form>
</body>
</html>
