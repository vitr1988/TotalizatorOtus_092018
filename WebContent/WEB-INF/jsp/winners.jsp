<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="static ru.otus.TotalizatorConstant.*"%>
<%@page import="static ru.otus.servlet.TotalizatorServlet.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Победители</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/style.css" rel="stylesheet">
	<script src="js/jquery.min.js"></script>
</head>
<body>
	<div class="col-sm-4 flex-container">
        <div class="col-sm-12 thumbnail text-center">
        	<a href="<%= DESTINATION_FILE %>" download="emails.csv" target="_blank">
            	<img alt="Поздравляем" class="img-responsive" src="images/congrats.gif"/>
            </a>
            <div class="caption">
                <h4><%= request.getAttribute(WINNER_REQUEST_ATTRIBUTE) %></h4>
	        </div>
	    </div>
	</div>
</body>
</html>