package com.globalwaves.httpserver.core;

import com.globalwaves.httpserver.HttpServer;
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
		final String jsonTest = "{\"message\": \"Hello World!\"}";

		/* TODO : Parse the request. */
		String r = HttpParser.parseTarget(request);

		/* TODO: Send to application. */

		/*TODO: Get from application response and send back to client. */
		//---//

		String response = "HTTP/1.1 200 OK" + CRLF
				+ "Content-Type: application/json" + CRLF
				+ "Access-Control-Allow-Origin: *" + CRLF
				+ "Content-Length: " + jsonTest.length() + CRLF
				+ CRLF + jsonTest + CRLF;

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
