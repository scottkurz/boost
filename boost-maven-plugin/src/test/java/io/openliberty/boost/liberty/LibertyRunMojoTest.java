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
package io.openliberty.boost.liberty;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LibertyRunMojoTest extends AbstractMojoTestCase {
    private static String URL;
    
    /** {@inheritDoc} */
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    /** {@inheritDoc} */
    protected void tearDown()
        throws Exception
    {
        super.tearDown();
    }
    
    @Test
    public void testServlet() throws Exception {
        HttpClient client = new HttpClient();
        
        GetMethod method = new GetMethod(URL);
       
        File pom = getTestFile( "src/test/resources/spring-boot-app/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        LibertyRunMojo libertyRunMojo = (LibertyRunMojo) lookupMojo( "run", pom );
        assertNotNull( libertyRunMojo );
        libertyRunMojo.execute();

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
