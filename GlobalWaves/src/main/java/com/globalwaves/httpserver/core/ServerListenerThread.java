package com.globalwaves.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class ServerListenerThread extends Thread{

	private int port;
	private String webroot;
	private ServerSocket connectSocket;

	private final static Logger LOGGER = Logger.getLogger("ServerListenerThread");
	/**
	 * Initializes a new platform {@code Thread}. This constructor has the same
	 * effect as {@linkplain #Thread(ThreadGroup, Runnable, String) Thread}
	 * {@code (null, null, gname)}, where {@code gname} is a newly generated
	 * name. Automatically generated names are of the form
	 * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
	 *
	 * <p> This constructor is only useful when extending {@code Thread} to
	 * override the {@link #run()} method.
	 *
	 * @see <a href="#inheritance">Inheritance when creating threads</a>
	 */
	public ServerListenerThread(final int port, final String webroot) throws IOException {
		this.port = port;
		this.webroot = webroot;
		this.connectSocket = new ServerSocket(this.port);
	}

	private String getHTML() throws IOException {
		Path htmlFile = Path.of("src/main/resources/GlobalWaves.html");
		return Files.readString(htmlFile);
	}

	@Override
	public void run() {
		String html = null;
		try {
			html = getHTML();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			while (connectSocket.isBound() && !connectSocket.isClosed()) {
				Socket socketClient = connectSocket.accept();

				LOGGER.info(" * Connection accepted: " + socketClient.getInetAddress());

				HttpConnectionWorkerThread processClient = new HttpConnectionWorkerThread(socketClient, html);
				processClient.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("Error on accepting connection.");
		} finally {
			try {
				connectSocket.close();
			} catch (IOException e) {}
		}
	}
}
