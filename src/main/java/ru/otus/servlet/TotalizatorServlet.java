package ru.otus.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.PushBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.TotalizatorConstant.*;
import static ru.otus.util.EmailRandomizer.*;

/**
 * Servlet implementation of Totalizator
 */
@WebServlet(SLASH + TOTALIZATOR_URL)
@MultipartConfig
public class TotalizatorServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(TotalizatorServlet.class.getName());
	
	private static final long serialVersionUID = 5885899233778066005L;
	
	/**
	 * Output files with obfuscated emails
	 */
	public static final String DESTINATION_FILE = "obfuscatedEmails.csv";
	
	/**
	 * Congratulation JSP
	 */
	private static final String WINNER_JSP = "/WEB-INF/jsp/winners.jsp";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final List<String> allLines = new ArrayList<>();
		// process all files from multiple file input
		request.getParts().stream()
			.filter(part -> FILE_INPUT_NAME.equals(part.getName())
					&& part.getSubmittedFileName().toLowerCase().endsWith(AVAILABLE_FOR_UPLOADING_FILE_EXTENSION))// Retrieves <input type="file" name="file" multiple="true">
			.forEach(part -> {
				// Scanner тоже неплох :)
				try (BufferedReader buffer = new BufferedReader(
						new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {
					allLines.addAll(buffer.lines().collect(Collectors.toList()));
				}
				catch (IOException e){
					e.printStackTrace();
				}
			});

		ServletContext context = getServletContext();
		List<String> obfuscatedEmailsToFile = saveObfuscatedEmailsToFile(context.getRealPath(SLASH) + DESTINATION_FILE, allLines);
		request.setAttribute(WINNER_REQUEST_ATTRIBUTE, getWinnersCommaSeparated(
				getSeed(context), obfuscatedEmailsToFile, getWinnerCount(context)));

		PushBuilder pushBuilder = request.newPushBuilder(); // push builder works only with https
        logger.info("Push begin");
		if (pushBuilder != null) {
			pushGif(request, pushBuilder, "images/congrats.gif");
		}
		request.getRequestDispatcher(WINNER_JSP).forward(request, response);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath());
	}

	private void pushGif(HttpServletRequest request, PushBuilder pushBuilder, String path) {
        pushBuilder
				.path(path)
					.addHeader("content-type", "image/gif")
				.push();
		logger.info("Push end");
	}
}
