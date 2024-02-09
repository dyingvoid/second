<%@ page import="java.io.File" %>
<%@ page import="com.example.second.domain.Explorer" %><%--
  Created by IntelliJ IDEA.
  User: Dying
  Date: 2/9/2024
  Time: 1:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Files</title>
</head>
<body>
    <h1><%= request.getAttribute("path")%></h1>
    <ul>
        <%
            File[] files = (File[]) request.getAttribute("files");
            for(File file : files){
        %>
                <li><a href="files?<%=Explorer.filterPath(file.getAbsolutePath())%>"><%=file.getName()%></a></li>
        <%
            }
        %>
    </ul>
</body>
</html>
