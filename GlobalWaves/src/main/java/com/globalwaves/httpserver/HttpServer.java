package com.globalwaves.httpserver;

import com.globalwaves.httpserver.config.Configuration;
import com.globalwaves.httpserver.config.ConfigurationManager;
import com.globalwaves.httpserver.core.ServerListenerThread;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Driver class for http server.
 */
public class HttpServer {

	private final static Logger LOGGER = Logger.getLogger("HttpServer");

	/* When server is born. */
	public static long dataStart = System.currentTimeMillis();

	public static void main(String[] args) throws IOException {
		LOGGER.info("Server starting...");

		ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
		Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

		LOGGER.info("Using port : " + conf.getPort());
		LOGGER.info("Using webroot : " + conf.getWebroot());

		ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
		serverListenerThread.start();
	}
}
