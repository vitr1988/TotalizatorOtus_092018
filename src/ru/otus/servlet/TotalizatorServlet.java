package ru.otus.servlet;

import static ru.otus.TotalizatorConstant.TOTALIZATOR_URL;
import static ru.otus.TotalizatorConstant.WINNER_REQUEST_ATTRIBUTE;
import static ru.otus.util.EmailRandomizer.getWinnerCount;
import static ru.otus.util.EmailRandomizer.getWinnersAsList;
import static ru.otus.util.EmailRandomizer.saveObfuscatedEmailsToFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
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
 * Servlet implementation of Totalizator
 */
@WebServlet(TOTALIZATOR_URL)
public class TotalizatorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5885899233778066005L;
	
	/**
	 * Output files with obfuscated emails
	 */
	public final static String DESTINATION_FILE = "obfuscatedEmails.txt";
	
	/**
	 * Congratulation JSP
	 */
	private final static String WINNER_JSP = "WEB-INF/jsp/winners.jsp";
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
	      // Создание фабрики элементов дисковых файлов
	      FileItemFactory factory = new DiskFileItemFactory();
	      // Создание обработчика загрузки файла
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      try {
	        FileItem file = (FileItem) upload.parseRequest(request).stream().findFirst().get();
	        List<String> allLines;
	        // Scanner тоже неплох :)
	        try (BufferedReader buffer = new BufferedReader(
	        		new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
	            allLines = buffer.lines().collect(Collectors.toList());
	        }
	        ServletContext context = getServletContext();
	        allLines = saveObfuscatedEmailsToFile(context.getRealPath(DESTINATION_FILE), allLines);
	        request.setAttribute(WINNER_REQUEST_ATTRIBUTE, getWinnersAsList(allLines, getWinnerCount(context)));
	        request.getRequestDispatcher(WINNER_JSP).forward(request, response); 
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
