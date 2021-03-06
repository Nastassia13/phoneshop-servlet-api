<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" class="com.es.phoneshop.model.order.Order" scope="request"/>
<jsp:useBean id="errors" class="java.util.HashMap" scope="request"/>
<tags:master pageTitle="Order overview">
  <h1>Order overview</h1>
  <table>
    <thead>
    <tr>
      <td>
          Image
      </td>
      <td>
          Description
      </td>
      <td class="quantity">
          Quantity
      </td>
      <td class="price">
          Price
      </td>
    </tr>
    </thead>
    <c:forEach var="item" items="${order.items}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src="${item.product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
            ${item.product.description}
          </a>
        </td>
        <td class="quantity">
          ${item.quantity}
          <input type="hidden" name="productId" value="${item.product.id}">
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/history/${item.product.id}">
            <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
          </a>
        </td>
      </tr>
    </c:forEach>
    <tags:priceInTable label="Subtotal:" priceValue="${order.subtotal}"/>
    <tags:priceInTable label="Delivery cost:" priceValue="${order.deliveryCost}"/>
    <tags:priceInTable label="Total cost:" priceValue="${order.totalCost}"/>
  </table>
  <h2>Your details:</h2>
  <table>
    <tags:orderOverviewRow name="firstName" label="First name" order="${order}"/>
    <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"/>
    <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
    <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
    <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"/>
    <tags:orderOverviewRow name="paymentMethod" label="Payment method" order="${order}"/>
  </table>
</tags:master>