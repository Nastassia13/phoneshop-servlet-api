<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product">
  <p>
    Cart:
    <c:forEach var="item" items="${cart.getItems()}">
      <p> &#8226; ${item}</p>
    </c:forEach>
  </p>
  <c:if test="${not empty param.message and empty error}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty error}">
    <p class="error">
        There was an error adding to cart
    </p>
  </c:if>
  <p>
    ${product.description}
  </p>
  <form method="post">
    <table>
      <tr>
        <td>Image</td>
        <td>
          <img src="${product.imageUrl}">
        </td>
      </tr>
      <tr>
        <td>Code</td>
        <td>
          ${product.code}
        </td>
      </tr>
      <tr>
        <td>Stock</td>
        <td>
          ${product.stock}
        </td>
      </tr>
      <tr>
        <td>Price</td>
        <td class="price">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
      <tr>
        <td>Quantity</td>
        <td>
          <input name="quantity" class="quantity" value="${not empty error ? param.quantity : 1}">
          <c:if test="${not empty error}">
            <p class="error">
              ${error}
            </p>
          </c:if>
        </td>
      </tr>
    </table>
    <p><button>Add to cart</button></p>
  </form>
  <tags:recentlyViewed/>
</tags:master>