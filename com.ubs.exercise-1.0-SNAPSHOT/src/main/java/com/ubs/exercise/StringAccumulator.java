package com.ubs.exercise;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringAccumulator {

	public static final String DEFAULT_DELIMITER = ",|\n";
	
	public static int add(final String numbers) {
		String numStr = numbers;
		
		if (numStr.isEmpty()) {
			return 0;
		} else {
			String delimiter = DEFAULT_DELIMITER;
			if (numStr.startsWith("//")) {
				String[] split = numStr.split("\n", 2);
				if (split.length > 1) {
					numStr = split[1];
					delimiter = split[0].substring(2);
					// handle delimiters
					Pattern symbol = Pattern.compile("[^A-Za-z0-9]");
					Pattern pipe = Pattern.compile("\\|");
					StringBuilder sb = new StringBuilder();
					for (String s : delimiter.split(""))
						sb.append(symbol.matcher(s).find() && !pipe.matcher(s).find() ? "\\" + s : s);
					delimiter = sb.toString();
				}
			}
			System.out.println("add:: " + numStr + ".split(" + delimiter + ")");

			Matcher regx_matcher = Pattern.compile(delimiter).matcher(numStr);
			long regx_count = 0;
			while (regx_matcher.find()){ regx_count++; }
			Matcher num_matcher = Pattern.compile("\\d+").matcher(numStr);
			long num_count = 0;
			while (num_matcher.find()){ num_count++; }

			if (num_count > regx_count) {
				// do nothing
			} else {
				throw new IllegalArgumentException("There are less numbers than the delimiters");
			}

			Pattern pattern = Pattern.compile(delimiter);			
			return Arrays.stream(pattern.split(numStr))
					// validate all numbers
					.mapToInt(s -> validateAndParseInt(s))
					 // number bigger than 1000 are ignored												
					.filter(i -> i <= 1000)
					.sum();
		}
	}

	private static int validateAndParseInt(String s) {
		if (s.trim().isEmpty()) {
			return 0;
			//throw new NumberFormatException(s);
		}
		int n = Integer.parseInt(s);
		
		// Negative numbers are not allowed
		if (n < 0) {
			throw new IllegalArgumentException("Negatives not allowed: " + n);
		}
		
		return n;
	}
}
