<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty viewedProducts}">
<h2>Recently viewed</h2>
</c:if>
<c:forEach var="product" items="${viewedProducts}">
  <div>
    <p>
      <img class="product-tile" src="${product.imageUrl}">
    </p>
    <p>
      <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
        ${product.description}
      </a>
    </p>
    <p>
      <a href="${pageContext.servletContext.contextPath}/products/history/${product.id}">
        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
      </a>
    </p>
  </div>
</c:forEach>