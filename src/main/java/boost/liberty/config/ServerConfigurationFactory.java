/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package boost.liberty.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Reads server.xml into memory, writes changes back to server.xml
 *
 * @author Tim Burns
 *
 */
public class ServerConfigurationFactory {

    private static ServerConfigurationFactory INSTANCE;

    public static ServerConfigurationFactory getInstance() throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new ServerConfigurationFactory();
        }
        return INSTANCE;
    }

    public static ServerConfiguration fromFile(File file) throws Exception {
        return getInstance().unmarshal(new FileInputStream(file));
    }

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    private ServerConfigurationFactory() throws Exception {
        JAXBContext context = JAXBContext.newInstance(ServerConfiguration.class);
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        this.unmarshaller = context.createUnmarshaller();
    }

    /**
     * Expresses a server configuration in an XML document.
     *
     * @param sourceConfig
     *            the configuration you want to marshal
     * @param targetFile
     *            the location where you want to marshal state information.
     *            parent directories will be created automatically
     * @throws Exception
     *             if generation fails
     */
    public void marshal(ServerConfiguration sourceConfig, File targetFile) throws Exception {
        if (targetFile == null) {
            return; // if the target location is null, there's nothing to do
        }
        targetFile.getParentFile().mkdirs();
        this.marshal(sourceConfig, new FileOutputStream(targetFile));
    }

    /**
     * Expresses a server configuration in an XML document.
     *
     * @param sourceConfig
     *            the configuration you want to marshal
     * @param outputStream
     *            the stream where you want to marshal state information. the
     *            stream will be closed before this method returns.
     * @throws Exception
     *             if generation fails
     */
    public void marshal(ServerConfiguration sourceConfig, OutputStream outputStream) throws Exception {
        if (outputStream == null) {
            return; // if the outputStream is null, there's nothing to do
        }
        try {
            this.marshaller.marshal(sourceConfig, outputStream);
        } finally {
            outputStream.close();
        }
    }

    /**
     * Converts a server configuration XML file into a series of Java objects.
     *
     * @param inputStream
     *            a server configuration XML file as a stream
     * @return a Java object representation of the server configuration, or null
     *         if the input stream is null
     * @throws Exception
     *             if the XML can't be parsed
     */
    public ServerConfiguration unmarshal(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return null; // nothing to unmarshall
        }
        try {
            return (ServerConfiguration) this.unmarshaller.unmarshal(inputStream);
        } finally {
            inputStream.close();
        }
    }



}
