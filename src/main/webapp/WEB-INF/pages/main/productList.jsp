<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="errors" class="java.util.HashMap" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
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
          <tags:sortLink sort="description" order="asc"/>
          <tags:sortLink sort="description" order="desc"/>
        </td>
        <td class="quantity">
          Quantity
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc"/>
          <tags:sortLink sort="price" order="desc"/>
        </td>
      </tr>
      </thead>
      <c:forEach var="product" items="${products}" varStatus="status">
        <tr>
          <td>
            <img class="product-tile" src="${product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}
            </a>
          </td>
          <td class="quantity">
            <fmt:formatNumber var="quantity" value="1"/>
            <c:set var="error" value="${errors[product.id]}"/>
            <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : quantity}" class="quantity">
            <c:if test="${not empty error}">
              <p class="error">
                ${errors[product.id]}
              </p>
            </c:if>
            <input type="hidden" name="productId" value="${product.id}">
          </td>
          <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/history/${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </a>
          </td>
          <td>
            <button name="button${product.id}"
                    formaction="${pageContext.servletContext.contextPath}/products">
              Add to cart
            </button>
        </tr>
      </c:forEach>
    </table>
  </form>
  <tags:recentlyViewed/>
</tags:master>