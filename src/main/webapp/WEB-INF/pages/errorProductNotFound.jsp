<%@ page import="com.es.phoneshop.model.product.ProductNotFoundException" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%! ProductNotFoundException exception;%>
<% exception = (ProductNotFoundException) request.getAttribute("javax.servlet.error.exception");
   request.setAttribute("prodId", exception.getProductId());
%>

<tags:master pageTitle="Product not found">
  <h1>
    Product with code ${prodId} not found
  </h1>
  <tags:goToMainPage/>
</tags:master>