<%@page import="fnote.user.domain.service.BlackIpService" %>
<%@ page language="java" import="fnote.user.domain.service.LoginService" pageEncoding="utf-8" %>
<%@ page import="fnote.user.domain.service.UserSessionService" %>
<%@ page import="fnote.web.common.FlyWebUtil" %>
<%@ page import="flynote.infrastructure.common.LogConst" %>
<%@ page import="fnote.user.domain.entity.LoginUser" %>
<%@ page import="fnote.common.web.SpringContextHolder" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.concurrent.atomic.AtomicLong" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>clientInfo</title>
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String clear = request.getParameter("clear");
    LoginService userService = SpringContextHolder.getBean("loginService");
    BlackIpService ipBlackUserService = SpringContextHolder.getBean("blackIpService");
    FlyWebUtil webUtil = SpringContextHolder.getBean("flyWebUtil");
    LoginUser loginUser = userService.getLoginUserByCookieCheckedSession(request);
    Map<String, AtomicLong> blackIps = new HashMap<String, AtomicLong>();
    boolean authFlag = userService.isAdmin(loginUser.pin);
    if (authFlag) {
        blackIps = ipBlackUserService.getAllLoginFailIps();
    }
    if (authFlag && StringUtils.isNotBlank(clear)) {

        ipBlackUserService.clearBlackIP();
        out.print("清除ip黑名单成功<br/>");

    }

    //user session
    UserSessionService userSessionService = SpringContextHolder.getBean("userSessionService");
    Map<String, LoginUser> userSessions=userSessionService.getUserSessions();


%>


<%
    if (authFlag) {
        for (Map.Entry<String, AtomicLong> entry : blackIps.entrySet()) {
%>
ip:<%=entry.getKey()%> 次数:<%=entry.getValue()%><br>
<%}%>
<br>

<a href="ipBlackUser.jsp?clear=true">清除ip黑名单</a>

<%}%>
usersession:<br>
<%=userSessions.size()%><br/>

<%for(Map.Entry<String,LoginUser> entryLoginUser:userSessions.entrySet()){%>
key:<%=entryLoginUser.getKey()%> pin:<%=entryLoginUser.getValue().pin%> sid:<%=entryLoginUser.getValue().getSid()%> <br>
<%}%>

</body>
</html>
<script>

</script>
