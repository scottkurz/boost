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

import java.io.File;

import java.io.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

import boost.liberty.config.ServerConfiguration;
import boost.liberty.config.ServerConfigurationFactory;

import org.junit.Test;
import org.codehaus.plexus.util.FileUtils;

public class FeatureVersionIT {
	
	private static String GENERATED_CONFIG_LOCATION;

	@BeforeClass
	public static void init() {
		GENERATED_CONFIG_LOCATION = "./target/liberty/wlp/usr/servers/BoostServer/configDropins/overrides/featureList.xml";
	}
	
    @Test
    public void testConfig() throws Exception {

            InputStream is = new FileInputStream(GENERATED_CONFIG_LOCATION);
            try {
                    ServerConfigurationFactory configFactory = ServerConfigurationFactory.getInstance();
                    ServerConfiguration serverConfig = configFactory.unmarshal(is);
                    Set<String> features = serverConfig.getFeatureManager().getFeatures();
                    Set<String> expected = new HashSet<String>();
                    expected.add("springBoot-2.0");
                    assertEquals("Unexpected feature set", expected, features);
            } finally {
                    is.close();
            }
    }

}
