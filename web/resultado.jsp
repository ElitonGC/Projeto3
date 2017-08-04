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
        <h1>Parabéns <b>${pessoa.nome}</b>!</h1> <br>
        Você acaba de logar no sistema. <br>
        O seu login é '${pessoa.login}' e o seu e-mail é '${pessoa.email}' <br>
        Hoje é Fri Jul 07 08:03:14 BRT 2017
    </body>
</html>