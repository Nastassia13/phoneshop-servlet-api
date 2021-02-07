<%@ page import="com.es.phoneshop.model.product.PriceHistory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<%! List<PriceHistory> reverseHistory;%>
<% reverseHistory = new ArrayList(product.getHistory());
   Collections.reverse(reverseHistory);
%>
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
    <c:forEach var="hist" items="<%= reverseHistory%>">
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