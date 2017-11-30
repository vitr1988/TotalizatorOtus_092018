package ru.otus.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailRandomizer {
	
	private static final String EMAIL_SEPARATOR = "@";

	public static Stream<String> sweepstake(List<String> emails, int winnerCount) throws IOException {
		return new EmailCollection(emails).randomEmails(winnerCount);
	}
	
	public static String getWinnersAsList(List<String> emails, int winnerCount) throws IOException {
		return sweepstake(emails, winnerCount).collect(Collectors.joining(", "));
	}
	
	public static void saveToFileObfuscatedEmails(String fileName, List<String> emails) throws IOException {
		List<String> obfuscatedEmails = emails.stream()
			    .map(email -> email.contains(EMAIL_SEPARATOR) ? email.split(EMAIL_SEPARATOR)[0] : email)
			    .collect(Collectors.toList());
		
		Files.write(Paths.get(fileName), obfuscatedEmails);
	}
}
