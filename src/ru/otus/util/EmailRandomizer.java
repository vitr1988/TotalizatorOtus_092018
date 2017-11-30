package ru.otus.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailRandomizer {
	
	public static Stream<String> sweepstake(List<String> emails, int winnerCount) throws IOException {
		return new EmailCollection(emails).randomEmails(winnerCount);
	}
	
	public static String getWinnersAsList(List<String> emails, int winnerCount) throws IOException {
		return sweepstake(emails, winnerCount).collect(Collectors.joining(", "));
	}
}
