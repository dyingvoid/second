<%@ page import="com.example.second.GlobalManager" %><%--
  Created by IntelliJ IDEA.
  User: Dying
  Date: 3/14/2024
  Time: 4:58 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="login">
    <a href="${pageContext.request.contextPath}/register"><h1>register</h1></a>
    <label>Enter name</label>
    <input type="text" name="name" id="name"><br/>
    <label>Enter password</label>
    <input type="password" name="password" id="password"><br/>
    <button type="submit" class="registerbtn">Login</button>
</form>
</body>
</html>
