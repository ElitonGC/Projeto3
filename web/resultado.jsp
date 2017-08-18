<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resultado</title>
    </head>
    <body>
    <c:if test = "true">    
        <% System.out.println("Testando"); %>
        <c:forEach items="${respostas}" var="resp">    
            <% System.out.println("Aqui"); %>
            <a href="${resp.link}">${resp.titulo}</a><br>
            ${resp.texto}<br>
        </c:forEach>
    </c:if>
</body>
</html>