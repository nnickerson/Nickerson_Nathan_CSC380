<%@ page import="Models.Restaurant" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: bwatson
  Date: 7/26/13
  Time: 8:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Place Order</title>
</head>
<body>
<h1>Choose a restaurant</h1>

<p><% Object rests = request.getAttribute("restaurants");
    for (Restaurant r : (List<Restaurant>) rests) {
        out.println(r.getName());
    } %>
</p>


</body>
</html>