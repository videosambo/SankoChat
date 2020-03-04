package com.videosambo.sankochat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author videosambo
 * 
 * onnikontiokorpi@gmail.com
 **/
public class Matcher {
	
	Main plugin = Main.getPlugin(Main.class);
	ArrayList<String> allowedUrls = (ArrayList<String>) plugin.getConfig().getStringList("allowed-urls");
	
	public boolean isLink(String message) {
		for (String allowedUrl : allowedUrls) {
			if (message.contains(allowedUrl)) {
				return false;
			}
		}
		
		if (!extractUrls(message).isEmpty() && plugin.getConfig().getBoolean("filter-urls")) {
			return true;
		} else if (!extractIps(message).isEmpty() && plugin.getConfig().getBoolean("filter-ips")) {
			return true;
		} else if (!extractDN(message).isEmpty() && plugin.getConfig().getBoolean("filter-dn")) {
			return true;
		} else
			return false;
	}

	public static List<String> extractUrls(String text) {
		List<String> containedUrls = new ArrayList<String>();
		String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		java.util.regex.Matcher urlMatcher = pattern.matcher(text);

		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
		}

		return containedUrls;
	}

	public static List<String> extractIps(String text) {
		List<String> containedIps = new ArrayList<String>();
		String ipRegex = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		Pattern pattern = Pattern.compile(ipRegex);
		java.util.regex.Matcher ipMatcher = pattern.matcher(text);

		while (ipMatcher.find()) {
			containedIps.add(text.substring(ipMatcher.start(0), ipMatcher.end(0)));
		}
		return containedIps;
	}

	public static List<String> extractDN(String text) {
		List<String> containedDN = new ArrayList<String>();
		String dnRegex = "\\b([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}\\b";
		Pattern pattern = Pattern.compile(dnRegex, Pattern.CASE_INSENSITIVE);
		java.util.regex.Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			containedDN.add(text.substring(matcher.start(0), matcher.end(0)));
		}
		return containedDN;
	}
}
