<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="static ru.otus.TotalizatorConstant.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Розыгрыш скидок на оплату обучения</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/style.css" rel="stylesheet">
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.fileinput.js"></script>
	<script>
		$(document).ready(function(){
			var fileExtensionError = $('#fileExtensionError');
			var fileInput = $('#<%= FILE_INPUT_NAME %>');
			fileInput.bootstrapFileInput();
			fileInput.on('change', function(){
				fileExtensionError.hide();
				if (!fileInput.val().match(/\.<%= AVAILABLE_FOR_UPLOADING_FILE_EXTENSION %>/)) {
					fileExtensionError.show();
					return;
				}
				this.form.submit();
			});
		});
	</script>
</head>
<body>
	<div class="flex-container">
		<form enctype="multipart/form-data" action="<%= TOTALIZATOR_URL %>" method="post">
			<div id="fileExtensionError" class="label label-default error">*выберите <%= AVAILABLE_FOR_UPLOADING_FILE_EXTENSION %>-файл</div>
			<label class="btn btn-default btn-file blockLabel" for="<%= FILE_INPUT_NAME %>">
				Загружаем файл со списком email участников розыгрыша:
				<input type="file" id="<%= FILE_INPUT_NAME %>" name="<%= FILE_INPUT_NAME %>" title="Загрузить" data-filename-placement="inside" data-fv-file-extension="<%= AVAILABLE_FOR_UPLOADING_FILE_EXTENSION %>" accept=".<%= AVAILABLE_FOR_UPLOADING_FILE_EXTENSION %>" multiple="true">
			</label>
		</form>
	</div>
</body>
</html>