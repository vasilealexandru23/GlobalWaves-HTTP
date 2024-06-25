package com.globalwaves.httpserver.core;

import com.globalwaves.httpserver.config.HttpConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class HttpConnectionWorkerThread extends Thread {
	private final String html;
	private final Socket socketClient;
	private final static Logger LOGGER = Logger.getLogger("HttpConnectionWorkerThread");

	public HttpConnectionWorkerThread(final Socket socketClient, final String html) {
		this.socketClient = socketClient;
		this.html = html;
	}

	@Override
	public void run() {
		InputStream requestClient = null;
		OutputStream replyClient = null;
		try {
			requestClient = socketClient.getInputStream();
			replyClient = socketClient.getOutputStream();

			final String CRLF = "\n\r";
			String response =
					"HTTP/1.1 200 OK" + CRLF
							+ "Content-Length: " + html.length() + CRLF
							+ CRLF + html + CRLF;

			replyClient.write(response.getBytes());

			LOGGER.info(" * Connection Processing Finished.");
		} catch (IOException e) {
			LOGGER.info("Error on getting the streams from client.");
		} finally {
			if (requestClient != null) {
				try {
					requestClient.close();
				} catch (IOException e) {}
			}

			if (replyClient != null) {
				try {
					replyClient.close();
				} catch (IOException e) {}
			}

			if (socketClient != null) {
				try {
					socketClient.close();
				} catch (IOException e) {}
			}
		}
	}
}
