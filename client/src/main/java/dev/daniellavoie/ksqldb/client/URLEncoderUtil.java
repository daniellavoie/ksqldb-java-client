package dev.daniellavoie.ksqldb.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class URLEncoderUtil {
	public static String encode(String value) {
		return URLEncoderUtil.encode(value, StandardCharsets.UTF_8.toString());
	}

	public static String encode(String value, String enc) {
		try {
			return URLEncoder.encode(value, enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
