package com.globalwaves.httpserver.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.fileio.input.CommandInput;
import com.globalwaves.httpserver.fileio.input.LibraryInput;
import com.globalwaves.httpserver.parser.HttpParser;
import com.globalwaves.httpserver.parser.HttpParsingException;
import com.globalwaves.httpserver.parser.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.logging.Logger;

public class HttpConnectionWorkerThread extends Thread {
	final String CRLF = "\r\n";
	private final String html;
	private final Socket socketClient;
	private final static Logger LOGGER = Logger.getLogger("HttpConnectionWorkerThread");

	public HttpConnectionWorkerThread(final Socket socketClient, final String html) {
		this.socketClient = socketClient;
		this.html = html;
	}

	static {
		LibraryInput.setLibraryPath("src/main/resources/Library.json");
		try {
			LibraryInput library = LibraryInput.getInstance();
			Database.setLibrary(library);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Function that sends to client the html.
	 * @param client			where to send data
	 */
	private void sendHtml(final OutputStream client) throws IOException {
		String response = "HTTP/1.1 200 OK" + CRLF
						+ "Content-Length: " + html.length() + CRLF
						+ CRLF + html + CRLF;

		client.write(response.getBytes());
	}

	/**
	 * Function that sends to client the background image.
	 * @param client			where to send data
	 */
	private void sendBackground(final OutputStream client) throws IOException {
		byte[] imgData = Files.readAllBytes(Path.of("src/main/resources/globalwaves.jpg"));

		String response = "HTTP/1.1 200 OK" + CRLF
						+ "Content-Type: image/jpg" + CRLF
						+ "Content-Length: " + imgData.length + CRLF
						+ CRLF;

		client.write(response.getBytes());
		client.write(imgData);
		client.write(CRLF.getBytes());
	}

	/**
	 * Function that sends to client the website's icon.
	 * @param client			where to send data
	 */
	private void sendIcon(final OutputStream client) throws IOException {
		byte[] imgData = Files.readAllBytes(Path.of("src/main/resources/icon.png"));

		String response = "HTTP/1.1 200 OK" + CRLF
						+ "Content-Type: image/png" + CRLF
						+ "Content-Length: " + imgData.length + CRLF
						+ CRLF;

		client.write(response.getBytes());

		client.write(imgData);
		client.write(CRLF.getBytes());
	}

	private void processRequest(final OutputStream client, final String request) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode outputs = objectMapper.createArrayNode();

		/* TODO : Parse the request. */
		ObjectNode r = HttpParser.parseTarget(request);

		CommandInput newCommand = objectMapper.convertValue(r, CommandInput.class);

		newCommand.constructCommand();

		outputs.add(newCommand.getCommands().get(0).execute(Database.getInstance()));

		JsonNode message = outputs.findValue("message");
		JsonNode result = outputs.findValue("results");
		JsonNode stats = outputs.findValue("stats");

		String jsonResult = "";

		if (message != null) {
			jsonResult = message.toString() + "\n";
		}

		if (result != null) {
			jsonResult += result.toString() + "\n";
		}

		if (stats != null) {
			jsonResult += stats.toString() + "\n";
		}

		System.out.println(jsonResult);

		String response = "HTTP/1.1 200 OK" + CRLF
				+ "Content-Type: application/json" + CRLF
				+ "Access-Control-Allow-Origin: *" + CRLF
				+ "Content-Length: " + jsonResult.length() + CRLF
				+ CRLF + jsonResult + CRLF;

		client.write(response.getBytes());
	}

	@Override
	public void run() {
		try (InputStream requestClient = socketClient.getInputStream();
			 OutputStream replyClient = socketClient.getOutputStream()) {

			/* By default, browser requests with GET for html. */
			HttpParser parser = new HttpParser();
			HttpRequest req = parser.parseHttpRequest(requestClient);

			String requestTarget = req.getRequestTarget();

			switch (requestTarget) {
				case "/80" -> sendHtml(replyClient);
				case "/globalwaves.jpg" -> sendBackground(replyClient);
				case "/favicon.ico" -> sendIcon(replyClient);
				default -> processRequest(replyClient, requestTarget);
			}

			LOGGER.info(" * Connection Processing Finished.");
		} catch (IOException e) {
			LOGGER.info("Error on getting the streams from client.");
		} catch (HttpParsingException e) {
			throw new RuntimeException(e);
		} finally {

			if (socketClient != null) {
				try {
					socketClient.close();
				} catch (IOException e) {
				}
			}
			LOGGER.info("Connection closed with client!");
		}
	}
}
