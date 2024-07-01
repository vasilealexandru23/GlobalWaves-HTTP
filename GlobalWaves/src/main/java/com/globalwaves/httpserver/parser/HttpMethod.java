package com.globalwaves.httpserver.parser;

public enum HttpMethod {
	GET, HEAD, POST;

	public static final int MAXSIZE;

	static {
		int tempMaxLength = -1;
		for (HttpMethod httpMethod : values()) {
			if (httpMethod.name().length() > tempMaxLength) {
				tempMaxLength = httpMethod.name().length();
			}
		}

		MAXSIZE = tempMaxLength;
	}
}
