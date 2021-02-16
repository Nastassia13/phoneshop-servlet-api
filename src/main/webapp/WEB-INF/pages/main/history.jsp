<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="history" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Price history">
  <h1>Price history</h1>
  <h2>${product.description}</h2>
  <table>
    <thead>
      <tr>
        <td><b>Start date</b></td>
        <td><b>Price</b></td>
      </tr>
    </thead>
    <c:forEach var="hist" items="${history}">
      <tr>
        <td>
          <fmt:formatDate value="${hist.startDate}" type="date"/>
        </td>
        <td>
          <fmt:formatNumber value="${hist.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td
      </tr>
    </c:forEach>
  </table>
  <p>
    <a href="${pageContext.servletContext.contextPath}">Back</a>
  </p>
</tags:master>