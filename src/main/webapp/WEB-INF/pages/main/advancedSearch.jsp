<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="errors" class="java.util.HashMap" scope="request"/>
<tags:master pageTitle="Product List">
  <h1>
    Advanced search
  </h1>
  <form>
    <p>
      Description:
      <input name="description" value="${param.description}">
      <select name="typeSearch">
        <option>ALL_WORDS</option>
        <option>ANY_WORDS</option>
      </select>
    </p>
    <p>
      Min price:
      <input name="minPrice" value="${param.minPrice}">
    </p>
    <p>
      Max price:
      <input name="maxPrice" value="${maxPrice}">
    </p>
    <button>Search</button>
  </form>
  <c:if test="${not empty param.message and empty errors}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty errors}">
    <p class="error">
      There were errors adding cart
    </p>
  </c:if>
  <form method="post">
    <table>
      <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
        </td>
        <td class="price">
          Price
        </td>
      </tr>
      </thead>
      <c:forEach var="product" items="${products}">
        <tr>
          <td>
            <img class="product-tile" src="${product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                ${product.description}
            </a>
          </td>
          <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/history/${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </form>
  <tags:recentlyViewed/>
</tags:master>