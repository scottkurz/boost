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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;


/**
 * Represents a server configuration document for the WAS 8.5 Liberty Profile.
 */
@XmlRootElement(name = "server")
public class ServerConfiguration implements Cloneable {

    private String description;

    @XmlElement(name = "featureManager")
    private FeatureManager featureManager;

    /**
     * @return the featureManager for this configuration
     */
    public FeatureManager getFeatureManager() {
        if (this.featureManager == null) {
            this.featureManager = new FeatureManager();
        }
        return this.featureManager;
    }


    /**
     * Retrieves a description of this configuration.
     *
     * @return a description of this configuration
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this configuration
     *
     * @param description the description of this configuration
     */
    @XmlAttribute
    public void setDescription(String description) {
        this.description = ConfigElement.getValue(description);
    }
    
}
