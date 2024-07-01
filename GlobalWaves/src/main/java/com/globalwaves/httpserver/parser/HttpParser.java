package com.globalwaves.httpserver.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class HttpParser {
	private final static Logger LOGGER = Logger.getLogger("HttpParser");
	private final static int SP = 0x20; // 32
	private final static int CR = 0x0D; // 13
	private final static int LF = 0x0A; // 10

	public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
		InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

		HttpRequest request = new HttpRequest();
		parseRequestLine(reader, request);
		parseHeaders(reader, request);
		parseBody(reader, request);

		return request;
	}

	private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
		int _byte;
		StringBuilder dataBuffer = new StringBuilder();

		boolean methodParsed = false;
		boolean targetParsed = false;

		while ((_byte = reader.read()) >= 0) {
			if (_byte == CR) {
				_byte = reader.read();
				if (_byte == LF) {
					LOGGER.info("Request line VERSION : " + dataBuffer.toString());
					if (!methodParsed || !targetParsed) {
						throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_404_BAD_REQUEST);
					}
					return;
				} else {
					throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_404_BAD_REQUEST);
				}
			}

			if (_byte == SP) {
				if (!methodParsed) {
					LOGGER.info("Request line METHOD : " + dataBuffer.toString());
					request.setMethod(dataBuffer.toString());
					methodParsed = true;
				} else if (!targetParsed) {
					LOGGER.info("Request line TARGET : " + dataBuffer.toString());
					request.setRequestTarget(dataBuffer.toString());
					targetParsed = true;
				} else {
					LOGGER.info("Request line to Process :" + dataBuffer.toString());
					throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_404_BAD_REQUEST);
				}
				dataBuffer.delete(0, dataBuffer.length());
			} else {
				dataBuffer.append((char) _byte);
				if (!methodParsed && dataBuffer.length() >= HttpMethod.MAXSIZE) {
					System.out.println(dataBuffer.toString());
					throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
				}
			}
		}
	}

	private void parseHeaders(InputStreamReader reader, HttpRequest request) {

	}

	private void parseBody(InputStreamReader reader, HttpRequest request) {
	}
}
