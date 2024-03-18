<%@ page import="java.io.File" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        <%@include file="css/style.css"%>
    </style>
    <title>Files</title>
</head>
<body>
<div class="logout" id="logout">
    <form action="logout" method="post">
        <button type="submit" class="lobtn">Log out</button>
    </form>
</div>
<h1><%= request.getAttribute("path")%></h1>
<h2><a href=<%=request.getAttribute("back")%>>Go back</a></h2>
<h3><%=LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))%></h3>
<ul>
    <%
        File[] files = (File[]) request.getAttribute("files");
        for(File file : files){
    %>
            <li>
                <a href="files?<%=file.getAbsolutePath().replace("\\", "/")%>">
                    <%=file.getName()%>
                </a>
            </li>
    <%
        }
    %>
</ul>
</body>
</html>
