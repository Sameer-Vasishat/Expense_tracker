package com.practice.expense.utilities;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import com.practice.expense.exception.BadRequestException;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.SimpleTokenizer;

@Component
public class Utils {

	private DocumentCategorizerME categorizer; // ✅ No static, keep it instance-based
	private DoccatModel model;

	@PostConstruct
	public void init() {
		// ✅ Load trained model once
		reload();

	}

	private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
		    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
		    DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm"),
		    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),
		    DateTimeFormatter.ofPattern("dd-MM-yyyy H:mm"),
		    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
		    DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"),
		    DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"),
		    DateTimeFormatter.ofPattern("dd MMM yyyy H:mm"),
		    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
		    DateTimeFormatter.ofPattern("dd-MM-yyyy"),
		    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
		    DateTimeFormatter.ofPattern("dd MMM yyyy")
		);
	public void reload() {
		try {
			//
			model = new DoccatModel(new FileInputStream("invoice-category-model.bin"));
			System.out.println("Invoice category model loaded successfully.");
			categorizer = new DocumentCategorizerME(model);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load invoice category model", e);
		}

	}

	/**
	 * Extracts last valid amount from OCR text //Limitation: 1.Not accurate with
	 * blurry images 2. May not work with all formats of invoices 3. May not work
	 * with all currencies 4. May not work with all languages 5. May not work in
	 * multi-line formats with column as value
	 */
	public Double extractAmount(String text) {
		if (text == null)
			return null;

		String normalized = text.toLowerCase().replaceAll("\\s+", " ");
		String[] keywords = { "total amount", "net amount", "gross amount", "amount due", "balance due", "total" };

		Double lastKeywordAmount = null;

		// Step 1: Look for keyword-based amounts
		for (String keyword : keywords) {
			// Example: "Total Amount: ₹ 1234.56" or "Total: 1234.56 inr"
			// This regex captures the keyword, optional currency symbol, and the amount
			 String regex = keyword + "\\s*[:\\-]?\\s*₹?\\s*(?:inr)?\\s*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]{1,2})?)";
		        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		        Matcher matcher = pattern.matcher(normalized);

		        while (matcher.find()) {
		            try {
		                String amountStr = matcher.group(1).replace(",", ""); // remove thousand separators
		                lastKeywordAmount = Double.parseDouble(amountStr);
		            } catch (Exception ignored) {}
		        }
		    }
		if (lastKeywordAmount != null)
			return lastKeywordAmount;

		// Step 2: Fallback decimal numbers like "1234.56" or "1234,56"
		java.util.regex.Pattern decimalPattern = java.util.regex.Pattern.compile("([0-9]+\\.[0-9]{1,2})");
		java.util.regex.Matcher decimalMatcher = decimalPattern.matcher(text);

		double maxAmount = 0.0;
		while (decimalMatcher.find()) {
			try {
				double value = Double.parseDouble(decimalMatcher.group(1).replace(",", ""));
				if (value > maxAmount)
					maxAmount = value;
			} catch (Exception ignored) {
			}
		}
		if (maxAmount > 0)
			return maxAmount;

		// Step 3: Fallback to currency prefixed
		java.util.regex.Pattern currencyPattern = java.util.regex.Pattern.compile("(?:₹|inr)\\s*([0-9]+)",
				java.util.regex.Pattern.CASE_INSENSITIVE);
		java.util.regex.Matcher currencyMatcher = currencyPattern.matcher(text);

		while (currencyMatcher.find()) {
			try {
				double value = Double.parseDouble(currencyMatcher.group(1).replace(",", ""));
				if (value > maxAmount)
					maxAmount = value;
			} catch (Exception ignored) {
			}
		}
		if (maxAmount <= 0)
			throw new BadRequestException("Could not extract amount from invoice");
		return maxAmount > 0 ? maxAmount : null;
	}

	/** Extract invoice/bill/receipt number */
	public String extractInvoiceNumber(String text) {
		if (text == null)
			return null;

		// Pass 1: same-line (Bill No: SM/2019-20/168)
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(
				"(?im)(?:bill|invoice|receipt)\\s*(?:no\\.?|number|#|wo\\.?|n0)?\\s*[:\\-]?\\s*([A-Z0-9][A-Z0-9\\-/]*)");
		java.util.regex.Matcher m = p.matcher(text);
		if (m.find())
			return m.group(1).trim();

		// Pass 2: label on one line, value on next

		return null;
	}

	/** Fuzzy string matching */
	public static boolean fuzzyMatch(String text, String target, int threshold) {
		LevenshteinDistance distance = new LevenshteinDistance();
		int score = distance.apply(text.toLowerCase(), target.toLowerCase());
		return score <= threshold;
	}

	/** Categorize invoice text using train data */
	public String extractCategory(String text) {
		if (categorizer == null)
			return "Miscellaneous";

		// Tokenize
		String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text);

		// Categorize
		double[] outcomes = categorizer.categorize(tokens);

		// Print labels for debugging
		System.out.println("****** Categorization labels: " + categorizer.getAllResults(outcomes));

		if (outcomes != null && outcomes.length > 0) {
			String bestCategory = categorizer.getBestCategory(outcomes);
			double confidence = outcomes[categorizer.getIndex(bestCategory)];

			// Return category only if confidence > 0.50
			if (confidence >= 0.50) {
				return bestCategory;
			}
		}

		return "Miscellaneous";
	}

	public LocalDateTime extractInvoiceDate(String ocrText) {
		String dateStr = findDateString(ocrText);
		return parseDateString(dateStr);
	}

	 
	    public static String findDateString(String text) {
	        String[] datePatterns = {
	                "(\\d{2}/\\d{2}/\\d{4}(?: \\d{1,2}:\\d{2})?)",
	                "(\\d{2}-\\d{2}-\\d{4}(?: \\d{1,2}:\\d{2})?)",
	                "(\\d{4}-\\d{2}-\\d{2}(?: \\d{1,2}:\\d{2})?)",
	                "(\\d{2} \\w{3} \\d{4}(?: \\d{1,2}:\\d{2})?)"
	        };

	        for (String pattern : datePatterns) {
	            Matcher matcher = Pattern.compile(pattern).matcher(text);
	            if (matcher.find()) {
	                return matcher.group(1); // capturing group now safe
	            }
	        }

	        return null; // no date found
	    }

	    /**
	     * Converts extracted date string to LocalDateTime.
	     * If time is missing, sets to 00:00.
	     */
	    public static LocalDateTime parseDateString(String dateStr) {
	        if (dateStr == null) {
	            return null;
	        }

	        for (DateTimeFormatter fmt : FORMATTERS) {
	            try {
	                if (fmt.toString().contains("H")) {
	                    // contains time
	                    return LocalDateTime.parse(dateStr, fmt);
	                } else {
	                    // date only, set time to midnight
	                    LocalDate ld = LocalDate.parse(dateStr, fmt);
	                    return LocalDateTime.of(ld, LocalTime.MIDNIGHT);
	                }
	            } catch (DateTimeParseException ignored) {}
	        }

	        throw new IllegalArgumentException("Unrecognized date format: " + dateStr);
	    }


	public int getCategoryID(String category) {
		switch (category.toLowerCase()) {
		case "food":
			return 1;
		case "travel":
			return 2;
		case "utilities":
			return 3;
		case "shopping":
			return 4;
		case "medical":
			return 5;
		case "entertainment":
			return 6;
		case "education":
			return 7;
		case "rent":
			return 8;
		default:
			return 0;
			
		}
		
	}
}
