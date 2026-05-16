package part4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import part3.CharCounter;
import part3.CharCounterImpl;

public class CharCounterUtil {

	/**
	 * Counts the letters in the provided File.
	 * The returned CharCounter accepts only letters.
	 *
	 * @param file the file to read
	 * @return the CharCounter with letter counts
	 * @throws IOException if reading goes wrong
	 */
	public static CharCounter countLetters(final File file) throws IOException {
		CharCounterImpl cc = new CharCounterImpl(Character::isLetter);
		try (FileInputStream stream = new FileInputStream(file)) {
    		cc.countChars(stream);
		}
		return cc;
	}

	/**
	 * Computes a measure of distance between character frequencies.
	 * This is useful for guessing the language in a fragment of text.
	 * For each of the counted characters, sum the square of difference in frequency.
	 * The frequency of a char c in a CharCounter cc,
	 * is the count of c in cc divided by total char count in cc.
	 *
	 * @param cc1
	 * @param cc2
	 * @return
	 */
	public static double computeDistance(final CharCounter cc1, final CharCounter cc2) {
		double charTotCount1 = cc1.getTotalCharCount();
		double charTotCount2 = cc2.getTotalCharCount();
		Collection<Character> charsCounted1 = cc1.getCountedChars();
		Collection<Character> charsCounted2 = cc2.getCountedChars();
		List<Double> freqDiff = new ArrayList<>();
		for (char c : charsCounted1) {
			if (charsCounted2.contains(c)) {
				freqDiff.add(((double) cc2.getCharCount(c) / charTotCount2) - ((double) cc1.getCharCount(c) / charTotCount1)); // adding difference for the intersecting characters
				charsCounted2.remove(c);
			}
			else {
				freqDiff.add(((double) cc1.getCharCount(c) / charTotCount1)); // adding frequency for unique elements for cc1
			}
		}
		charsCounted2.stream().forEach(c -> freqDiff.add((double) cc2.getCharCount(c) / charTotCount2)); // adding frequency for unique elements for cc2
		double distance = 0;
		for (double d : freqDiff) {
			distance += d * d;
		}
		return distance;
	}

	/**
	 * Returns an unmodifiable CharCounter that is a view of the CharCounter-argument.
	 * Query/read operations on the returned CharCounter "read through"
	 * to the specified CharCounter, and attempts to modify the returned
	 * CharCounter result in an UnsupportedOperationException.
	 *
	 * @param the CharCounter for which an unmodifiable view is to be returned
	 * @return the unmodifiable view of the specified CharCounter
	 */
	public static CharCounter unmodifiableCharCounter(final CharCounter delegate) {
		return new CharCounter() {
			@Override
			public Collection<Character> getCountedChars() {
				return delegate.getCountedChars();
			}

			@Override
			public boolean acceptsChar(char c) {
				return delegate.acceptsChar(c);
			}

			@Override
			public int getTotalCharCount() {
				return delegate.getTotalCharCount();
			}

			@Override
			public int getCharCount(char c) {
				return delegate.getCharCount(c);
			}

			@Override
			public void countChar(char c, int increment) {
				throw new UnsupportedOperationException();
			}
		};
	}
}
