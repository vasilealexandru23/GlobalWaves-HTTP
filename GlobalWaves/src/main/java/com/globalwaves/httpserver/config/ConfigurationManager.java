package com.globalwaves.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.globalwaves.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ConfigurationManager {
	private static ConfigurationManager myConfigurationManager = null;
	private static Configuration myCurrentConfiguration;

	private ConfigurationManager() {
		/* TODO */
	}

	public static ConfigurationManager getInstance() {
		if (myConfigurationManager == null) {
			myConfigurationManager = new ConfigurationManager();
		}

		return myConfigurationManager;
	}

	/**
	 * Used to load a configuration file given a path
	 * @param filePath 				file path to config
	 */
	public void loadConfigurationFile(final String filePath) throws IOException {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			throw new HttpConfigurationException(e);
		}

		int charRead;
		StringBuffer sb = new StringBuffer();

		try {
			while ((charRead = fileReader.read()) != -1) {
				sb.append((char)charRead);
			}
		} catch (IOException e) {
			throw new HttpConfigurationException(e);
		}

		JsonNode configuration = null;
		try {
			configuration = Json.parse(sb.toString());
		} catch (IOException e) {
			throw new HttpConfigurationException("Error parsing configuration file", e);
		}

		try {
			myCurrentConfiguration = Json.fromJson(configuration, Configuration.class);
		} catch (JsonProcessingException e) {
			throw new HttpConfigurationException("Error parsing configuration file, internal", e);
		}
	}

	/**
	 * Returns the current loaded configuration
	 */
	public Configuration getCurrentConfiguration() throws HttpConfigurationException {
		if (myCurrentConfiguration == null) {
			throw new HttpConfigurationException("No current configuration set.");
		}

		return myCurrentConfiguration;
	}
}
