package ru.otus.util;

import java.util.*;
import java.util.stream.Stream;

class EmailCollection {
	
	private Random random;
	private List<String> emails;

	public EmailCollection(Random random, Collection<String> emails){
		Objects.requireNonNull(random);
		Objects.requireNonNull(emails);
		this.random = random;
		this.emails = new ArrayList<>(emails);
	}
	
	public String get(int index) {
		if (index < 0 || index >= size()){
			throw new IllegalArgumentException("Wrong parameter 'index' " + index);
		}
		return emails.get(index);
	}
	
	public int size() {
		return emails.size();
	}
	
	public Stream<String> randomEmails(int limit) {
		return random.ints(0, size())			// второй аргумент не включен
				.distinct()						// только уникальные значения
				.limit(limit)					// ограничиваем количество записей
				.mapToObj(this::get);			// трансформируем индекс в реальный email
	}
}
