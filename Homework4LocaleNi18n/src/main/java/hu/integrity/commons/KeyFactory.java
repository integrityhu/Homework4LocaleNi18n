package hu.integrity.commons;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KeyFactory {

	private static final int KEY_LEN = 9;
	private static final Random RND = new SecureRandom();
	private static List<Character> KEY_LETTERS = Arrays.asList('Q', 'W', 'E',
			'R', 'T', 'Z', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K',
			'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M', '2', '3', '4', '5', '6',
			'7', '8', '9');

	public static String generateKey() {
		return generateKey(KEY_LEN);
	}

	public static synchronized String generateKey(int len) {
		Collections.shuffle(KEY_LETTERS, RND);
		int n = Math.min(len, KEY_LETTERS.size());
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			sb.append(KEY_LETTERS.get(i));
		}
		return sb.toString();
	}

}
