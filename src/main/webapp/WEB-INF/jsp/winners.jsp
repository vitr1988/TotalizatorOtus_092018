<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="static ru.otus.TotalizatorConstant.*"%>
<%@page import="static ru.otus.servlet.TotalizatorServlet.*"%>
<%@page import="static ru.otus.util.EmailRandomizer.COMMA_SEPARATOR"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Победители розыгрыша</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/style.css" rel="stylesheet">
	<script src="js/jquery.min.js"></script>
	<script>
		<% String winnerList = (String) request.getAttribute(WINNER_REQUEST_ATTRIBUTE);%>
        $(document).ready(function(){
			$('#winnerListDownload').on('click', function(){
			    var filename = 'winnerList.csv';
                var csvContent = '<%= winnerList.replaceAll("\\s+", "") %>'.replace(/<%= COMMA_SEPARATOR %>/g, '\r\n'); //replace all spaces
                var blob = new Blob([csvContent], {type: 'text/csv'});
                if(window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveBlob(blob, filename);
                }
                else{
                    var elem = window.document.createElement('a');
                    elem.href = window.URL.createObjectURL(blob);
                    elem.download = filename;
                    document.body.appendChild(elem);
                    elem.click();
                    document.body.removeChild(elem);
                }
			});
        });
	</script>
</head>
<body>
	<div class="col-sm-4 flex-container">
        <div class="col-sm-12 thumbnail text-center">
        	<a href="<%= DESTINATION_FILE %>" download="emails.csv" target="_blank">
            	<img alt="Поздравляем победителей" class="img-responsive" src="images/congrats.gif"/>
            </a>
            <div class="caption">
				<h4 id="winnerListDownload"><%= winnerList %></h4>
	        </div>
	    </div>
	</div>
</body>
</html>