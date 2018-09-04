package ru.otus.util;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailRandomizer {

	private final static String WINNER_COUNT_INIT_PARAM = "winnerCount";
	private final static String SEED_INIT_PARAM = "seed";
	private final static String REGEXP_TO_OBFUSCATE_EMAIL = "(.*)@(.*)\\.(.*)";
    public final static String COMMA_SEPARATOR = ",";

	public static Stream<String> sweepstake(String seedString, List<String> emails, int winnerCount) {
		return new EmailCollection(new Random(stringToSeed(seedString)), emails).randomEmails(winnerCount);
	}
	
	public static String getWinnersCommaSeparated(String seedString, List<String> emails, int winnerCount) {
		return sweepstake(seedString, emails, winnerCount).collect(Collectors.joining(COMMA_SEPARATOR + " "));
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

	public static String getSeed(ServletContext context) {
		return context.getInitParameter(SEED_INIT_PARAM);
	}

	private static long stringToSeed(String s) {
		if (s == null) {
			return 0;
		}
		long hash = 0;
		for (char c : s.toCharArray()) {
			hash = 31L*hash + c;
		}
		return hash;
	}
}
