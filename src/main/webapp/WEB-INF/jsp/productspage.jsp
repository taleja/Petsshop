<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Pets shop products</title>
</head>
<body>
<h1>Products</h1>
 
<c:url var="addUrl" value="/petsshop/welcome/addProduct" />
<a href="${addUrl}">Add</a>
<table style="border: 1px solid; width: 500px; text-align:center">
 <thead style="background:#fcf">
  <tr>
   <th>Product name</th>
   <th>Description</th>
   <th>Quantity</th>
   <th colspan="3"></th>
  </tr>
 </thead>
 <tbody>
 <c:forEach items="${products}" var="product">
 	<c:url var="editUrl" value="/petsshop/welcome/editProduct?id=${product.id}" />
    <c:url var="deleteUrl" value="/petsshop/welcome/delete?id=${product.id}" />
  <tr>
   <td><c:out value="${product.productName}" /></td>
   <td><c:out value="${product.description}" /></td>
   <td><c:out value="${product.quantity}" /></td>
   <td><a href="${editUrl}">Edit</a></td>
   <td><a href="${deleteUrl}">Delete</a></td>
  
  </tr>
 </c:forEach>
 </tbody>
</table>

<c:if test="${empty products}">
 There are currently no products in the list. <a href="${addUrl}">Add</a> a product.
</c:if>

</body>
</html>