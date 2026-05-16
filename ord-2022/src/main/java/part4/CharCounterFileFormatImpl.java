package part4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import part3.CharCounter;
import part3.CharCounterImpl;

/**
 * TODO: Document the file format here
 * a:2
b:1
c:1
d:1
 */
public class CharCounterFileFormatImpl implements CharCounterFileFormat {

	@Override
	public void save(final CharCounter cc, final OutputStream out) throws IOException {
		out.write(buildString(cc).getBytes());
	}

	@Override
	public CharCounterImpl load(final InputStream in) throws IOException {
		CharCounterImpl interCC = new CharCounterImpl(c -> true);
		loadInto(interCC, in); // need to load it to gather what chars are in the stream
		CharCounterImpl cc = new CharCounterImpl(interCC.getCountedCharsAsString()); // make the limit the chars in the stream
		for (char c : interCC.getCountedChars()) {
			cc.countChar(c, interCC.getCharCount(c));
		}
		return cc;
	}

	@Override
	public void loadInto(final CharCounter cc, final InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			try {
				char c = line.charAt(0); // first elem of the line is the char
				String[] parts = line.split(":");
				int n = Integer.valueOf(parts[parts.length - 1]); // the last element will be the int value, regardless of the char is ':' and parts hence is 3 long
				if (cc.acceptsChar(c)) cc.countChar(c, n);
			} catch (Exception e) {
				continue; // corrupted lines or nonaccepted characters -> continue
			}
		}

	}

	public String buildString(CharCounter cc) {
		StringBuilder sb = new StringBuilder();
		for (char c : cc.getCountedChars()) {
			sb.append(c + ":" + cc.getCharCount(c) + "\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		String filePath = "src/main/java/part4/CharCounterFileFormat-sample.txt";

		CharCounterImpl cc = new CharCounterImpl("abcd");
		cc.countChars("This is a test abcdefghijklmnopqrstuvwxyz");
		System.out.println("Counted: " + cc.getCountedCharsAsString());
		System.out.println("Total: " + cc.getTotalCharCount());

		CharCounterFileFormatImpl ls = new CharCounterFileFormatImpl();

		try (OutputStream os = new FileOutputStream(filePath)) {
			ls.save(cc, os);
			System.out.println("Saved to " + filePath);
		}

		try (InputStream is = new FileInputStream(filePath)) {
			CharCounterImpl loaded = ls.load(is);
			System.out.println("Loaded: " + loaded.getCountedCharsAsString());
			System.out.println("Loaded total: " + loaded.getTotalCharCount());
		}
	}
}
