package me.steffenjacobs.effectivemusic.util;

import java.util.Base64;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/** @author Steffen Jacobs */
@Component
public class Base64Service {

	private static final Pattern REGEX = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");

	public boolean isBase64(String data) {
		return REGEX.matcher(data).find();
	}

	public String decode(String base64) {
		return new String(Base64.getDecoder().decode(base64));
	}
}
