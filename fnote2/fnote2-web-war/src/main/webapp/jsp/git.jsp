<%@ page import="fly.application.git.GitService" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Git</title>
</head>
<body>

<form action="git.jsp" method="post">

    <pre>
        status          cd /root/FlyData2022; git status -s
        pull            cd /root/FlyData2022; git pull
        commitAndPush   cd /root/FlyData2022; git add . ; git commit -am 'init' ; git push
    </pre>
    Git命令：
    <input type="radio" name="git" value="status"> status
    <input type="radio" name="git" value="pull">pull
    <input type="radio" name="git" value="commitAndPush">commitAndPush

    <input type="radio" name="git" value="pullAll">pullAll
    <br/>
    <input type="submit">
    <br/>
    <%
        String git = request.getParameter("git");
        if (StringUtils.isNotBlank(git)) {
            String result = GitService.exe(git);
            out.print(request.getParameter("git")+":<br/>"+result);
        } else {
            out.print("input empty");
        }

    %>
</form>
</body>
</html>
