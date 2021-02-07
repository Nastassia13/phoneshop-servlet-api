<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true"%>
<%@ attribute name="order" required="true"%>

<a href="?sort=${sort}&order=${order}&query=${param.query}"
   style="${sort eq param.sort and order eq param.order ? 'font-size: 125%; color: aquamarine;' : ''}">
    ${order eq 'asc' ? '&#8679' : '&#8681'}
</a>
