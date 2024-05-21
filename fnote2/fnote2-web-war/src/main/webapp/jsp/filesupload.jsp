<%@ page import="fly4j.common.http.file.FileUpload" %>
<%@ page import="java.nio.file.Path" %>
<%@ page language="java" pageEncoding="gbk" %>

<%
    String flyClientToken = request.getParameter("flyClientToken");
    if (!"flyPas_s123096sd_4".equals(flyClientToken)) {

    }
    String imageUrl = FileUpload.uploadFile(request, Path.of("d:/back/"), 50);
    out.println(imageUrl);
    out.println("<br/>");

%>

<%--<script>--%>
<%--var imageUrl = '<%=imageUrl%>';--%>

<%--if (imageUrl != '') {--%>

<%--parent.UploadLoaded("<%=SiteConsts.domainUrl%>", imageUrl);--%>
<%--//alert("1");--%>

<%--//opener.document.all("src").value = imageUrl;--%>
<%--//window.close();--%>
<%--}--%>
<%--</script>--%>
<style type="text/css">
    .input {
        background: #fff;
        border-bottom: #808080 1px solid;
        border-left: #808080 1px solid;
        border-top: #808080 1px solid;
        border-right: #808080 1px solid;
    }
</style>
<body topmargin="0" leftmargin="0">
<form action="filesupload.jsp" method="post" enctype="multipart/form-data">
    <input type="file" name="file" size="30" class="input">
    <input type="submit" value="ÉÏ´«">
</form>
</body>