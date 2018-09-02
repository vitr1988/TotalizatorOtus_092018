package ru.otus.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.TotalizatorConstant.*;
import static ru.otus.util.EmailRandomizer.*;

/**
 * Servlet implementation of Totalizator
 */
@WebServlet(SLASH + TOTALIZATOR_URL)
@MultipartConfig(
	fileSizeThreshold = 6 * 1024 * 1024, // 6 MB
	maxFileSize = 10 * 1024 * 1024, // 10 MB
	maxRequestSize = 20 * 1024 * 1024 // 20 MB
)
public class TotalizatorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5885899233778066005L;
	
	/**
	 * Output files with obfuscated emails
	 */
	public static final String DESTINATION_FILE = "obfuscatedEmails.csv";
	
	/**
	 * Congratulation JSP
	 */
	private static final String WINNER_JSP = "WEB-INF/jsp/winners.jsp";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// process all files from multiple file input
		List<Part> fileParts = request.getParts().stream()
                .filter(part -> FILE_INPUT_NAME.equals(part.getName())
                        && part.getSubmittedFileName().toLowerCase().endsWith(AVAILABLE_FOR_UPLOADING_FILE_EXTENSION))
                .collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
		for (Part filePart : fileParts) {
			// Scanner тоже неплох :)
			List<String> allLines;
			try (BufferedReader buffer = new BufferedReader(
					new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8))) {
				allLines = buffer.lines().collect(Collectors.toList());
			}
			ServletContext context = getServletContext();
			allLines = saveObfuscatedEmailsToFile(context.getRealPath(SLASH) + DESTINATION_FILE, allLines);
			request.setAttribute(WINNER_REQUEST_ATTRIBUTE, getWinnersAsList(allLines, getWinnerCount(context)));
			request.getRequestDispatcher(WINNER_JSP).forward(request, response);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath());
	}
}
