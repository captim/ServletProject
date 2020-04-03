<%@ page import="test.model.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="test.dao.WebLogicDAOConnection" %>
<%@ page import="test.dao.OracleDAOConnection" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AllStudents</title>
</head>
<body>
<%
    List<Student> students = null;
    students = OracleDAOConnection.getInstance().selectAllStudents();
%>
<table border='1' cellpadding='2' width='100%'>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Scholarship</th>
    </tr>
    <%for (Student student : students) {%>


    <tr>
        <td><%=student.getId()%></td>
        <td><%=student.getName()%></td>
        <td><%=student.getScholarship()%></td>
    </tr>

    <%}%>
</table>
</body>
</html>
