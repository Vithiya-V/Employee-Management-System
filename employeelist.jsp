<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>Employees</title>
</head>

<body>

<h2>Employee Management System</h2>

<a href="/employees/new">
Add Employee
</a>

<table border="1">

<tr>
<th>ID</th>
<th>Name</th>
<th>Email</th>
<th>Department</th>
<th>Salary</th>
<th>Action</th>
</tr>

<c:forEach var="emp" items="${employees}">

<tr>
<td>${emp.id}</td>
<td>${emp.firstName} ${emp.lastName}</td>
<td>${emp.email}</td>
<td>${emp.department}</td>
<td>${emp.salary}</td>

<td>
<a href="/employees/edit/${emp.id}">
Edit
</a>

<a href="/employees/delete/${emp.id}">
Delete
</a>
</td>

</tr>

</c:forEach>

</table>

</body>
</html>
