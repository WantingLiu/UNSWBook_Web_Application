package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLtoLink {
	public static String urlToLink(String urlText) {
		String regexp = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))" + ".*?"
				+ "(?=(&nbsp;|\\s|　|<br />|$|[<>]))";
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(urlText);
		StringBuffer stringbuffer = new StringBuffer();
		while (matcher.find()) {
			String url = matcher.group().substring(0, 3).equals("www") ? "http://" + matcher.group() : matcher.group();
			String tempString = "<a href=\"" + url + "\">" + matcher.group() + "</a>";
			int tempLength = tempString.length();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < tempLength; ++i) {
				char c = tempString.charAt(i);
				if (c == '\\' || c == '$') {
					buffer.append("\\").append(c);
				} else {
					buffer.append(c);
				}
			}
			tempString = buffer.toString();
			matcher.appendReplacement(stringbuffer, tempString);
		}
		matcher.appendTail(stringbuffer);
		return stringbuffer.toString();
	}
}
