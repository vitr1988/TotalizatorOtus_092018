package ru.otus.servlet;

import static ru.otus.TotalizatorConstant.WINNER_REQUEST_ATTRIBUTE;
import static ru.otus.util.EmailRandomizer.getWinnersAsList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/upload")
public class FileUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5885899233778066005L;
	
	private final static int WINNER_COUNT = 2;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
	      // Создание фабрики элементов дисковых файлов
	      FileItemFactory factory = new DiskFileItemFactory();
	      // Создание обработчика загрузки файла
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      try {
	        // Последовательный upload
	        List<FileItem> items = upload.parseRequest(request);
	        FileItem file = items.stream().findFirst().get();
	        List<String> allLines = new ArrayList<>();
	        try (Scanner scanner = new Scanner(file.getInputStream())) {
	        	while(scanner.hasNextLine()) {
	        		allLines.add(scanner.nextLine());
	        	}
	        }
	        request.setAttribute(WINNER_REQUEST_ATTRIBUTE, getWinnersAsList(allLines, WINNER_COUNT));
	        request.getRequestDispatcher("WEB-INF/jsp/winners.jsp").forward(request, response); 
	      }
	      catch(Exception e){
	    	  e.printStackTrace();
	      }
		}
	  }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath());
	}
}
