package ru.otus.util;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailRandomizer {

	private final static String WINNER_COUNT_INIT_PARAM = "winnerCount";
	private final static String REGEXP_TO_OBFUSCATE_EMAIL = "(.*)@(.*)\\.(.*)";

	public static Stream<String> sweepstake(List<String> emails, int winnerCount) {
		return new EmailCollection(emails).randomEmails(winnerCount);
	}
	
	public static String getWinnersAsList(List<String> emails, int winnerCount) {
		return sweepstake(emails, winnerCount).collect(Collectors.joining(", "));
	}
	
	public static List<String> saveObfuscatedEmailsToFile(String fileName, List<String> emails) throws IOException {
		List<String> obfuscatedEmails = emails.stream()
				.map(email -> email.replaceAll(REGEXP_TO_OBFUSCATE_EMAIL, "$1@****.$3"))
			    .sorted(Comparator.comparing(String::hashCode))
			    .collect(Collectors.toList());
		
		Files.write(Paths.get(fileName), obfuscatedEmails);
		
		return obfuscatedEmails;
	}
	
	public static int getWinnerCount(ServletContext context) {
		return Integer.decode(context.getInitParameter(WINNER_COUNT_INIT_PARAM));
	}
}
