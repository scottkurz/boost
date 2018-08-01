/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package it;

import java.io.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import boost.liberty.config.ServerConfiguration;
import boost.liberty.config.ServerConfigurationFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class EndpointIT {
	private static String URL;
	private static String GENERATED_CONFIG_LOCATION;

	@BeforeClass
	public static void init() {
		URL = "http://localhost:9080/";
		GENERATED_CONFIG_LOCATION = "./target/liberty/wlp/usr/servers/BoostServer/configDropins/overrides/featureList.xml";

	}

	@Test
	public void testServlet() throws Exception {
		HttpClient client = new HttpClient();

		GetMethod method = new GetMethod(URL);

		try {
			int statusCode = client.executeMethod(method);

			assertEquals("HTTP GET failed", HttpStatus.SC_OK, statusCode);

			String response = method.getResponseBodyAsString(1000);

			assertTrue("Unexpected response body", response.contains("Greetings from Spring Boot!"));
		} finally {
			method.releaseConnection();
		}
	}

	@Test
	public void testConfig() throws Exception {

		InputStream is = new FileInputStream(GENERATED_CONFIG_LOCATION);
		try {
			ServerConfigurationFactory configFactory = ServerConfigurationFactory.getInstance();
			ServerConfiguration serverConfig = configFactory.unmarshal(is);
			Set<String> features = serverConfig.getFeatureManager().getFeatures();
			Set<String> expected = new HashSet<String>();
			expected.add("springBoot-1.5");
			assertEquals("Unexpected feature set", expected, features);
		} finally {
			is.close();
		}
	}
}
