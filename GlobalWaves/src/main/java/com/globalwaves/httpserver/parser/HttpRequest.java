package com.globalwaves.httpserver.parser;

public class HttpRequest extends HttpMessage {

	private HttpMethod method;
	private String requestTarget;
	private String httpVersion;

	HttpRequest() {
	}

	public HttpMethod getMethod() { return method; }

	public String getRequestTarget() { return requestTarget; }

	public String getHttpVersion() { return httpVersion; }

	void setRequestTarget(final String requestTarget) { this.requestTarget = requestTarget; }

	void setMethod(final String method) throws HttpParsingException {
		for (HttpMethod httpMethod : HttpMethod.values()) {
			if (httpMethod.name().equals(method)) {
				this.method = HttpMethod.valueOf(method);
				return;
			}
		}

		throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_404_BAD_REQUEST);
	}
}
