<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Not found">
  <h1>
    ${pageContext.exception.getClassName()} with code ${pageContext.exception.id} not found
  </h1>
  <tags:goToMainPage/>
</tags:master>