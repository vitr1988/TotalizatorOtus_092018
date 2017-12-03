package ru.otus.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

public class EmailRandomizer {

	private final static String WINNER_COUNT_INIT_PARAM = "winnerCount";

	public static Stream<String> sweepstake(List<String> emails, int winnerCount) throws IOException {
		return new EmailCollection(emails).randomEmails(winnerCount);
	}
	
	public static String getWinnersAsList(List<String> emails, int winnerCount) throws IOException {
		return sweepstake(emails, winnerCount).collect(Collectors.joining(", "));
	}
	
	public static List<String> saveObfuscatedEmailsToFile(String fileName, List<String> emails) throws IOException {
		List<String> obfuscatedEmails = emails.stream()
			    .map(email -> email.replaceAll("(.*)@(.*)\\.(.*)", "$1@****.$3"))
			    .sorted(Comparator.comparing(String::hashCode))
			    .collect(Collectors.toList());
		
		Files.write(Paths.get(fileName), obfuscatedEmails, StandardOpenOption.CREATE_NEW);
		
		return obfuscatedEmails;
	}
	
	public static int getWinnerCount(ServletContext context) {
		return Integer.decode(context.getInitParameter(WINNER_COUNT_INIT_PARAM));
	}
}
