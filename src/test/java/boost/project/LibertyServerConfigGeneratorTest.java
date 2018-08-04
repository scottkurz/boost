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
package boost.project;

import static org.junit.Assert.assertEquals;
import static util.DOMUtils.getDirectChildrenByTag;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Element;

public class LibertyServerConfigGeneratorTest {

    @Test
    public void testZeroFeaturesInDefaultServerConfig() throws ParserConfigurationException {
        LibertyServerConfigGenerator g = new LibertyServerConfigGenerator();
        Element serverRoot = g.doc.getDocumentElement();
        List<Element> featureMgrList = getDirectChildrenByTag(serverRoot, LibertyServerConfigGenerator.FEATURE_MANAGER);
        assertEquals("Didn't find one and only one featureMgr", 1, featureMgrList.size());
        Element featureMgr = featureMgrList.get(0);
        List<Element> featureList = getDirectChildrenByTag(featureMgr, LibertyServerConfigGenerator.FEATURE);
        assertEquals("Didn't find empty list of features", 0, featureList.size());
    }
}
