package part3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of CharCounter that accepts only letters,
 * based on the corresponding method in Character.
 */

public class CharCounterImpl2 implements CharCounter {
	private final String acceptedChars = "qwertyuiopasdfghjklzxcvbnm"; // assuming english alphabet
	private int[] counters = new int[26]; // make an int array in alphabetic order normalized 
	// TODO: fields, but no use of Map or Map-implementation

	@Override
	public boolean acceptsChar(final char c) {
		return acceptedChars.indexOf(Character.toLowerCase(c)) != -1;
	}

	@Override
	public void countChar(final char c, final int increment) throws IllegalArgumentException {
		if (increment < 1 || !acceptsChar(c)) throw new IllegalArgumentException();
		if (acceptsChar(c)) this.counters[Character.toLowerCase(c) - 'a'] += increment;
	}

	@Override
	public Collection<Character> getCountedChars() {
		List<Character> countedChars = new ArrayList<>();
		int i;
		for (i = 0; i < 26; i++) {
			if (counters[i] != 0) countedChars.add((char) (i + 'A')); // could change the normalisation if i want lower case
		}
		return countedChars;
	}

	@Override
	public int getCharCount(final char c) {
		if (acceptsChar(c)) return this.counters[Character.toLowerCase(c) - 'a'];
		return 0;
	}

	@Override
	public int getTotalCharCount() {
		return Arrays.stream(this.counters).sum();
	}

	public static void main(String[] args) {
		
		final CharCounterImpl2 counter = new CharCounterImpl2();
		System.out.println(counter.acceptsChar('A')); // true
		System.out.println(counter.acceptsChar('1')); // false
		counter.countChar('A', 2);
		counter.countChar('B', 3);
		System.out.println(counter.getCharCount('A')); // 2
		System.out.println(counter.getTotalCharCount()); // 5
		System.out.println(counter.getCountedChars()); // [A, B]
	}

}
