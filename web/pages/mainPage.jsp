<%--
  Created by IntelliJ IDEA.
  User: olbe0615
  Date: 17.02.2020
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
<div>Hello, <%= session.getAttribute("userName") %></div>
</br>
<div><a href="/ServletProjectGroup1_war_exploded/test?action=logOut">Exit</a></div>


</body>
</html>
