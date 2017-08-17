<%-- 
    Document   : Mensagem
    Created on : 07/07/2017, 07:59:14
    Author     : aluno
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <c:forEach items="${respostas}" var="resp">
        <a href="${resp.link}">${resp.titulo}</a><br>
        ${resp.texto}
    </c:forEach>
</body>
</html>