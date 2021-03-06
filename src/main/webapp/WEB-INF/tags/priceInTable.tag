<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="label" required="true"%>
<%@ attribute name="priceValue" required="true"%>

<tr>
  <td></td>
  <td></td>
  <td class="price">
    ${label}
  </td>
  <td class="price">
    <fmt:formatNumber value="${priceValue}" type="currency" currencySymbol="$"/>
  </td>
</tr>