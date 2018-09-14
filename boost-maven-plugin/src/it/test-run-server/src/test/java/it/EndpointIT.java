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

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class EndpointIT {
    private static String URL;
    
    private static Process process;
    
    @BeforeClass 
    public static void init() throws Exception {
        URL = "http://localhost:8080/";
        
        process = Runtime.getRuntime().exec("mvn boost:run");
        
        File file = new File("target/liberty/wlp/usr/servers/BoostServer/logs/messages.log");
        
        int timeout = 0;
        boolean serverStarted = false;
        
        while(timeout < 10 && !serverStarted) {
            
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                
                // Check for startup message
                if (line.contains("CWWKF0011I")) {
                    serverStarted = true;
                    break;
                }
            }
            
            fileReader.close();
            
            if (!serverStarted) {
                
                Thread.sleep(1000);
                timeout++;
            }
            
        }
    }
    
    @AfterClass
    public static void teardown() {
        process.destroy();
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
}
