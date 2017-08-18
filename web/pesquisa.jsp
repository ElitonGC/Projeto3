<%-- 
    Document   : Login
    Created on : 07/07/2017, 07:57:25
    Author     : aluno
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pesquisa</title>
    </head>
    <body>
    <c:if test = "true">
        ${mensagem}
    </c:if>
    <form action="PesquisaServlet" method="GET">      
        Pesquisa: <input type="text" name="pesquisa" /><br>
        <input type="submit" value="Pesquisar"/>
    </form>       
    <c:if test = "true">    
        <c:forEach items="${respostas}" var="resp">    
            <% System.out.println("Aqui");%>
            <a href="${resp.link}">${resp.titulo}</a><br>
            ${resp.texto}<br>
        </c:forEach>
    </c:if>
</body>
</html>