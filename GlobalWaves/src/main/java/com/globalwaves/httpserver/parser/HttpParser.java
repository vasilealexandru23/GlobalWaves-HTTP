package com.globalwaves.httpserver.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

	public static ObjectNode parseTarget(final String line) throws UnsupportedEncodingException {
		ObjectNode c = new ObjectMapper().createObjectNode();

		// Remove the leading slash and question mark
		String target = line.substring(2);

		// Split the string by the ampersand
		String[] pairs = target.split("&");

		// For each pair, split it by the equals sign and add it to the JSONObject
		for (String pair : pairs) {
			String[] keyValue = pair.split("=");

			/* Invalid field. */
			if (keyValue.length < 2) continue;

			if (keyValue[0].equals("command")) {
				ObjectNode filters = new ObjectMapper().createObjectNode();
				String[] commandParts = keyValue[1].split("\\+");

				if ("search".equals(commandParts[0])) {
					if (commandParts.length >= 3) {
						String action = commandParts[0];
						String type = commandParts[1];
						String filterPart = commandParts[2];
						String[] filterKeyValue = filterPart.split("%3D", 2);
						if (filterKeyValue.length == 2) {
							String filterKey = filterKeyValue[0];
							String filterValue = URLDecoder.decode(filterKeyValue[1], StandardCharsets.UTF_8.toString());
							filters.put(filterKey, filterValue);
						}
						c.put("command", action);
						c.put("type", type);
						c.putPOJO("filters", filters);
					}
				} else if ("select".equals(commandParts[0])) {
					c.put("command", commandParts[0]);
					c.put("itemNumber", commandParts[1]);
				} else {
					c.put("command", commandParts[0]);
				}
			} else if (keyValue[0].equals("timestamp")) {
				c.put(keyValue[0], (Long.parseLong(keyValue[1]) - HttpServer.dataStart) / 1000);
			} else {
				c.put(keyValue[0], keyValue[1]);
			}
		}

		return c;
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

	/* TODO */
	private void parseHeaders(InputStreamReader reader, HttpRequest request) {}

	/* TODO */
	private void parseBody(InputStreamReader reader, HttpRequest request) {}
}
