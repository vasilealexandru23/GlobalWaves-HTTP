package com.globalwaves.httpserver.parser;

public class HttpParsingException extends Exception {

	private final HttpStatusCodes errorCode;

	public HttpParsingException (final HttpStatusCodes errorCode) {
		super(errorCode.MESSAGE);
		this.errorCode = errorCode;
	}

	public HttpStatusCodes getErrorCode() {
		return errorCode;
	}
}
